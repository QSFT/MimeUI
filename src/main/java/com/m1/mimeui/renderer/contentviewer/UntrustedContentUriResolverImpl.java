package com.m1.mimeui.renderer.contentviewer;

import com.m1.mimeui.api.renderer.ContentLocationProvider;
import com.m1.mimeui.api.renderer.UntrustedContentUriResolver;
import com.m1.mimeui.api.codec.CidUriCodec;
import com.m1.mimeui.api.codec.DataUriCodec;
import com.m1.mimeui.api.codec.MailtoUriCodec;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;

import java.net.URI;
import java.net.URISyntaxException;

public class UntrustedContentUriResolverImpl implements UntrustedContentUriResolver
{
    private final ContentLocationProvider contentLocationProvider;
    private final ExternalReferenceMode externalReferenceMode;
    private final MailtoUriCodec mailtoUriCodec;
    private final CidUriCodec cidUriCodec;

    public UntrustedContentUriResolverImpl (final ContentLocationProvider contentLocationProvider,
                                            final ExternalReferenceMode externalReferenceMode,
                                            final MailtoUriCodec mailtoUriCodec,
                                            final CidUriCodec cidUriCodec)
    {
        this.contentLocationProvider = contentLocationProvider;
        this.externalReferenceMode = externalReferenceMode;
        this.mailtoUriCodec = mailtoUriCodec;
        this.cidUriCodec = cidUriCodec;
    }

    private String resolveCidUri (final String cidUri, final AnalyzedMessage analyzedMessage)
            throws IllegalUriException
    {
        final IncorporatedPart part = analyzedMessage.getPartByContentId(this.cidUriCodec.parse(cidUri));

        if (part == null)
        {
            throw new IllegalUriException("Unable to find message part for CID URI: " + cidUri);
        }
        else
        {
            try
            {
                return this.contentLocationProvider.getRawPartDownloadUrl(part);
            }
            catch (final ContentLocationProvider.UnsupportedServiceException e)
            {
                throw new IllegalUriException("Service does not support CID URI resolution: " + cidUri);
            }
        }
    }

    private String resolveMailtoUri (final String mailtoUri)
            throws IllegalUriException
    {
        try
        {
            return this.contentLocationProvider.getMessageCompositionUrl(this.mailtoUriCodec.parse(mailtoUri));
        }
        catch (final ContentLocationProvider.UnsupportedServiceException e)
        {
            throw new IllegalUriException("Service does not support mailto URI resolution: " + mailtoUri);
        }
    }

    private String resolveInternalUri (final String internalUri, final AnalyzedMessage analyzedMessage)
            throws IllegalUriException
    {
        // todo: Note that we actually remove special characters from file names before this point, so this can miss.
        try
        {
            final IncorporatedPart part = analyzedMessage.getPartByContentLocation(new URI(internalUri));

            if (part == null)
            {
                throw new IllegalUriException("Unable to find message part for internal URI: " + internalUri);
            }
            else
            {
                return this.contentLocationProvider.getRawPartDownloadUrl(part);
            }
        }
        catch (final URISyntaxException e)
        {
            throw new IllegalUriException("Unable to parse URI: " + internalUri);
        }
        catch (final ContentLocationProvider.UnsupportedServiceException e)
        {
            throw new IllegalUriException("Service does not support internal URI resolution: " + internalUri);
        }
    }

    @Override
    public String resolveUntrustedUri (final String contentUri, final AnalyzedMessage analyzedMessage, final boolean userInvoked)
            throws IllegalUriException
    {
        try
        {
            return resolveInternalUri(contentUri, analyzedMessage);
        }
        catch (final IllegalUriException e)
        {
            if (contentUri.startsWith(CidUriCodec.CID_URI_SCHEME))
            {
                return resolveCidUri(contentUri, analyzedMessage);
            }
            else if (contentUri.startsWith(MailtoUriCodec.MAILTO_URI_SCHEME))
            {
                return resolveMailtoUri(contentUri);
            }
            else if (contentUri.startsWith(DataUriCodec.DATA_URI_SCHEME))
            {
                return contentUri;
            }

            switch (this.externalReferenceMode)
            {
                case DENY_EXTERNAL_REFERENCES:
                    throw new IllegalUriException("External references are not permitted.");
                case ALLOW_ALL_EXTERNAL_REFERENCES:
                    return contentUri;
                case ALLOW_USER_INVOKED_EXTERNAL_REFERENCES:
                    if (userInvoked)
                    {
                        return contentUri;
                    }
                    else
                    {
                        throw new IllegalUriException("External references are not permitted.");
                    }
            }

            throw new IllegalUriException("External references are not permitted.");
        }
    }
}
