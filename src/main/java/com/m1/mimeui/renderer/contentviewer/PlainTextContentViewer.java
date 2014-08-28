package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.renderer.ContentViewer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;

public class PlainTextContentViewer implements ContentViewer
{
    private static final int BUFFER_SIZE = 4096;

    @Override
    public Collection<String> getSupportedMimeTypes ()
    {
        // Technically we can handle anything under text/*, but we don't want to inline source code and etc...
        return Collections.singleton("text/plain");
    }

    @Override
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException
    {
        InputStream inputStream = null;
        Reader reader = null;

        try
        {
            inputStream = part.getInputStream();
            reader = new InputStreamReader(inputStream, part.getCharacterEncoding());

            final char[] buffer = new char[BUFFER_SIZE];
            int characterCount;

            // todo: Consider adding support for RFC-3676.

            while ((characterCount = reader.read(buffer)) != -1)
            {
                htmlOut.print(StringEscapeUtils.escapeHtml4(String.valueOf(buffer, 0, characterCount)));
            }
        }
        catch (final UnsupportedEncodingException e)
        {
            throw new MimeUIException("Unable to read a textual part.", e);
        }
        catch (final IOException e)
        {
            throw new MimeUIException("Unable to read a textual part.", e);
        }
        finally
        {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStream);
        }
    }
}
