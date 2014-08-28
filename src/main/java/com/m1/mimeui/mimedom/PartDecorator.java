package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.Part;

import java.io.InputStream;
import java.net.URI;

abstract class PartDecorator implements Part
{
    private final Part decoratee;

    PartDecorator (final Part decoratee)
    {
        this.decoratee = decoratee;
    }

    @Override
    public String getContentId ()
    {
        return this.decoratee.getContentId();
    }

    @Override
    public URI getContextLocation ()
    {
        return this.decoratee.getContextLocation();
    }

    @Override
    public URI getContentLocation ()
    {
        return this.decoratee.getContentLocation();
    }

    @Override
    public ContentType getContentType ()
    {
        return this.decoratee.getContentType();
    }

    @Override
    public String getCharacterEncoding ()
    {
        return this.decoratee.getCharacterEncoding();
    }

    @Override
    public Disposition getDisposition ()
    {
        return this.decoratee.getDisposition();
    }

    @Override
    public String getFilename ()
    {
        return this.decoratee.getFilename();
    }

    @Override
    public InputStream getInputStream ()
    {
        return this.decoratee.getInputStream();
    }
}
