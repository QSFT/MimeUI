package com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations;

import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;

public class ImgSrcCidTransformation implements Transformation
{
    private final IncorporatedPart part;
    private final UntrustedContentUriResolver untrustedContentUriResolver;
    private final String imagePlaceholderUrl;

    public ImgSrcCidTransformation (final IncorporatedPart part,
                                    final UntrustedContentUriResolver untrustedContentUriResolver,
                                    final String imagePlaceholderUrl)
    {
        this.part = part;
        this.untrustedContentUriResolver = untrustedContentUriResolver;
        this.imagePlaceholderUrl = imagePlaceholderUrl;
    }

    @Override
    public String getSelector ()
    {
        return "img";
    }

    @Override
    public void transform (final Element element)
    {
        try
        {
            final URI srcUri = new URI(element.attr("src"));
            final String absoluteSrc = part.getContextLocation().resolve(srcUri).toString();

            element.attr("src",
                    this.untrustedContentUriResolver.resolveUntrustedUri(absoluteSrc, this.part.getAnalyzedMessage(), false));
        }
        catch (final UntrustedContentUriResolver.IllegalUriException e)
        {
            if (this.imagePlaceholderUrl != null)
            {
                element.attr("src", this.imagePlaceholderUrl);
            }
            else
            {
                element.removeAttr("src");
            }
        }
        catch (final URISyntaxException e)
        {
            if (this.imagePlaceholderUrl != null)
            {
                element.attr("src", this.imagePlaceholderUrl);
            }
            else
            {
                element.removeAttr("src");
            }
        }
    }
}
