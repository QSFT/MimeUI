package com.m1.mimeui.api.mimedom;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;

public interface AnalyzedMessageFactory<E>
{
    public AnalyzedMessage getAnalyzedMessage (final E messageEntity,
                                               final ContentTypeAdjudicator contentTypeAdjudicator);
}
