package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.Part;
import org.apache.commons.lang3.ObjectUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MixedPartProvider<E> implements PartProvider<E>
{
    private final EntityHandler<E> entityHandler;
    private final PartProviderFactory<E> partProviderFactory;

    public MixedPartProvider (final EntityHandler<E> entityHandler,
                              final PartProviderFactory<E> partProviderFactory)
    {
        this.entityHandler = entityHandler;
        this.partProviderFactory = partProviderFactory;
    }

    @Override
    public List<Part> getParts (final E entity, final URI contextLocation,
                                final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        final List<Part> parts = new ArrayList<Part>();
        final List<E> entities = this.entityHandler.getMultipartChildEntities(entity);
        final URI contentLocation = ObjectUtils.defaultIfNull(
                this.entityHandler.getContentLocation(entity, contextLocation), contextLocation);

        for (final E childEntity : entities)
        {
            final EntityHandler.EntityType entityType = this.entityHandler.getEntityType(childEntity);
            final PartProvider<E> partProvider = this.partProviderFactory.getPartProvider(entityType);

            parts.addAll(partProvider.getParts(childEntity, contentLocation, contentTypeAdjudicator));
        }

        return parts;
    }
}
