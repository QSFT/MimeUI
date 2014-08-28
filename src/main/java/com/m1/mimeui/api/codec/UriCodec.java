package com.m1.mimeui.api.codec;

public interface UriCodec<T>
{
    public T parse (final String uri);
    public String toString (final T object);
}
