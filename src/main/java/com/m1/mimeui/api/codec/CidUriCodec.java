package com.m1.mimeui.api.codec;

public interface CidUriCodec extends UriCodec<String>
{
    public final static String CID_URI_SCHEME = "cid:"; // RFC-2392 Section 2

    public String parse (final String cidUri);
    public String toString (final String contentId);
}
