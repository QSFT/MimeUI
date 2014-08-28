package com.m1.mimeui.api.mimedom;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a linear collection of selected message parts.
 */
public interface AnalyzedMessage
{
    /**
     * Returns the subject of the message, or null if the message has no subject.
     *
     * @return The subject, or null.
     */
    public String getSubject ();

    /**
     * Returns the sent date according to the message headers, or null if the message has no sent date. This value can't
     * be trusted because it can be forged, incorrect, or inaccurate. As a result, this value is fairly useless. If
     * possible, use the "received date" instead.
     *
     * @return The sent date, or null.
     */
    public Date getSentDate ();

    /**
     * Returns the sender of this message if the message has been sent "on behalf of". "On behalf of" means that one
     * mailbox is sending on behalf of another mailbox or set of mailboxes. In this case the sending mailbox is said to
     * be the "Sender" and the "on behalf of" mailboxes are said to be the "Froms".
     *
     * <p />The typical case for email, however, is that the message has only one "From" mailbox, and that mailbox is
     * also the "Sender". These messages are not "on behalf of" messages.
     *
     * <p />If the message has been sent "on behalf of", the "Sender" is the actual sender of the message, and the
     * mailbox(es) that the message was sent on behalf of are listed as "Froms".
     *
     * <p />If the message has not been sent "on behalf of", the "Sender" will be null, and the "Froms" will contain the
     * sender/from mailbox(es). In this case, the actual sender isn't known when there are more than one "From" mailbox.
     *
     * @return The sender mailbox, if applicable. Otherwise null.
     */
    public Mailbox getSender ();

    /**
     * Returns the mailboxes that the message was sent from. This is never null, but can be empty.
     *
     * @return The from mailboxes.
     */
    public Set<Mailbox> getFroms (); // the From header if present, otherwise the Sender header, otherwise null

    /**
     * Returns the mailboxes that the message was sent to. This is never null, but can be empty. Only applicable
     * recipient lists are contained in the map. Each recipient list in the map is guaranteed to contain at least one
     * mailbox.
     *
     * @return The recipient mailboxes.
     */
    public Map<RecipientList, Set<Mailbox>> getRecipients ();

    /**
     * All the parts in the message of the specified dispositions (INLINE, INLINE_ATTACHMENT, ATTACHMENT, and/or
     * RELATED). If more than one disposition is specified, a logical OR is performed. If no dispositions are specified,
     * all the parts are returned. Note that "multipart/*" parts are not included here as they have already been
     * resolved.
     *
     * @param dispositionArray The dispositions to filter for.
     * @return All the parts of the specified dispositions.
     */
    public List<IncorporatedPart> getParts (final Disposition... dispositionArray);

    /**
     * Returns the entity with the specified ID, or null if no such entity exists. Note that this is the ID, not the
     * Content ID.
     *
     * @param index The ID of the entity to be returned.
     * @return The part with the specified ID, or null if no such entity exists.
     */
    public IncorporatedPart getPartByIndex (final int index);

    /**
     * Returns the entity with the specified Content ID, or null if no such entity exists. Note that this is the
     * Content ID, not the ID.
     *
     * @param contentId The Content ID of the entity to be returned.
     * @return The part with the specified Content ID, or null if no such part exists.
     */
    public IncorporatedPart getPartByContentId (final String contentId);

    /**
     * Returns the entity with the specified content location, or null is no such entity exists.
     *
     * @param contentLocation The content location of the entity to be returned. This should be absolute.
     * @return The part with the specified content location, or null if no such part exists.
     */
    public IncorporatedPart getPartByContentLocation (final URI contentLocation);
}
