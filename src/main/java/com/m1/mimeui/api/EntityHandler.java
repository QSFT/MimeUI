package com.m1.mimeui.api;

import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.RecipientList;
import com.m1.mimeui.api.parser.ParseHandler;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityHandler<E>
{
    public static enum EntityType
    {
        SINGLE, RELATED, ALTERNATIVE, MIXED
    }

    public EntityType getEntityType (final E entity);
    public String getContentId (final E entity);
    public URI getContentLocation (final E entity, final URI contextLocation);
    public String getMimeType (final E entity);
    public String getCharacterEncoding (final E entity);
    public Disposition getDisposition (final E entity, final ContentTypeAdjudicator contentTypeAdjudicator);
    public String getFilename (final E entity);
    public InputStream getInputStream (final E entity);

    public String getMultipartRelatedRootContentId (final E entity);
    public List<E> getMultipartChildEntities (final E entity);

    public String getSubject (final E messageEntity);
    public Date getSentDate (final E messageEntity);
    public Mailbox getSender (final E messageEntity);
    public Set<Mailbox> getFroms (final E messageEntity);
    public Map<RecipientList, Set<Mailbox>> getRecipients (final E messageEntity);

    public <T> T parseAndExecute (final InputStream inputStream, final ParseHandler<E, T> function)
            throws MimeUIException;
}
