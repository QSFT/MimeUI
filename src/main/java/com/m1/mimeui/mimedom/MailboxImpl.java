package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Mailbox;

public class MailboxImpl implements Mailbox
{
    private final String displayName;
    private final String emailAddress;

    public MailboxImpl (final String displayName, final String emailAddress)
    {
        this.displayName = displayName;
        this.emailAddress = emailAddress;
    }

    @Override
    public String getDisplayName ()
    {
        return this.displayName;
    }

    @Override
    public String getEmailAddress ()
    {
        return this.emailAddress;
    }
}
