package com.m1.mimeui.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.AttachmentListRenderer;
import com.m1.mimeui.api.renderer.BodyRenderer;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.HeaderRenderer;
import com.m1.mimeui.api.renderer.MessageRenderer;
import com.m1.mimeui.api.renderer.MessageRendererEventHandler;

import java.io.PrintWriter;

public class MessageRendererImpl implements MessageRenderer
{
    private final MessageRendererEventHandler eventHandler;
    private final HeaderRenderer headerRenderer;
    private final AttachmentListRenderer attachmentListRenderer;
    private final BodyRenderer bodyRenderer;

    public MessageRendererImpl (final MessageRendererEventHandler eventHandler,
                                final HeaderRenderer headerRenderer,
                                final AttachmentListRenderer attachmentListRenderer,
                                final BodyRenderer bodyRenderer)
    {
        this.eventHandler = eventHandler;
        this.headerRenderer = headerRenderer;
        this.attachmentListRenderer = attachmentListRenderer;
        this.bodyRenderer = bodyRenderer;
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
            throws MimeUIException
    {
        this.eventHandler.onBeforeMessage(htmlOut);

        try
        {
            this.headerRenderer.render(analyzedMessage, contentViewerFactory, htmlOut);
            this.attachmentListRenderer.render(analyzedMessage, contentViewerFactory, htmlOut);
            this.bodyRenderer.render(analyzedMessage, contentViewerFactory, htmlOut);
        }
        finally
        {
            this.eventHandler.onAfterMessage(htmlOut);
        }
    }
}
