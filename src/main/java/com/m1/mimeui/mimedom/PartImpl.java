package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.InputStreamProvider;
import com.m1.mimeui.api.mimedom.Part;

import java.io.InputStream;
import java.net.URI;

public final class PartImpl implements Part
{
    private final String contentId;
    private final URI contextLocation;
    private final URI contentLocation;
    private final ContentType contentType;
    private final String characterEncoding;
    private final Disposition disposition;
    private final String filename;
    private final InputStreamProvider inputStreamProvider;

    public PartImpl (final String contentId, final URI contextLocation, final URI contentLocation,
                     final ContentType contentType, final String characterEncoding,
                     final Disposition disposition, final String filename, final InputStreamProvider inputStreamProvider)
    {
        this.contentId = contentId;
        this.contextLocation = contextLocation;
        this.contentLocation = contentLocation;
        this.contentType = contentType;
        this.characterEncoding = characterEncoding;
        this.disposition = disposition;
        this.filename = filename;
        this.inputStreamProvider = inputStreamProvider;
    }

    @Override
    public String getContentId ()
    {
        return this.contentId;
    }

    @Override
    public URI getContextLocation ()
    {
        return this.contextLocation;
    }

    @Override
    public URI getContentLocation ()
    {
        return this.contentLocation;
    }

    @Override
    public ContentType getContentType ()
    {
        return this.contentType;
    }

    @Override
    public String getCharacterEncoding ()
    {
        return this.characterEncoding;
    }

    @Override
    public Disposition getDisposition ()
    {
        return this.disposition;
    }

    @Override
    public String getFilename ()
    {
        return this.filename;
    }

    @Override
    public InputStream getInputStream ()
    {
        return this.inputStreamProvider.getInputStream();
    }
}
