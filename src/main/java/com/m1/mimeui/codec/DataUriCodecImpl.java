package com.m1.mimeui.codec;

import com.m1.mimeui.api.codec.DataUriCodec;
import com.m1.mimeui.api.codec.DataUriPayload;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DataUriCodecImpl implements DataUriCodec
{
    @Override
    public DataUriPayload parse (final String dataUri)
    {
        final String noScheme = dataUri.substring(DATA_URI_SCHEME.length()).trim();
        final String[] headerBodyPair = noScheme.split(",", 2);

        if (headerBodyPair.length != 2)
        {
            return new DataUriPayloadImpl("text/plain", new byte[] {});
        }

        final String header = headerBodyPair[0];
        final String body = headerBodyPair[1];

        final String mediaType;
        final byte[] data;

        final boolean isBase64;

        if (StringUtils.isBlank(header))
        {
            isBase64 = false;
            mediaType = "text/plain";
        }
        else
        {
            isBase64 = header.toLowerCase().endsWith(";base64");
            mediaType = isBase64 ? header.substring(0, header.length() - 7) : header;
        }

        try
        {
            if (isBase64)
            {
                data = Base64.decode(URLDecoder.decode(body, "UTF-8"));
            }
            else
            {
                data = URLDecoder.decode(body, "UTF-8").getBytes();
            }
        }
        catch (final UnsupportedEncodingException e)
        {
            return new DataUriPayloadImpl(mediaType, new byte[] {});
        }

        return new DataUriPayloadImpl(mediaType, data);
    }

    @Override
    public String toString (final DataUriPayload data)
    {
        return DATA_URI_SCHEME
                            + data.getContentType()
                            + ";base64,"
                            + Base64.encodeToString(data.getBytes(), false);
    }
}
