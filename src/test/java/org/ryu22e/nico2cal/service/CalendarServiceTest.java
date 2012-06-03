package org.ryu22e.nico2cal.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VEvent;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.MyCalendarLogMeta;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.ryu22e.nico2cal.model.MyCalendarLog;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.DateUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.mail.MailServicePb.MailMessage;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public final class CalendarServiceTest extends AppEngineTestCase {

    /**
     * 
     */
    private CalendarService service = new CalendarService();

    /**
     * 
     */
    private List<Key> testDataKeys = new LinkedList<Key>();

    /**
     * @return
     * @throws IOException 
     */
    private CalendarService createMockService() throws IOException {
        CalendarService mockService = spy(new CalendarService());
        doReturn(
            com.google.api.services.calendar.Calendar.builder(
                new NetHttpTransport(),
                new JacksonFactory()).build())
            .when(mockService)
            .createGoogleCalendarClientFromUserId("dummy");
        CalendarList calendarList = new CalendarList();
        calendarList.setItems(Arrays.asList(
            new CalendarListEntry(),
            new CalendarListEntry(),
            new CalendarListEntry()));
        doReturn(calendarList).when(mockService).getGoogleCalendarList(
            new User("dummy@gmail.com", "example.com", "dummy"));

        return mockService;
    }

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
            nicoliveIndex2.setKeyword("説明");
            nicoliveIndex2.setNicoliveKey(key);
            NicoliveIndex nicoliveIndex3 = new NicoliveIndex();
            nicoliveIndex3.setKeyword("文");
            nicoliveIndex3.setNicoliveKey(key);
            testDataKeys.addAll(Datastore.put(
                nicoliveIndex1,
                nicoliveIndex2,
                nicoliveIndex3));
        }

        Nicolive nicolive = new Nicolive();
        nicolive.setTitle("テスト");
        nicolive.setDescription(new Text("テスト"));
        DateTime datetime = new DateTime(2011, 1, 1, 0, 0, 0, 0);
        nicolive.setOpenTime(datetime.toDate());
        nicolive.setLink(new Link("http://ryu22e.org/"));
        testDataKeys.add(Datastore.put(nicolive));
        NicoliveIndex nicoliveIndex = new NicoliveIndex();
        nicoliveIndex.setKeyword("テスト");
        nicoliveIndex.setNicoliveKey(nicolive.getKey());
        testDataKeys.add(Datastore.put(nicoliveIndex));
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
        condition.setKeywords(Arrays.asList("テスト", "説明"));

        Calendar calendar = service.getCalendar(condition);
        assertThat(calendar, not(nullValue()));
        assertThat(calendar.getProperty("PRODID").getValue(), is("nico2ical"));
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
        assertThat(calendar.getProperty("PRODID").getValue(), is("nico2ical"));
        assertThat(calendar.getProperty("VERSION").getValue(), is("2.0"));
        assertThat(
            calendar.getProperty("X-WR-CALNAME").getValue(),
            is("ニコニコ生放送"));
        ComponentList components = calendar.getComponents();
        assertThat(components, is(notNullValue()));
        assertThat(components.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendarAPI用Flowを取得する() throws Exception {
        assertThat(service, is(notNullValue()));

        AuthorizationCodeFlow flow = service.createNewGoogleCalendarApiFlow();
        assertThat(flow, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void GoogleCalendarAPI用Clientを取得する_パラメータがnull() throws Exception {
        CalendarService mockService = createMockService();
        assertThat(mockService, is(notNullValue()));

        mockService.createGoogleCalendarClient(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendarAPI用Clientを取得する() throws Exception {
        CalendarService mockService = createMockService();
        assertThat(mockService, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        Map<String, Object> envAttributes = new HashMap<String, Object>();
        envAttributes.put(
            "com.google.appengine.api.users.UserService.user_id_key",
            "42");
        tester.environment.setAttributes(envAttributes);
        com.google.api.services.calendar.Calendar calendar =
                mockService.createGoogleCalendarClient(new User(
                    "dummy@gmail.com",
                    "example.com",
                    "dummy"));
        assertThat(calendar, is(notNullValue()));
        verify(mockService, times(1)).createGoogleCalendarClientFromUserId(
            "dummy");
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendarのカレンダー一覧を取得する_ログインしている() throws Exception {
        CalendarService mockService = createMockService();
        assertThat(mockService, is(notNullValue()));

        User user = new User("dummy@gmail.com", "example.com", "dummy");
        CalendarList feed = mockService.getGoogleCalendarList(user);
        assertThat(feed, is(notNullValue()));
        assertThat(feed.getItems(), is(notNullValue()));
        assertThat(feed.getItems().size(), is(3));
        verify(mockService, times(1)).getGoogleCalendarList(user);
    }

    /**
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void 連携対象のGoogleCalendarを登録する_ログインしていない() throws Exception {
        assertThat(service, is(notNullValue()));

        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("test1");
        service.putMyCalendar(myCalendar);
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象のGoogleCalendarを登録する_ログインしている() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("test1");
        myCalendar.setKeyword("keyword1 keyword2");
        Key key = service.putMyCalendar(myCalendar);
        MyCalendar storedMyCalendar =
                Datastore.getOrNull(MyCalendar.class, key);
        assertThat(storedMyCalendar, is(notNullValue()));
        assertThat(storedMyCalendar.getUser(), is(notNullValue()));
        assertThat(storedMyCalendar.getUser().getEmail(), is("dummy@gmail.com"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象のGoogleCalendarを登録する_既に登録されたデータを上書きする() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        // テストデータの登録。
        MyCalendar testData = new MyCalendar();
        testData.setCalendarId("test1");
        testData.setUser(UserServiceFactory.getUserService().getCurrentUser());
        Key testDataKey = Datastore.put(testData);

        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("test2");
        myCalendar.setKeyword("keyword1 keyword2 keyword3 keyword4");
        Key key = service.putMyCalendar(myCalendar);
        MyCalendar storedMyCalendar =
                Datastore.getOrNull(MyCalendar.class, key);
        assertThat(storedMyCalendar, is(notNullValue()));
        assertThat(storedMyCalendar.getKey(), is(testDataKey));
        assertThat(storedMyCalendar.getCalendarId(), is("test2"));
        assertThat(
            storedMyCalendar.getKeyword(),
            is("keyword1 keyword2 keyword3 keyword4"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void 連携対象のGoogleCalendarを登録する_パラメータmyCalendarがnull()
            throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        service.putMyCalendar(null);
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void 連携対象のGoogleCalendarを登録する_パラメータcalendarIdがnull()
            throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId(null);
        service.putMyCalendar(myCalendar);
    }

    /**
     * @throws Exception
     */
    @Test
    public void データストア上の連携対象のGoogleCalendarを取得する_ログインしていない場合() throws Exception {
        assertThat(service, is(notNullValue()));

        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("test1");
        myCalendar
            .setUser(UserServiceFactory.getUserService().getCurrentUser());
        Datastore.put(myCalendar);
        MyCalendar result = service.getCurrentMyCalendar();
        assertThat(result, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void データストア上の連携対象のGoogleCalendarを取得する_データが存在する場合() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("test1");
        myCalendar
            .setUser(UserServiceFactory.getUserService().getCurrentUser());
        Key key = Datastore.put(myCalendar);
        MyCalendar result = service.getCurrentMyCalendar();
        assertThat(result, is(notNullValue()));
        assertThat(result.getKey(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void データストア上の連携対象のGoogleCalendarを取得する_データが存在しない場合() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        MyCalendar result = service.getCurrentMyCalendar();
        assertThat(result, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendar連携をやめる_ログインしている場合() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        Map<String, Object> envAttributes = new HashMap<String, Object>();
        envAttributes.put(
            "com.google.appengine.api.users.UserService.user_id_key",
            "42");
        tester.environment.setAttributes(envAttributes);
        // テストデータの登録。
        MyCalendar testData = new MyCalendar();
        testData.setCalendarId("test1");
        testData.setUser(UserServiceFactory.getUserService().getCurrentUser());
        Key testDataKey = Datastore.put(testData);
        Credential credential = new GoogleCredential();
        new AppEngineCredentialStore().store("42", credential);
        assertThat(
            new AppEngineCredentialStore().load("42", credential),
            is(true));

        service.disConnectMyCalendar();

        MyCalendar result = Datastore.getOrNull(MyCalendar.class, testDataKey);
        assertThat(result, is(notNullValue()));
        assertThat(result.isDisabled(), is(true));
        assertThat(
            new AppEngineCredentialStore().load("42", credential),
            is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendar連携をやめる_元々連携していない場合() throws Exception {
        assertThat(service, is(notNullValue()));

        tester.environment.setEmail("dummy@gmail.com");
        Map<String, Object> envAttributes = new HashMap<String, Object>();
        envAttributes.put(
            "com.google.appengine.api.users.UserService.user_id_key",
            "42");
        tester.environment.setAttributes(envAttributes);

        // 例外は発生しない。
        service.disConnectMyCalendar();
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendar連携をやめる_ログインしていない場合() throws Exception {
        assertThat(service, is(notNullValue()));

        // テストデータの登録。
        MyCalendar testData = new MyCalendar();
        testData.setCalendarId("test1");
        testData.setUser(UserServiceFactory.getUserService().getCurrentUser());
        Key testDataKey = Datastore.put(testData);
        Credential credential = new GoogleCredential();
        new AppEngineCredentialStore().store("42", credential);
        assertThat(
            new AppEngineCredentialStore().load("42", credential),
            is(true));

        service.disConnectMyCalendar();

        int count = Datastore.query(MyCalendar.class, testDataKey).count();
        assertThat(count, is(1));
        assertThat(
            new AppEngineCredentialStore().load("42", credential),
            is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象カレンダーのリストを取得する() throws Exception {
        assertThat(service, is(notNullValue()));

        // テストデータの登録。
        List<MyCalendar> testDataList = new ArrayList<MyCalendar>();
        for (int i = 0; i < 10; i++) {
            MyCalendar myCalendar = new MyCalendar();
            myCalendar.setCalendarId("test" + 1);
            myCalendar.setUser(new User(
                "test" + 1 + "@gmail.com",
                "example.com",
                "test" + 1));
            myCalendar.setDisabled(false);
            testDataList.add(myCalendar);
        }
        List<Key> testDatakeys = Datastore.put(testDataList);
        MyCalendar disabledMyCalendar = new MyCalendar();
        disabledMyCalendar.setCalendarId("disabledCalendarId");
        disabledMyCalendar.setUser(new User(
            "test" + 1 + "@gmail.com",
            "example.com",
            "test" + 1));
        disabledMyCalendar.setDisabled(true);
        Datastore.put(disabledMyCalendar);

        List<MyCalendar> result = service.getMyCalendars();
        assertThat(result, is(notNullValue()));
        List<Key> resultKeys = new ArrayList<Key>();
        for (MyCalendar myCalendar : result) {
            resultKeys.add(myCalendar.getKey());
        }
        assertThat(resultKeys, is(testDatakeys));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象カレンダーのリストを取得する_データが存在しない場合() throws Exception {
        assertThat(service, is(notNullValue()));

        List<MyCalendar> result = service.getMyCalendars();
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象カレンダーのキーリストを取得する() throws Exception {
        assertThat(service, is(notNullValue()));

        // テストデータの登録。
        List<MyCalendar> testDataList = new ArrayList<MyCalendar>();
        for (int i = 0; i < 10; i++) {
            MyCalendar myCalendar = new MyCalendar();
            myCalendar.setCalendarId("test" + 1);
            myCalendar.setUser(new User(
                "test" + 1 + "@gmail.com",
                "example.com",
                "test" + 1));
            myCalendar.setDisabled(false);
            testDataList.add(myCalendar);
        }
        List<Key> testDatakeys = Datastore.put(testDataList);
        MyCalendar disabledMyCalendar = new MyCalendar();
        disabledMyCalendar.setCalendarId("disabledCalendarId");
        disabledMyCalendar.setUser(new User(
            "test" + 1 + "@gmail.com",
            "example.com",
            "test" + 1));
        disabledMyCalendar.setDisabled(true);
        Datastore.put(disabledMyCalendar);

        List<Key> result = service.getMyCalendarKeys();
        assertThat(result, is(notNullValue()));
        assertThat(result, is(testDatakeys));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象カレンダーのキーリストを取得する_データが存在しない() throws Exception {
        assertThat(service, is(notNullValue()));

        List<Key> result = service.getMyCalendarKeys();
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendarにニコニコ生放送の放送予定日をインポートする() throws Exception {
        CalendarService mockService = createMockService();
        assertThat(mockService, is(notNullValue()));

        User user = new User("dummy@gmail.com", "example.com", "dummy");

        doReturn(
            com.google.api.services.calendar.Calendar.builder(
                new NetHttpTransport(),
                new JacksonFactory()).build())
            .when(mockService)
            .createGoogleCalendarClient(user);

        // テストデータの登録。
        List<Nicolive> testDataList = new ArrayList<Nicolive>();
        for (int i = 0; i < 10; i++) {
            Nicolive nicolive = new Nicolive();
            nicolive.setTitle("ほげほげ");
            nicolive.setDescription(new Text("ほげほげ説明文"));
            DateTime datetime = new DateTime();
            nicolive.setOpenTime(datetime.toDate());
            nicolive.setLink(new Link("http://ryu22e.org/"));
            testDataList.add(nicolive);
        }
        List<Key> testDataKeys = Datastore.put(testDataList);
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("calendar1");
        myCalendar.setUser(user);
        Key myCalendarKey = Datastore.put(myCalendar);

        mockService.importToMyCalendar(myCalendarKey);

        MyCalendarLogMeta ml = MyCalendarLogMeta.get();
        MyCalendarLog myCalendarLog =
                Datastore
                    .query(ml)
                    .filter(
                        ml.user.equal(user),
                        ml.calendarId.equal("calendar1"))
                    .asSingle();
        assertThat(myCalendarLog, is(notNullValue()));
        assertThat(myCalendarLog.getNicoliveKeys(), is(testDataKeys));
    }

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendarインポートに失敗した旨をユーザーにメールで通知する() throws Exception {
        assertThat(service, is(notNullValue()));

        MyCalendar myCalendar = new MyCalendar();
        myCalendar.setCalendarId("calendar1");
        myCalendar.setUser(new User("dummy@gmail.com", "example.com", "dummy"));

        service.sendImportErrorMail(myCalendar);
        assertThat(tester.mailMessages.size(), is(1));
        MailMessage mailMessage = tester.mailMessages.get(0);
        assertThat(mailMessage.tos().size(), is(1));
        assertThat(mailMessage.tos().get(0), is("dummy@gmail.com"));
        assertThat(mailMessage.getSender(), is("ryu22e@gmail.com"));
        assertThat(
            mailMessage.getSubject(),
            is("Google Calendarへのインポートに失敗しました"));
        assertThat(mailMessage.getTextBody(), is(notNullValue()));
        assertThat(mailMessage.getTextBody(), not(""));
    }

    @Test
    public void testname() throws Exception {
        String keyword = "a b c";
        for (String s : Arrays.asList(keyword.split(" "))) {
            System.out.println(s);
        }
    }
}
