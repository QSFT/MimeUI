package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.mimedom.PartFactory;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class SinglePartProvider<E> implements PartProvider<E>
{
    private final PartFactory<E> partFactory;

    public SinglePartProvider (final PartFactory<E> partFactory)
    {
        this.partFactory = partFactory;
    }

    @Override
    public List<Part> getParts (final E entity, final URI contextLocation,
                                final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        return Collections.singletonList(this.partFactory.getPart(entity, contextLocation, contentTypeAdjudicator));
    }
}
