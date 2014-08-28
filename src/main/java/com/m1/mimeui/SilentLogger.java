package com.m1.mimeui;

import com.m1.mimeui.api.Logger;

public class SilentLogger implements Logger
{
    @Override
    public void debug (final String message)
    {
        // no-op
    }

    @Override
    public void error (final String message)
    {
        // no-op
    }

    @Override
    public void error (final Throwable cause, final String message)
    {
        // no-op
    }
}
