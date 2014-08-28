package com.m1.mimeui.api.mimedom;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;

import java.net.URI;

public interface PartFactory<E>
{
    public Part getPart (final E entity, final URI contextLocation, final ContentTypeAdjudicator contentTypeAdjudicator);
}
