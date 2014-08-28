package com.m1.mimeui.analyzer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.mimedom.AnalyzedMessageFactory;

public class MessageAnalyzerImpl<E> implements MessageAnalyzer<E>
{
    private final AnalyzedMessageFactory<E> analyzedMessageFactory;

    public MessageAnalyzerImpl (final AnalyzedMessageFactory<E> analyzedMessageFactory)
    {
        this.analyzedMessageFactory = analyzedMessageFactory;
    }

    @Override
    public <T> T analyzeAndExecute (final E messageEntity, final ContentTypeAdjudicator contentTypeAdjudicator,
                                    final AnalysisHandler<T> handler)
            throws MimeUIException
    {
        return handler.handle(this.analyzedMessageFactory.getAnalyzedMessage(messageEntity, contentTypeAdjudicator));
    }
}
