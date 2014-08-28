package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.mimedom.PartFactory;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.ContentTypeFactory;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;

import static org.testng.Assert.assertNotNull;

/**
 * Note: We allow test coverage to leak to the PartImpl constructor from here because we are testing PartFactoryImpl.
 */
public class PartFactoryImplTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    private final ContentTypeFactory contentTypeFactory = EasyMock.createMock(ContentTypeFactory.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    private final URI contextLocation = URI.create("thismessage:/");

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.contentTypeFactory, this.entity
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.contentTypeFactory, this.entity
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.contentTypeFactory, this.entity
        );
    }

    @Test
    public void testGetPart ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getFilename(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getContentId(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMimeType(this.entity))
                .andReturn(null);
        EasyMock.expect(this.contentTypeFactory.getContentType(null, null))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getCharacterEncoding(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getDisposition(this.entity, null))
                .andReturn(null);

        replay();

        // ------------------

        final PartFactory<Unused> UUT = new PartFactoryImpl<Unused>(this.entityHandler, this.contentTypeFactory);

        final Part part = UUT.getPart(this.entity, this.contextLocation, null);

        assertNotNull(part, "Expected a non-null part to be returned.");

        // ------------------

        verify();
    }
}
