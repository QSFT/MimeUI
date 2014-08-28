package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;

public final class DispositionPartDecorator extends PartDecorator
{
    private final Disposition disposition;

    public DispositionPartDecorator (final Part decoratee, final Disposition disposition)
    {
        super(decoratee);

        this.disposition = disposition;
    }

    @Override
    public Disposition getDisposition ()
    {
        return this.disposition;
    }
}
