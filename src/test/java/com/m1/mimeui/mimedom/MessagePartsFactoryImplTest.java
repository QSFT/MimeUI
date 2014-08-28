package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.AnalyzedMessageFactory;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.Unused;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;

import static org.testng.Assert.assertNotNull;

public class MessagePartsFactoryImplTest
{
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    @SuppressWarnings("unchecked")
    private final PartProviderFactory<Unused> partProviderFactory = EasyMock.createMock(PartProviderFactory.class);
    @SuppressWarnings("unchecked")
    private final PartProvider<Unused> partProvider = EasyMock.createMock(PartProvider.class);
    final Unused entity = EasyMock.createMock(Unused.class);

    private void reset ()
    {
        EasyMock.reset(
                this.entityHandler, this.partProviderFactory, this.partProvider, this.entity
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.entityHandler, this.partProviderFactory, this.partProvider, this.entity
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.entityHandler, this.partProviderFactory, this.partProvider, this.entity
        );
    }

    @Test
    public void testGetMessageParts ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.entityHandler.getEntityType(this.entity))
                .andReturn(EntityHandler.EntityType.SINGLE);
        EasyMock.expect(this.partProviderFactory.getPartProvider(EntityHandler.EntityType.SINGLE))
                .andReturn(this.partProvider);
        EasyMock.expect(this.partProvider.getParts(EasyMock.eq(this.entity), EasyMock.anyObject(URI.class),
                EasyMock.<ContentTypeAdjudicator>isNull()))
                .andReturn(Collections.<Part>emptyList());
        EasyMock.expect(this.entityHandler.getSubject(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getSentDate(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getSender(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getFroms(this.entity))
                .andReturn(null);
        EasyMock.expect(this.entityHandler.getRecipients(this.entity))
                .andReturn(null);

        replay();

        // ------------------

        final AnalyzedMessageFactory<Unused> UUT
                = new AnalyzedMessageFactoryImpl<Unused>(this.entityHandler, this.partProviderFactory);

        final AnalyzedMessage analyzedMessage = UUT.getAnalyzedMessage(this.entity, null);

        assertNotNull(analyzedMessage, "Expected a non-null part to be returned.");

        // ------------------

        verify();
    }
}
