package com.m1.mimeui.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewer;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.PartRenderer;
import com.m1.mimeui.api.renderer.PartRendererEventHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

public class PartRendererImpl implements PartRenderer
{
    private static final int INITIAL_BUFFER_SIZE = 4096;

    private final PartRendererEventHandler eventHandler;

    public PartRendererImpl (final PartRendererEventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    @Override
    public void render (final IncorporatedPart part, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
    {
        final ContentViewer contentViewer = contentViewerFactory.getContentViewer(part.getContentType().getMimeType());

        if (contentViewer != null)
        {
            try
            {
                this.eventHandler.onBeforePart(part, htmlOut);

                // The output buffer is intended to prevent partial output on ContentViewerException.
                final StringWriter outputBuffer = new StringWriter(INITIAL_BUFFER_SIZE);

                contentViewer.render(part, new PrintWriter(outputBuffer, true));

                htmlOut.print(outputBuffer.toString());
            }
            catch (final MimeUIException e)
            {
                this.eventHandler.onRenderException(part, htmlOut, e);
            }
            finally
            {
                this.eventHandler.onAfterPart(part, htmlOut);
            }
        }
        else
        {
            this.eventHandler.onUnsupportedContentType(part, htmlOut);
        }
    }
}
