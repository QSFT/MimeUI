package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.io.PrintWriter;
import java.util.Collection;

public interface ContentViewer
{
    /**
     * This is a collection of all the mime types that this viewer can handle.
     *
     * @return An unmodifiable collection of supported mime types.
     */
    public Collection<String> getSupportedMimeTypes ();

    /**
     * Renders the specified part as HTML.
     *
     * todo: Consider replacing "part" with the input stream directly.
     *
     * @param part The part to be rendered.
     * @param htmlOut The print writer that HTML should be written to.
     * @throws MimeUIException Indicates that the part could not be rendered correctly.
     */
    public void render (final IncorporatedPart part, final PrintWriter htmlOut)
            throws MimeUIException;
}
