package com.m1.mimeui.partselector;

import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

public class ContentIdPartSelector implements PartSelector
{
    private final String contentId;

    public ContentIdPartSelector (final String contentId)
    {
        this.contentId = contentId;
    }

    @Override
    public IncorporatedPart select (final AnalyzedMessage analyzedMessage)
    {
        return analyzedMessage.getPartByContentId(this.contentId);
    }

    @Override
    public String toString ()
    {
        return "(content id: " + this.contentId + ")";
    }
}
