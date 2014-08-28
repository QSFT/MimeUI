package com.m1.mimeui.api;

import com.m1.mimeui.api.mimedom.ContentType;

public interface ContentTypeFactory
{
    public ContentType getContentType (final String mimeType, final String filename);
}
