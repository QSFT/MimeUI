package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class PartDecoratorTest
{
    private final Part part = EasyMock.createMock(Part.class);
    private final ContentType contentType = EasyMock.createMock(ContentType.class);

    private void reset ()
    {
        EasyMock.reset(
                this.part, this.contentType
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.part, this.contentType
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.part, this.contentType
        );
    }

    @Test
    public void testUntouchedMethods ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getContentId())
                .andReturn("foo");
        EasyMock.expect(this.part.getContextLocation())
                .andReturn(URI.create("thismessage:/foo"));
        EasyMock.expect(this.part.getContentLocation())
                .andReturn(URI.create("thismessage:/baz"));
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.part.getCharacterEncoding())
                .andReturn("ASCII");
        EasyMock.expect(this.part.getDisposition())
                .andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part.getFilename())
                .andReturn("baz");
        EasyMock.expect(this.part.getInputStream())
                .andReturn(null);

        replay();

        // ------------------

        // Note: NoOpPartDecorator is an empty concrete class that simply extends PartDecorator, which is abstract.
        final Part UUT = new NoOpPartDecorator(this.part);

        assertEquals(UUT.getContentId(), "foo",
                "Expected the content ID to be unmodified.");
        assertEquals(UUT.getContextLocation(), URI.create("thismessage:/foo"),
                "Expected the context location to be unmodified.");
        assertEquals(UUT.getContentLocation(), URI.create("thismessage:/baz"),
                "Expected the content location to be unmodified.");
        assertEquals(UUT.getContentType(), this.contentType,
                "Expected the content type to be unmodified.");
        assertEquals(UUT.getCharacterEncoding(), "ASCII",
                "Expected the character encoding to be unmodified.");
        assertEquals(UUT.getDisposition(), Disposition.ATTACHMENT,
                "Expected the disposition to be unmodified.");
        assertEquals(UUT.getFilename(), "baz",
                "Expected the filename to be unmodified.");
        assertNull(UUT.getInputStream(),
                "Expected the input stream to be unmodified.");

        // ------------------

        verify();
    }

    private class NoOpPartDecorator extends PartDecorator
    {
        private NoOpPartDecorator (final Part part)
        {
            super(part);
        }
    }
}
