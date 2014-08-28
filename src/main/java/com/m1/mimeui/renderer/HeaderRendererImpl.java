package com.m1.mimeui.renderer;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.RecipientList;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.HeaderRenderer;
import com.m1.mimeui.api.renderer.HeaderRendererEventHandler;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Set;

public class HeaderRendererImpl implements HeaderRenderer
{
    private final HeaderRendererEventHandler eventHandler;

    public HeaderRendererImpl (final HeaderRendererEventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    protected void renderSender (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        final Mailbox sender = analyzedMessage.getSender();

        if (sender != null)
        {
            this.eventHandler.onSender(sender, htmlOut);
        }
        else
        {
            this.eventHandler.onNoSender(htmlOut);
        }
    }

    protected void renderFromList (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        final Set<Mailbox> fromSet = analyzedMessage.getFroms();

        if (fromSet != null && !fromSet.isEmpty())
        {
            this.eventHandler.onBeforeFromList(htmlOut);

            for (final Mailbox from : fromSet)
            {
                this.eventHandler.onFrom(from, htmlOut);
            }

            this.eventHandler.onAfterFromList(htmlOut);
        }
        else
        {
            this.eventHandler.onEmptyFromList(htmlOut);
        }
    }

    protected void renderSubject (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        final String subject = analyzedMessage.getSubject();

        if (subject != null)
        {
            this.eventHandler.onSubject(subject, htmlOut);
        }
        else
        {
            this.eventHandler.onNoSubject(htmlOut);
        }

    }

    protected void renderSentDate (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        final Date sentDate = analyzedMessage.getSentDate();

        if (sentDate != null)
        {
            this.eventHandler.onSentDate(sentDate, htmlOut);
        }
        else
        {
            this.eventHandler.onNoSentDate(htmlOut);
        }
    }

    protected void renderBasicHeaders (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        this.eventHandler.onBeforeBasic(htmlOut);

        renderSender(analyzedMessage, htmlOut);
        renderFromList(analyzedMessage, htmlOut);
        renderSentDate(analyzedMessage, htmlOut);
        renderSubject(analyzedMessage, htmlOut);

        this.eventHandler.onAfterBasic(htmlOut);
    }

    protected void renderRecipientList (final RecipientList recipientList, final AnalyzedMessage analyzedMessage,
                                        final PrintWriter htmlOut)
    {
        final Set<Mailbox> recipients = analyzedMessage.getRecipients().get(recipientList);

        if (recipients != null && !recipients.isEmpty())
        {
            this.eventHandler.onBeforeRecipientList(recipientList, htmlOut);

            for (final Mailbox recipient : recipients)
            {
                this.eventHandler.onRecipient(recipient, htmlOut);
            }

            this.eventHandler.onAfterRecipientList(recipientList, htmlOut);
        }
        else
        {
            this.eventHandler.onEmptyRecipientList(recipientList, htmlOut);
        }
    }

    protected void renderRecipientLists (final AnalyzedMessage analyzedMessage, final PrintWriter htmlOut)
    {
        this.eventHandler.onBeforeRecipientLists(htmlOut);

        for (final RecipientList recipientList : RecipientList.values())
        {
            renderRecipientList(recipientList, analyzedMessage, htmlOut);
        }

        this.eventHandler.onAfterRecipientLists(htmlOut);
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
    {
        this.eventHandler.onBeforeHeader(htmlOut);

        renderBasicHeaders(analyzedMessage, htmlOut);
        renderRecipientLists(analyzedMessage, htmlOut);

        this.eventHandler.onAfterHeader(htmlOut);
    }
}
