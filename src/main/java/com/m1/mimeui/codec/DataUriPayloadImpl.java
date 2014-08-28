package com.m1.mimeui.codec;

import com.m1.mimeui.api.codec.DataUriPayload;

public class DataUriPayloadImpl implements DataUriPayload
{
    private final String contentType;
    private final byte[] bytes;

    public DataUriPayloadImpl (final String contentType, final byte[] bytes)
    {
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public String getContentType ()
    {
        return contentType;
    }

    @Override
    public byte[] getBytes ()
    {
        return bytes;
    }
}
