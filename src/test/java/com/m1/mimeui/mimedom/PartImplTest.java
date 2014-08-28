package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.InputStreamProvider;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;

import static org.testng.Assert.assertEquals;

public class PartImplTest
{
    @Test
    public void testGetters ()
    {
        final ContentType contentType = EasyMock.createMock(ContentType.class);
        final InputStreamProvider inputStreamProvider = EasyMock.createMock(InputStreamProvider.class);
        final InputStream inputStream = new ByteArrayInputStream("".getBytes());
        final URI contextLocation = URI.create("thismessage:/");
        final URI contentLocation = URI.create("thismessage:/part-0000.txt");

        EasyMock.expect(inputStreamProvider.getInputStream())
                .andReturn(inputStream);

        EasyMock.replay(contentType, inputStreamProvider);

        final Part UUT = new PartImpl("foo", contextLocation, contentLocation,
                contentType, "baz", Disposition.INLINE_ALTERNATIVE, "bar", inputStreamProvider);

        assertEquals(UUT.getContentId(), "foo",
                "Expected the content id to be returned unmodified.");
        assertEquals(UUT.getContentLocation(), contentLocation,
                "Expected the content location to be returned unmodified.");
        assertEquals(UUT.getContextLocation(), contextLocation,
                "Expected the context location to be returned unmodified.");
        assertEquals(UUT.getContentType(), contentType,
                "Expected the content type to be returned unmodified.");
        assertEquals(UUT.getCharacterEncoding(), "baz",
                "Expected the character encoding to be returned unmodified.");
        assertEquals(UUT.getDisposition(), Disposition.INLINE_ALTERNATIVE,
                "Expected the disposition to be returned unmodified.");
        assertEquals(UUT.getFilename(), "bar",
                "Expected the file name to be returned unmodified.");
        assertEquals(UUT.getInputStream(), inputStream,
                "Expected the input stream to be returned unmodified.");

        EasyMock.verify(contentType, inputStreamProvider);
    }
}
