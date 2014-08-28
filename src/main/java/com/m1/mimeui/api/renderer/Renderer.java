package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.MimeUIException;

import java.io.PrintWriter;

public interface Renderer<T>
{
    public void render (final T object, final ContentViewerFactory contentViewerFactory, final PrintWriter htmlOut)
            throws MimeUIException;
}
