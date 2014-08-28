/*
 * Mime4jEntityHandlerTest
 * Copyright (c) 2001-2014 Dell MessageOne Inc.
 */
package com.m1.mimeui.mime4j;

import org.apache.james.mime4j.dom.Entity;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author doug_tucker
 */
public class Mime4jEntityHandlerTest {
    public static final String ALREADY_DECODED_STRING = "filename.txt";
    public static final String TEST_STRING = "=?iso-8859-1?q?this is some text?=";
    public static final String TEST_STRING_NO_SPACES = "=?iso-8859-1?q?this=20is=20some=20text?=";
    public static final String TEST_STRING_DECODED = "this is some text";
    public static final String MULTIPLE_TEST_STRINGS = "=?iso-8859-1?q?this is some text?= =?iso-8859-1?q?this is some text?=";
    public static final String UTF8_ALL_LTR_ENCODED_STRING = "=?utf-8?B?zrXOu867zrfOvc65zrrOrC50eHQ=?=";
    public static final String UTF8_ALL_LTR_DECODED_STRING = "ελληνικά.txt";
    public static final String UTF8_MIXED_LTR_AND_RTL_ENCODED_STRING = "=?utf-8?B?4oCt4oCO2KfZhNi52LHYqNmK2KkudHh0?=";
    public static final String UTF8_MIXED_LTR_AND_RTL_DECODED_STRING = "العربية.txt" ;
    public static final String MULTIPLE_TEST_STRINGS_DECODED = TEST_STRING_DECODED + TEST_STRING_DECODED;

    @Test
    public void testGetFilename() throws Exception {
        Mime4jEntityHandler handler = EasyMock.createMockBuilder(Mime4jEntityHandler.class)
                .addMockedMethod("sanitizeString")
                .createStrictMock();
        Entity mockEntity = EasyMock.createStrictMock(Entity.class);

        // already decoded string
        EasyMock.expect(mockEntity.getFilename()).andReturn(ALREADY_DECODED_STRING);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(ALREADY_DECODED_STRING);
        EasyMock.replay(mockEntity, handler);
        assertEquals("strings should match", ALREADY_DECODED_STRING, handler.getFilename(mockEntity));
        EasyMock.verify(mockEntity, handler);

        // string without spaces
        EasyMock.reset(mockEntity, handler);
        EasyMock.expect(mockEntity.getFilename()).andReturn(TEST_STRING_NO_SPACES);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(TEST_STRING_DECODED);
        EasyMock.replay(mockEntity, handler);
        assertEquals("strings should match", TEST_STRING_DECODED, handler.getFilename(mockEntity));
        EasyMock.verify(mockEntity, handler);

        // string with spaces
        EasyMock.reset(mockEntity, handler);
        EasyMock.expect(mockEntity.getFilename()).andReturn(TEST_STRING);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(TEST_STRING_DECODED);
        EasyMock.replay(mockEntity, handler);
        assertEquals("strings should match", TEST_STRING_DECODED, handler.getFilename(mockEntity));
        EasyMock.verify(mockEntity, handler);

        // multiple encoded strings
        EasyMock.reset(mockEntity, handler);
        EasyMock.expect(mockEntity.getFilename()).andReturn(MULTIPLE_TEST_STRINGS);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(MULTIPLE_TEST_STRINGS_DECODED);
        EasyMock.replay(mockEntity, handler);
        assertEquals("strings should match", MULTIPLE_TEST_STRINGS_DECODED, handler.getFilename(mockEntity));
        EasyMock.verify(mockEntity, handler);

        // all LTR (greek + english)
        EasyMock.reset(mockEntity, handler);
        EasyMock.expect(mockEntity.getFilename()).andReturn(UTF8_ALL_LTR_ENCODED_STRING);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(UTF8_ALL_LTR_DECODED_STRING);
        EasyMock.replay(mockEntity, handler);
        assertTrue("strings should match", UTF8_ALL_LTR_DECODED_STRING.equals(handler.getFilename(mockEntity)));
        EasyMock.verify(mockEntity, handler);

        // mixed LTR and RTL (arabic + english)
        EasyMock.reset(mockEntity, handler);
        EasyMock.expect(mockEntity.getFilename()).andReturn(UTF8_MIXED_LTR_AND_RTL_ENCODED_STRING);
        EasyMock.expect(handler.sanitizeString(EasyMock.anyObject(String.class))).andReturn(UTF8_MIXED_LTR_AND_RTL_ENCODED_STRING);
        EasyMock.replay(mockEntity, handler);
        assertTrue("strings should match", UTF8_MIXED_LTR_AND_RTL_ENCODED_STRING.equals(handler.getFilename(mockEntity)));
        EasyMock.verify(mockEntity, handler);
    }
}
