package com.m1.mimeui.codec;

import com.m1.mimeui.api.codec.CidUriCodec;

public class CidUriCodecImpl implements CidUriCodec
{
    @Override
    public String parse (final String cidUri)
    {
        return cidUri.substring(CID_URI_SCHEME.length()).trim();
    }

    @Override
    public String toString (final String contentId)
    {
        return CID_URI_SCHEME + contentId;
    }
}
