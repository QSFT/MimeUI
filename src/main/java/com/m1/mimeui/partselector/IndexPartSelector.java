package com.m1.mimeui.partselector;

import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

public class IndexPartSelector implements PartSelector
{
    private final int index;

    public IndexPartSelector (final int index)
    {
        this.index = index;
    }

    @Override
    public IncorporatedPart select (final AnalyzedMessage analyzedMessage)
    {
        return analyzedMessage.getPartByIndex(this.index);
    }

    @Override
    public String toString ()
    {
        return "(part index: " + this.index + ")";
    }
}
