package com.m1.mimeui.api.codec;

public enum MailtoUriField
{
    TO("to"),
    CC("cc"),
    // BCC("bcc"), // Removing due to security issues. See RFC-6068 Section 7.
    SUBJECT("subject"),
    BODY("body"),
    ;
    private final String headerName;

    MailtoUriField (final String headerName)
    {
        this.headerName = headerName;
    }

    public String getHeaderName ()
    {
        return this.headerName;
    }

    public static MailtoUriField fromHeaderName (final String headerName)
    {
        for (final MailtoUriField mailtoUriField : MailtoUriField.values())
        {
            if (mailtoUriField.getHeaderName().toLowerCase().equals(headerName.toLowerCase()))
            {
                return mailtoUriField;
            }
        }

        return null;
    }
}
