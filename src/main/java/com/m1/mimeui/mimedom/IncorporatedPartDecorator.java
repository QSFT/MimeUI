package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.Part;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

public final class IncorporatedPartDecorator extends PartDecorator implements IncorporatedPart
{
    private final AnalyzedMessage analyzedMessage;
    private final int index;

    public IncorporatedPartDecorator (final Part decoratee, final AnalyzedMessage analyzedMessage, final int index)
    {
        super(decoratee);

        this.analyzedMessage = analyzedMessage;
        this.index = index;
    }

    @Override
    public AnalyzedMessage getAnalyzedMessage ()
    {
        return this.analyzedMessage;
    }

    @Override
    public int getIndex ()
    {
        return this.index;
    }

    /**
     * Once incorporated, a part's absolute content location will never be null. If not specified in the message, a
     * default will be used, based on the part's context and file name. Note that context and file name could have
     * themselves been defaulted (derived from other attributes).
     *
     * @return The absolute content location (URI) of the part, defaulted if needed.
     */
    @Override
    public URI getContentLocation ()
    {
        final URI contentLocation = super.getContentLocation();

        if (contentLocation == null)
        {
            try
            {
                // This case is only used when no content location information is present. Use the (resolved) filename.
                // Todo our handling here causes issues with Content-Location based references in some edge cases.

                return getContextLocation().resolve(new URI(URLEncoder.encode(getFilename(), "UTF-8")));
            }
            catch (final UnsupportedEncodingException e)
            {
                // This will never happen because Java guarantees UTF-8 support.
                return getContextLocation();
            }
            catch (final URISyntaxException e)
            {
                // We may still have a bad URI maybe?
                return getContextLocation();
            }
        }

        return contentLocation;
    }

    /**
     * This is only here because this is where we have an index, which is part of the default file name.
     *
     * <p />The upshot is that we can't default/fix the filename until we have an IncorporatedPart.
     *
     * @return A sanitized, defaulted filename.
     */
    @Override
    public String getFilename ()
    {
        final String filename = super.getFilename();

        // See RFC-2183 Section 2.3, especially paragraph 2:
        //
        // "It is important that the receiving MUA not blindly use the suggested
        // filename.  The suggested filename SHOULD be checked (and possibly
        // changed) to see that it conforms to local filesystem conventions,
        // does not overwrite an existing file, and does not present a security
        // problem (see Security Considerations below)."

        final ContentType contentType = getContentType();
        final List<String> extensions = contentType.getExtensions();
        final String defaultExtension = contentType.getDefaultExtension();

        if (StringUtils.isBlank(filename))
        {
            return getDefaultFileName(defaultExtension);
        }

        final String filteredFilename = filename;

        if (isMissingExtension(filteredFilename, extensions))
        {
            if (defaultExtension != null)
            {
                return filteredFilename + "." + defaultExtension;
            }

            // If the defaultExtension is null, that means that we didn't recognize the mime type of the part. When that
            // happens, we simply accept the provided file extension. If we ever choose to explicitly remove them, we
            // would do that here.
        }

        return filteredFilename;
    }

    private String getDefaultFileName (final String defaultExtension)
    {
        return "part-" + String.format("%08d", this.index) + (defaultExtension == null ? "" : "." + defaultExtension);
    }

    private boolean isMissingExtension (final String fileName, final Iterable<String> extensions)
    {
        for (final String extension : extensions)
        {
            if (fileName.toLowerCase().endsWith("." + extension.toLowerCase()))
            {
                return false;
            }
        }

        return true;
    }
}
