package com.m1.mimeui.api.parser;

import com.m1.mimeui.api.MimeUIException;

public interface ParseHandler<E, T>
{
    public T handle (final E messageEntity)
            throws MimeUIException;
}
