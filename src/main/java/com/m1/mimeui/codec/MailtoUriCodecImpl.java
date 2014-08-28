package com.m1.mimeui.codec;

import com.m1.mimeui.api.codec.MailtoUriCodec;
import com.m1.mimeui.api.codec.MailtoUriField;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MailtoUriCodecImpl implements MailtoUriCodec
{
    private <K, V> void putSetValue (final Map<K, Set<V>> map, final K key, final V value)
    {
        if (map.containsKey(key))
        {
            map.get(key).add(value);
        }
        else
        {
            map.put(key, new HashSet<V>());

            putSetValue(map, key, value);
        }
    }

    @Override
    public Map<MailtoUriField, Set<String>> parse (String mailtoUri)
    {
        final Map<MailtoUriField, Set<String>> fields
                = new HashMap<MailtoUriField, Set<String>>();
        final String noScheme = mailtoUri.substring(MAILTO_URI_SCHEME.length()).trim();
        final int querySeparatorIndex = noScheme.indexOf('?');
        final int fragmentSeparatorIndex = noScheme.indexOf('#') == -1 ? noScheme.length() : noScheme.indexOf('#');

        if (querySeparatorIndex == -1)
        {
            try
            {
                putSetValue(fields, MailtoUriField.TO,
                        URLDecoder.decode(noScheme.substring(0, fragmentSeparatorIndex), "UTF-8").trim());
            }
            catch (final UnsupportedEncodingException e)
            {
                // This never happens because the java platform guarantees support for UTF-8.
                // In any case, we'll just skip the TO field.
            }
        }
        else
        {
            final String query;

            try
            {
                putSetValue(fields, MailtoUriField.TO,
                        URLDecoder.decode(noScheme.substring(0, querySeparatorIndex).trim(), "UTF-8").trim());
            }
            catch (UnsupportedEncodingException e)
            {
                // This never happens because the java platform guarantees support for UTF-8.
                // In any case, we'll just skip the TO field.
            }

            query = noScheme.substring(querySeparatorIndex + 1, fragmentSeparatorIndex);

            final String[] pairs = query.split("&");

            for (final String pair : pairs)
            {
                final String[] keyValue = pair.split("=", 2);

                if (keyValue.length == 0)
                {
                    continue;
                }

                final MailtoUriField field;

                try
                {
                    field = MailtoUriField.fromHeaderName(URLDecoder.decode(keyValue[0], "UTF-8"));
                }
                catch (final UnsupportedEncodingException e)
                {
                    // This never happens because the java platform guarantees support for UTF-8.
                    // In any case, we'll just skip this field.

                    continue;
                }

                if (field == null)
                {
                    continue;
                }

                if (keyValue.length == 2)
                {
                    final String value;

                    try
                    {
                        value = URLDecoder.decode(keyValue[1], "UTF-8");
                    }
                    catch (final UnsupportedEncodingException e)
                    {
                        // This never happens because the java platform guarantees support for UTF-8.
                        // In any case, we'll just skip this field.
                        continue;
                    }

                    putSetValue(fields, field, value);
                }
                else
                {
                    putSetValue(fields, field, null);
                }
            }
        }

        return fields;
    }

    private String getCommaList (final Set<String> valueSet)
    {
        if (valueSet == null || valueSet.isEmpty())
        {
            return null;
        }

        try
        {
            return URLEncoder.encode(StringUtils.join(valueSet, TO_DELIMITER), URI_ENCODING);
        }
        catch (final UnsupportedEncodingException e)
        {
            return null;
        }
    }

    @Override
    public String toString (Map<MailtoUriField, Set<String>> fields)
    {
        final String to = getCommaList(fields.get(MailtoUriField.TO));
        final Set<String> headerSet = new HashSet<String>();

        for (Map.Entry<MailtoUriField, Set<String>> field : fields.entrySet())
        {
            if (field.getKey() != MailtoUriField.TO && field.getValue() != null)
            {
                for (final String value : field.getValue())
                {
                    if (value != null)
                    {
                        try
                        {
                            headerSet.add(
                                    field.getKey().getHeaderName()
                                    + HEADER_DELIMITER
                                    + URLEncoder.encode(value, URI_ENCODING)
                            );
                        }
                        catch (final UnsupportedEncodingException e)
                        {
                            // Skip this field.
                        }
                    }
                }
            }
        }

        return MAILTO_URI_SCHEME
                + (to == null ? "" : to)
                + (headerSet.isEmpty() ? "" : (HEADER_PREFIX + StringUtils.join(headerSet, HEADERS_DELIMITER)));
    }
}
