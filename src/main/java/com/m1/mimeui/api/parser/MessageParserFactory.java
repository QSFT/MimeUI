package com.m1.mimeui.api.parser;

public interface MessageParserFactory<E>
{
    public MessageParser<E> getMessageParser ();
}
