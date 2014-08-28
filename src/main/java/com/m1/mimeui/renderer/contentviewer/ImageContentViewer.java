package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewer;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ImageContentViewer implements ContentViewer
{
    private final ContentLocationProvider contentLocationProvider;

    public ImageContentViewer (final ContentLocationProvider contentLocationProvider)
    {
        this.contentLocationProvider = contentLocationProvider;
    }

    @Override
    public Collection<String> getSupportedMimeTypes ()
    {
        // Important: Only state that we support image types that are commonly supported by browsers.
        //            In other words, we don't claim to support bmp, ico, svg, tif, etc...
        return new ArrayList<String>(Arrays.asList("image/png", "image/gif", "image/jpeg", "image/pjpeg"));
    }

    @Override
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException
    {
        try
        {
            htmlOut.print("<img src=\"");
            htmlOut.print(StringEscapeUtils.escapeHtml4(this.contentLocationProvider.getRawPartDownloadUrl(part)));
            htmlOut.print("\" alt=\"");
            htmlOut.print(StringEscapeUtils.escapeHtml4(part.getFilename()));
            htmlOut.print("\"");
            htmlOut.println("/>");
        }
        catch (ContentLocationProvider.UnsupportedServiceException e)
        {
            throw new MimeUIException("The location of this content cannot be determined.");
        }
    }
}
