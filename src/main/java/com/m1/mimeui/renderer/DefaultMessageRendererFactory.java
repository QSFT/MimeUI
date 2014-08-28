package com.m1.mimeui.renderer;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.AttachmentListRenderer;
import com.m1.mimeui.api.renderer.AttachmentListRendererEventHandler;
import com.m1.mimeui.api.renderer.BodyRenderer;
import com.m1.mimeui.api.renderer.BodyRendererEventHandler;
import com.m1.mimeui.api.renderer.DocumentRenderer;
import com.m1.mimeui.api.renderer.DocumentRendererEventHandler;
import com.m1.mimeui.api.renderer.HeaderRenderer;
import com.m1.mimeui.api.renderer.HeaderRendererEventHandler;
import com.m1.mimeui.api.renderer.MessageRenderer;
import com.m1.mimeui.api.renderer.MessageRendererEventHandler;
import com.m1.mimeui.api.renderer.MessageRendererFactory;
import com.m1.mimeui.api.renderer.PartRenderer;
import com.m1.mimeui.api.renderer.PartRendererEventHandler;
import com.m1.mimeui.api.renderer.Renderer;

@SuppressWarnings("unused") // For external use.
public class DefaultMessageRendererFactory implements MessageRendererFactory
{
    @Override
    public DocumentRenderer getDocumentRenderer (final Renderer<AnalyzedMessage> renderer,
                                                 final DocumentRendererEventHandler eventHandler)
    {
        return new DocumentRendererImpl(renderer, eventHandler);
    }

    @Override
    public MessageRenderer getMessageRenderer (final MessageRendererEventHandler eventHandler)
    {
        return new MessageRendererImpl(
                eventHandler,
                getHeaderRenderer(eventHandler),
                getAttachmentListRenderer(eventHandler),
                getBodyRenderer(eventHandler));
    }

    @Override
    public HeaderRenderer getHeaderRenderer (final HeaderRendererEventHandler eventHandler)
    {
        return new HeaderRendererImpl(eventHandler);
    }

    @Override
    public AttachmentListRenderer getAttachmentListRenderer (final AttachmentListRendererEventHandler eventHandler)
    {
        return new AttachmentListRendererImpl(eventHandler);
    }

    @Override
    public BodyRenderer getBodyRenderer (final BodyRendererEventHandler eventHandler)
    {
        return new BodyRendererImpl(eventHandler, getPartRenderer(eventHandler));
    }

    @Override
    public PartRenderer getPartRenderer (final PartRendererEventHandler eventHandler)
    {
        return new PartRendererImpl(eventHandler);
    }
}
