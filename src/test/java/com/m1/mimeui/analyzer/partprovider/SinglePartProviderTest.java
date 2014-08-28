package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.Unused;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.mimedom.PartFactory;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class SinglePartProviderTest
{
    @SuppressWarnings("unchecked")
    private final PartFactory<Unused> partFactory = EasyMock.createMock(PartFactory.class);
    private final Unused entity = EasyMock.createMock(Unused.class);
    private final Part part = EasyMock.createMock(Part.class);
    private final URI contextLocation = URI.create("thismessage:/");

    private void reset ()
    {
        EasyMock.reset(
                this.partFactory, this.entity, this.part
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.partFactory, this.entity, this.part
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.partFactory, this.entity, this.part
        );
    }

    @Test
    public void testSinglePart ()
            throws Exception
    {
        reset();

        // ------------------

        EasyMock.expect(this.partFactory.getPart(this.entity, this.contextLocation, null))
                .andReturn(this.part);

        replay();

        // ------------------

        final PartProvider<Unused> UUT = new SinglePartProvider<Unused>(this.partFactory);

        final List<Part> parts = UUT.getParts(this.entity, this.contextLocation, null);

        assertEquals(parts.size(), 1, "Expect a single part to always be returned.");
        assertEquals(parts.get(0), this.part, "Expect the same part to be returned.");

        // ------------------

        verify();
    }
}
