package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.io.PrintWriter;

public interface AttachmentListRendererEventHandler
{
    public void onBeforeAttachmentList (final PrintWriter htmlOut);
    public void onAfterAttachmentList (final PrintWriter htmlOut);
    public void onEmptyAttachmentList (final PrintWriter htmlOut);

    public void onAttachment (final IncorporatedPart attachment, final PrintWriter htmlOut);
}
