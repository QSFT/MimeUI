package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DispositionPartDecoratorTest
{
    private final Part part = EasyMock.createMock(Part.class);
    private final ContentType contentType = EasyMock.createMock(ContentType.class);

    private void reset ()
    {
        EasyMock.reset(
                this.part
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.part
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.part
        );
    }

    @Test
    public void testGetDisposition ()
            throws Exception
    {
        reset();

        // ------------------

        // Specifically don't expect getDisposition to be called. We test the other in testUntouchedMethods().

        replay();

        // ------------------

        final Part UUT = new DispositionPartDecorator(this.part, Disposition.INLINE_ATTACHMENT);

        assertEquals(UUT.getDisposition(), Disposition.INLINE_ATTACHMENT,
                "Expected the part to be decorated as INLINE_ATTACHMENT.");

        // ------------------

        verify();
    }

    @Test
    public void testUntouchedMethods ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getContentId())
                .andReturn("foo");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.part.getCharacterEncoding())
                .andReturn("ASCII");
        EasyMock.expect(this.part.getFilename())
                .andReturn("baz");
        EasyMock.expect(this.part.getInputStream())
                .andReturn(null);

        replay();

        // ------------------

        final Part UUT = new DispositionPartDecorator(this.part, Disposition.INLINE_ATTACHMENT);

        assertEquals(UUT.getContentId(), "foo",
                "Expected the content ID to be unmodified.");
        assertEquals(UUT.getContentType(), this.contentType,
                "Expected the content type to be unmodified.");
        assertEquals(UUT.getCharacterEncoding(), "ASCII",
                "Expected the character encoding to be unmodified.");
        assertEquals(UUT.getFilename(), "baz",
                "Expected the filename to be unmodified.");
        assertNull(UUT.getInputStream(),
                "Expected the input stream to be unmodified.");

        // ------------------

        verify();
    }
}
