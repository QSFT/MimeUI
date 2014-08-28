package com.m1.mimeui.api;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

public interface PartSelector
{
    public IncorporatedPart select (final AnalyzedMessage analyzedMessage);
}
