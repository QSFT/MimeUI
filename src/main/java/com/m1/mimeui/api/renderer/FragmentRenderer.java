package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.MimeUIException;

import java.io.PrintWriter;

public interface FragmentRenderer<T> extends Renderer<T>
{
    @Override
    public void render (final T object, final ContentViewerFactory contentViewerFactory, final PrintWriter htmlOut)
            throws MimeUIException;
}
