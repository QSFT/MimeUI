package com.m1.mimeui.utils;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

public abstract class AbstractPartSelectingAnalysisHandler<T> implements AnalysisHandler<T>
{
    private final PartSelector partSelector;

    protected AbstractPartSelectingAnalysisHandler (final PartSelector partSelector)
    {
        this.partSelector = partSelector;
    }

    abstract protected T handlePart (final IncorporatedPart part)
            throws MimeUIException;

    @Override
    public T handle (final AnalyzedMessage analyzedMessage)
            throws MimeUIException
    {
        final IncorporatedPart part = this.partSelector.select(analyzedMessage);

        if (part == null)
        {
            throw new MimeUIException("Part not found: " + this.partSelector.toString());
        }

        return handlePart(part);
    }
}
