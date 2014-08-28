package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts and triages the parts from multipart/related entities in accordance with RFC-2387. Please don't make any
 * functional changes to this class without first reading and becoming familiar with this RFC!
 */
public class RelatedPartProvider<E> implements PartProvider<E>
{
    private final EntityHandler<E> entityHandler;
    private final PartHandler partHandler;
    private final PartProviderFactory<E> partProviderFactory;

    public RelatedPartProvider (final EntityHandler<E> entityHandler, final PartHandler partHandler,
                                final PartProviderFactory<E> partProviderFactory)
    {
        this.entityHandler = entityHandler;
        this.partHandler = partHandler;
        this.partProviderFactory = partProviderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Part> getParts (final E entity, final URI contextLocation,
                                final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        final List<Part> parts = new ArrayList<Part>();
        final List<E> childEntities = this.entityHandler.getMultipartChildEntities(entity);
        final URI contentLocation = ObjectUtils.defaultIfNull(
                this.entityHandler.getContentLocation(entity, contextLocation), contextLocation);

        // Implementation note: RFC-2387 section 3.1: We ignore the "type" parameter. Instead we'll recurse into the
        // children and pick the content type of the root up from that.
        // final String rootContentType = contentTypeField.getParameter("type");

        // Implementation note: RFC-2387 section 3.2: We extract the "start" parameter from the raw content type header.
        // This should be a bracketed "cid" string. We remove the brackets to get the actual content id itself.
        final String rootContentId = this.entityHandler.getMultipartRelatedRootContentId(entity);

        // Implementation note: RFC-2387 section 3.3: We have no use for the "start-info" parameter so we ignore it.
        // I'm not sure how or if this parameter is actually used in the wild. It's probably obsolete.

        // Implementation note: RFC-2387 section 4: Our output must consist of only the root part having dispositions
        // other than RELATED. All other parts will be forced to a disposition of "RELATED". It is common for messages
        // to specify a Content-Disposition header for child parts of a message, but that is only done for backwards
        // compatibility.
        //
        // Note that if the root part is itself a multipart/mixed or some-such, that it may contain child parts with
        // dispositions other than INLINE, such as ATTACHMENT or ALTERNATIVE. We go ahead and promote them as-is. In
        // other words, we don't force the root part to be INLINE if it wants to be something else or wants to contain
        // something else. I don't know if this is correct or not.

        // Implementation note: RFC-2387 section 6, paragraph 1: I have no idea what this means or why you would need to
        // give the user the option to suppress the entire entity. Clearly there is a typo here, which is actually
        // mentioned in the errata reports, but I can't tell what the intention of the statement actually is. Maybe we
        // should contact the author an ask: Edward Levinson, Ph: +1 (908) 494-1606, Em: XIson@cnj.digex.com

        // Implementation note: RFC-2387 section 6.2: We don't provide any mechanism for processing the
        // multipart/related entity for storage. Doing so would likely be far too complex to handle correctly for most
        // types. It might be worth doing this later though for at least some aspects of HTML. This would allow users to
        // download the HTML document and get also any referenced documents as well as objects embedded within the HTML.

        // Implementation note: RFC-2387 section 6.3: We don't handle recursion correctly yet. Doing so requires that we
        // implement storage processing (see previous note). It just isn't worth doing in my opinion, because nested
        // multipart/related entities should be quite rare, and frankly, HTML wouldn't be affected by this anyway do to
        // the way we handle CID look-ups universally across the entire message rather than within each multipart entity
        // individually.

        boolean isSatisfied = false;

        for (final E relatedEntity : childEntities)
        {
            final EntityHandler.EntityType entityType = this.entityHandler.getEntityType(relatedEntity);
            final PartProvider<E> partProvider = this.partProviderFactory.getPartProvider(entityType);
            final List<Part> childParts = partProvider.getParts(relatedEntity, contentLocation, contentTypeAdjudicator);

            if (isSatisfied)
            {
                // The root has already been encountered, these children are RELATED.
                parts.addAll(this.partHandler.getTriagedParts(childParts, Disposition.RELATED));
            }
            else
            {
                if (rootContentId == null)
                {
                    // No "start" parameter was provided so the first part encountered is the root.

                    // Todo root part should be marked as such via a decorator and additional part property.
                    //      Root parts for example, shouldn't be offered up to the user as downloadable, for example.
                    //      See RFC-2387 section 6.2 for the reasoning.

                    parts.addAll(childParts);
                    isSatisfied = true;
                }
                else
                {
                    // A "start" parameter was provided so we need to check content ids to detect the root.

                    final String relatedEntityContentId = this.entityHandler.getContentId(relatedEntity);

                    if (StringUtils.equals(rootContentId, relatedEntityContentId))
                    {
                        // Content id matches so this is the root

                        // Todo root part should be marked as such via a decorator and additional part property.
                        //      Root parts for example, shouldn't be offered up to the user as downloadable, for example.
                        //      See RFC-2387 section 6.2 for the reasoning.

                        parts.addAll(childParts);
                        isSatisfied = true;
                    }
                    else
                    {
                        // Content id does not matches so this is not the root
                        parts.addAll(this.partHandler.getTriagedParts(childParts, Disposition.RELATED));
                    }
                }
            }
        }

        // If isSatisfied is false by this point, any entities we'd encountered would have been forced to RELATED. This
        // happens when either the start parameter pointed to an entity that didn't exist, or the multipart related is
        // empty. Do we care about either of these cases? Probably not. It seems acceptable in both cases to return any
        // encountered parts as RELATED and leave nothing inline at all.

        return parts;
    }
}
