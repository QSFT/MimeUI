package com.m1.mimeui.api.renderer;

import com.m1.mimeui.api.mimedom.AnalyzedMessage;

public interface UntrustedContentUriResolver
{
    public enum ExternalReferenceMode
    {
        DENY_EXTERNAL_REFERENCES,
        ALLOW_USER_INVOKED_EXTERNAL_REFERENCES,
        ALLOW_ALL_EXTERNAL_REFERENCES,
    }

    public static class IllegalUriException extends Exception
    {
        public IllegalUriException (final String message)
        {
            super(message);
        }
    }

    /**
     * Resolves URIs that would be in emails to URLs that can be displayed in a web-based email system safely.
     *
     * <p />In practice, this resolves several cases:
     *
     * <ul>
     *     <li>CID Urls: These are resolved to point to the part whose content id specified.</li>
     *     <li>MAILTO Urls: These may be left intact, or converted to some other composition URL.</li>
     *     <li>External Urls: These may be left intact, or replaced with the defaultUri.</li>
     *     <li>Internal Urls: These are resolved to point to the part they point to.</li>
     * </ul>
     *
     *
     * @param contentUri The untrusted URI that was in the message content.
     * @param analyzedMessage The current message (used to resolve CID and internal URIs).
     * @return A safe, resolved URI.
     */
    public String resolveUntrustedUri (final String contentUri, final AnalyzedMessage analyzedMessage, final boolean userInvoked)
            throws IllegalUriException;
}
