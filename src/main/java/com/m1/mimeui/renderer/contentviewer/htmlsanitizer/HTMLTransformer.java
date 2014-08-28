package com.m1.mimeui.renderer.contentviewer.htmlsanitizer;

import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations.ImgSrcCidTransformation;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations.LinkTransformation;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations.StyleAttributeSanitizingTransformation;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations.StyleElementSanitizingTransformation;
import com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations.Transformation;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;

public class HTMLTransformer
{
    private final Collection<Transformation> htmlTransformations = new ArrayList<Transformation>();

    public HTMLTransformer (final IncorporatedPart part,
                            final UntrustedContentUriResolver untrustedContentUriResolver,
                            final ContentLocationProvider contentLocationProvider)
    {
        String imagePlaceholderUrl;

        try
        {
            imagePlaceholderUrl = contentLocationProvider.getDefaultImageUrl();
        }
        catch (final ContentLocationProvider.UnsupportedServiceException e)
        {
            imagePlaceholderUrl = null;
        }

        this.htmlTransformations.add(new ImgSrcCidTransformation(part, untrustedContentUriResolver, imagePlaceholderUrl));
        this.htmlTransformations.add(new LinkTransformation(part, untrustedContentUriResolver));
        this.htmlTransformations.add(new StyleElementSanitizingTransformation());
        this.htmlTransformations.add(new StyleAttributeSanitizingTransformation());
    }

    public void transform (final Document cleanDocument)
    {
        for (final Transformation transformation : this.htmlTransformations)
        {
            final Elements elements = cleanDocument.select(transformation.getSelector());

            for (final Element element : elements)
            {
                transformation.transform(element);
            }
        }
    }
}
