package com.m1.mimeui.api.analyzer;

import com.m1.mimeui.api.EntityHandler;

public interface PartProviderFactory<E>
{
    public PartProvider<E> getPartProvider (final EntityHandler.EntityType entityType);
}
