package com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations;

import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;

public class LinkTransformation implements Transformation
{
    private final IncorporatedPart part;
    private final UntrustedContentUriResolver untrustedContentUriResolver;

    public LinkTransformation (final IncorporatedPart part, final UntrustedContentUriResolver untrustedContentUriResolver)
    {
        this.part = part;
        this.untrustedContentUriResolver = untrustedContentUriResolver;
    }

    @Override
    public String getSelector ()
    {
        return "a[href]";
    }

    @Override
    public void transform (final Element element)
    {
        try
        {
            final URI hrefUri = new URI(element.attr("href"));
            final String absoluteHref = part.getContextLocation().resolve(hrefUri).toString();

            element.attr("href",
                    this.untrustedContentUriResolver.resolveUntrustedUri(absoluteHref, this.part.getAnalyzedMessage(), true));
            element.attr("target", "_blank");
        }
        catch (final UntrustedContentUriResolver.IllegalUriException e)
        {
            element.removeAttr("href");
        }
        catch (final URISyntaxException e)
        {
            element.removeAttr("href");
        }
    }
}
