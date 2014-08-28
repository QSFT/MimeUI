package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.PartFactory;

public class PartProviderFactoryImpl<E> implements PartProviderFactory<E>
{
    private final PartProvider<E> singlePartProvider;
    private final PartProvider<E> mixedPartProvider;
    private final PartProvider<E> alternativePartProvider;
    private final PartProvider<E> relatedPartProvider;

    public PartProviderFactoryImpl (final PartFactory<E> partFactory, final EntityHandler<E> entityHandler,
                                    final PartHandler partHandler)
    {
        this.singlePartProvider = new SinglePartProvider<E>(partFactory);
        this.mixedPartProvider = new MixedPartProvider<E>(entityHandler, this);
        this.alternativePartProvider = new AlternativePartProvider<E>(entityHandler, partHandler, this);
        this.relatedPartProvider = new RelatedPartProvider<E>(entityHandler, partHandler, this);
    }

    @Override
    public PartProvider<E> getPartProvider (final EntityHandler.EntityType entityType)
    {
        switch (entityType)
        {
            case SINGLE:
                return this.singlePartProvider;
            case RELATED:
                return this.relatedPartProvider;
            case ALTERNATIVE:
                return this.alternativePartProvider;
            case MIXED:
                return this.mixedPartProvider;
            default:
                return null;
        }
    }
}
