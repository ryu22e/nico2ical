/**
 * 
 */
package org.ryu22e.nico2cal.rome.parser;

import org.jdom.Element;
import org.jdom.Namespace;
import org.ryu22e.nico2cal.rome.module.NicoliveModule;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleParser;

/**
 * ニコニコ生放送RSS用のRomeパーサ。
 * @author ryu22e
 * 
 */
public final class NicoliveParser implements ModuleParser {

    /**
     * 
     */
    private static final Namespace NS = Namespace.getNamespace(
        "nicolive",
        NicoliveModule.URI);

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    public String getNamespaceUri() {
        return NicoliveModule.URI;
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    public Module parse(Element element) {
        NicoliveModule module = new NicoliveModule();
        Element openTime = element.getChild("open_time", NS);
        module.setOpenTime(openTime.getText());
        Element startTime = element.getChild("start_time", NS);
        module.setStartTime(startTime.getText());
        Element type = element.getChild("type", NS);
        module.setType(type.getText());
        return module;
    }

}
