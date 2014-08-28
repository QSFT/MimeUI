package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.io.PrintWriter;

public interface PartRenderer extends FragmentRenderer<IncorporatedPart>
{
    @Override
    public void render (final IncorporatedPart part, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut);
}
