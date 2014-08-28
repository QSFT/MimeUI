package com.m1.mimeui;

import com.m1.mimeui.api.Logger;
import org.testng.annotations.Test;

public class SilentLoggerTest
{
    @Test
    public void testSilent ()
    {
        // Really low hanging fruit: A class that does nothing.

        final Logger UUT = new SilentLogger();

        UUT.debug("Hi.");
        UUT.error("Hi.");
        UUT.error(new Exception(), "Hi.");
    }
}
