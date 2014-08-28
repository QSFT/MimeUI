package com.m1.mimeui.api.mimedom;

import java.util.List;

public interface ContentType
{
    public String getMimeType ();
    public List<String> getExtensions ();
    public String getDefaultExtension ();
    public boolean hasSecurityConsiderations ();
    public boolean equals (final String type, final String subtype);
}
