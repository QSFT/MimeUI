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

public class AlternativePartProviderTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    private final PartHandler partHandler = EasyMock.createMock(PartHandler.class);
    @SuppressWarnings("unchecked")
    private final PartProviderFactory<Unused> partProviderFactory = EasyMock.createMock(PartProviderFactory.class);
    @SuppressWarnings("unchecked")
    private final PartProvider<Unused> partProvider = EasyMock.createMock(PartProvider.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    private final Unused alternativeChildEntity1 = EasyMock.createMock(Unused.class);
    private final Unused alternativeChildEntity2 = EasyMock.createMock(Unused.class);
    private final Part childPart = EasyMock.createMock(Part.class);
    private final URI contextLocation = URI.create("thismessage:/");

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.alternativeChildEntity1, this.alternativeChildEntity2, this.childPart
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.alternativeChildEntity1, this.alternativeChildEntity2, this.childPart
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.partHandler, this.partProviderFactory, this.partProvider,
                this.entity, this.alternativeChildEntity1, this.alternativeChildEntity2, this.childPart
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

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the parent part is empty.");

        // ------------------

        verify();
    }

    @Test
    public void testOneNoChildParts ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Unused> childEntities = Collections.singletonList(this.alternativeChildEntity1);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList1, Disposition.INLINE_ATTACHMENT))
                .andReturn(false);

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoUnsupported ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> childEntities = new ArrayList<Unused>();

        childEntities.add(this.alternativeChildEntity1);
        childEntities.add(this.alternativeChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);

        // Child Entity 1 (Unsupported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList1, Disposition.INLINE_ATTACHMENT))
                .andReturn(true);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList1, Disposition.INLINE_ALTERNATIVE))
                .andReturn(Collections.<Part>emptyList());
        // We expected inline alternative because the first part checked is the most complex part.

        // Child Entity 1 (Unsupported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList2, Disposition.INLINE_ATTACHMENT))
                .andReturn(true);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList2, Disposition.ALTERNATIVE))
                .andReturn(Collections.<Part>emptyList());

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoWorstSupported ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> childEntities = new ArrayList<Unused>();

        childEntities.add(this.alternativeChildEntity1);
        childEntities.add(this.alternativeChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);

        // Child Entity 1 (Unsupported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList1, Disposition.INLINE_ATTACHMENT))
                .andReturn(true);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList1, Disposition.INLINE_ALTERNATIVE))
                .andReturn(Collections.<Part>emptyList());
        // We expected inline alternative because the first part checked is the most complex part.

        // Child Entity 1 (Supported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList2, Disposition.INLINE_ATTACHMENT))
                .andReturn(false);

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoBestSupported ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> childEntities = new ArrayList<Unused>();

        childEntities.add(this.alternativeChildEntity1);
        childEntities.add(this.alternativeChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);

        // Child Entity 1 (Supported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList1, Disposition.INLINE_ATTACHMENT))
                .andReturn(false);

        // Child Entity 1 (Unsupported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList2, Disposition.ALTERNATIVE))
                .andReturn(Collections.<Part>emptyList());
        // Note that we don't call hasDispositions() because we already know that we have a better supported format.

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }

    @Test
    public void testTwoBothSupported ()
            throws Exception
    {
        final List<Part> emptyPartsList1 = Collections.emptyList();
        final List<Part> emptyPartsList2 = Collections.emptyList();
        final List<Unused> childEntities = new ArrayList<Unused>();

        childEntities.add(this.alternativeChildEntity1);
        childEntities.add(this.alternativeChildEntity2);

        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getMultipartChildEntities(this.entity))
                .andReturn(childEntities);
        EasyMock.expect(this.entityHandler.getContentLocation(this.entity, this.contextLocation))
                .andReturn(null);

        // Child Entity 1 (Supported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity1))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity1, this.contextLocation, null))
                .andReturn(emptyPartsList1);
        EasyMock.expect(this.partHandler.hasDispositions(emptyPartsList1, Disposition.INLINE_ATTACHMENT))
                .andReturn(false);

        // Child Entity 1 (Supported)
        EasyMock.expect(this.entityHandler.getEntityType(this.alternativeChildEntity2))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(this.alternativeChildEntity2, this.contextLocation, null))
                .andReturn(emptyPartsList2);
        EasyMock.expect(this.partHandler.getTriagedParts(emptyPartsList2, Disposition.ALTERNATIVE))
                .andReturn(Collections.<Part>emptyList());
        // Note that we don't call hasDispositions() because we already know that we have a better supported format.

        replay();

        // ------------------

        final PartProvider<Unused> UUT
                = new AlternativePartProvider<Unused>(this.entityHandler, this.partHandler, this.partProviderFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 0, "Expect no parts to be returned since the part provider doesn't return any.");

        // ------------------

        verify();
    }
}
