package com.m1.mimeui.api.codec;

public interface DataUriCodec extends UriCodec<DataUriPayload>
{
    public final static String DATA_URI_SCHEME = "data:"; // RFC-2397 Section 3

    public DataUriPayload parse (final String dataUri);
    public String toString (final DataUriPayload data);
}
