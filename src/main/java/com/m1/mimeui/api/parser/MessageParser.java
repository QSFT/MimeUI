package com.m1.mimeui.api.parser;

import com.m1.mimeui.api.MimeUIException;

import java.io.InputStream;

public interface MessageParser<E>
{
    public <T> T parseAndExecute (final InputStream inputStream, final ParseHandler<E, T> parseHandler)
            throws MimeUIException;
}
