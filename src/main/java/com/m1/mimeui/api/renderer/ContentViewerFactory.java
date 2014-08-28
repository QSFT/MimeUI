package com.m1.mimeui.api.renderer;

public interface ContentViewerFactory
{
    /**
     * Returns a content viewer that can render the provided mime type as HTML. Returns null if no content viewer is
     * available for the specified type.
     *
     * @param mimeType The mime type to retrieve a content viewer for.
     * @return An appropriate content viewer.
     */
    public ContentViewer getContentViewer (final String mimeType);
}
