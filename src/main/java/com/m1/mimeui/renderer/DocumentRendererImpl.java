package com.m1.mimeui.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.DocumentRenderer;
import com.m1.mimeui.api.renderer.DocumentRendererEventHandler;
import com.m1.mimeui.api.renderer.Renderer;

import java.io.PrintWriter;

public class DocumentRendererImpl implements DocumentRenderer
{
    final Renderer<AnalyzedMessage> renderer;
    private final DocumentRendererEventHandler eventHandler;

    public DocumentRendererImpl (final Renderer<AnalyzedMessage> renderer,
                                 final DocumentRendererEventHandler eventHandler)
    {
        this.renderer = renderer;
        this.eventHandler = eventHandler;
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
            throws MimeUIException
    {
        this.eventHandler.onBeforeDocument(htmlOut);

        this.renderer.render(analyzedMessage, contentViewerFactory, htmlOut);

        this.eventHandler.onAfterDocument(htmlOut);
    }
}
