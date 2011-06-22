package org.ryu22e.nico2cal.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.DateUtil;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

public final class CalendarServiceTest extends AppEngineTestCase {

    /**
     * 
     */
    private CalendarService service = new CalendarService();

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

        NamespaceManager.set("test");

        // テストデータを登録する。
        testDataKeys.clear();
        for (int i = 0; i < 99; i++) {
            Nicolive nicolinve = new Nicolive();
            nicolinve.setTitle("テスト" + i);
            nicolinve.setDescription(new Text("テスト説明文" + i));
            DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
            datetime = datetime.minusDays(i);
            nicolinve.setOpenTime(datetime.toDate());
            nicolinve.setLink(new Link("http://ryu22e.org/" + i));
            Key key = Datastore.put(nicolinve);
            testDataKeys.add(key);
            NicoliveIndex nicoliveIndex1 = new NicoliveIndex();
            nicoliveIndex1.setKeyword("テスト");
            nicoliveIndex1.setNicoliveKey(key);
            NicoliveIndex nicoliveIndex2 = new NicoliveIndex();
            nicoliveIndex2.setKeyword("説明文");
            nicoliveIndex2.setNicoliveKey(key);
            testDataKeys.addAll(Datastore.put(nicoliveIndex1, nicoliveIndex2));
        }

        Nicolive nicolive = new Nicolive();
        nicolive.setTitle("検索対象外のデータ");
        nicolive.setDescription(new Text("検索対象外のデータ"));
        DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
        nicolive.setOpenTime(datetime.toDate());
        nicolive.setLink(new Link("http://ryu22e.org/"));
        testDataKeys.add(Datastore.put(nicolive));
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

        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void RSSフィードをiCalendar形式に変換する_パラメータが不正() throws Exception {
        assertThat(service, is(notNullValue()));

        CalendarCondition condition = new CalendarCondition();
        condition.setStartDate(null);
        service.getCalendar(condition);
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void RSSフィードをiCalendar形式に変換する_パラメータがnull() throws Exception {
        assertThat(service, is(notNullValue()));

        service.getCalendar(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void RSSフィードをiCalendar形式に変換する_該当するデータが存在する() throws Exception {
        assertThat(service, is(notNullValue()));

        CalendarCondition condition = new CalendarCondition();
        DateTime startDate = new DateTime(2011, 1, 1, 0, 0, 0, 0);
        startDate = startDate.minusDays(7);
        condition.setStartDate(startDate.toDate());
        condition.setKeywords(Arrays.asList("テスト"));

        Calendar calendar = service.getCalendar(condition);
        assertThat(calendar, not(nullValue()));
        assertThat(calendar.getProperty("PRODID").getValue(), is("nico2cal"));
        assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
        assertThat(
            calendar.getProperty("X-WR-CALNAME").getValue(),
            is("ニコニコ生放送"));
        ComponentList components = calendar.getComponents();
        assertThat(components, is(notNullValue()));
        assertThat(components.size(), is(8));
        // DTSTARTが古い順にイベントが並んでいる。
        int i = 7;
        for (Object object : components) {
            assertThat(object, is(notNullValue()));
            assertThat(object, is(instanceOf(VEvent.class)));

            VEvent event = (VEvent) object;
            assertThat(event.getSummary().getValue(), is("テスト" + i));
            assertThat(event.getDescription().getValue(), is("テスト説明文" + i));
            assertThat(event.getUrl().getValue(), is("http://ryu22e.org/" + i));
            DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
            datetime = datetime.minusDays(i);
            assertThat(event.getStartDate().getDate(), is(notNullValue()));
            assertThat(
                DateUtil.toString(event.getStartDate().getDate()),
                is(DateUtil.toString(datetime.toDate())));
            assertThat(event.getEndDate(), is(notNullValue()));
            assertThat(
                DateUtil.toString(event.getEndDate().getDate()),
                is(DateUtil.toString(datetime.toDate())));

            i--;
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void RSSフィードをiCalendar形式に変換する_該当するデータが存在しない() throws Exception {
        assertThat(service, is(notNullValue()));

        CalendarCondition condition = new CalendarCondition();
        DateTime startDate = new DateTime(2011, 1, 2, 0, 0, 0, 0);
        condition.setStartDate(startDate.toDate());

        Calendar calendar = service.getCalendar(condition);
        // データが存在しない場合でもnullにはならない。
        assertThat(calendar, not(nullValue()));
        // イベントなしのiCalendar形式データが取得される。
        assertThat(calendar.getProperty("PRODID").getValue(), is("nico2cal"));
        assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
        assertThat(
            calendar.getProperty("X-WR-CALNAME").getValue(),
            is("ニコニコ生放送"));
        ComponentList components = calendar.getComponents();
        assertThat(components, is(notNullValue()));
        assertThat(components.size(), is(0));
    }
}
