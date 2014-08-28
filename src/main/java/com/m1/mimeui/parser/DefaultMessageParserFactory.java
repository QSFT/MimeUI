package com.m1.mimeui.parser;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.parser.MessageParser;
import com.m1.mimeui.api.parser.MessageParserFactory;

public class DefaultMessageParserFactory<E> implements MessageParserFactory<E>
{
    private final EntityHandler<E> entityHandler;

    public DefaultMessageParserFactory (final EntityHandler<E> entityHandler)
    {
        this.entityHandler = entityHandler;
    }

    @Override
    public MessageParser<E> getMessageParser ()
    {
        return new MessageParserImpl<E>(this.entityHandler);
    }
}
