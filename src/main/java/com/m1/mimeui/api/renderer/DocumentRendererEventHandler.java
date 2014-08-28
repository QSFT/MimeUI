package com.m1.mimeui.api.renderer;

import java.io.PrintWriter;

public interface DocumentRendererEventHandler
{
    public void onBeforeDocument (final PrintWriter htmlOut);
    public void onAfterDocument (final PrintWriter htmlOut);
}
