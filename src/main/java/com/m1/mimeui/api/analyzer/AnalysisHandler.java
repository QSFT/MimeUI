package com.m1.mimeui.api.analyzer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;

public interface AnalysisHandler<T>
{
    public T handle (final AnalyzedMessage analyzedMessage)
            throws MimeUIException;
}
