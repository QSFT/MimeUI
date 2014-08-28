package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.codec.MailtoUriField;
import com.m1.mimeui.api.mimedom.IncorporatedPart;

import java.util.Map;
import java.util.Set;

public interface ContentLocationProvider
{
    public static class UnsupportedServiceException extends Exception
    {
        public UnsupportedServiceException ()
        {
            super("The service does not support this operation.");
        }
    }

    /**
     * This is called for part downloads. For example, the links that are generated for inline attachments and inline
     * alternatives use the URL returned by this method.
     *
     * <p />When a GET request is made to the provided URL, the response should have the correct content type header,
     * and charset. A file name should be provided. The content-disposition should be "Attachment". The body of the
     * response should be the raw part content.
     *
     * @param part The part to return the URL for.
     * @return A string URL. Can be relative to the server root (e.g. "/wfe/...'), or absolute. Not escaped or encoded.
     * @throws UnsupportedServiceException Indicates that the service cannot provide the URL requested.
     */
    public String getRawPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException;

    /**
     * This is called for any auxiliary part content (CSS, images, etc...) that is linked to from HTML content.
     *
     * <p />Depending on the type of part and any security considerations that may exist, this URL may return the raw
     * part, or the part sanitized in some way. The normal use case for this would be linked to CSS files where it may
     * be prudent to remove all "url" references and so on.
     *
     * <p />The behavior of the service that this URL points to is identical to that of getRawPartDownloadURL with
     * respect to response headers that are expected to the set.
     *
     * @param part The part to return the sanitization URL for.
     * @return A string URL. Can be relative to the server root (e.g. "/wfe/...'), or absolute. Not escaped or encoded.
     * @throws UnsupportedServiceException Indicates that the service cannot provide the URL requested.
     */
    public String getSanitizedPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException;

    /**
     * At present this is never called. In the future there may be cases when links to rendered parts are needed.
     *
     * @param part The part to return the rendering URL for.
     * @return A string URL. Can be relative to the server root (e.g. "/wfe/...'), or absolute. Not escaped or encoded.
     * @throws UnsupportedServiceException Indicates that the service cannot provide the URL requested.
     */
    public String getRenderedPartDownloadUrl (final IncorporatedPart part)
            throws UnsupportedServiceException;

    /**
     * This returns a URL that should be used to launch a message composition form. The obvious implementation of this
     * method simply generates a "mailto:" URI.
     *
     * @param fields Any fields to pre-populate in the composition form.
     * @return Any URL that when navigated to by a web browser will cause a message composition form to appear.
     * @throws UnsupportedServiceException Indicates that the service cannot provide the URL requested.
     */
    public String getMessageCompositionUrl (final Map<MailtoUriField, Set<String>> fields)
            throws UnsupportedServiceException;

    /**
     * This returns a URL to an image that will be substituted for external images in the message when external
     * references are disabled.
     *
     * @return Any URL that will resolve to an image.
     * @throws UnsupportedServiceException Indicates that the service cannot provide such the URL requested.
     */
    public String getDefaultImageUrl ()
            throws UnsupportedServiceException;
}
