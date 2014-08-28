package com.m1.mimeui.analyzer;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;

public class PleasantContentTypeAdjudicator implements ContentTypeAdjudicator
{
    @Override
    public boolean isSupportedContentType (final String mimeType, final String characterEncoding)
    {
        return true;
    }
}
