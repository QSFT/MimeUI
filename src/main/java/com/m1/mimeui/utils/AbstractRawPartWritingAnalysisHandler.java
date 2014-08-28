package com.m1.mimeui.utils;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.PartSelector;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractRawPartWritingAnalysisHandler extends AbstractPartSelectingAnalysisHandler<Unused>
{
    protected AbstractRawPartWritingAnalysisHandler (final PartSelector partSelector)
    {
        super(partSelector);
    }

    abstract protected OutputStream getOutputStream ();

    @Override
    public Unused handlePart (final IncorporatedPart part)
            throws MimeUIException
    {
        InputStream partInputStream = null;

        try
        {
            partInputStream = part.getInputStream();

            IOUtils.copy(partInputStream, getOutputStream());
        }
        catch (final IOException e)
        {
            throw new MimeUIException("Unable to get part content.", e);
        }
        finally
        {
            IOUtils.closeQuietly(partInputStream);
        }

        return null;
    }
}
