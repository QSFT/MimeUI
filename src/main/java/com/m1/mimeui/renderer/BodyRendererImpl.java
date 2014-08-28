package com.m1.mimeui.renderer;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.renderer.BodyRenderer;
import com.m1.mimeui.api.renderer.BodyRendererEventHandler;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.PartRenderer;

import java.io.PrintWriter;
import java.util.Collection;

public class BodyRendererImpl implements BodyRenderer
{
    private final BodyRendererEventHandler eventHandler;
    private final PartRenderer partRenderer;

    public BodyRendererImpl (final BodyRendererEventHandler eventHandler, final PartRenderer partRenderer)
    {
        this.eventHandler = eventHandler;
        this.partRenderer = partRenderer;
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
    {
        final Collection<IncorporatedPart> inlineParts =
                analyzedMessage.getParts(Disposition.INLINE, Disposition.INLINE_ATTACHMENT, Disposition.INLINE_ALTERNATIVE);

        if (inlineParts.isEmpty())
        {
            this.eventHandler.onEmptyBody(analyzedMessage, htmlOut);
        }
        else
        {
            this.eventHandler.onBeforeBody(analyzedMessage, htmlOut);

            try
            {
                for (final IncorporatedPart inlinePart : inlineParts)
                {
                    if (inlinePart.getContentType().equals("message", "external-body"))
                    {
                        this.eventHandler.onExternalPartContent(inlinePart, htmlOut);

                        continue;
                    }

                    switch (inlinePart.getDisposition())
                    {
                        case INLINE:
                            this.partRenderer.render(inlinePart, contentViewerFactory, htmlOut);

                            break;
                        case INLINE_ATTACHMENT:
                            this.eventHandler.onInlineAttachment(inlinePart, htmlOut);

                            break;
                        case INLINE_ALTERNATIVE:
                            this.eventHandler.onInlineAlternative(inlinePart, htmlOut);

                            break;
                        default:
                            // This should never happen (see "getParts(...)" call above).
                    }
                }
            }
            finally
            {
                this.eventHandler.onAfterBody(analyzedMessage, htmlOut);
            }
        }
    }
}
