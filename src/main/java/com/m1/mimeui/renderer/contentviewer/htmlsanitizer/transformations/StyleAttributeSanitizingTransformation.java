package com.m1.mimeui.renderer.contentviewer.htmlsanitizer.transformations;

import org.jsoup.nodes.Element;

public class StyleAttributeSanitizingTransformation implements Transformation
{
    @Override
    public String getSelector ()
    {
        return "*[style]";
    }

    @Override
    public void transform (Element element)
    {
        String css = element.attr("style");

        // todo: When removing Escape/Entity/NCR sequences, consider substituting the target character directly.
        css = css.replaceAll("\\\\", "");                       // Remove CSS Escape Sequences
        css = css.replaceAll("&", "");                          // (just in case)

        css = css.replaceAll("(?i)url", "xurl");                // Remove URL References (also defeats embedded JavaScript URLs)
        // todo: Resolve CID urls.
        css = css.replaceAll("(?i)import", "ximport");          // Remove Import Directives
        css = css.replaceAll("(?i)behavior", "xbehavior");      // Remove Microsoft Behavior References
        css = css.replaceAll("(?i)binding", "xbinding");        // Remove Mozilla Behavior References
        css = css.replaceAll("(?i)absolute", "xabsolute");      // Remove Absolute Positioning
        css = css.replaceAll("(?i)relative", "xrelative");      // (just in case)
        css = css.replaceAll("(?i)expression", "xexpression");  // Remove JavaScript Expressions
        css = css.replaceAll("(?i)javascript", "xjavascript");  // (just in case)
        css = css.replaceAll("(?i)vbscript", "xvbscript");      // (just in case)

        // todo: If we ever implement more complex sanitizing, consider prefixing IDs and Classes.

        element.attr("style", css);
    }
}
