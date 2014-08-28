package com.m1.mimeui.api.mimedom;

import java.io.InputStream;
import java.net.URI;

/**
 * Represents a message part. A part can be either an attachment, or an inline body part.
 *
 * <p />The message parts are linearized such that multipart/* parts have already been resolved, and are never
 * represented by a Part. Multipart/mixed child parts are promoted, multipart/alternative child parts are triaged and
 * promoted, multipart/relative child parts are triaged and promoted, etc...
 *
 * <p />Note that the MIME standard specifies many other attributes, which are not accessible here. This is for brevity.
 * Any additional attributes could be added in the future as needed.
 */
public interface Part
{
    /**
     * Returns the content ID of this entity, if any. This is the ID assigned to the entity by the originator of the
     * message, and since it is optional, may be omitted. If the entity has no content ID assigned to it, this method
     * returns null. Note that the format of the content ID is undefined, although it is guaranteed to be unique. It
     * may be important to understand the distinction between ID and Content ID.
     *
     * @return The content ID of this entity, or null if no content id was present.
     */
    public String getContentId ();

    /**
     * Returns the context location of this entity. Message roots are assume the default context location of
     * "thismessage:/". Context locations are passed to child entities from their parent multipart entities. That-is, if
     * a multipart entity has the *content* location "thismessage:/example", this URI will be passed to all of its
     * children as their *context* location. In this way *context* location, can never be null. Even if a message
     * contains no "Content-Location" headers what-so-ever, all of its parts will at least have a context location of
     * "thismessage:/".
     *
     * todo: possibly force this to always be a hierarchical URI by replacing opaque URIs with "thismessage:/".
     *
     * @return The context location of this entity.
     */
    public URI getContextLocation ();

    /**
     * Returns the content location of this entity, if any. This is really a URI for the entity, not the URI per se. In
     * other words, the value here is never actually resolved against a network, and can, in fact, by any URI, in any
     * schema. In fact, it would not be uncommon for this to be a "cid:" URI, although that would normally be done via
     * the Content-Id header (see above) instead of the Content-Location header.
     *
     * <p>Note that once a part is incorporated, that a default content location will be provided (derived from the file
     * name) if one is not explicitly provided. The derived content location for incorporated parts is the part's
     * (possibly derived) filename, resolved (as needed and possible) against its context location.
     *
     * <p>Note that this is almost always an absolute URI. If the entity provided a relative URI, it was resolved based
     * on that entities context. The only time a relative URI might be returned is when the context location is an
     * opaque URI (e.g. a mailto:... URI, a news:... URI, a cid:... URI, etc...), and the explicit content location is
     * relative.
     *
     * @return The content location of this entity, of null if no content location was present.
     */
    public URI getContentLocation ();

    /**
     * Returns the content type of this entity. For entities with no content type specified, the parser will derive
     * the appropriate default content type automatically. This method should therefore never return null. Additionally
     * since multipart/* parts are resolved by the parser and never converted to Part objects, the content type will
     * never be a multipart/*.
     *
     * @return The content ID of this entity.
     */
    public ContentType getContentType ();

    /**
     * Returns the character encoding of this entity, or null if this entity is not textual. Generally, only text/*
     * content types have character encoding. For textual entities with no character encoding specified, the parser
     * will derive the appropriate default character encoding automatically (usually ASCII). This method should
     * therefore never return null, except for non-textual entities.
     *
     * @return The character encoding of this entity, or null of this entity is not textual.
     */
    public String getCharacterEncoding ();

    /**
     * Returns the disposition of this entity. For entities with no disposition specified, the parser will derive the
     * appropriate default disposition. This method should therefore never return null.
     *
     * @return The disposition of this entity.
     */
    public Disposition getDisposition ();

    /**
     * Returns the file name of this entity, or null if no file name was specified. Generally, only entities with a
     * disposition of ATTACHMENT will have a specified file name, but this is not always the case. Some attachments
     * will not have a file name specified, in which case this method will return null, and it will be the job of the
     * caller to provide a default value if one is required.
     *
     * @return The file name of this entity.
     */
    public String getFilename ();

    /**
     * Returns the input stream from which the content of this entity can be read. Since multipart/* and other abstract
     * parts are resolved by the parser, this method should always return non-null.
     *
     * <p>For parts that have a non-null character encoding, it is recommended that callers read the part by wrapping
     * the input stream with a Reader.
     *
     * @return The input stream from which the content of this entity can be read.
     */
    public InputStream getInputStream ();
}
