package com.m1.mimeui.analyzer.partprovider;

import com.m1.mimeui.api.EntityHandler;
import com.m1.mimeui.api.analyzer.ContentTypeAdjudicator;
import com.m1.mimeui.api.analyzer.PartHandler;
import com.m1.mimeui.api.analyzer.PartProvider;
import com.m1.mimeui.api.analyzer.PartProviderFactory;
import com.m1.mimeui.api.mimedom.Disposition;
import com.m1.mimeui.api.mimedom.Part;
import org.apache.commons.lang3.ObjectUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AlternativePartProvider<E> implements PartProvider<E>
{
    private final EntityHandler<E> entityHandler;
    private final PartHandler partHandler;
    private final PartProviderFactory<E> partProviderFactory;

    public AlternativePartProvider (final EntityHandler<E> entityHandler, final PartHandler partHandler,
                                    final PartProviderFactory<E> partProviderFactory)
    {
        this.entityHandler = entityHandler;
        this.partHandler = partHandler;
        this.partProviderFactory = partProviderFactory;
    }

    /**
     * Selects the first part from the end that we can completely render. We iterate in reverse order, last part first,
     * and select the first part that we encounter that doesn't contain any sub-parts that cannot be rendered.
     *
     * @param entity The multipart/alternative part to traverse.
     * @return The parts, if any, that this part contains.
     */
    @Override
    public List<Part> getParts (final E entity, final URI contextLocation,
                                final ContentTypeAdjudicator contentTypeAdjudicator)
    {
        final List<Part> parts = new ArrayList<Part>();
        final List<E> childEntities = this.entityHandler.getMultipartChildEntities(entity);
        final URI contentLocation = ObjectUtils.defaultIfNull(
                this.entityHandler.getContentLocation(entity, contextLocation), contextLocation);

        boolean isSatisfied = false;

        // Implementation Note: RFC-2046 section 5.1.4 paragraph 2: We always assume that the LAST entity in this
        // multipart is the BEST entity (e.g. that parts are listed in an order of INCREASING faithfulness to the
        // original content). This is in accordance with both the RFC and with standard practice. Some mail clients may
        // give preference to certain specific content types, but we do not. We simply assume that the LAST entity is
        // the BEST entity. So if HTML is listed FIRST and TEXT is listed LAST, we will go with TEXT even though we
        // support them both and HTML is almost certainly actually better.

        // Implementation Note: RFC-2046 section 5.1.4 paragraph 6: We do not give the user a choice in which
        // alternative to view. We simply go with the LAST entity that doesn't contain a unsupported type.

        // Implementation Note: We detect content supportability by calling the configuration property
        // PROPERTY_CONTENT_SUPPORTED_FUNCTION. This is currently the only way that the part to be selected by a
        // multipart/alternative can be controlled, and by default, it simply checks that a ContentViewer is available
        // for each part's content type.

        // Implementation Note: RFC-2046 section 5.1.4 paragraph 7: Apparently the entities in a multipart/alternative
        // may contain duplicate content-id headers. This sucks as we assume that content-ids are always unique
        // elsewhere in the code. To compensate, we should probably do something like mask out any content-ids on parts
        // that are not selected here. That-is: Only the selected parts' content-ids are used. Also note that the
        // content-id on the multipart/alternative entity itself is also ignored. We may have some bugs here.
        // todo Handle duplicated content ids values somehow maybe.

        // Iterate over the parts in reverse order.
        for (int i = childEntities.size() - 1; i >= 0; i--)
        {
            final E childEntity = childEntities.get(i);
            final EntityHandler.EntityType entityType = this.entityHandler.getEntityType(childEntity);
            final PartProvider<E> partProvider = this.partProviderFactory.getPartProvider(entityType);
            final List<Part> childParts = partProvider.getParts(childEntity, contentLocation, contentTypeAdjudicator);

            if (isSatisfied || this.partHandler.hasDispositions(childParts, Disposition.INLINE_ATTACHMENT))
            {
                // If we've already encountered a better part, or if this part has any parts that we can't render, then
                // don't select this part.

                parts.addAll(this.partHandler.getTriagedParts(
                        childParts,
                        i == childEntities.size() - 1 ? Disposition.INLINE_ALTERNATIVE : Disposition.ALTERNATIVE
                ));
            }
            else
            {
                // Otherwise, select this part.

                isSatisfied = true;
                parts.addAll(childParts);
            }
        }

        // Note that if isSatisfied is still false by the time we get here, that there were no supported alternatives.
        // In this case we will still promote the best alternative as an "INLINE_ALTERNATIVE" (in theory making it
        // available for users to download). This should be ok.

        return parts;
    }
}
