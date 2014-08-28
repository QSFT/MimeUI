package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.PartFactory;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PartProviderFactoryImplTest
{
    @SuppressWarnings("unchecked")
    private final PartFactory<Unused> partFactory = EasyMock.createMock(PartFactory.class);
    @SuppressWarnings("unchecked")
    private final EntityHandler<Unused> entityHandler = EasyMock.createMock(EntityHandler.class);
    private final PartHandler partHandler = EasyMock.createMock(PartHandler.class);

    private void reset ()
    {
        EasyMock.reset(
                this.partFactory, this.entityHandler, this.partHandler
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.partFactory, this.entityHandler, this.partHandler
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.partFactory, this.entityHandler, this.partHandler
        );
    }

    @Test
    public void testGetPartProvider ()
            throws Exception
    {
        reset();

        replay();

        final PartProviderFactory<Unused> UUT
                = new PartProviderFactoryImpl<Unused>(this.partFactory, this.entityHandler, this.partHandler);

        assertTrue(UUT.getPartProvider(EntityHandler.EntityType.SINGLE) instanceof SinglePartProvider,
                "Expected the correct part provider type to be returned.");
        assertTrue(UUT.getPartProvider(EntityHandler.EntityType.RELATED) instanceof RelatedPartProvider,
                "Expected the correct part provider type to be returned.");
        assertTrue(UUT.getPartProvider(EntityHandler.EntityType.ALTERNATIVE) instanceof AlternativePartProvider,
                "Expected the correct part provider type to be returned.");
        assertTrue(UUT.getPartProvider(EntityHandler.EntityType.MIXED) instanceof MixedPartProvider,
                "Expected the correct part provider type to be returned.");

        verify();
    }
}
