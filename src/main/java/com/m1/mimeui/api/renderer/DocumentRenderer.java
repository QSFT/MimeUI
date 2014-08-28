package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;

import java.io.PrintWriter;

public interface DocumentRenderer extends Renderer<AnalyzedMessage>
{
    @Override
    public void render (final AnalyzedMessage analyzedMessage, final ContentViewerFactory contentViewerFactory,
                        final PrintWriter htmlOut)
            throws MimeUIException;
}
