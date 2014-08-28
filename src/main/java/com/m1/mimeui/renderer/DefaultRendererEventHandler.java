package com.m1.mimeui.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.codec.MailtoUriField;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.RecipientList;
import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.renderer.DocumentRendererEventHandler;
import com.m1.mimeui.api.renderer.MessageRendererEventHandler;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused") // Exported for use by external client code.
public class DefaultRendererEventHandler implements MessageRendererEventHandler, DocumentRendererEventHandler
{
    private final ContentLocationProvider contentLocationProvider;

    public DefaultRendererEventHandler (final ContentLocationProvider contentLocationProvider)
    {
        this.contentLocationProvider = contentLocationProvider;
    }

    protected Collection<String> getScriptPaths ()
    {
        return Collections.emptyList();
    }

    protected Collection<String> getStylePaths ()
    {
        return Collections.emptyList();
    }

    protected void renderScriptAndStyleHeaders (final PrintWriter htmlOut)
    {
        final Collection<String> scripts = getScriptPaths();
        final Collection<String> styles = getStylePaths();

        for (final String style : styles)
        {
            htmlOut.print("<link href=\"");
            htmlOut.print(style);
            htmlOut.print("\" rel=\"stylesheet\" type=\"text/css\">");
        }

        for (final String script : scripts)
        {
            htmlOut.print("<script src=\"");
            htmlOut.print(script);
            htmlOut.print("\"></script>");
        }
    }

    protected void renderPartDownloadLink (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        try
        {
            htmlOut.print("<a href=\"");
            htmlOut.print(StringEscapeUtils.escapeHtml4(this.contentLocationProvider.getRawPartDownloadUrl(part)));
            htmlOut.print("\" title=\"");
            htmlOut.print(StringEscapeUtils.escapeHtml4(getPartDownloadLinkTitle(part)));
            htmlOut.print("\">");
            htmlOut.print(StringEscapeUtils.escapeHtml4(part.getFilename()));
            htmlOut.print("</a>");
        }
        catch (final ContentLocationProvider.UnsupportedServiceException e)
        {
            htmlOut.print("<");
            htmlOut.print(getPartDownloadLinkUnavailableTagName());
            htmlOut.print(">");
            htmlOut.print(getPartDownloadLinkUnavailableText(part));
            htmlOut.print("</");
            htmlOut.print(getPartDownloadLinkUnavailableTagName());
            htmlOut.print(">");
        }
    }

    protected void renderMailbox (final Mailbox mailbox, final PrintWriter htmlOut)
    {
        final String displayName = mailbox.getDisplayName();
        final String emailAddress = mailbox.getEmailAddress();

        final Map<MailtoUriField, Set<String>> compositionFields
                = Collections.singletonMap(MailtoUriField.TO, Collections.singleton(emailAddress));

        try
        {
            final String compositionUrl = this.contentLocationProvider.getMessageCompositionUrl(compositionFields);

            htmlOut.print("<a href=\"");
            htmlOut.print(StringEscapeUtils.escapeHtml4(compositionUrl));
            htmlOut.print("\"");

            if (displayName != null)
            {
                htmlOut.print(" title=\"");
                htmlOut.print(StringEscapeUtils.escapeHtml4(emailAddress));
                htmlOut.print("\"");
            }

            htmlOut.print(">");
            htmlOut.print(StringEscapeUtils.escapeHtml4(displayName != null ? displayName : emailAddress ));
            htmlOut.print("</a>");
        }
        catch (final ContentLocationProvider.UnsupportedServiceException e)
        {
            htmlOut.print("<span");

            if (displayName != null)
            {
                htmlOut.print(" title=\"");
                htmlOut.print(StringEscapeUtils.escapeHtml4(emailAddress));
                htmlOut.print("\"");
            }

            htmlOut.print(">");
            htmlOut.print(StringEscapeUtils.escapeHtml4(displayName != null ? displayName : emailAddress));
            htmlOut.print("</span>");
        }
    }

    protected void renderDate (final Date date, final PrintWriter htmlOut)
    {
        if (date != null)
        {
            htmlOut.print(SimpleDateFormat.getDateTimeInstance().format(date));
        }
    }

