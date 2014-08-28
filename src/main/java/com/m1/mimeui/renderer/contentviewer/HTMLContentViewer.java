package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewer;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.HTMLTransformer;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.HTMLWhiteList;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

public class HTMLContentViewer implements ContentViewer
{
    private final UntrustedContentUriResolver untrustedContentUriResolver;
    private final ContentLocationProvider contentLocationProvider;

    public HTMLContentViewer (final UntrustedContentUriResolver untrustedContentUriResolver,
                              final ContentLocationProvider contentLocationProvider)
    {
        this.untrustedContentUriResolver = untrustedContentUriResolver;
        this.contentLocationProvider = contentLocationProvider;
    }

    @Override
    public Collection<String> getSupportedMimeTypes ()
    {
        return Collections.singleton("text/html");
    }

    @Override
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException
    {
        InputStream inputStream = null;

        try
        {
            inputStream = part.getInputStream();

            // todo Move all of this logic to a separate class with a String sanitize(String) method.
            // This normalizes and sanitizes the HTML, preventing cross site scripting attacks and other issues.
            final Cleaner htmlCleaner = new Cleaner(new HTMLWhiteList());
            final HTMLTransformer htmlTransformer
                    = new HTMLTransformer(part, this.untrustedContentUriResolver, this.contentLocationProvider);
            final String htmlSource = IOUtils.toString(inputStream, part.getCharacterEncoding());
            final Document dirtyDocument = Jsoup.parse(htmlSource, part.getContextLocation().toString());
            final Document cleanDocument = htmlCleaner.clean(dirtyDocument);

            htmlTransformer.transform(cleanDocument);

            // this removes the body element, which often contains a style/class attribute.
            htmlOut.println(cleanDocument.body().html());
        }
        catch (final IOException e)
        {
            throw new MimeUIException("Unable to read a textual part.", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
