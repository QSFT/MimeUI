package com.m1.mimeui.analyzer;

import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.AnalysisHandler;
import com.m1.mimeui.api.analyzer.MessageAnalyzer;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.AnalyzedMessageFactory;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class MessageAnalyzerImplTest
{
    @SuppressWarnings("unchecked")
    private final AnalyzedMessageFactory<Unused> analyzedMessageFactory = EasyMock.createMock(AnalyzedMessageFactory.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    @SuppressWarnings("unchecked")
    private final AnalysisHandler<String> function = EasyMock.createMock(AnalysisHandler.class);
    private final AnalyzedMessage analyzedMessage = EasyMock.createMock(AnalyzedMessage.class);

    private void reset ()
    {
        EasyMock.reset(
                this.analyzedMessageFactory, this.entity, this.function, this.analyzedMessage
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.analyzedMessageFactory, this.entity, this.function, this.analyzedMessage
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.analyzedMessageFactory, this.entity, this.function, this.analyzedMessage
        );
    }

    @Test
    public void testSuccess ()
            throws Exception
    {
        reset();

        EasyMock.expect(this.analyzedMessageFactory.getAnalyzedMessage(this.entity, null))
                .andReturn(this.analyzedMessage);
        EasyMock.expect(this.function.handle(this.analyzedMessage))
                .andReturn("baz");

        replay();

        final MessageAnalyzer<Unused> UUT = new MessageAnalyzerImpl<Unused>(this.analyzedMessageFactory);

        final String ret = UUT.analyzeAndExecute(this.entity, null, this.function);

        assertEquals(ret, "baz", "Expected 'baz' to be returned.");

        verify();
    }

    @Test
    public void testFailure ()
            throws Exception
    {
        reset();

        EasyMock.expect(this.analyzedMessageFactory.getAnalyzedMessage(this.entity, null))
                .andReturn(this.analyzedMessage);
        EasyMock.expect(this.function.handle(this.analyzedMessage))
                .andThrow(new MimeUIException("Hi."));

        replay();

        final MessageAnalyzer<Unused> UUT = new MessageAnalyzerImpl<Unused>(this.analyzedMessageFactory);

        try
        {
            UUT.analyzeAndExecute(this.entity, null, this.function);
            fail();
        }
        catch (final MimeUIException e)
        {
            // pass
        }

        verify();
    }
}
