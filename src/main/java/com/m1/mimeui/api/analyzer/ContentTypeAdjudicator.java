package com.m1.mimeui.api.analyzer;

public interface ContentTypeAdjudicator
{
    /**
     * Indicates whether or not a given content type is supported for analysis purposes. Unsupported content types will
     * be triaged to the disposition "INLINE_ATTACHMENT" instead of "INLINE", and will not be selected in
     * multipart/alternative parts.
     *
     * <p />The typical use case for this is for this to return "false" for any part that we can't render.
     *
     * @param mimeType The MIME type for the content being adjudicated, for example "text/plain".
     * @param characterEncoding The character encoding for the content being adjudicated, for example "iso-8859-1".
     * @return True if the content type is supported, false if it is not supported.
     */
    public boolean isSupportedContentType (final String mimeType, final String characterEncoding);
}
