package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;

@SuppressWarnings("unused") // For external use.
public interface MessageRendererFactory
{
    public DocumentRenderer getDocumentRenderer (final Renderer<AnalyzedMessage> renderer,
                                                 final DocumentRendererEventHandler eventHandler);
    public MessageRenderer getMessageRenderer (final MessageRendererEventHandler eventHandler);
    public HeaderRenderer getHeaderRenderer (final HeaderRendererEventHandler eventHandler);
    public AttachmentListRenderer getAttachmentListRenderer (final AttachmentListRendererEventHandler eventHandler);
    public BodyRenderer getBodyRenderer (final BodyRendererEventHandler eventHandler);
    public PartRenderer getPartRenderer (final PartRendererEventHandler eventHandler);
}