    protected String getDocumentEncoding ()
    {
        return "utf-8";
    }

    protected String getDocumentTitle ()
    {
        return "Message View";
    }

    protected String getExtraClassName ()
    {
        return "mime_ui";
    }

    protected String getDocumentHeaderTagName ()
    {
        return "h1";
    }

    protected String getDocumentHeaderClassName ()
    {
        return "document_header";
    }

    protected String getMessageTagName ()
    {
        return "div";
    }

    protected String getMessageClassName ()
    {
        return "message";
    }

    protected String getHeaderTagName ()
    {
        return "h2";
    }

    protected String getHeaderClassName ()
    {
        return "header";
    }

    protected String getBasicTagName ()
    {
        return "div";
    }

    protected String getBasicClassName ()
    {
        return "basic";
    }

    protected String getSenderTagName ()
    {
        return "div";
    }

    protected String getSenderClassName ()
    {
        return "sender";
    }

    protected String getFromListTagName ()
    {
        return "ul";
    }

    protected String getFromListClassName ()
    {
        return "from_list";
    }

    protected String getFromTagName ()
    {
        return "li";
    }

    protected String getFromClassName ()
    {
        return "from";
    }

    protected String getSubjectTagName ()
    {
        return "div";
    }

    protected String getSubjectClassName ()
    {
        return "subject";
    }

    protected String getSubjectEmptyClassName ()
    {
        return "empty";
    }

    protected String getSubjectEmptyText ()
    {
        return "No Subject";
    }

    protected String getSentDateTagName ()
    {
        return "div";
    }

    protected String getSentDateClassName ()
    {
        return "sent_date";
    }

    protected String getSentDateEmptyClassName ()
    {
        return "empty";
    }

    protected String getSentDateEmptyText ()
    {
        return "Unknown";
    }

    protected String getRecipientListsTagName ()
    {
        return "div";
    }

    protected String getRecipientListsClassName ()
    {
        return "recipient_lists";
    }

    protected String getRecipientListTagName ()
    {
        return "ul";
    }

    protected String getRecipientListClassName ()
    {
        return "recipient_list";
    }

    protected String getRecipientTagName ()
    {
        return "li";
    }

    protected String getRecipientClassName ()
    {
        return "recipient";
    }

    protected String getAttachmentListTagName ()
    {
        return "ul";
    }

    protected String getAttachmentListClassName ()
    {
        return "attachment_list";
    }

    protected String getAttachmentTagName ()
    {
        return "li";
    }

    protected String getAttachmentClassName ()
    {
        return "attachment";
    }

    protected String getBodyTagName ()
    {
        return "div";
    }

    protected String getBodyClassName ()
    {
        return "body";
    }

    protected String getEmptyBodyClassName ()
    {
        return "empty";
    }

    protected String getEmptyBodyText ()
    {
        return "There are no body parts that can be displayed inline.";
    }

    protected String getPartTagName ()
    {
        return "div";
    }

    protected String getPartClassName ()
    {
        return "part";
    }

    protected String getMimeTypeTypeClassNamePrefix ()
    {
        return "mime_type_";
    }

    protected String getInlineStubClassName ()
    {
        return "inline_stub";
    }

    protected String getRenderExceptionClassName ()
    {
        return "error";
    }

    protected String getRenderExceptionText (@SuppressWarnings("unused") final IncorporatedPart part,
                                             @SuppressWarnings("unused") final MimeUIException exception)
    {
        return "An unexpected error occurred while displaying this part's content.";
    }

    protected String getInlineAttachmentClassName ()
    {
        return "inline_attachment";
    }

    protected String getInlineAttachmentText (final IncorporatedPart part)
    {
        return "This part cannot be displayed because it's content type, \""
                + part.getContentType().getMimeType()
                + "\", isn't supported.";
    }

    protected String getInlineAlternativeClassName ()
    {
        return "inline_alternative";
    }

    protected String getInlineAlternativeText (@SuppressWarnings("unused") final IncorporatedPart part)
    {
        return "A high fidelity representation of this content is available.";
    }

    protected String getPartDownloadTagName ()
    {
        return "div";
    }

