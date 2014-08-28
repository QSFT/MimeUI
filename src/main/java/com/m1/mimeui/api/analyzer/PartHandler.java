package com.m1.mimeui.api.analyzer;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;

import java.util.Collection;

public interface PartHandler
{
    public Collection<Part> getTriagedParts (final Collection<? extends Part> parts, final Disposition disposition);

    /**
     * Returns true if any of the parts provided have a disposition provided.
     *
     * @param parts The parts to check.
     * @param dispositions Dispositions to check for.
     * @return True if any provided part has any provided disposition.
     */
    public boolean hasDispositions (final Collection<? extends Part> parts, final Disposition... dispositions);
}
