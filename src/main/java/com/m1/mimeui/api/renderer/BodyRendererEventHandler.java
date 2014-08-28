package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;

import java.io.PrintWriter;

public interface BodyRendererEventHandler extends PartRendererEventHandler
{
    /**
     * Occurs immediately before a message body is rendered. Use this to, for example, open an enclosing HTML tag.
     *
     * <p />Note that this also occurs for nested messages (like those in a multipart/digest), so don't assume that this
     * will be called only once. Also, if only a single specific part is being rendered (as opposed to a body), this
     * will not be called at all. Finally, if the message has no body, this will not be called.
     *
     * @param analyzedMessage
     *                      The message parts object for the message body about to be rendered. This actually contains
     *                      not only the inline body parts, but all the parts including attachments that won't be
     *                      rendered at all.
     * @param htmlOut
     *                      The print writer that content is being written to.
     */
    public void onBeforeBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut);

    /**
     * Occurs immediately after a message body is rendered. Use this to, for example, close any enclosing HTML tag that
     * was left open by onBeforeBodyContent.
     *
     * <p />Note that this also occurs for nested messages (like those in a multipart/digest), so don't assume that this
     * will be called only once. Also, if only a single specific part is being rendered (as opposed to a body), this
     * will not be called at all. Finally, if the message has no body, this will not be called.
     *
     * @param analyzedMessage
     *                      The message parts object for the message body that had been written. This actually contains
     *                      not only the inline body parts, but all the parts including attachments that won't be
     *                      rendered at all.
     * @param htmlOut
     *                      The print writer that content is being written to.
     */
    public void onAfterBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut);

    /**
     * Occurs when a message contains no body parts at all. This only occurs when there is absolutely no body. If there
     * is a body, for example, that consists only of inline attachments, this will event not be triggered.
     *
     * <p />Note that if this occurs, onBeforeBodyContent and onAfterBodyContent will <em>not</em> also be called.
     *
     * @param analyzedMessage
     *                      The message parts object for the message body that would have been written. This contains
     *                      the attachments and possibly other non-inline parts that the message contains.
     * @param htmlOut
     *                      The print writer that the content would have been written to.
     */
    public void onEmptyBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut);

    /**
     * Occurs when an inline part is encountered for which there is no content viewer available.
     *
     * <p />For inline attachments (when this event occurs) onBeforePartContent and onAfterPartContent is not called.
     *
     * @param part The part whose content cannot be displayed as HTML.
     * @param htmlOut The print writer that content would have been written to.
     */
    public void onInlineAttachment (final IncorporatedPart part, final PrintWriter htmlOut);

    /**
     * Occurs when an alternative part is encountered for an inline part that is actually in a format that is preferred
     * over the format of the part that was selected.
     *
     * <p />For example, if a message has three alternative body representations: plain text, HTML, and PDF, and no
     * content viewer is available for PDF, but there is for plain text and HTML, the plain text part will be ignored,
     * the HTML part will be rendered, and this event will be triggered for the PDF.
     *
     * <p />No assumptions should be made about which parts will be encountered first, and which last because the
     * implementation may change. At the time that this comment was written however, this inline alternative event will
     * be triggered BEFORE the inline part's events will be triggered. Also note that we don't store the metadata that
     * would link the parts to one another. Once message analysis is complete, each part is considered an independent
     * unit.
     *
     * <p />For inline attachments (when this event occurs) onBeforePartContent and onAfterPartContent is not called.
     *
     * @param part The part whose content cannot be displayed as HTML.
     * @param htmlOut The print writer that content would have been written to.
     */
    public void onInlineAlternative (final IncorporatedPart part, final PrintWriter htmlOut);

    /**
     * Occurs when a part of type message/external-body is encountered. The framework will never attempt to fetch and
     * render the part. Instead it just calls this method. Technically this method could fetch and render the part, but
     * doing so could be unsafe, so it is better just to show an error message.
     *
     * @param part The message/external-body part.
     * @param htmlOut The print writer that the content would have been written to.
     */
    public void onExternalPartContent (final IncorporatedPart part, final PrintWriter htmlOut);
}
