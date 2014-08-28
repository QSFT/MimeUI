package com.m1.mimeui.utils;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.Renderer;

import java.io.PrintWriter;

public class RenderingAnalysisHandler implements AnalysisHandler<Unused>
{
    private final Renderer<AnalyzedMessage> renderer;
    private final ContentViewerFactory contentViewerFactory;
    private final PrintWriter htmlOut;

    public RenderingAnalysisHandler (final Renderer<AnalyzedMessage> renderer,
                                     final ContentViewerFactory contentViewerFactory, final PrintWriter htmlOut)
    {
        this.renderer = renderer;
        this.contentViewerFactory = contentViewerFactory;
        this.htmlOut = htmlOut;
    }

    @Override
    public Unused handle (final AnalyzedMessage analyzedMessage)
            throws MimeUIException
    {
        this.renderer.render(analyzedMessage, this.contentViewerFactory, this.htmlOut);

        return null;
    }
}
