package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.mimedom.InputStreamProvider;

import java.io.InputStream;

public class InputStreamProviderImpl<E> implements InputStreamProvider
{
    private final E entity;
    private final EntityHandler<E> entityHandler;

    public InputStreamProviderImpl (final E entity, final EntityHandler<E> entityHandler)
    {
        this.entity = entity;
        this.entityHandler = entityHandler;
    }

    @Override
    public InputStream getInputStream ()
    {
        return this.entityHandler.getInputStream(this.entity);
    }
}
