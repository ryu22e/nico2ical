package org.ryu22e.nico2cal.controller;

import org.ryu22e.nico2cal.service.NicoliveRssService;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.sun.syndication.feed.synd.SyndFeed;

/**
 * ニコニコ生放送RSSからデータストアのデータを生成するコントローラー。
 * @author ryu22e
 *
 */
public final class GenerateNicoliveController extends Controller {

    /**
     * @see NicoliveRssService
     */
    private NicoliveRssService nicoliveRssService = new NicoliveRssService();

    /**
     * @see NicoliveService
     */
    private NicoliveService nicoliveService = new NicoliveService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        SyndFeed feed = nicoliveRssService.getFeed();
        nicoliveService.putNicolive(feed);
        return null;
    }
}
