package com.m1.mimeui.analyzer;

import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.mimedom.DispositionPartDecorator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class PartHandlerImpl implements PartHandler
{
    @Override
    public Collection<Part> getTriagedParts (final Collection<? extends Part> parts, final Disposition disposition)
    {
        final Collection<Part> triagedParts = new ArrayList<Part>(parts.size());

        for (final Part part : parts)
        {
            switch (part.getDisposition())
            {
                case INLINE:
                case INLINE_ATTACHMENT:
                case INLINE_ALTERNATIVE:
                    triagedParts.add(new DispositionPartDecorator(part, disposition));
                    break;
                default:
                    triagedParts.add(part);
                    break;
            }
        }

        return triagedParts;
    }

    @Override
    public boolean hasDispositions (final Collection<? extends Part> parts, final Disposition... dispositionArray)
    {
        final Collection<Disposition> dispositions = new HashSet<Disposition>(Arrays.asList(dispositionArray));

        for (final Part part : parts)
        {
            if (dispositions.contains(part.getDisposition()))
            {
                return true;
            }
        }

        return false;
    }
}
