package com.m1.mimeui.renderer;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.AttachmentListRenderer;
import com.m1.mimeui.api.renderer.AttachmentListRendererEventHandler;
import com.m1.mimeui.api.renderer.ContentViewerFactory;

import java.io.PrintWriter;
import java.util.List;

public class AttachmentListRendererImpl implements AttachmentListRenderer
{
    private final AttachmentListRendererEventHandler eventHandler;

    public AttachmentListRendererImpl (final AttachmentListRendererEventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
    {
        final List<IncorporatedPart> attachments = analyzedMessage.getParts(Disposition.ATTACHMENT);

        if (!attachments.isEmpty())
        {
            this.eventHandler.onBeforeAttachmentList(htmlOut);

            for (final IncorporatedPart attachment : attachments)
            {
                this.eventHandler.onAttachment(attachment, htmlOut);
            }

            this.eventHandler.onAfterAttachmentList(htmlOut);
        }
        else
        {
            this.eventHandler.onEmptyAttachmentList(htmlOut);
        }
    }
}
