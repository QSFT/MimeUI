package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.AnalyzedMessageFactory;

import java.net.URI;

public class AnalyzedMessageFactoryImpl<E> implements AnalyzedMessageFactory<E>
{
    private final EntityHandler<E> entityHandler;
    private final PartProviderFactory<E> partProviderFactory;

    public AnalyzedMessageFactoryImpl (final EntityHandler<E> entityHandler,
                                       final PartProviderFactory<E> partProviderFactory)
    {
        this.entityHandler = entityHandler;
        this.partProviderFactory = partProviderFactory;
    }

    @Override
    public AnalyzedMessage getAnalyzedMessage (final E messageEntity,
                                               final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        final EntityHandler.EntityType entityType = this.entityHandler.getEntityType(messageEntity);
        final PartProvider<E> partProvider = this.partProviderFactory.getPartProvider(entityType);

        // Implementation Note: RFC-2557 Section 5 items d and e: We default each message to "thismessage:/", not the
        // current server's URL (if applicable). That-is, we do not support item d.

        return new AnalyzedMessageImpl(
                partProvider.getParts(messageEntity, URI.create("thismessage:/"), contentTypeAdjudicator),
                this.entityHandler.getSubject(messageEntity),
                this.entityHandler.getSentDate(messageEntity),
                this.entityHandler.getSender(messageEntity),
                this.entityHandler.getFroms(messageEntity),
                this.entityHandler.getRecipients(messageEntity)
        );
    }
}
