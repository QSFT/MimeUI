package com.m1.mimeui.partselector;

import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.net.URI;

public class ContentLocationPartSelector implements PartSelector
{
    private final URI contentLocation;

    public ContentLocationPartSelector (final URI contentLocation)
    {
        this.contentLocation = contentLocation;
    }

    @Override
    public IncorporatedPart select (final AnalyzedMessage analyzedMessage)
    {
        // todo: this should be used by  UntrustedContentUriResolverImpl.
        return analyzedMessage.getPartByContentLocation(this.contentLocation);
    }

    @Override
    public String toString ()
    {
        return "(content location: " + this.contentLocation + ")";
    }
}
