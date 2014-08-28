package com.m1.mimeui.api.codec;

import java.util.Map;
import java.util.Set;

public interface MailtoUriCodec extends UriCodec<Map<MailtoUriField, Set<String>>>
{
    public final static String MAILTO_URI_SCHEME = "mailto:"; // RFC-2368 Section 2
    public final static String TO_DELIMITER = ","; // RFC-2368 Section 2
    public final static String HEADER_PREFIX = "?"; // RFC-2368 Section 2
    public final static String HEADERS_DELIMITER = "&"; // RFC-2368 Section 2
    public final static String HEADER_DELIMITER = "="; // RFC-2368 Section 2
    public final static String URI_ENCODING = "UTF-8"; // RFC-3986 Section 2.5

    public Map<MailtoUriField, Set<String>> parse (final String mailtoUri);
    public String toString (final Map<MailtoUriField, Set<String>> fields);
}
