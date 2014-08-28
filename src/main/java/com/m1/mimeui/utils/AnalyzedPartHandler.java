package com.m1.mimeui.utils;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

public interface AnalyzedPartHandler<T>
{
    public T handlePart (final IncorporatedPart part)
            throws MimeUIException;
}
