package org.ryu22e.nico2cal.controller;

import java.util.logging.Logger;

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
     * 
     */
    private static final Logger LOGGER = Logger
        .getLogger(GenerateNicoliveController.class.getName());

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
        LOGGER.info("BEGIN: " + this.getClass().getName());

        SyndFeed feed = nicoliveRssService.getFeed();
        nicoliveService.put(feed);

        LOGGER.info("END: " + this.getClass().getName());
        return null;
    }
}
