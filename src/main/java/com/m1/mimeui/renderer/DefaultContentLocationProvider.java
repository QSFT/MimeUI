package com.m1.mimeui.renderer;

import com.m1.mimeui.codec.DataUriPayloadImpl;
import com.m1.mimeui.api.codec.DataUriCodec;
import com.m1.mimeui.codec.DataUriCodecImpl;
import com.m1.mimeui.api.codec.MailtoUriCodec;
import com.m1.mimeui.codec.MailtoUriCodecImpl;
import com.m1.mimeui.api.codec.MailtoUriField;
import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.Logger;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class DefaultContentLocationProvider implements ContentLocationProvider
{
    private final MailtoUriCodec mailtoUriCodec = new MailtoUriCodecImpl();
    private final DataUriCodec dataUriCodec = new DataUriCodecImpl();
    private final Logger logger;

    public DefaultContentLocationProvider (final Logger logger)
    {
        this.logger = logger;
    }

    /**
     * Creates "data:" urls, and therefore doesn't require a hosting service to provide downloads. This is not ideal in
     * situations where parts are very large.
     *
     *
     * @param part The part to return the URL for.
     * @return The data url to will resolve to this part, or null if an IOException occurs.
     * @throws UnsupportedServiceException If an IOException occurs when reading part data.
     */
    @Override
    public String getRawPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException
    {
        InputStream inputStream = null;

        try
        {
            inputStream = part.getInputStream();

            return this.dataUriCodec.toString(
                    new DataUriPayloadImpl(part.getContentType().getMimeType(), IOUtils.toByteArray(inputStream)));
        }
        catch (final IOException e)
        {
            this.logger.error(e, "Unable to read content from the input stream for part: " + part.getIndex());

            throw new UnsupportedServiceException();
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * Doesn't support sanitization URLs.
     *
     * @param part The part to return the sanitization URL for.
     * @throws UnsupportedServiceException Always.
     */
    @Override
    public String getSanitizedPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException
    {
        throw new UnsupportedServiceException();
    }

    /**
     * Doesn't support rendering URLs.
     *
     * @param part The part to return the rendering URL for.
     * @throws UnsupportedServiceException Always.
     */
    @Override
    public String getRenderedPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException
    {
        throw new UnsupportedServiceException();
    }

    /**
     * Returns a "mailto:..." URI.
     *
     * @param fields Any fields to pre-populate in the composition form.
     * @return A "mailto:..." URI, or null if a URL encoding error occurs.
     */
    @Override
    public String getMessageCompositionUrl (final Map<MailtoUriField, Set<String>> fields)
            throws UnsupportedServiceException
    {
        return this.mailtoUriCodec.toString(fields);
    }

    @Override
    public String getDefaultImageUrl ()
            throws UnsupportedServiceException
    {
        return "data:image/gif;base64,R0lGODlhCgAKAPcAAMbGxv"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "/////////////////////////////////////////////////////////////////////////////////////////////////////////"
        + "//////////////////////////////////////////////////////////////////////////"
        + "ywAAAAACgAKAAAIEwABCBxIsKDBgwgTKlzIsGHCgAAAOw==";
    }
}
