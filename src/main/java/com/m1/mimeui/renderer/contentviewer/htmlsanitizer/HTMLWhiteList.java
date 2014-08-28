package com.m1.mimeui.renderer.contentviewer.htmlsanitizer;

import org.jsoup.safety.Whitelist;

/**
 * Defines a list of tags, attributes, and protocols that are allowed to be present in an HTML document. Any item not
 * explicitly declared in this white list will be removed from the document.
 * <p>
 * This white list is applied BEFORE we run our other transformations such as converting "cid:" URIs, removing external
 * image references, etc...
 * <p>
 * Note that although this is a white list, not a black list, there are several tags, attributes, and protocols are are
 * especially forbidden because they have security implications (no black-list can possibly be complete, so don't
 * assume that any item not listed below can be considered safe -- this list is just for informational purposes).
 * <p>
 * Especially forbidden tags:
 *     html, body, head, base, basefont, meta, link, script, noscript, applet, object, param, embed, audio, video,
 *     frame, iframe, frameset, noframes, bgsound, layer, form, input, button, select, fieldset, legend, textarea,
 *     option, optgroup, label, isindex
 * <p>
 * Especially forbidden attributes:
 *     on* (e.g. onClick, onResize, etc...), cite, background, style, style tag's type attribute
 * <p>
 * Especially forbidden protocols:
 *     data, javascript, vbscript, behavior, mocha, livescript, keyword
 * <p>
 * Note that the style attribute could be allowed if we add the code to sanitize it. I'm not entirely sure that we have
 * to remove forms, but I'm assuming that we should at least for now.
 * <p>
 * There are several other items that I have no yet explicitly added, but I think may be able to be added in the future
 * pending additional investigation, such as:
 * <ul>
 *     <li>area (image maps)
 *     <li>bdo (internationalization)
 *     <li>cite (attribute on blockquote, del, ins, and q)
 *     <li>map (image maps)
 * </ul>
 * <p>
 * Additionally, none of the new HTML5 tags are in the white list yet.
 */
public class HTMLWhiteList extends Whitelist
{
    // todo: we will allow id, class, and style attributes -- though they need to be sanitized after (?).
    public HTMLWhiteList ()
    {
        super();

        // todo make configurable

        addTags(
            "a", "abbr", "acronym", "address",
            "b", "big", "blockquote", "br",
            "caption", "center", "cite", "code", "col", "colgroup",
            "dd", "del", "dfn", "dir", "div", "dl", "dt",
            "em",
            "font",
            "h1", "h2", "h3", "h4", "h5", "h6", "hr",
            "i", "img", "ins",
            "kbd",
            "li",
            "menu",
            "ol",
            "p", "pre",
            "q",
            "s", "samp", "small", "span", "strike", "strong", "style", "sub", "sup",
            "table", "tbody", "td", "tfoot", "th", "thead", "tr", "tt",
            "u", "ul",
            "var"
        );

        addAttributes("a",              "style", "class", "title", "href");
        addAttributes("abbr",           "style", "class", "title");
        addAttributes("acronym",        "style", "class", "title");
        addAttributes("address",        "style", "class", "title");
        addAttributes("b",              "style", "class", "title");
        addAttributes("big",            "style", "class", "title");
        addAttributes("blockquote",     "style", "class", "title");
        addAttributes("br",             "style", "class", "title");
        addAttributes("caption",        "style", "class", "title", "align");
        addAttributes("center",         "style", "class", "title");
        addAttributes("cite",           "style", "class", "title");
        addAttributes("code",           "style", "class", "title");
        addAttributes("col",            "style", "class", "title", "align", "valign", "char", "charoff", "span", "width");
        addAttributes("colgroup",       "style", "class", "title", "align", "valign", "char", "charoff", "span", "width");
        addAttributes("dd",             "style", "class", "title");
        addAttributes("del",            "style", "class", "title");
        addAttributes("dfn",            "style", "class", "title");
        addAttributes("dir",            "style", "class", "title");
        addAttributes("div",            "style", "class", "title", "align");
        addAttributes("dl",             "style", "class", "title");
        addAttributes("dt",             "style", "class", "title");
        addAttributes("em",             "style", "class", "title");
        addAttributes("font",           "style", "class", "title", "face", "size", "color");
        addAttributes("h1",             "style", "class", "title", "align");
        addAttributes("h2",             "style", "class", "title", "align");
        addAttributes("h3",             "style", "class", "title", "align");
        addAttributes("h4",             "style", "class", "title", "align");
        addAttributes("h5",             "style", "class", "title", "align");
        addAttributes("h6",             "style", "class", "title", "align");
        addAttributes("hr",             "style", "class", "title", "align", "noshade", "size", "width");
        addAttributes("i",              "style", "class", "title");
        addAttributes("img",            "style", "class", "title", "align", "alt", "border", "hspace", "vspace", "height",
                                        "src", "width");
        addAttributes("ins",            "style", "class", "title");
        addAttributes("kbd",            "style", "class", "title");
        addAttributes("li",             "style", "class", "title", "type", "value");
        addAttributes("menu",           "style", "class", "title");
        addAttributes("ol",             "style", "class", "title", "start", "type", "compact");
        addAttributes("p",              "style", "class", "title", "align");
        addAttributes("pre",            "style", "class", "title", "width");
        addAttributes("q",              "style", "class", "title");
        addAttributes("s",              "style", "class", "title");
        addAttributes("samp",           "style", "class", "title");
        addAttributes("small",          "style", "class", "title");
        addAttributes("span",           "style", "class", "title", "align");
        addAttributes("strike",         "style", "class", "title");
        addAttributes("strong",         "style", "class", "title");
        addAttributes("sub",            "style", "class", "title");
        addAttributes("sup",            "style", "class", "title");
        addAttributes("table",          "style", "class", "title", "align", "summary", "width", "bgcolor", "border",
                                        "cellspacing", "cellpadding");
        addAttributes("tbody",          "style", "class", "title", "align", "valign", "char", "charoff");
        addAttributes("td",             "style", "class", "title", "align", "valign", "char", "charoff", "abbr", "axis",
                                        "colspan", "rowspan", "scope", "nowrap", "width", "height", "bgcolor");
        addAttributes("tfoot",          "style", "class", "title", "align", "valign", "char", "charoff");
        addAttributes("th",             "style", "class", "title", "align", "valign", "char", "charoff", "abbr", "axis",
                                        "colspan", "rowspan", "scope", "nowrap", "width", "height", "bgcolor");
        addAttributes("thead",          "style", "class", "title", "align", "valign", "char", "charoff");
        addAttributes("tr",             "style", "class", "title", "align", "valign", "char", "charoff", "bgcolor");
        addAttributes("tt",             "style", "class", "title");
        addAttributes("u",              "style", "class", "title");
        addAttributes("ul",             "style", "class", "title", "type", "compact");
        addAttributes("var",            "style", "class", "title");

        addEnforcedAttribute("a",       "rel",      "nofollow");
        addEnforcedAttribute("style",   "type",     "text/css");
    }
}
