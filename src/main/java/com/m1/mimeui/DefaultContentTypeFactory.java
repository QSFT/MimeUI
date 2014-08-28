package com.m1.mimeui;

import com.m1.mimeui.api.ContentTypeFactory;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.mimedom.ContentTypeImpl;
import com.m1.mimeui.utils.MimeTypesParser;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultContentTypeFactory implements ContentTypeFactory
{
    final Map<String, List<String>> mimeTypeMap = new HashMap<String, List<String>>();
    final Map<String, String> fileExtensionMap = new HashMap<String, String>();

    public DefaultContentTypeFactory ()
            throws IOException
    {
        this(DefaultContentTypeFactory.class.getResourceAsStream("mime.types"));
    }

    public DefaultContentTypeFactory (final InputStream inputStream)
            throws IOException
    {
        this(new InputStreamReader(inputStream));
    }

    public DefaultContentTypeFactory (final Reader reader)
            throws IOException
    {
        final MimeTypesParser parser = new MimeTypesParser();

        parser.parse(reader, this.mimeTypeMap, this.fileExtensionMap);
    }

    @Override
    public ContentType getContentType (final String mimeType, final String filename)
    {
        if ("application/octet-stream".equals(mimeType) && !StringUtils.isBlank(filename))
        {
            // No mime type was provided.

            // For example, if a linux user attached a Microsoft Outlook ".pst" file to an email, chances are good that
            // their local linux-based email client didn't know that the content is "vnd.ms-outlook", so it used the
            // "catch-all" mime type "application/octet-stream" instead.

            // We make an attempt to look up a more correct mime type based on the file extension, if one was provided.

            final int extensionIndex = filename.lastIndexOf('.');

            if (extensionIndex != -1)
            {
                final String extension = filename.substring(extensionIndex + 1);

                if (this.fileExtensionMap.containsKey(extension))
                {
                    final String derivedMimeType = this.fileExtensionMap.get(extension);

                    return new ContentTypeImpl(derivedMimeType, this.mimeTypeMap.get(derivedMimeType), false);
                }
            }
        }
        else if (this.mimeTypeMap.containsKey(mimeType))
        {
            // We recognize the mime type.

            return new ContentTypeImpl(mimeType, this.mimeTypeMap.get(mimeType), false);
        }

        // We don't recognize the mime type at all.

        return new ContentTypeImpl(mimeType, Collections.<String>emptyList(), true);
    }
}