    protected String getPartDownloadClassName ()
    {
        return "part_download";
    }

    protected String getPartDownloadLinkTitle (@SuppressWarnings("unused") final IncorporatedPart part)
    {
        return "Download";
    }

    protected String getPartDownloadLinkUnavailableTagName ()
    {
        return "em";
    }

    protected String getPartDownloadLinkUnavailableText (@SuppressWarnings("unused") final IncorporatedPart part)
    {
        return "Download not available.";
    }

    protected String getExternalContentClass ()
    {
        return "external_content";
    }

    protected String getExternalPartContentText (@SuppressWarnings("unused") final IncorporatedPart part)
    {
        return "This part cannot be displayed because its body content is externally hosted. "
                + "Externally hosted body content is not supported.";
    }

    protected void renderPartDownload (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getPartDownloadTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartDownloadClassName());
        htmlOut.print("\">");

        renderPartDownloadLink(part, htmlOut);

        htmlOut.print(" - ");
        htmlOut.print(StringEscapeUtils.escapeHtml4(part.getContentType().toString()));

        htmlOut.print("</");
        htmlOut.print(getPartDownloadTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeDocument (final PrintWriter htmlOut)
    {
        htmlOut.println("<!DOCTYPE html>");
        htmlOut.print("<html>");
        htmlOut.print("<head>");
        htmlOut.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
        htmlOut.print(getDocumentEncoding());
        htmlOut.print("\">");
        htmlOut.print("<title>");
        htmlOut.print(StringEscapeUtils.escapeHtml4(getDocumentTitle()));
        htmlOut.print("</title>");

        renderScriptAndStyleHeaders(htmlOut);

        htmlOut.print("</head>");
        htmlOut.print("<body>");
        htmlOut.print("<");
        htmlOut.print(getDocumentHeaderTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getDocumentHeaderClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getDocumentTitle()));

        htmlOut.print("</");
        htmlOut.print(getDocumentHeaderTagName());
        htmlOut.print(">");
    }

    @Override
    public void onAfterDocument (final PrintWriter htmlOut)
    {
        htmlOut.print("</body>");
        htmlOut.print("</html>");
    }

    @Override
    public void onBeforeMessage (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getMessageTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getMessageClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterMessage (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getMessageTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeHeader (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getHeaderTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getHeaderClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterHeader (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getHeaderTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeBasic (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getBasicTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getBasicClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterBasic (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getBasicTagName());
        htmlOut.print(">");
    }

    @Override
    public void onSender (final Mailbox sender, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getSenderTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getSenderClassName());
        htmlOut.print("\">");

        renderMailbox(sender, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getSenderTagName());
        htmlOut.print(">");
    }

    @Override
    public void onNoSender (final PrintWriter htmlOut)
    {
        // Render nothing.
    }

    @Override
    public void onBeforeFromList (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getFromListTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getFromListClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterFromList (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getFromListTagName());
        htmlOut.print(">");
    }

    @Override
    public void onEmptyFromList (final PrintWriter htmlOut)
    {
        // Render nothing.
    }

    @Override
    public void onFrom (final Mailbox from, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getFromTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getFromClassName());
        htmlOut.print("\">");

        renderMailbox(from, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getFromTagName());
        htmlOut.print(">");
    }

    @Override
    public void onSubject (final String subject, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getSubjectTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getSubjectClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(subject));

        htmlOut.print("</");
        htmlOut.print(getSubjectTagName());
        htmlOut.print(">");
    }

    @Override
    public void onNoSubject (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getSubjectTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getSubjectClassName());
        htmlOut.print(" ");
        htmlOut.print(getSubjectEmptyClassName());
        htmlOut.print("\">");

        htmlOut.print(getSubjectEmptyText());

        htmlOut.print("</");
        htmlOut.print(getSubjectTagName());
        htmlOut.print(">");
    }

    @Override
    public void onSentDate (final Date sentDate, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getSentDateTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getSentDateClassName());
        htmlOut.print("\">");

