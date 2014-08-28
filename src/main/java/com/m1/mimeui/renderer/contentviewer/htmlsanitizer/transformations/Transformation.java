package com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations;

import org.jsoup.nodes.Element;

public interface Transformation
{
    public String getSelector ();
    public void transform (final Element element);
}
