/**
 * 
 */
package org.ryu22e.nico2cal.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 文字列中のHTMLのタグを除去するユーティリティクラス。
 * @author ryu22e
 *
 */
public final class HtmlRemoveUtil {

    /**
     * コンストラクタ。
     */
    private HtmlRemoveUtil() {
        throw new AssertionError("Can not call this constructor.");
    }

    /**
     * 文字列中のHTMLタグを除去する。
     * @param html HTMLタグを含む文字列
     * @return HTMLタグを除去された文字列
     * @throws IOException 
     * @throws SAXException 
     */
    public static String removeHtml(String html) throws SAXException,
            IOException {
        if (html == null) {
            return null;
        }

        DOMFragmentParser parser = new DOMFragmentParser();

        // フィルターの設定
        ElementRemover remover = new ElementRemover();
        XMLDocumentFilter[] filters = { remover };
        parser.setProperty(
            "http://cyberneko.org/html/properties/filters",
            filters);
        HTMLDocument document = new HTMLDocumentImpl();
        DocumentFragment fragment = document.createDocumentFragment();

        InputSource inputSource = new InputSource(new StringReader(html));
        parser.parse(inputSource, fragment);
        StringWriter writer = new StringWriter();
        OutputFormat format = new OutputFormat();

        format.setOmitXMLDeclaration(true);
        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputCharStream(writer);
        serializer.setOutputFormat(format);
        serializer.serialize(fragment);

        return writer.getBuffer().toString();
    }

}
