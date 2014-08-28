package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.utils.AnalyzingParseHandler;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.parser.MessageParser;
import com.m1.mimeui.api.renderer.ContentViewer;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.MessageRenderer;
import com.m1.mimeui.utils.RenderingAnalysisHandler;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

public class MessageContentViewer<E> implements ContentViewer
{
    private final MessageParser<E> parser;
    private final MessageAnalyzer<E> analyzer;
    private final MessageRenderer renderer;
    private final ContentViewerFactory contentViewerFactory;
    private ContentTypeAdjudicator contentTypeAdjudicator;

    public MessageContentViewer (final MessageParser<E> parser, final MessageAnalyzer<E> analyzer,
                                 final MessageRenderer renderer,
                                 final ContentViewerFactory contentViewerFactory,
                                 final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        this.parser = parser;
        this.analyzer = analyzer;
        this.renderer = renderer;
        this.contentViewerFactory = contentViewerFactory;
        this.contentTypeAdjudicator = contentTypeAdjudicator;
    }

    @Override
    public Collection<String> getSupportedMimeTypes ()
    {
        // This also includes .mht and .mhtml files. See RFC-2387.
        return Collections.singleton("message/rfc822");
    }

    @Override
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException
    {
        InputStream messageInputStream = null;

        try
        {
            messageInputStream = part.getInputStream();

            this.parser.parseAndExecute(
                    messageInputStream,
                    new AnalyzingParseHandler<E, Unused>(
                            this.analyzer,
                            this.contentTypeAdjudicator,
                            new RenderingAnalysisHandler(
                                    this.renderer,
                                    this.contentViewerFactory,
                                    htmlOut
                            )
                    )
            );
        }
        finally
        {
            IOUtils.closeQuietly(messageInputStream);
        }
    }
}
