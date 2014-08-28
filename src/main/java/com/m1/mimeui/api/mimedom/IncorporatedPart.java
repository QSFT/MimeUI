package com.m1.mimeui.api.mimedom;

public interface IncorporatedPart extends Part
{
    /**
     * Returns the MessageParts object that this part belongs to.
     *
     * @return The MessageParts object that this part belongs to.
     */
    public AnalyzedMessage getAnalyzedMessage ();

    /**
     * Returns the ID of this entity. This is the ID assigned to the entity by the parser for this entity at
     * parse time. All entities have an ID that is unique within the message, and predictable between parses for any
     * given message. It may be important to understand the distinction between ID and Content ID.
     *
     * @return The ID of this entity.
     */
    public int getIndex ();
}
