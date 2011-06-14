package org.ryu22e.nico2cal.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.ryu22e.nico2cal.rome.module.NicoliveModule;
import org.slim3.tester.AppEngineTestCase;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * @author ryu22e
 *
 */
public final class NicoliveRssServiceTest extends AppEngineTestCase {

    /**
     * 
     */
    private NicoliveRssService service = new NicoliveRssService();

    /**
     * @throws Exception
     */
    @Test
    public void RSSフィードを取得する() throws Exception {
        assertThat(service, is(notNullValue()));
        SyndFeed feed = service.getFeed();
        assertThat(feed, not(nullValue()));
        assertThat(feed.getTitle(), is("ニコニコ生放送"));
        assertThat(feed.getEntries(), is(notNullValue()));
        assertThat(feed.getEntries().size(), is(not(0)));
        @SuppressWarnings("unchecked")
        List<SyndEntry> entries = (List<SyndEntry>) feed.getEntries();
        for (SyndEntry entry : entries) {
            assertThat(entry.getTitle(), is(notNullValue()));
            assertThat(entry.getDescription(), is(notNullValue()));
            assertThat(entry.getModules(), is(notNullValue()));
            assertThat(entry.getModules().size(), is(not(0)));
            assertThat(
                entry.getModules().get(0),
                is(instanceOf(NicoliveModule.class)));
            NicoliveModule module = (NicoliveModule) entry.getModules().get(0);
            assertThat(module.getOpenTime(), is(notNullValue()));
            assertThat(module.getStartTime(), is(notNullValue()));
            assertThat(module.getType(), is(notNullValue()));
            assertThat(module.getPassword(), is(notNullValue()));
            assertThat(module.getPremiumOnly(), is(notNullValue()));
        }
    }
}
