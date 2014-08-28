package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.*;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class IncorporatedPartDecoratorTest
{
    private static final int ARBITRARY_INDEX = 12345678;

    private final Part part = EasyMock.createMock(Part.class);
    private final AnalyzedMessage analyzedMessage = EasyMock.createMock(AnalyzedMessage.class);
    private final ContentType contentType = EasyMock.createMock(ContentType.class);
    private final List<String> extensions = new ArrayList<String>();

    public IncorporatedPartDecoratorTest ()
    {
        this.extensions.add("txt"); // first extension is the default
        this.extensions.add("readme");
        this.extensions.add("text");
    }

    private void reset ()
    {
        EasyMock.reset(
                this.part, this.analyzedMessage, this.contentType
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.part, this.analyzedMessage, this.contentType
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.part, this.analyzedMessage, this.contentType
        );
    }

    @Test
    public void testOmittedContentLocation ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getContentLocation())
                .andReturn(null);
        EasyMock.expect(this.part.getFilename())
                .andReturn("content.txt");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));
        EasyMock.expect(this.part.getContextLocation())
                .andReturn(URI.create("thismessage:/path/to/context.htm"));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getContentLocation(), URI.create("thismessage:/path/to/content.txt"),
                "Expected the content location to be defaulted correctly.");

        // ------------------

        verify();
    }

    @Test void testProvidedContentLocation ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getContentLocation())
                .andReturn(URI.create("thismessage:/foo"));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getContentLocation(), URI.create("thismessage:/foo"),
                "Expected the content location to be passed through correctly.");

        // ------------------

        verify();
    }

    @Test
    public void testOmittedFilename ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn(null);
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "part-12345678.txt",
                "Expected the filename to be defaulted correctly.");

        // ------------------

        verify();
    }

    @Test
    public void testProvidedFilename ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("foo.txt");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "foo.txt",
                "Expected the filename to be passed through correctly.");

        // ------------------

        verify();
    }

    @Test
    public void testArabicFilename ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("العربية.txt");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "العربية.txt",
                "Expected the filename to be passed through correctly.");

        // ------------------

        verify();
    }

    @Test
    public void testGreekFilename ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("ελληνικά.txt");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "ελληνικά.txt",
                "Expected the filename to be passed through correctly.");

        // ------------------

        verify();
    }

    @Test
    public void testProvidedFilenameMissingExtension ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("foo");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "foo.txt",
                "Expected the filename to be given the right extension.");

        // ------------------

        verify();
    }

    @Test
    public void testProvidedFilenameUnrecognizedExtension ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("foo.exe");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "foo.exe.txt",
                "Expected the filename to be given the right extension.");

        // ------------------

        verify();
    }

    @Test
    public void testProvidedFilenameNonDefaultExtension ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part.getFilename())
                .andReturn("foo.text");
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.contentType.getExtensions())
                .andReturn(this.extensions);
        EasyMock.expect(this.contentType.getDefaultExtension())
                .andReturn(this.extensions.get(0));

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getFilename(), "foo.text",
                "Expected the filename to be allowed with a recognized but non-default extension.");

        // ------------------

        verify();
    }

    @Test
    public void testGetMessageParts ()
    {
        reset();

        // ------------------

        replay();

        // ------------------

        final IncorporatedPart UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, 1);

        assertEquals(UUT.getAnalyzedMessage(), this.analyzedMessage,
                "Expected the same message parts to be returned.");

        // ------------------

        verify();
    }

    @Test
    public void testGetIndex ()
    {
        reset();

        // ------------------

        replay();

        // ------------------

        final IncorporatedPart UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getIndex(), ARBITRARY_INDEX,
                "Expected the same index to be returned.");

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
        EasyMock.expect(this.part.getContextLocation())
                .andReturn(URI.create("thismessage:/foo"));
        EasyMock.expect(this.part.getContentType())
                .andReturn(this.contentType);
        EasyMock.expect(this.part.getCharacterEncoding())
                .andReturn("ASCII");
        EasyMock.expect(this.part.getDisposition())
                .andReturn(Disposition.INLINE_ALTERNATIVE);
        EasyMock.expect(this.part.getInputStream())
                .andReturn(null);

        replay();

        // ------------------

        final Part UUT = new IncorporatedPartDecorator(this.part, this.analyzedMessage, ARBITRARY_INDEX);

        assertEquals(UUT.getContentId(), "foo",
                "Expected the content ID to be unmodified.");
        assertEquals(UUT.getContextLocation(), URI.create("thismessage:/foo"),
                "Expected the context location to be unmodified.");
        assertEquals(UUT.getContentType(), this.contentType,
                "Expected the content type to be unmodified.");
        assertEquals(UUT.getCharacterEncoding(), "ASCII",
                "Expected the character encoding to be unmodified.");
        assertEquals(UUT.getDisposition(), Disposition.INLINE_ALTERNATIVE,
                "Expected the disposition to be unmodified.");
        assertNull(UUT.getInputStream(),
                "Expected the input stream to be unmodified.");

        // ------------------

        verify();
    }
}
