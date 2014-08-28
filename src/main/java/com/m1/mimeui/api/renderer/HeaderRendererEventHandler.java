package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.RecipientList;

import java.io.PrintWriter;
import java.util.Date;

public interface HeaderRendererEventHandler
{
    public void onBeforeHeader (final PrintWriter htmlOut);
    public void onAfterHeader (final PrintWriter htmlOut);

    public void onBeforeBasic (final PrintWriter htmlOut);
    public void onAfterBasic (final PrintWriter htmlOut);

    public void onSender (final Mailbox sender, final PrintWriter htmlOut);
    public void onNoSender (final PrintWriter htmlOut);

    public void onBeforeFromList (final PrintWriter htmlOut);
    public void onAfterFromList (final PrintWriter htmlOut);
    public void onEmptyFromList (final PrintWriter htmlOut);

    public void onFrom (final Mailbox from, final PrintWriter htmlOut);

    public void onSubject (final String subject, final PrintWriter htmlOut);
    public void onNoSubject (final PrintWriter htmlOut);
    public void onSentDate (final Date sentDate, final PrintWriter htmlOut);
    public void onNoSentDate (final PrintWriter htmlOut);

    public void onBeforeRecipientLists (final PrintWriter htmlOut);
    public void onAfterRecipientLists (final PrintWriter htmlOut);

    public void onBeforeRecipientList (final RecipientList recipientList, final PrintWriter htmlOut);
    public void onAfterRecipientList (final RecipientList recipientList, final PrintWriter htmlOut);
    public void onEmptyRecipientList (final RecipientList recipientList, final PrintWriter htmlOut);

    public void onRecipient (final Mailbox recipient, final PrintWriter htmlOut);
}
