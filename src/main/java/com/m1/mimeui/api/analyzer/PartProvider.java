package com.m1.mimeui.api.analyzer;

import com.m1.mimeui.api.mimedom.Part;

import java.net.URI;
import java.util.List;

public interface PartProvider<E>
{
    public List<Part> getParts (final E entity, final URI contextLocation,
                                final ContentTypeAdjudicator contentTypeAdjudicator);
}
