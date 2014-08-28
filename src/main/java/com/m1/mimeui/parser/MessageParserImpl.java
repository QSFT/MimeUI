package com.m1.mimeui.parser;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.parser.MessageParser;
import com.m1.mimeui.api.parser.ParseHandler;

import java.io.InputStream;

public class MessageParserImpl<E> implements MessageParser<E>
{
    private final EntityHandler<E> entityHandler;

    public MessageParserImpl (final EntityHandler<E> entityHandler)
    {
        this.entityHandler = entityHandler;
    }

    @Override
    public <T> T parseAndExecute (final InputStream inputStream, final ParseHandler<E, T> parseHandler)
            throws MimeUIException
    {
        return this.entityHandler.parseAndExecute(inputStream, parseHandler);
    }
}
