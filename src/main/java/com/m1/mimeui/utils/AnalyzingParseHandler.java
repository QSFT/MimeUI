package com.m1.mimeui.utils;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.parser.ParseHandler;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;

public class AnalyzingParseHandler<E, T> implements ParseHandler<E, T>
{
    private final MessageAnalyzer<E> analyzer;
    private final ContentTypeAdjudicator contentTypeAdjudicator;
    private final AnalysisHandler<T> handler;

    public AnalyzingParseHandler (final MessageAnalyzer<E> analyzer,
                                  final ContentTypeAdjudicator contentTypeAdjudicator,
                                  final AnalysisHandler<T> handler)
    {
        this.analyzer = analyzer;
        this.contentTypeAdjudicator = contentTypeAdjudicator;
        this.handler = handler;
    }

    @Override
    public T handle (final E messageEntity)
            throws MimeUIException
    {
        return this.analyzer.analyzeAndExecute(messageEntity, this.contentTypeAdjudicator, handler);
    }
}
