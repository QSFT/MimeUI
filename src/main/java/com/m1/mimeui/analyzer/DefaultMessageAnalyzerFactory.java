package com.m1.mimeui.analyzer;

import com.m1.mimeui.analyzer.partprovider.PartProviderFactoryImpl;
import com.m1.mimeui.api.ContentTypeFactory;
import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.analyzer.MessageAnalyzerFactory;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.AnalyzedMessageFactory;
import com.m1.mimeui.api.mimedom.PartFactory;
import com.m1.mimeui.mimedom.AnalyzedMessageFactoryImpl;
import com.m1.mimeui.mimedom.PartFactoryImpl;

public class DefaultMessageAnalyzerFactory<E> implements MessageAnalyzerFactory<E>
{
    private final EntityHandler<E> entityHandler;
    private final ContentTypeFactory contentTypeFactory;

    public DefaultMessageAnalyzerFactory (final EntityHandler<E> entityHandler,
                                          final ContentTypeFactory contentTypeFactory)
    {
        this.entityHandler = entityHandler;
        this.contentTypeFactory = contentTypeFactory;
    }

    @Override
    public MessageAnalyzer<E> getMessageAnalyser ()
    {
        final PartFactory<E> partFactory
                = new PartFactoryImpl<E>(this.entityHandler, this.contentTypeFactory);
        final PartHandler partHandler
                = new PartHandlerImpl();
        final PartProviderFactory<E> partProviderFactory
                = new PartProviderFactoryImpl<E>(partFactory, this.entityHandler, partHandler);
        final AnalyzedMessageFactory<E> analyzedMessageFactory
                = new AnalyzedMessageFactoryImpl<E>(this.entityHandler, partProviderFactory);

        return new MessageAnalyzerImpl<E>(analyzedMessageFactory);
    }
}
