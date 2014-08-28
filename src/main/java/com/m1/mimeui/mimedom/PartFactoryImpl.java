package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.ContentTypeFactory;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.mimedom.PartFactory;

import java.net.URI;

public class PartFactoryImpl<E> implements PartFactory<E>
{
    private final EntityHandler<E> entityHandler;
    private final ContentTypeFactory contentTypeFactory;

    public PartFactoryImpl (final EntityHandler<E> entityHandler, final ContentTypeFactory contentTypeFactory)
    {
        this.entityHandler = entityHandler;
        this.contentTypeFactory = contentTypeFactory;
    }

    @Override
    public Part getPart (final E entity, final URI contextLocation, final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        final String filename = this.entityHandler.getFilename(entity);

        return new PartImpl(
                this.entityHandler.getContentId(entity),
                contextLocation,
                this.entityHandler.getContentLocation(entity, contextLocation),
                this.contentTypeFactory.getContentType(this.entityHandler.getMimeType(entity), filename),
                this.entityHandler.getCharacterEncoding(entity),
                this.entityHandler.getDisposition(entity, contentTypeAdjudicator),
                filename,
                new InputStreamProviderImpl<E>(entity, this.entityHandler)
        );
    }
}
