package com.m1.mimeui.api.renderer;

import java.io.PrintWriter;

public interface MessageRendererEventHandler
        extends HeaderRendererEventHandler, AttachmentListRendererEventHandler, BodyRendererEventHandler
{
    /**
     * Occurs immediately before a message is rendered. This is generally the first event fired when rendering a
     * message.
     *
     * @param htmlOut The PrintWriter that any output can be written to.
     */
    public void onBeforeMessage (final PrintWriter htmlOut);

    /**
     * Occurs immediately after a message is rendered. This is generally the last event fired when rendering a message.
     *
     * @param htmlOut The PrintWriter that any output can be written to.
     */
    public void onAfterMessage (final PrintWriter htmlOut);
}
