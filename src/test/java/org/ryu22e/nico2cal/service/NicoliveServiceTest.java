package org.ryu22e.nico2cal.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.ryu22e.nico2cal.rome.module.NicoliveModule;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * @author ryu22e
 *
 */
public final class NicoliveServiceTest extends AppEngineTestCase {
    /**
     * 
     */
    private List<Key> testDataKeys = new LinkedList<Key>();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        // テストデータを登録する。
        testDataKeys.clear();
        Nicolive nicolinve = new Nicolive();
        nicolinve.setTitle("テスト");
        nicolinve.setDescription(new Text("テスト説明文"));
        DateTime datetime = new DateTime();
        datetime = datetime.minusDays(3);
        nicolinve.setOpenTime(datetime.toDate());
        nicolinve.setLink(new Link("http://ryu22e.org/3"));
        Key key = Datastore.put(nicolinve);
        testDataKeys.add(key);
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void tearDown() throws Exception {
        // テストデータを削除する。
        if (0 < testDataKeys.size()) {
            Datastore.delete(testDataKeys);
        }
        NicoliveMeta n = NicoliveMeta.get();
        List<Key> keys =
                Datastore
                    .query(n)
                    .filter(n.title.startsWith("テスト"))
                    .asKeyList();
        if (0 < keys.size()) {
            Datastore.delete(keys);
        }

        super.tearDown();
    }

    /**
     * 
     */
    private NicoliveService service = new NicoliveService();

    /**
     * テスト用のRSSフィードを生成する。
     * @return テスト用のRSSフィード
     */
    @SuppressWarnings("unchecked")
    private SyndFeed createFeed() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setModules(Arrays.asList(new NicoliveModule()));

        feed.setTitle("テストRSS");

        DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        List<SyndEntry> entries = new LinkedList<SyndEntry>();
        for (int i = 0; i < 9; i++) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("テスト" + i);
            SyndContent description = new SyndContentImpl();
            description.setValue("テスト説明" + i);
            entry.setDescription(description);
            entry.setLink("http://ryu22e.org/" + i);

            NicoliveModule module = new NicoliveModule();
            module.setOpenTime(datetime.minusDays(i).toString(df));
            module.setStartTime(datetime
                .minusDays(i)
                .plusMinutes(10)
                .toString(df));
            module.setType("official");
            module.setPassword("true");
            module.setPremiumOnly("true");
            entry.getModules().add(module);

            entries.add(entry);
        }
        // NicoliveModuleなしのデータも含める。
        SyndEntry invalidEntry = new SyndEntryImpl();
        invalidEntry.setTitle("NicoliveModuleなしデータ");
        SyndContent description = new SyndContentImpl();
        description.setValue("テスト説明");
        invalidEntry.setDescription(description);
        invalidEntry.setLink("http://ryu22e.org/");
        entries.add(invalidEntry);

        feed.setEntries(entries);

        return feed;
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void RSSフィードをデータストアに登録する_パラメータがnull() throws Exception {
        assertThat(service, is(notNullValue()));

        service.putNicolive(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void RSSフィードをデータストアに登録する() throws Exception {
        assertThat(service, is(notNullValue()));

        SyndFeed feed = createFeed();
        service.putNicolive(feed);

        DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
        NicoliveMeta n = NicoliveMeta.get();
        for (int i = 0; i < 9; i++) {
            // putNicoliveでエンティティを登録する際に重複するリンクを持つエンティティがある場合は、既存のエンティティを上書き更新する。
            // （つまり、リンクが同じエンティティが複数存在することはない。）
            int count =
                    Datastore
                        .query(n)
                        .filter(
                            n.link.equal(new Link("http://ryu22e.org/" + i)))
                        .count();
            assertThat(count, is(1));

            Nicolive nicolive =
                    Datastore
                        .query(n)
                        .filter(n.title.equal("テスト" + i))
                        .asSingle();
            assertThat(nicolive, is(notNullValue()));
            assertThat(nicolive.getTitle(), is("テスト" + i));
            assertThat(nicolive.getDescription(), is(notNullValue()));
            assertThat(nicolive.getDescription().getValue(), is("テスト説明" + i));
            assertThat(nicolive.getOpenTime(), is(datetime
                .minusDays(i)
                .toDate()));
            assertThat(nicolive.getStartTime(), is(datetime
                .minusDays(i)
                .plusMinutes(10)
                .toDate()));
            assertThat(nicolive.getType(), is("official"));
            assertThat(nicolive.getPassword(), is(true));
            assertThat(nicolive.getPremiumOnly(), is(true));
            assertThat(nicolive.getLink(), is(notNullValue()));
            assertThat(nicolive.getLink().getValue(), is("http://ryu22e.org/"
                    + i));
        }

        // NicoliveModuleがないエントリーは登録されない。
        int count =
                Datastore
                    .query(n)
                    .filter(n.title.equal("NicoliveModuleなしデータ"))
                    .count();
        assertThat(count, is(0));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void 全文検索用インデックスを作成する_パラメータがnull() throws Exception {
        assertThat(service, is(notNullValue()));

        service.createIndex(null);
    }

    @Test
    public void 全文検索用インデックスを作成する() throws Exception {
        assertThat(service, is(notNullValue()));

        // テストデータを作成する。
        DateTime datetime = new DateTime();
        datetime = datetime.minusDays(3);
        Nicolive nicolinve1 = new Nicolive();
        nicolinve1.setTitle("テスト");
        nicolinve1.setDescription(new Text("本日は晴天なり。"));
        nicolinve1.setOpenTime(datetime.toDate());
        nicolinve1.setStartTime(datetime.plusMinutes(10).toDate());
        nicolinve1.setLink(new Link("http://ryu22e.org/1"));
        Key key1 = Datastore.put(nicolinve1);
        testDataKeys.add(key1);
        Nicolive nicolinve2 = new Nicolive();
        nicolinve2.setTitle("テスト");
        nicolinve2.setDescription(new Text("本日は晴天なり。"));
        nicolinve2.setOpenTime(datetime.toDate());
        nicolinve2.setStartTime(datetime.plusMinutes(10).toDate());
        nicolinve2.setLink(new Link("http://ryu22e.org/2"));
        Key key2 = Datastore.put(nicolinve2);
        testDataKeys.add(key2);
        NicoliveIndex nicoliveIndex = new NicoliveIndex();
        nicoliveIndex.setKeyword("テスト");
        nicoliveIndex.setNicoliveKeys(new ArrayList<Key>(Arrays.asList(key2)));
        testDataKeys.add(Datastore.put(nicoliveIndex));

        service.createIndex(nicolinve1);

        // TitleとDescriptionが文節ごとに分解されて、各文節とNicoliveのKeyがエンティティに登録される。
        NicoliveIndexMeta n = NicoliveIndexMeta.get();
        testDataKeys.addAll(Datastore
            .query(n)
            .filter(n.nicoliveKeys.in(key1))
            .asKeyList());
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("テスト"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("本日"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("は"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("晴天"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("なり"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
        assertThat(
            Datastore
                .query(n)
                .filter(n.keyword.equal("。"), n.nicoliveKeys.in(key1))
                .count(),
            is(1));
    }
}
