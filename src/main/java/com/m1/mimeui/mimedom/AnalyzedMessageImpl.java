package com.m1.mimeui.mimedom;

import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.IncorporatedPart;
import com.m1.mimeui.api.mimedom.AnalyzedMessage;
import com.m1.mimeui.api.mimedom.Mailbox;
import com.m1.mimeui.api.mimedom.Part;
import com.m1.mimeui.api.mimedom.RecipientList;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnalyzedMessageImpl implements AnalyzedMessage
{
    private final List<IncorporatedPart> parts = new ArrayList<IncorporatedPart>();
    private final String subject;
    private final Date sentDate;
    private final Mailbox sender;
    private final Set<Mailbox> froms;
    private final Map<RecipientList, Set<Mailbox>> recipients;

    private final Map<String, IncorporatedPart> contentIdMap = new HashMap<String, IncorporatedPart>();
    private final Map<URI, IncorporatedPart> contentLocationMap = new HashMap<URI, IncorporatedPart>();
    private final Map<Disposition, List<IncorporatedPart>> dispositionMap = new HashMap<Disposition, List<IncorporatedPart>>();

    public AnalyzedMessageImpl (final Iterable<Part> parts, final String subject, final Date sentDate,
                                final Mailbox sender, final Set<Mailbox> froms,
                                final Map<RecipientList, Set<Mailbox>> recipients)
    {
        this.subject = subject;
        this.sentDate = sentDate;
        this.sender = sender;
        this.froms = froms;
        this.recipients = recipients;

        for (final Part part : parts)
        {
            final IncorporatedPart incorporatedPart = new IncorporatedPartDecorator(part, this, this.parts.size());
            final String contentId = incorporatedPart.getContentId();
            final URI contentLocation = incorporatedPart.getContentLocation();
            final Disposition disposition = incorporatedPart.getDisposition();

            this.parts.add(incorporatedPart);

            if (disposition != null)
            {
                final List<IncorporatedPart> dispositionParts;

                if (this.dispositionMap.containsKey(disposition))
                {
                    dispositionParts = this.dispositionMap.get(disposition);
                }
                else
                {
                    dispositionParts = new ArrayList<IncorporatedPart>();
                }

                dispositionParts.add(incorporatedPart);

                this.dispositionMap.put(disposition, dispositionParts);
            }

            if (contentId != null)
            {
                this.contentIdMap.put(contentId, incorporatedPart);
            }

            if (contentLocation != null)
            {
                // Note: Given the current implementation, an incorporated part will never have a null content location.

                this.contentLocationMap.put(contentLocation, incorporatedPart);
            }
        }
    }

    @Override
    public String getSubject ()
    {
        return this.subject;
    }

    @Override
    public Date getSentDate ()
    {
        return this.sentDate;
    }

    @Override
    public Mailbox getSender ()
    {
        return this.sender;
    }

    @Override
    public Set<Mailbox> getFroms ()
    {
        return this.froms;
    }

    @Override
    public Map<RecipientList, Set<Mailbox>> getRecipients ()
    {
        return this.recipients;
    }

    @Override
    public List<IncorporatedPart> getParts (final Disposition... dispositionArray)
    {
        // The special case here is when no dispositions are specified. In that case we return all parts.
        if (dispositionArray.length == 0)
        {
            return Collections.unmodifiableList(this.parts);
        }

        final List<IncorporatedPart> filteredParts = new ArrayList<IncorporatedPart>(this.parts.size());

        // Sending the disposition array through a hash set will remove duplicate dispositions.
        final Iterable<Disposition> dispositions = new HashSet<Disposition>(Arrays.asList(dispositionArray));

        for (final Disposition disposition : dispositions)
        {
            if (this.dispositionMap.containsKey(disposition))
            {
                filteredParts.addAll(this.dispositionMap.get(disposition));
            }
        }

        Collections.sort(filteredParts, new Comparator<IncorporatedPart>()
        {
            @Override
            public int compare (final IncorporatedPart left, final IncorporatedPart right)
            {
                return new Integer(left.getIndex()).compareTo(right.getIndex());
            }
        });

        return Collections.unmodifiableList(filteredParts);
    }

    @Override
    public IncorporatedPart getPartByIndex (final int index)
    {
        try
        {
            return this.parts.get(index);
        }
        catch (final IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    @Override
    public IncorporatedPart getPartByContentId (final String contentId)
    {
        return this.contentIdMap.get(contentId);
    }

    @Override
    public IncorporatedPart getPartByContentLocation (final URI contentLocation)
    {
        return this.contentLocationMap.get(contentLocation);
    }
}
