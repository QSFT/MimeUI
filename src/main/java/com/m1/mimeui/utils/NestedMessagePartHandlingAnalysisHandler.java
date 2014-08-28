package com.m1.mimeui.utils;

import com.m1.mimeui.analyzer.PleasantContentTypeAdjudicator;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.parser.MessageParser;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NestedMessagePartHandlingAnalysisHandler<E, T> implements AnalysisHandler<T>
{
    private final List<Integer> partIndexList;
    private final AnalyzedPartHandler<T> partHandler;
    private final MessageParser<E> parser;
    private final MessageAnalyzer<E> analyzer;

    public NestedMessagePartHandlingAnalysisHandler (final List<Integer> partIndexList,
                                                     final AnalyzedPartHandler<T> partHandler,
                                                     final MessageParser<E> parser, final MessageAnalyzer<E> analyzer)
    {
        this.partIndexList = new ArrayList<Integer>(partIndexList);
        this.partHandler = partHandler;
        this.parser = parser;
        this.analyzer = analyzer;
    }

    /**
     * This method indirectly recurses into itself for each index in the partIndexList. So if
     * partIndexList contains five elements, this method will be called a total of five times -- called externally the
     * first time, then calling itself four times. It will call the partHandler only once for the final part identified
     * in the list.
     *
     * @param analyzedMessage The analyzed message being handled.
     * @return Whatever the partHandler ultimately returns.
     * @throws com.m1.mimeui.api.MimeUIException Will happen if partIndexList is empty or an parsing error occurs.
     */
    @Override
    public T handle (final AnalyzedMessage analyzedMessage)
            throws MimeUIException
    {
        if (this.partIndexList.isEmpty())
        {
            throw new MimeUIException("No parts specified.");
        }

        final IncorporatedPart part = analyzedMessage.getPartByIndex(this.partIndexList.remove(0));

        if (this.partIndexList.isEmpty())
        {
            return this.partHandler.handlePart(part);
        }
        else
        {
            if (!part.getContentType().equals("message", "rfc822"))
            {
                throw new MimeUIException("Unable to recurse into a part that is not a valid message.");
            }

            InputStream messageInputStream = null;

            try
            {
                messageInputStream = part.getInputStream();

                return this.parser.parseAndExecute(
                    messageInputStream,
                    new AnalyzingParseHandler<E, T>(
                        this.analyzer,
                        new PleasantContentTypeAdjudicator(),
                        this
                    )
                );
            }
            finally
            {
                IOUtils.closeQuietly(messageInputStream);
            }
        }
    }
}
