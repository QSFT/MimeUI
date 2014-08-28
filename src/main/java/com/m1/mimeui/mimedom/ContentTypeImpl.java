package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.ContentType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ContentTypeImpl implements ContentType
{
    private final String mimeType;
    private final List<String> extensions;
    private final boolean hasSecurityConsiderations;

    public ContentTypeImpl (final String mimeType,
                            final Collection<String> extensions, final boolean hasSecurityConsiderations)
    {
        this.mimeType = mimeType.toLowerCase();
        this.extensions = new ArrayList<String>(extensions.size());
        this.hasSecurityConsiderations = hasSecurityConsiderations;

        this.extensions.addAll(extensions);
    }

    @Override
    public String getMimeType ()
    {
        return this.mimeType;
    }

    @Override
    public List<String> getExtensions ()
    {
        return Collections.unmodifiableList(this.extensions);
    }

    @Override
    public String getDefaultExtension ()
    {
        return this.extensions.isEmpty() ? null : this.extensions.get(0);
    }

    @Override
    public boolean hasSecurityConsiderations ()
    {
        return this.hasSecurityConsiderations;
    }

    @Override
    public boolean equals (final String type, final String subtype)
    {
        return this.mimeType.equals(type.trim().toLowerCase() + "/" + subtype.trim().toLowerCase());
    }

    // todo Implement equals and hash code.

    @Override
    public String toString()
    {
        return this.mimeType;
    }
}
