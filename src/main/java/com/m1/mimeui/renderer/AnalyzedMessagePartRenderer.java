package com.m1.mimeui.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewerFactory;
import com.m1.mimeui.api.renderer.FragmentRenderer;
import com.m1.mimeui.api.renderer.PartRenderer;

import java.io.PrintWriter;

public class AnalyzedMessagePartRenderer implements FragmentRenderer<AnalyzedMessage>
{
    private final PartRenderer partRenderer;
    private final PartSelector partSelector;

    public AnalyzedMessagePartRenderer (final PartRenderer partRenderer, final PartSelector partSelector)
    {
        this.partRenderer = partRenderer;
        this.partSelector = partSelector;
    }

    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
            throws MimeUIException
    {
        final IncorporatedPart part = this.partSelector.select(analyzedMessage);

        if (part != null)
        {
            this.partRenderer.render(part, contentViewerFactory, htmlOut);
        }
        else
        {
            throw new MimeUIException("Part not found by part selector: " + this.partSelector.toString());
        }
    }
}
