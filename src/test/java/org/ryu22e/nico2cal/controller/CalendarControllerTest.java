package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

/**
 * @author ryu22e
 *
 */
public final class CalendarControllerTest extends ControllerTestCase {

    /**
     * 
     */
    private List<Key> testDataKeys = new LinkedList<Key>();

    /**
     * 
     */
    private void clearDataStore() {
        NicoliveMeta n = NicoliveMeta.get();
        Datastore.delete(Datastore.query(n).asKeyList());
        NicoliveIndexMeta ni = NicoliveIndexMeta.get();
        Datastore.delete(Datastore.query(ni).asKeyList());
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        NamespaceManager.set("test");

        clearDataStore();

        // テストデータを登録する。
        testDataKeys.clear();
        List<Nicolive> nicolives = new LinkedList<Nicolive>();
        List<NicoliveIndex> indexes = new LinkedList<NicoliveIndex>();
        DateTime datetime = new DateTime();
        for (int i = 0; i < 4; i++) {
            Nicolive nicolive = new Nicolive();
            nicolive.setKey(Datastore.allocateId(NicoliveMeta.get()));
            nicolive.setTitle("テスト" + i);
            nicolive.setDescription(new Text("テスト説明文" + i));
            nicolive.setOpenTime(datetime.minusWeeks(i).toDate());
            nicolive.setLink(new Link("http://ryu22e.org/" + i));
            nicolives.add(nicolive);

            NicoliveIndex index1 = new NicoliveIndex();
            index1.setKeyword("テスト");
            index1.setNicoliveKey(nicolive.getKey());
            indexes.add(index1);

            NicoliveIndex index2 = new NicoliveIndex();
            index2.setKeyword("説明");
            index2.setNicoliveKey(nicolive.getKey());
            indexes.add(index2);

            NicoliveIndex index3 = new NicoliveIndex();
            index3.setKeyword("文");
            index3.setNicoliveKey(nicolive.getKey());
            indexes.add(index3);

            NicoliveIndex index4 = new NicoliveIndex();
            index4.setKeyword(Integer.toString(i));
            index4.setNicoliveKey(nicolive.getKey());
            indexes.add(index4);
        }
        testDataKeys.addAll(Datastore.put(nicolives));
        testDataKeys.addAll(Datastore.put(indexes));
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
    @Test
    public void バリデーションのテスト_パラメータを指定しない() throws Exception {
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void バリデーションのテスト_keywordのみを指定() throws Exception {
        tester.param("keyword", "テスト");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void バリデーションのテスト_startWeekが数字以外() throws Exception {
        tester.param("startWeek", "invalid");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    @Test
    public void バリデーションのテスト_startWeekが1未満() throws Exception {
        tester.param("startWeek", 0);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    @Test
    public void バリデーションのテスト_startWeekが4を超える() throws Exception {
        tester.param("startWeek", 5);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void バリデーションのテスト_keywordが最大文字列長を超える() throws Exception {
        // エラーになる。
        char[] chars = Arrays.copyOf(new char[] { 'あ' }, 51);
        String keyword = new String(chars);
        tester.param("startWeek", 1);
        tester.param("keyword", keyword);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void バリデーションのテスト_keywordが最大文字列長と同じ() throws Exception {
        // エラーにならない。
        char[] chars = Arrays.copyOf(new char[] { 'あ' }, 50);
        String keyword = new String(chars);
        tester.param("startWeek", 1);
        tester.param("keyword", keyword);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void バリデーションのテスト_keywordが最大文字列長より短い() throws Exception {
        // エラーにならない。
        char[] chars = Arrays.copyOf(new char[] { 'あ' }, 49);
        String keyword = new String(chars);
        tester.param("startWeek", 1);
        tester.param("keyword", keyword);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_該当するデータが存在しない() throws Exception {
        tester.param("startWeek", 1);
        tester.param("keyword", "存在しないキーワード");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        assertThat(
            tester.response.getContentType(),
            is("text/calendar;charset=UTF-8"));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Reader reader = null;
        try {
            reader = new StringReader(output);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(reader);
            assertThat(calendar, is(notNullValue()));
            assertThat(
                calendar.getProperty("PRODID").getValue(),
                is("nico2ical"));
            assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
            assertThat(
                calendar.getProperty("X-WR-CALNAME").getValue(),
                is("ニコニコ生放送"));
            ComponentList components = calendar.getComponents();
            assertThat(components, is(notNullValue()));
            assertThat(components.size(), is(0));
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_startWeekのみを指定する() throws Exception {
        tester.param("startWeek", 1);
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_１週間前以降のデータを取得する() throws Exception {
        tester.param("startWeek", 1);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        assertThat(
            tester.response.getContentType(),
            is("text/calendar;charset=UTF-8"));
        assertThat(
            tester.response.getHeader("Content-Disposition"),
            is("filename=\"nico2ical.ics\""));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Reader reader = null;
        try {
            reader = new StringReader(output);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(reader);
            assertThat(calendar, is(notNullValue()));
            assertThat(
                calendar.getProperty("PRODID").getValue(),
                is("nico2ical"));
            assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
            assertThat(
                calendar.getProperty("X-WR-CALNAME").getValue(),
                is("ニコニコ生放送"));
            ComponentList components = calendar.getComponents();
            assertThat(components, is(notNullValue()));
            assertThat(components.size(), is(1));
            // DTSTARTが古い順にイベントが並んでいる。
            int i = 0;
            for (Object object : components) {
                assertThat(object, is(notNullValue()));
                assertThat(object, is(instanceOf(VEvent.class)));

                VEvent event = (VEvent) object;
                assertThat(event.getSummary().getValue(), is("テスト" + i));
                assertThat(event.getDescription().getValue(), is("テスト説明文" + i));
                assertThat(event.getUrl().getValue(), is("http://ryu22e.org/"
                        + i));
                assertThat(event.getStartDate(), is(notNullValue()));
                assertThat(event.getEndDate(), is(notNullValue()));

                i--;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_２週間前以降のデータを取得する() throws Exception {
        tester.param("startWeek", 2);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        assertThat(
            tester.response.getContentType(),
            is("text/calendar;charset=UTF-8"));
        assertThat(
            tester.response.getHeader("Content-Disposition"),
            is("filename=\"nico2ical.ics\""));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Reader reader = null;
        try {
            reader = new StringReader(output);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(reader);
            assertThat(calendar, is(notNullValue()));
            assertThat(
                calendar.getProperty("PRODID").getValue(),
                is("nico2ical"));
            assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
            assertThat(
                calendar.getProperty("X-WR-CALNAME").getValue(),
                is("ニコニコ生放送"));
            ComponentList components = calendar.getComponents();
            assertThat(components, is(notNullValue()));
            assertThat(components.size(), is(2));
            // DTSTARTが古い順にイベントが並んでいる。
            int i = 1;
            for (Object object : components) {
                assertThat(object, is(notNullValue()));
                assertThat(object, is(instanceOf(VEvent.class)));

                VEvent event = (VEvent) object;
                assertThat(event.getSummary().getValue(), is("テスト" + i));
                assertThat(event.getDescription().getValue(), is("テスト説明文" + i));
                assertThat(event.getUrl().getValue(), is("http://ryu22e.org/"
                        + i));
                assertThat(event.getStartDate(), is(notNullValue()));
                assertThat(event.getEndDate(), is(notNullValue()));

                i--;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_３週間前以降のデータを取得する() throws Exception {
        tester.param("startWeek", 3);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        assertThat(
            tester.response.getContentType(),
            is("text/calendar;charset=UTF-8"));
        assertThat(
            tester.response.getHeader("Content-Disposition"),
            is("filename=\"nico2ical.ics\""));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Reader reader = null;
        try {
            reader = new StringReader(output);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(reader);
            assertThat(calendar, is(notNullValue()));
            assertThat(
                calendar.getProperty("PRODID").getValue(),
                is("nico2ical"));
            assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
            assertThat(
                calendar.getProperty("X-WR-CALNAME").getValue(),
                is("ニコニコ生放送"));
            ComponentList components = calendar.getComponents();
            assertThat(components, is(notNullValue()));
            assertThat(components.size(), is(3));
            // DTSTARTが古い順にイベントが並んでいる。
            int i = 2;
            for (Object object : components) {
                assertThat(object, is(notNullValue()));
                assertThat(object, is(instanceOf(VEvent.class)));

                VEvent event = (VEvent) object;
                assertThat(event.getSummary().getValue(), is("テスト" + i));
                assertThat(event.getDescription().getValue(), is("テスト説明文" + i));
                assertThat(event.getUrl().getValue(), is("http://ryu22e.org/"
                        + i));
                assertThat(event.getStartDate(), is(notNullValue()));
                assertThat(event.getEndDate(), is(notNullValue()));

                i--;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void iCalendarファイルをダウンロードする_４週間前以降のデータを取得する() throws Exception {
        tester.param("startWeek", 4);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        assertThat(
            tester.response.getContentType(),
            is("text/calendar;charset=UTF-8"));
        assertThat(
            tester.response.getHeader("Content-Disposition"),
            is("filename=\"nico2ical.ics\""));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Reader reader = null;
        try {
            reader = new StringReader(output);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(reader);
            assertThat(calendar, is(notNullValue()));
            assertThat(
                calendar.getProperty("PRODID").getValue(),
                is("nico2ical"));
            assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
            assertThat(
                calendar.getProperty("X-WR-CALNAME").getValue(),
                is("ニコニコ生放送"));
            ComponentList components = calendar.getComponents();
            assertThat(components, is(notNullValue()));
            assertThat(components.size(), is(4));
            // DTSTARTが古い順にイベントが並んでいる。
            int i = 3;
            for (Object object : components) {
                assertThat(object, is(notNullValue()));
                assertThat(object, is(instanceOf(VEvent.class)));

                VEvent event = (VEvent) object;
                assertThat(event.getSummary().getValue(), is("テスト" + i));
                assertThat(event.getDescription().getValue(), is("テスト説明文" + i));
                assertThat(event.getUrl().getValue(), is("http://ryu22e.org/"
                        + i));
                assertThat(event.getStartDate(), is(notNullValue()));
                assertThat(event.getEndDate(), is(notNullValue()));

                i--;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void 同じ条件で２回iCalendarファイルをダウンロードする() throws Exception {
        String memcacheKey =
                "startWeek=1&keyword=" + URLEncoder.encode("テスト 説明 文", "UTF-8");
        Memcache.delete(memcacheKey);

        tester.param("startWeek", 1);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller1 = tester.getController();
        assertThat(controller1, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        String output1 = tester.response.getOutputAsString();
        Calendar calendar1 = null;
        try {
            Reader reader = new StringReader(output1);
            CalendarBuilder builder = new CalendarBuilder();
            calendar1 = builder.build(reader);
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        }

        // 取得した内容がキャッシュにも保存されている。
        Object cache = Memcache.get(memcacheKey);
        assertThat(output1, is(instanceOf(String.class)));
        Calendar calendarCache = null;
        try {
            Reader reader = new StringReader((String) cache);
            CalendarBuilder builder = new CalendarBuilder();
            calendarCache = builder.build(reader);
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        }
        assertThat(calendar1, is(calendarCache));

        // 同じ条件でもう一度コントローラーを呼ぶと、キャッシュに保存している内容を取得する。
        tester.param("startWeek", 1);
        tester.param("keyword", "テスト 説明 文");
        tester.start("/Calendar");
        CalendarController controller2 = tester.getController();
        assertThat(controller2, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        String output2 = tester.response.getOutputAsString();
        Calendar calendar2 = null;
        try {
            Reader reader = new StringReader(output2);
            CalendarBuilder builder = new CalendarBuilder();
            calendar2 = builder.build(reader);
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ParserException e) {
            fail(e.getMessage());
        }
        assertThat(calendar2, is(calendarCache));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlルーティングのテスト_startWeekのみを指定する() throws Exception {
        tester.start("/ical/1");
        assertThat(tester.isRedirect(), is(false));
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void urlルーティングのテスト_startWeekとkeywordを指定する() throws Exception {
        tester.start("/ical/1/test");
        CalendarController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
