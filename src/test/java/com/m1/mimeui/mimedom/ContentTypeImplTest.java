package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.ContentType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class ContentTypeImplTest
{
    @Test
    public void testGetters ()
            throws Exception
    {
        final List<String> extensions = new ArrayList<String>(1);

        extensions.add("foo");

        final ContentType UUT = new ContentTypeImpl("baz", extensions, false);

        assertEquals(UUT.getMimeType(), "baz",
                "Expected the mime type to be returned unmodified.");
        assertEquals(UUT.getExtensions().size(), 1,
                "Expected the extensions to be returned unmodified.");
        assertEquals(UUT.getExtensions().get(0), "foo",
                "Expected the extensions to be returned unmodified.");
        assertEquals(UUT.hasSecurityConsiderations(), false,
                "Expected the security considerations to be returned unmodified.");

        try
        {
            UUT.getExtensions().add("bar");
            fail("Expected the returned extensions array to be immutable.");
        }
        catch (final UnsupportedOperationException e)
        {
            // Pass
        }
    }

    @Test
    public void testToString ()
            throws Exception
    {
        final List<String> extensions = Collections.emptyList();
        final ContentType UUT = new ContentTypeImpl("baz", extensions, false);

        assertEquals(UUT.toString(), "baz",
                "Expected toString to return the mime type.");
    }

    @Test
    public void testDefaultExtensionEmpty ()
            throws Exception
    {
        final List<String> extensions = Collections.emptyList();
        final ContentType UUT = new ContentTypeImpl("baz", extensions, false);

        assertEquals(UUT.getDefaultExtension(), null,
                "Expected null to be returned since there are no extensions, there cannot be a default extension.");
    }

    @Test
    public void testDefaultExtensionOne ()
            throws Exception
    {
        final List<String> extensions = Collections.singletonList("foo");
        final ContentType UUT = new ContentTypeImpl("baz", extensions, false);

        assertEquals(UUT.getDefaultExtension(), "foo",
                "Expected baz to be returned since it is the only extension.");
    }

    @Test
    public void testDefaultExtensionMultiple ()
            throws Exception
    {
        final List<String> extensions = new ArrayList<String>();

        extensions.add("foo");
        extensions.add("bar");

        final ContentType UUT = new ContentTypeImpl("baz", extensions, false);

        assertEquals(UUT.getDefaultExtension(), "foo",
                "Expected foo to be returned since it is the first extension.");
    }
}
