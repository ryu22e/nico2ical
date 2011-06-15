package org.ryu22e.nico2cal.service;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.rome.module.NicoliveModule;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * {@link Nicolive}を操作するサービスクラス。
 * @author ryu22e
 *
 */
public final class NicoliveService {

    /**
     * RSSフィードをデータストアに登録する。
     * @param feed RSSフィード
     * @throws NullPointerException パラメータがnullの場合。
     */
    public void putNicolive(SyndFeed feed) {
        if (feed == null) {
            throw new NullPointerException("feed is null.");
        }

        NicoliveMeta n = NicoliveMeta.get();
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        List<Nicolive> nicolives = new LinkedList<Nicolive>();
        @SuppressWarnings("unchecked")
        List<SyndEntry> entries = (List<SyndEntry>) feed.getEntries();
        for (SyndEntry entry : entries) {
            // RSSフィードの「nicolive:***」部分を取得する。
            NicoliveModule module = null;
            @SuppressWarnings("unchecked")
            List<Object> modules = entry.getModules();
            for (Object o : modules) {
                if (o instanceof NicoliveModule) {
                    module = (NicoliveModule) o;
                    break;
                }
            }

            // 「nicolive:***」が取得できないエントリーは登録しない。
            if (module != null) {
                // 重複するリンクを持つエンティティがある場合は、既存のエンティティを上書き更新する。
                Nicolive nicolive =
                        Datastore
                            .query(n)
                            .filter(n.link.equal(new Link(entry.getLink())))
                            .asSingle();
                if (nicolive == null) {
                    nicolive = new Nicolive();
                }

                nicolive.setTitle(entry.getTitle());
                nicolive.setDescription(new Text(entry
                    .getDescription()
                    .getValue()));
                nicolive.setOpenTime(df
                    .parseDateTime(module.getOpenTime())
                    .toDate());
                nicolive.setStartTime(df
                    .parseDateTime(module.getStartTime())
                    .toDate());
                nicolive.setType(module.getType());
                nicolive
                    .setPassword(Boolean.parseBoolean(module.getPassword()));
                nicolive.setPremiumOnly(Boolean.parseBoolean(module
                    .getPremiumOnly()));
                nicolive.setLink(new Link(entry.getLink()));

                nicolives.add(nicolive);
            }
        }

        Datastore.put(nicolives);
    }

}
