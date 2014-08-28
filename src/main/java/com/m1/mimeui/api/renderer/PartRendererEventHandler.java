package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.io.PrintWriter;

public interface PartRendererEventHandler
{
    /**
     * Occurs immediately before a part is rendered. Use this to, for example, open an enclosing HTML tag.
     *
     * <p />This occurs even when a content viewer exception occurs, but this is NOT called for inline attachments or
     * inline alternatives.
     *
     * @param part The part that is about to be rendered.
     * @param htmlOut The print writer that content is being written to.
     */
    public void onBeforePart (final IncorporatedPart part, final PrintWriter htmlOut);

    /**
     * Occurs immediately after a part is rendered. Use this to, for example, close any enclosing HTML tag that was left
     * open by onBeforePartContent.
     *
     * <p />This occurs even when a content viewer exception occurs, but this is NOT called for inline attachments or
     * inline alternatives.
     *
     * @param part The part that had been rendered.
     * @param htmlOut The print writer that content had been written to.
     */
    public void onAfterPart (final IncorporatedPart part, final PrintWriter htmlOut);

    /**
     * Occurs when a content viewer throws an exception. Note that onBeforePartContent would have already been called
     * just before this event, and onAfterPartContent will also be called immediately after it.
     *
     * <p />This is never called for inline attachments or inline alternatives.
     *
     * @param part The part that was being rendered when the exception was caught.
     * @param htmlOut The print writer that content was being written to.
     * @param exception The exception that was caught.
     */
    public void onRenderException (final IncorporatedPart part, final PrintWriter htmlOut,
                                   final MimeUIException exception);

    public void onUnsupportedContentType (final IncorporatedPart part, final PrintWriter htmlOut);
}
