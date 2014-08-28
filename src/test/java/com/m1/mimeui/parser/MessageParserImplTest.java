package com.m1.mimeui.parser;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.parser.MessageParser;
import com.m1.mimeui.api.parser.ParseHandler;
import com.m1.mimeui.api.Unused;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class MessageParserImplTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    @SuppressWarnings("unchecked")
    private final ParseHandler<Unused, String> function = EasyMock.createMock(ParseHandler.class);
    private final Unused entity = EasyMock.createMock(Unused.class);

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.function, this.entity
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.function, this.entity
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.function, this.entity
        );
    }

    @Test
    public void testSuccess ()
            throws Exception
    {
        final InputStream inputStream = new ByteArrayInputStream("baz".getBytes());

        reset();

        EasyMock.expect(this.entityHandler.parseAndExecute(EasyMock.eq(inputStream),
                EasyMock.<ParseHandler<Unused, String>>anyObject()))
                .andAnswer(new IAnswer<String>()
                {
                    @Override
                    public String answer ()
                            throws Throwable
                    {
                        @SuppressWarnings("unchecked")
                        final ParseHandler<Unused, String> entityFunction =
                                (ParseHandler<Unused, String>) EasyMock.getCurrentArguments()[1];

                        return entityFunction.handle(MessageParserImplTest.this.entity);
                    }
                });
        EasyMock.expect(this.function.handle(this.entity))
                .andReturn("foo");

        replay();

        final MessageParser<Unused> UUT = new MessageParserImpl<Unused>(this.entityHandler);

        final String ret = UUT.parseAndExecute(inputStream, this.function);

        assertEquals(ret, "foo", "Expected the function to return foo.");

        verify();
    }

    @Test
    public void testFailure ()
            throws Exception
    {
        final InputStream inputStream = new ByteArrayInputStream("baz".getBytes());

        reset();

        EasyMock.expect(this.entityHandler.parseAndExecute(EasyMock.eq(inputStream),
                EasyMock.<ParseHandler<Unused, String>>anyObject()))
                .andThrow(new MimeUIException("Hi!"));

        replay();

        final MessageParser<Unused> UUT = new MessageParserImpl<Unused>(this.entityHandler);

        try
        {
            UUT.parseAndExecute(inputStream, this.function);
            fail();
        }
        catch (final MimeUIException e)
        {
            // Pass.
        }

        verify();
    }
}
