package com.m1.mimeui.mime4j;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.MimeUIException;
import com.m1.mimeui.api.parser.ParseHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.RecipientList;
import com.m1.mimeui.mimedom.MailboxImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecoderUtil;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.FieldName;
import org.apache.james.mime4j.field.ContentIdFieldImpl;
import org.apache.james.mime4j.field.ContentLocationFieldImpl;
import org.apache.james.mime4j.stream.Field;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Mime4jEntityHandler implements EntityHandler<Entity>
{
    private String getUnbracketedContentId (final String contentId)
    {
        if (StringUtils.isBlank(contentId))
        {
            return null;
        }
        else if (contentId.charAt(0) == '<' && contentId.charAt(contentId.length() - 1) == '>')
        {
            return contentId.substring(1, contentId.length() - 1);
        }
        else
        {
            return contentId;
        }
    }

    @Override
    public EntityType getEntityType (final Entity entity)
    {
        final Body body = entity.getBody();

        if (body instanceof Multipart)
        {
            final String multipartType = ((Multipart) body).getSubType().toLowerCase();

            if ("alternative".equals(multipartType))
            {
                return EntityType.ALTERNATIVE;
            }
            else if ("related".equals(multipartType))
            {
                return EntityType.RELATED;
            }
            else
            {
                // This case covers "mixed", "report", "digest", "parallel", and any other multipart/* not covered
                // above.

                // Note that for multipart/report, we assume that each child entity in the report has a content-type
                // header, as needed. A omitted content-type header will cause the child entity to be treated like
                // text/plain. That may be fine for the first (human readable) part, but subsequent parts are usually
                // intended to be machine readable (e.g. message/delivery-status, et al).

                // Note that for multipart/digest, mime4j correctly defaults child entities' content-types to
                // message/rfc822 for us so we don't have to deal with it here.

                return EntityType.MIXED;
            }
        }
        else if (body instanceof SingleBody)
        {
            return EntityType.SINGLE;
        }
        else
        {
            // This should never happen because we have a custom mime4j MessageBuilder that returns attached messages
            // as "SingleBody" (above) instead of as "Message" and there are no other "Body" implementations in mime4j.
            // In other words, the above cases cover every possibility that should normally arise.

            // todo Consider throwing an exception. Returning null here will cause NPEs.
            return null;
        }
    }

    @Override
    public String getContentId (final Entity entity)
    {
        final Header header = entity.getHeader();

        if (header == null)
        {
            return null;
        }

        final Field field = header.getField(FieldName.CONTENT_ID);

        if (field == null)
        {
            return null;
        }

        // todo this is going to be a PITA to test because of ContentIdFieldImpl.
        // todo Inject a FieldParser<ContentIdField> (then use ContentIdFieldImpl.PARSER in MimeUIFactory).

        return getUnbracketedContentId(ContentIdFieldImpl.PARSER.parse(field, null).getId());
    }

    /**
     * Returns the Content-Location header, if present. If that header isn't present, then Content-Base is used as a
     * fall back. If neither is present, null is returned.
     *
     * <p />If either header is a relative URI, we resolve it against "locationContext". If it is absolute, we simply
     * return it. Accordingly, the returned URI is always absolute.
     *
     * <p />Note that we accept Content-Base as a fallback in order to be compatible with RFC-2110. This RFC was
     * obsoleted by RFC-2557, and RFC-2557 specifically removes the Content-Base header, but allows it to be used for
     * compatibility reasons.
     *
     * @param entity The entity whose content location is to be returned.
     * @param contextLocation The absolute location to use as this entity's context.
     * @return The absolute content location.
     */
    @Override
    public URI getContentLocation (final Entity entity, final URI contextLocation)
    {
        final Header header = entity.getHeader();

        if (header == null)
        {
            return null;
        }

        final Field usedLocationField;
        final Field contentLocationField = header.getField(FieldName.CONTENT_LOCATION);

        if (contentLocationField == null)
        {
            final Field contentBaseField = header.getField("Content-Base");

            if (contentBaseField == null)
            {
                return null;
            }
            else
            {
                usedLocationField = contentBaseField;
            }
        }
        else
        {
            usedLocationField = contentLocationField;
        }

        try
        {
            final String relativeLocationString
                    = ContentLocationFieldImpl.PARSER.parse(usedLocationField, null).getLocation();
            final URI relativeLocationUri = new URI(relativeLocationString);

            return contextLocation.resolve(relativeLocationUri);
        }
        catch (final URISyntaxException e)
        {
            return null;
        }
    }

    @Override
    public String getMimeType (final Entity entity)
    {
        return entity.getMimeType();
    }

    @Override
    public String getCharacterEncoding (final Entity entity)
    {
        return entity.getCharset();
    }

    @Override
    public Disposition getDisposition (final Entity entity, final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        if (ContentDispositionField.DISPOSITION_TYPE_ATTACHMENT.equals(entity.getDispositionType()))
        {
            return Disposition.ATTACHMENT;
        }
        else if (contentTypeAdjudicator.isSupportedContentType(getMimeType(entity), getCharacterEncoding(entity)))
        {
            return Disposition.INLINE;
        }
        else
        {
            return Disposition.INLINE_ATTACHMENT;
        }
    }

    @Override
    public String getFilename (final Entity entity)
    {
        // todo: Track Mime4j-109 - Add support for RFC-2231 (MIME Parameter Value Encoded Word Extensions)
        String filename = entity.getFilename();

        if (filename == null)
        {
            final ContentTypeField contentTypeField = (ContentTypeField) entity.getHeader().getField("Content-Type");
            filename = contentTypeField.getParameter("name");
        }

        // see EMSUI-614
        if (filename != null) {
            filename = DecoderUtil.decodeEncodedWords(filename, null);

            // note: if combining LTR strings and RTL strings, DecoderUtil#decodeEncodedWords could return
            //        a string with some Unicode control characters. Sanitize this string.
            filename = sanitizeString(filename);
        }

        return filename;
    }

    protected String sanitizeString(String dirtyString) {
        StringBuilder sb = new StringBuilder(dirtyString.length());
        for (int i=0; i<dirtyString.length(); i++) {
            if(!Character.isIdentifierIgnorable(dirtyString.charAt(i))) {
                sb.append(dirtyString.charAt(i));
            }
        }
        return sb.toString();
    }

    @Override
    public InputStream getInputStream (final Entity entity)
    {
        final SingleBody singleBody = (SingleBody) entity.getBody();

        try
        {
            return singleBody.getInputStream();
        }
        catch (final IOException e)
        {
            // todo log this
        }

        return null;
    }

    @Override
    public String getMultipartRelatedRootContentId (final Entity entity)
    {
        final ContentTypeField contentTypeField = (ContentTypeField) entity.getHeader().getField("Content-Type");
        return getUnbracketedContentId(contentTypeField.getParameter("start"));
    }

    @Override
    public List<Entity> getMultipartChildEntities (final Entity entity)
    {
        return ((Multipart) entity.getBody()).getBodyParts();
    }

    @Override
    public String getSubject (final Entity messageEntity)
    {
        return messageEntity instanceof Message ? ((Message) messageEntity).getSubject() : null;
    }

    @Override
    public Date getSentDate (final Entity messageEntity)
    {
        return messageEntity instanceof Message ? ((Message) messageEntity).getDate() : null;
    }

    private Mailbox makeMailbox (final org.apache.james.mime4j.dom.address.Mailbox mime4jMailbox)
    {
        return mime4jMailbox == null ? null : new MailboxImpl(mime4jMailbox.getName(), mime4jMailbox.getAddress());
    }

    private Set<Mailbox> makeMailboxSet (final MailboxList mime4jMailboxList)
    {
        if (mime4jMailboxList == null || mime4jMailboxList.isEmpty())
        {
            return Collections.emptySet();
        }

        final Set<Mailbox> mailboxSet = new HashSet<Mailbox>(mime4jMailboxList.size());

        for (final org.apache.james.mime4j.dom.address.Mailbox mime4jMailbox : mime4jMailboxList)
        {
            mailboxSet.add(makeMailbox(mime4jMailbox));
        }

        return mailboxSet;
    }

    private Set<Mailbox> makeMailboxSet (final AddressList mime4jAddressList)
    {
        return mime4jAddressList == null ? Collections.<Mailbox>emptySet() : makeMailboxSet(mime4jAddressList.flatten());
    }

    @Override
    public Mailbox getSender (final Entity messageEntity)
    {
        if (messageEntity instanceof Message)
        {
            return makeMailbox(((Message) messageEntity).getSender());
        }
        else
        {
            return null;
        }
    }

    @Override
    public Set<Mailbox> getFroms (final Entity messageEntity)
    {
        if (messageEntity instanceof Message)
        {
            return makeMailboxSet(((Message) messageEntity).getFrom());
        }
        else
        {
            return null;
        }
    }

    @Override
    public Map<RecipientList, Set<Mailbox>> getRecipients (final Entity messageEntity)
    {
        if (messageEntity instanceof Message)
        {
            final Map<RecipientList, Set<Mailbox>> recipients = new HashMap<RecipientList, Set<Mailbox>>();

            recipients.put(RecipientList.TO, makeMailboxSet(((Message) messageEntity).getTo()));
            recipients.put(RecipientList.CC, makeMailboxSet(((Message) messageEntity).getCc()));
            recipients.put(RecipientList.BCC, makeMailboxSet(((Message) messageEntity).getBcc()));

            return recipients;
        }
        else
        {
            return null;
        }
    }

    @Override
    public <T> T parseAndExecute (final InputStream inputStream,
                                  final ParseHandler<Entity, T> handler)
            throws MimeUIException
    {
        final MessageBuilder builder = new NonRecursiveMime4jMessageBuilder();
        Message message = null;

        try
        {
            message = builder.parseMessage(inputStream);

            return handler.handle(message);
        }
        catch (final IOException e)
        {
            throw new MimeUIException("Mime4j was unable to parse the message.", e);
        }
        catch (final MimeException e)
        {
            throw new MimeUIException("Mime4j was unable to parse the message.", e);
        }
        finally
        {
            if (message != null)
            {
                message.dispose();
            }
        }
    }
}
