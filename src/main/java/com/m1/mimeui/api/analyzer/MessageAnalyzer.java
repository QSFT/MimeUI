package com.m1.mimeui.api.analyzer;

import com.m1.mimeui.api.MimeUIException;

public interface MessageAnalyzer<E>
{
    /**
     * Evaluates the supplied Mime4j message object, then calls the supplied function passing a MessageParts object and
     * the configuration object.
     *
     * @param messageEntity The Mime4j message object to evaluate.
     * @param contentTypeAdjudicator The content type adjudicator to use for triage.
     * @param handler The handler to pass message part to.
     * @param <T> The type of object returned by the function.
     * @return The T object returned by the function.
     * @throws com.m1.mimeui.api.MimeUIException Indicates that the function could not be called.
     */
    public <T> T analyzeAndExecute (final E messageEntity, final ContentTypeAdjudicator contentTypeAdjudicator,
                                    final AnalysisHandler<T> handler)
            throws MimeUIException;
}
