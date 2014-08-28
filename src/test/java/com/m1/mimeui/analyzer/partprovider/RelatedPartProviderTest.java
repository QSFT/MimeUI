package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class RelatedPartProviderTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    private final PartHandler partHandler = EasyMock.createMock(PartHandler.class);
    @SuppressWarnings("unchecked")
    private final PartProviderFactory<Unused> partProviderFactory = EasyMock.createMock(PartProviderFactory.class);
    @SuppressWarnings("unchecked")
    private final PartProvider<Unused> partProvider = EasyMock.createMock(PartProvider.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    private final Unused relatedChildEntity1 = EasyMock.createMock(Unused.class);
    private final Unused relatedChildEntity2 = EasyMock.createMock(Unused.class);
    private final Part childPart = EasyMock.createMock(Part.class);
    private final URI contextLocation = URI.create("thismessage:/");

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.relatedChildEntity1, this.relatedChildEntity2, this.childPart
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.relatedChildEntity1, this.relatedChildEntity2, this.childPart
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.relatedChildEntity1, this.relatedChildEntity2, this.childPart
        );
    }

    @Test
    public void testEmptyWithStart ()
            throws Exception
    {
        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(Collections.<Unused>emptyList());
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn("foo");

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the parent part is empty.");

        // ------------------

        verify();
    }

    @Test
    public void testEmptyWithoutStart ()
            throws Exception
    {
        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(Collections.<Unused>emptyList());
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn(null);

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the parent part is empty.");

        // ------------------

        verify();
    }

    @Test
    public void testOneWithoutStartNoChildParts ()
            throws Exception
    {
        final List<Unused> childEntities = Collections.singletonList(this.relatedChildEntity1);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity1, this.contextLocation, null))
                .andReturn(Collections.<Part>emptyList());

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testOneWithoutStartOneChildParts ()
            throws Exception
    {
        final List<Unused> childEntities = Collections.singletonList(this.relatedChildEntity1);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity1, this.contextLocation, null))
                .andReturn(Collections.singletonList(this.childPart));

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 1, "Expect no parts to be returned since the related is empty.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoWithoutStartNoChildParts ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> childEntities = new ArrayList<Unused>(2);

        childEntities.add(this.relatedChildEntity1);
        childEntities.add(this.relatedChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList2, Disposition.RELATED))
                .andReturn(Collections.<Part>emptyList());

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoWithStartNoChildParts ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> relatedChildEntities = new ArrayList<Unused>(2);

        relatedChildEntities.add(this.relatedChildEntity1);
        relatedChildEntities.add(this.relatedChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(relatedChildEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getMultipartRelatedRootContentId(this.entity))
                .andReturn("foo");

        // First Related Child Entity (non-root)
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.entityHandler.getContentId(this.relatedChildEntity1))
                .andReturn("bar");
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList1, Disposition.RELATED))
                .andReturn(Collections.<Part>emptyList());

        // Second Related Child Entity (root by content id)
        EasyMock.expect(this.entityHandler.getEntityType(this.relatedChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.relatedChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.entityHandler.getContentId(this.relatedChildEntity2))
                .andReturn("foo");

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new RelatedPartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }
}
