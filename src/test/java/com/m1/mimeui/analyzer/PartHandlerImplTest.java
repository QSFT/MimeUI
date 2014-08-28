package com.m1.mimeui.analyzer;

import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.mimedom.ContentType;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.renderer.ContentViewer;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

public class PartHandlerImplTest
{
    private final Part part1 = EasyMock.createMock(Part.class);
    private final Part part2 = EasyMock.createMock(Part.class);
    private final Part part3 = EasyMock.createMock(Part.class);
    private final IncorporatedPart incorporatedPart = EasyMock.createMock(IncorporatedPart.class);

    private final Collection<Part> emptyParts = Collections.emptyList();
    private final Collection<Part> parts = new ArrayList<Part>();

    private final ContentType contentType = EasyMock.createMock(ContentType.class);
    private final ContentViewer contentViewer = EasyMock.createMock(ContentViewer.class);

    public PartHandlerImplTest ()
    {
        this.parts.add(this.part1);
        this.parts.add(this.part2);
        this.parts.add(this.part3);
    }

    private void reset ()
    {
        EasyMock.reset(
                this.part1, this.part2, this.part3, this.incorporatedPart,
                this.contentType, this.contentViewer
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.part1, this.part2, this.part3, this.incorporatedPart,
                this.contentType, this.contentViewer
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.part1, this.part2, this.part3, this.incorporatedPart,
                this.contentType, this.contentViewer
        );
    }

    @Test
    public void testGetTriagedPartsEmpty ()
    {
        reset();

        // No Expectations.

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final Collection<Part> triagedParts = UUT.getTriagedParts(this.emptyParts, Disposition.ATTACHMENT);

        assertEquals(triagedParts.size(), 0, "Expected the parts to still be empty.");

        verify();
    }

    @Test
    public void testGetTriagedPartsAllInline ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE_ALTERNATIVE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.INLINE_ATTACHMENT);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final Collection<Part> triagedParts = UUT.getTriagedParts(this.parts, Disposition.ATTACHMENT);

        final List<Part> triagedPartList = new ArrayList<Part>();

        triagedPartList.addAll(triagedParts);

        assertEquals(triagedParts.size(), 3, "Expected all the parts to be returned.");

        assertTrue(triagedPartList.get(0).getDisposition() == Disposition.ATTACHMENT,
                "Expected the part to be triaged.");
        assertTrue(triagedPartList.get(1).getDisposition() == Disposition.ATTACHMENT,
                "Expected the part to be triaged.");
        assertTrue(triagedPartList.get(2).getDisposition() == Disposition.ATTACHMENT,
                "Expected the part to be triaged.");

        verify();
    }

    @Test
    public void testGetTriagedPartsNoInline ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT).times(2);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.ALTERNATIVE).times(2);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED).times(2);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final Collection<Part> triagedParts = UUT.getTriagedParts(this.parts, Disposition.INLINE_ATTACHMENT);

        final List<Part> triagedPartList = new ArrayList<Part>();

        triagedPartList.addAll(triagedParts);

        assertEquals(triagedParts.size(), 3, "Expected all the parts to be returned.");

        assertTrue(triagedPartList.get(0).getDisposition() == Disposition.ATTACHMENT,
                "Expected the part not to be triaged.");
        assertTrue(triagedPartList.get(1).getDisposition() == Disposition.ALTERNATIVE,
                "Expected the part not to be triaged.");
        assertTrue(triagedPartList.get(2).getDisposition() == Disposition.RELATED,
                "Expected the part not to be triaged.");

        verify();
    }

    @Test
    public void testGetTriagedPartsSomeInline ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT).times(2);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED).times(2);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final Collection<Part> triagedParts = UUT.getTriagedParts(this.parts, Disposition.ALTERNATIVE);

        final List<Part> triagedPartList = new ArrayList<Part>();

        triagedPartList.addAll(triagedParts);

        assertEquals(triagedParts.size(), 3, "Expected all the parts to be returned.");

        assertTrue(triagedPartList.get(0).getDisposition() == Disposition.ATTACHMENT,
                "Expected the part not to be triaged.");
        assertTrue(triagedPartList.get(1).getDisposition() == Disposition.ALTERNATIVE,
                "Expected the part to be triaged.");
        assertTrue(triagedPartList.get(2).getDisposition() == Disposition.RELATED,
                "Expected the part not to be triaged.");

        verify();
    }

    @Test
    public void testHasDispositionsNoParts ()
    {
        reset();

        // No Expectations.

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.emptyParts, Disposition.ALTERNATIVE);

        assertFalse(hasDispositions, "When no parts are provided, expect false.");

        verify();
    }

    @Test
    public void testHasDispositionsNoDispositions ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.parts);

        assertFalse(hasDispositions, "When no dispositions are provided, expect false.");

        verify();
    }

    @Test
    public void testHasDispositionsOneDispositionNoMatch ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.parts, Disposition.ALTERNATIVE);

        assertFalse(hasDispositions, "Expected no match.");

        verify();
    }

    @Test
    public void testHasDispositionsOneDispositionMatch ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.parts, Disposition.RELATED);

        assertTrue(hasDispositions, "Expected a match.");

        verify();
    }

    @Test
    public void testHasDispositionsMultipleDispositionsNoMatch ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        EasyMock.expect(this.part3.getDisposition()).andReturn(Disposition.RELATED);

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.parts, Disposition.ALTERNATIVE, Disposition.INLINE_ALTERNATIVE);

        assertFalse(hasDispositions, "Expected no match.");

        verify();
    }

    @Test
    public void testHasDispositionsMultipleDispositionsMatch ()
    {
        reset();

        EasyMock.expect(this.part1.getDisposition()).andReturn(Disposition.ATTACHMENT);
        EasyMock.expect(this.part2.getDisposition()).andReturn(Disposition.INLINE);
        // We don't expect part3 to be queried because part2 was a match (we terminate early).

        replay();

        final PartHandler UUT = new PartHandlerImpl();

        final boolean hasDispositions = UUT.hasDispositions(this.parts, Disposition.ALTERNATIVE, Disposition.INLINE);

        assertTrue(hasDispositions, "Expected a match.");

        verify();
    }
}
