package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.Part;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class MessagePartsImplTest
{
    private final Part part1 = EasyMock.createMock(Part.class);
    private final Part part2 = EasyMock.createMock(Part.class);
    private final Part part3 = EasyMock.createMock(Part.class);
    private final Part part4 = EasyMock.createMock(Part.class);
    private final Part part5 = EasyMock.createMock(Part.class);

    private void reset ()
    {
        EasyMock.reset(
                this.part1, this.part2, this.part3, this.part4, this.part5
        );
    }

    private void replay ()
    {
        EasyMock.replay(
                this.part1, this.part2, this.part3, this.part4, this.part5
        );
    }

    private void verify ()
    {
        EasyMock.verify(
                this.part1, this.part2, this.part3, this.part4, this.part5
        );
    }

    private AnalyzedMessage makeUUT ()
    {
        reset();

        // ------------------

        EasyMock.expect(this.part1.getContentId())
                .andReturn("part1");
        EasyMock.expect(this.part1.getContentLocation())
                .andReturn(URI.create("thismessage:/part1"));
        EasyMock.expect(this.part1.getDisposition())
                .andReturn(Disposition.INLINE_ALTERNATIVE);

        EasyMock.expect(this.part2.getContentId())
                .andReturn("part2");
        EasyMock.expect(this.part2.getContentLocation())
                .andReturn(URI.create("thismessage:/part2"));
        EasyMock.expect(this.part2.getDisposition())
                .andReturn(Disposition.ALTERNATIVE);

        EasyMock.expect(this.part3.getContentId())
                .andReturn("part3");
        EasyMock.expect(this.part3.getContentLocation())
                .andReturn(URI.create("thismessage:/part3"));
        EasyMock.expect(this.part3.getDisposition())
                .andReturn(Disposition.INLINE);

        EasyMock.expect(this.part4.getContentId())
                .andReturn("part4");
        EasyMock.expect(this.part4.getContentLocation())
                .andReturn(URI.create("thismessage:/part4"));
        EasyMock.expect(this.part4.getDisposition())
                .andReturn(Disposition.ATTACHMENT);

        EasyMock.expect(this.part5.getContentId())
                .andReturn("part5");
        EasyMock.expect(this.part5.getContentLocation())
                .andReturn(URI.create("thismessage:/part5"));
        EasyMock.expect(this.part5.getDisposition())
                .andReturn(Disposition.ATTACHMENT);

        replay();

        // ------------------

        final List<Part> parts = new ArrayList<Part>();

        parts.add(this.part1);
        parts.add(this.part2);
        parts.add(this.part3);
        parts.add(this.part4);
        parts.add(this.part5);

        return new AnalyzedMessageImpl(parts, null, null, null, null, null);
    }

    @Test
    public void testGetPartsNoDispositions ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts().size(), 5,
                "Expected all of the parts to be returned.");

        verify();
    }

    @Test
    public void testGetPartsNoMatching ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.INLINE_ATTACHMENT).size(), 0,
                "Expected none of the parts to be returned since there are no inline attachments.");

        verify();
    }

    @Test
    public void testGetPartsOneMatching ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.INLINE).size(), 1,
                "Expected one of the parts to be returned since there is only one inline part.");

        verify();
    }

    @Test
    public void testGetPartsTwoMatching ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.ATTACHMENT).size(), 2,
                "Expected two of the parts to be returned since there are two attachments.");

        verify();
    }

    @Test
    public void testGetPartsAllDispositionsMatch ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.INLINE, Disposition.INLINE_ALTERNATIVE).size(), 2,
                "Expected two of the parts to be returned since there are two matching parts.");

        verify();
    }

    @Test
    public void testGetPartsMultipleDispositionsMatch ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.INLINE_ATTACHMENT, Disposition.INLINE_ALTERNATIVE).size(), 1,
                "Expected one of the parts to be returned since there is an inline alternative but no inline attachments.");

        verify();
    }

    @Test
    public void testPartOrder1 ()
    {
        final AnalyzedMessage UUT = makeUUT();
        final List<IncorporatedPart> parts = UUT.getParts(Disposition.INLINE, Disposition.ATTACHMENT, Disposition.INLINE_ALTERNATIVE);

        assertEquals(parts.get(0).getIndex(), 0, "Expected the parts to be sorted by index (0).");
        assertEquals(parts.get(1).getIndex(), 2, "Expected the parts to be sorted by index (1).");
        assertEquals(parts.get(2).getIndex(), 3, "Expected the parts to be sorted by index (2).");
        assertEquals(parts.get(3).getIndex(), 4, "Expected the parts to be sorted by index (3).");

        verify();
    }

    @Test
    public void testPartOrder2 ()
    {
        final AnalyzedMessage UUT = makeUUT();
        final List<IncorporatedPart> parts = UUT.getParts(Disposition.ATTACHMENT, Disposition.INLINE, Disposition.INLINE_ALTERNATIVE);

        assertEquals(parts.get(0).getIndex(), 0, "Expected the parts to be sorted by index (0).");
        assertEquals(parts.get(1).getIndex(), 2, "Expected the parts to be sorted by index (1).");
        assertEquals(parts.get(2).getIndex(), 3, "Expected the parts to be sorted by index (2).");
        assertEquals(parts.get(3).getIndex(), 4, "Expected the parts to be sorted by index (3).");

        verify();
    }

    @Test
    public void testPartDistinction ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getParts(Disposition.INLINE, Disposition.INLINE, Disposition.INLINE).size(), 1,
                "Expected one of the parts to be returned since there is only one inline part.");

        verify();
    }

    @Test
    public void testGetPartByIndexInvalid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertNull(UUT.getPartByIndex(5),
                "Expected null since there is no part with an index of 5.");

        verify();
    }

    @Test
    public void testGetPartByIndexValid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getPartByIndex(3).getIndex(), 3,
                "Expected to get 3 since it is the part with an index of 3.");

        verify();
    }

    @Test
    public void testGetPartByContentIdInvalid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertNull(UUT.getPartByContentId("foo"),
                "Expected null since there is no part with a content id of foo.");

        verify();
    }

    @Test
    public void testGetPartByContentIdValid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getPartByContentId("part3").getIndex(), 2,
                "Expected to get the part with an index of 2 since that is the index of part3.");

        verify();
    }

    @Test
    public void testGetPartByContentLocationInvalid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertNull(UUT.getPartByContentLocation(URI.create("thismessage:/baz")),
                "Expected null since there is no part with a content location of 'thismessage:/baz'.");

        verify();
    }

    @Test
    public void testGetPartByContentLocationValid ()
    {
        final AnalyzedMessage UUT = makeUUT();

        assertEquals(UUT.getPartByContentLocation(URI.create("thismessage:/part3")).getIndex(), 2,
                "Expected to get the part with an index of 2 since that is the index of part3.");

        verify();
    }
}
