package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.parser.MessageParser;
import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.renderer.ContentViewer;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.MessageRenderer;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import com.m1.mimeui.codec.CidUriCodecImpl;
import com.m1.mimeui.codec.MailtoUriCodecImpl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused") // For external use.
public class DefaultContentViewerFactory<E> implements ContentViewerFactory, ContentTypeAdjudicator
{
    private final Map<String, ContentViewer> mimeTypeMap = new HashMap<String, ContentViewer>();
    private final ContentLocationProvider contentLocationProvider;
    private final UntrustedContentUriResolver.ExternalReferenceMode externalReferenceMode;
    private final MessageParser<E> parser;
    private final MessageAnalyzer<E> analyzer;
    private final MessageRenderer renderer;

    public DefaultContentViewerFactory (final ContentLocationProvider contentLocationProvider,
                                        final UntrustedContentUriResolver.ExternalReferenceMode externalReferenceMode,
                                        final MessageParser<E> parser, final MessageAnalyzer<E> analyzer,
                                        final MessageRenderer renderer)
    {
        this.contentLocationProvider = contentLocationProvider;
        this.externalReferenceMode = externalReferenceMode;
        this.parser = parser;
        this.analyzer = analyzer;
        this.renderer = renderer;

        indexContentViewers();
    }

    @Override
    public ContentViewer getContentViewer (final String mimeType)
    {
        return this.mimeTypeMap.get(mimeType);
    }

    @Override
    public boolean isSupportedContentType (final String mimeType, final String characterEncoding)
    {
        return this.mimeTypeMap.containsKey(mimeType) && Charset.isSupported(characterEncoding);
    }

    protected Collection<ContentViewer> getContentViewers ()
    {
        final Collection<ContentViewer> contentViewers = new ArrayList<ContentViewer>();

        contentViewers.add(new MessageContentViewer<E>(this.parser, this.analyzer, this.renderer, this, this));
        contentViewers.add(new HTMLContentViewer(getUntrustedContentUriResolver(), this.contentLocationProvider));
        contentViewers.add(new ImageContentViewer(this.contentLocationProvider));
        contentViewers.add(new PlainTextContentViewer());

        return contentViewers;
    }

    protected UntrustedContentUriResolver getUntrustedContentUriResolver ()
    {
        return new UntrustedContentUriResolverImpl(this.contentLocationProvider,
                this.externalReferenceMode,
                new MailtoUriCodecImpl(),
                new CidUriCodecImpl());
    }

    private void indexContentViewers ()
    {
        for (final ContentViewer contentViewer : getContentViewers())
        {
            final Collection<String> mimeTypes = contentViewer.getSupportedMimeTypes();

            for (final String mimeType : mimeTypes)
            {
                this.mimeTypeMap.put(mimeType, contentViewer);
            }
        }
    }
}