        renderDate(sentDate, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getSentDateTagName());
        htmlOut.print(">");
    }

    @Override
    public void onNoSentDate (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getSentDateTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getSentDateClassName());
        htmlOut.print(" ");
        htmlOut.print(getSentDateEmptyClassName());
        htmlOut.print("\">");

        htmlOut.print(getSentDateEmptyText());

        htmlOut.print("</");
        htmlOut.print(getSentDateTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeRecipientLists (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getRecipientListsTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getRecipientListsClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterRecipientLists (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getRecipientListsTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeRecipientList (final RecipientList recipientList, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getRecipientListTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getRecipientListClassName());
        htmlOut.print(" ");
        htmlOut.print(recipientList.toString().toLowerCase());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterRecipientList (final RecipientList recipientList, final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getRecipientListTagName());
        htmlOut.print(">");
    }

    @Override
    public void onEmptyRecipientList (final RecipientList recipientList, final PrintWriter htmlOut)
    {
        // Render nothing.
    }

    @Override
    public void onRecipient (final Mailbox recipient, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getRecipientTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getRecipientClassName());
        htmlOut.print("\">");

        renderMailbox(recipient, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getRecipientTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeAttachmentList (final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getAttachmentListTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getAttachmentListClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterAttachmentList (final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getAttachmentListTagName());
        htmlOut.print(">");
    }

    @Override
    public void onEmptyAttachmentList (final PrintWriter htmlOut)
    {
        // Render nothing.
    }

    @Override
    public void onAttachment (final IncorporatedPart attachment, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getAttachmentTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getAttachmentClassName());
        htmlOut.print("\">");

        renderPartDownloadLink(attachment, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getAttachmentTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforeBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getBodyTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getBodyClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getBodyTagName());
        htmlOut.print(">");
    }

    @Override
    public void onEmptyBody (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getBodyTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getBodyClassName());
        htmlOut.print(" ");
        htmlOut.print(getEmptyBodyClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getEmptyBodyText()));

        htmlOut.print("</");
        htmlOut.print(getBodyTagName());
        htmlOut.print(">");
    }

    @Override
    public void onInlineAttachment (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getPartTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineStubClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineAttachmentClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getInlineAttachmentText(part)));

        renderPartDownload(part, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getPartTagName());
        htmlOut.print(">");
    }

    @Override
    public void onInlineAlternative (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getPartTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineStubClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineAlternativeClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getInlineAlternativeText(part)));

        renderPartDownload(part, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getPartTagName());
        htmlOut.print(">");
    }

    @Override
    public void onExternalPartContent (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getPartTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineStubClassName());
        htmlOut.print(" ");
        htmlOut.print(getExternalContentClass());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getExternalPartContentText(part)));

        htmlOut.print("</");
        htmlOut.print(getPartTagName());
        htmlOut.print(">");
    }

    @Override
    public void onBeforePart (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("<");
        htmlOut.print(getPartTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartClassName());
        htmlOut.print(" ");
        htmlOut.print(getMimeTypeTypeClassNamePrefix());
        htmlOut.print(part.getContentType().getMimeType().replaceAll("[^A-Za-z0-9]", "_"));
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");
    }

    @Override
    public void onAfterPart (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        htmlOut.print("</");
        htmlOut.print(getPartTagName());
        htmlOut.print(">");
    }

    @Override
    public void onRenderException (final IncorporatedPart part, final PrintWriter htmlOut,
                                   final MimeUIException exception)
    {
        htmlOut.print("<");
        htmlOut.print(getPartTagName());
        htmlOut.print(" class=\"");
        htmlOut.print(getPartClassName());
        htmlOut.print(" ");
        htmlOut.print(getInlineStubClassName());
        htmlOut.print(" ");
        htmlOut.print(getRenderExceptionClassName());
        htmlOut.print(" ");
        htmlOut.print(getExtraClassName());
        htmlOut.print("\">");

        htmlOut.print(StringEscapeUtils.escapeHtml4(getRenderExceptionText(part, exception)));

        renderPartDownload(part, htmlOut);

        htmlOut.print("</");
        htmlOut.print(getPartTagName());
        htmlOut.print(">");
    }

    @Override
    public void onUnsupportedContentType (final IncorporatedPart part, final PrintWriter htmlOut)
    {
        onInlineAttachment(part, htmlOut);
    }
}
