package org.ryu22e.nico2cal.service;

import java.io.IOException;
import java.net.URL;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * ニコニコ生放送RSSを操作するサービスクラス。
 * @author ryu22e
 *
 */
public final class NicoliveRssService {

    /**
     * RSSフィードのURL。
     */
    private static final String FEED_URL = "http://live.nicovideo.jp/rss";

    /**
     * ニコニコ生放送のRSSフィードを取得する。
     * @return ニコニコ生放送のRSSフィード
     * @throws IOException RSSフィードの取得に失敗した場合。
     * @throws FeedException RSSフィードのパースに失敗した場合。
     */
    public SyndFeed getFeed() throws FeedException, IOException {
        URL feedUrl = new URL(FEED_URL);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedUrl.openStream()));
    }

}
