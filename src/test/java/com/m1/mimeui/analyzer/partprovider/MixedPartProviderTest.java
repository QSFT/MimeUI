package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class MixedPartProviderTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    @SuppressWarnings("unchecked")
    private final PartProviderFactory<Unused> partProviderFactory = EasyMock.createMock(PartProviderFactory.class);
    @SuppressWarnings("unchecked")
    private final PartProvider<Unused> partProvider = EasyMock.createMock(PartProvider.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    private final Unused childEntity = EasyMock.createMock(Unused.class);
    private final Part childPart = EasyMock.createMock(Part.class);
    private final URI contextLocation = URI.create("thismessage:/");

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.childEntity, this.childPart
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.childEntity, this.childPart
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.childEntity, this.childPart
        );
    }

    @Test
    public void testEmpty ()
            throws Exception
    {
        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(Collections.<Unused>emptyList());
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);

        replay();

        // ------------------

        final PartProvider<Unused> UUT = new MixedPartProvider<Unused>(this.entityHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the parent part is empty.");

        // ------------------

        verify();
    }

    @Test
    public void testOne ()
            throws Exception
    {
        final List<Part> childParts = Collections.singletonList(this.childPart);
        final List<Unused> childEntities = Collections.singletonList(this.childEntity);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getEntityType(this.childEntity))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.childEntity, this.contextLocation, null))
                .andReturn(childParts);

        replay();

        // ------------------

        final PartProvider<Unused> UUT = new MixedPartProvider<Unused>(this.entityHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 1, "Expected the part to be returned.");
        assertEquals(parts.get(0), this.childPart, "Expected the same part to be returned.");

        // ------------------

        verify();
    }
}
