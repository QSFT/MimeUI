package com.m1.mimeui.mime4j;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.field.LenientFieldParser;
import org.apache.james.mime4j.message.BasicBodyFactory;
import org.apache.james.mime4j.message.DefaultBodyDescriptorBuilder;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.MessageImpl;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.mime4j.stream.MimeTokenStream;
import org.apache.james.mime4j.stream.RecursionMode;

import java.io.IOException;
import java.io.InputStream;

public class NonRecursiveMime4jMessageBuilder extends DefaultMessageBuilder
{
    @Override
    public Message parseMessage(final InputStream is)
            throws IOException
    {
        try
        {
            final MessageImpl message = new MessageImpl();
            final DecodeMonitor mon = DecodeMonitor.SILENT;
            final BodyDescriptorBuilder bdb = new DefaultBodyDescriptorBuilder(null, LenientFieldParser.getParser(), mon);
            final MimeTokenStream tokenStream = new MimeTokenStream(new MimeConfig(), mon, bdb);
            final MimeStreamParser parser = new MimeStreamParser(tokenStream);

            tokenStream.setRecursionMode(RecursionMode.M_NO_RECURSE);

            parser.setContentHandler(new MimeUIEntityBuilder(message, new BasicBodyFactory()));
            parser.setContentDecoding(true);
            parser.parse(is);

            return message;
        }
        catch (final MimeException e)
        {
            throw new MimeIOException(e);
        }
    }
}
