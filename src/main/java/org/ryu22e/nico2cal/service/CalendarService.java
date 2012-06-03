package org.ryu22e.nico2cal.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Url;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;

import org.ryu22e.nico2cal.meta.MyCalendarLogMeta;
import org.ryu22e.nico2cal.meta.MyCalendarMeta;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.ryu22e.nico2cal.model.MyCalendarLog;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.ryu22e.nico2cal.util.GoogleApiKeyUtil;
import org.ryu22e.nico2cal.util.HtmlRemoveUtil;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;
import org.slim3.util.AppEngineUtil;
import org.slim3.util.BeanUtil;
import org.slim3.util.CopyOptions;
import org.slim3.util.DateUtil;
import org.slim3.util.TimeZoneLocator;
import org.xml.sax.SAXException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.extensions.appengine.http.urlfetch.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * iCalendarファイルを操作するサービスクラス。
 * @author ryu22e
 *
 */
public class CalendarService {

    /**
     * 
     */
    private static final Logger LOGGER = Logger.getLogger(CalendarService.class
        .getName());

    /**
     * 
     */
    private static final ProdId PROD_ID = new ProdId("nico2ical");

    /**
     * 
     */
    private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

    /**
     * 
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * 
     */
    private static final String CALNAME = "ニコニコ生放送";

    /**
     * リスト1とリスト2の同じ要素をマージする。
     * @param <T> 型名
     * @param list1 リスト1
     * @param list2 リスト2
     * @return マージされたリスト
     */
    private <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> merged = new LinkedList<T>();
        if (list1.size() <= 0) {
            merged.addAll(list2);
        } else if (list2.size() <= 0) {
            merged.addAll(list1);
        } else {
            for (T t : list1) {
                if (list2.contains(t)) {
                    merged.add(t);
                }
            }
        }
        return merged;
    }

    /**
     * キーワードに該当する{@link Nicolive}のキーを取得する。
     * @param keywords キーワード
     * @return キーワードに該当する{@link Nicolive}のキー
     */
    private List<Key> getKeywordKeys(List<String> keywords) {
        NicoliveIndexMeta ni = NicoliveIndexMeta.get();
        List<Key> keywordKeys = new LinkedList<Key>();
        for (String keyword : keywords) {
            List<NicoliveIndex> indexes =
                    Datastore
                        .query(ni)
                        .filter(ni.keyword.equal(keyword))
                        .asList();
            List<Key> keys = new LinkedList<Key>();
            for (NicoliveIndex nicoliveIndex : indexes) {
                keys.add(nicoliveIndex.getNicoliveKey());
            }
            keywordKeys = merge(keys, keywordKeys);
        }
        return keywordKeys;
    }

    /**
     * @param userId
     * @return
     */
    protected com.google.api.services.calendar.Calendar createGoogleCalendarClientFromUserId(
            String userId) {
        Credential credential =
                createNewGoogleCalendarApiFlow().loadCredential(userId);
        if (credential == null) {
            return null;
        } else {
            return com.google.api.services.calendar.Calendar
                .builder(HTTP_TRANSPORT, JSON_FACTORY)
                .setHttpRequestInitializer(credential)
                .build();
        }
    }

    /**
     * Google Calendarインポートに失敗した旨をユーザーにメールで通知する。
     * @param myCalendar {@link MyCalendar}
     */
    protected void sendImportErrorMail(MyCalendar myCalendar) {
        Message message = new Message();
        message.setTo(myCalendar.getUser().getEmail());
        message.setSubject("Google Calendarへのインポートに失敗しました");
        message.setSender("ryu22e@gmail.com");
        Configuration config = new Configuration();
        config.setObjectWrapper(new DefaultObjectWrapper());

        try {
            config.setDirectoryForTemplateLoading(new File(
                "src/main/resources/templates/mail/"));
            Template template = config.getTemplate("importError.flt");
            StringWriter writer = new StringWriter();
            Map<String, String> root = new HashMap<String, String>();
            root.put("userId", myCalendar.getUser().getUserId());
            template.process(root, writer);
            message.setTextBody(new String(writer.getBuffer()));
            com.google.appengine.api.mail.MailService mailService =
                    MailServiceFactory.getMailService();
            mailService.send(message);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return;
        } catch (TemplateException e) {
            LOGGER.warning(e.getMessage());
            return;
        }
    }

    /**
     * Datastoreに登録されたRSSフィードをiCalendar形式のデータに変換する。
     * @param condition 検索条件
     * @return iCalendar形式のデータ
     * @throws NullPointerException パラメータがnullの場合。
     * @throws IllegalArgumentException 検索条件にStartDateが指定されていない場合。
     */
    public Calendar getCalendar(CalendarCondition condition) {
        if (condition == null) {
            throw new NullPointerException("condition is null.");
        }
        if (condition.getStartDate() == null) {
            throw new IllegalArgumentException("StartDate is null.");
        }

        Calendar calendar = new Calendar();
        calendar.getProperties().add(PROD_ID);
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(new XProperty("X-WR-CALNAME", CALNAME));

        NicoliveMeta n = NicoliveMeta.get();
        ModelQuery<Nicolive> query =
                Datastore
                    .query(n)
                    .filter(
                        n.openTime.greaterThanOrEqual(condition.getStartDate()))
                    .sort(n.openTime.getName(), SortDirection.ASCENDING);

        if (condition.getKeywords() != null
                && 0 < condition.getKeywords().size()) {
            List<Key> keywordKeys = getKeywordKeys(condition.getKeywords());
            if (0 < keywordKeys.size()) {
                query = query.filterInMemory(n.key.in(keywordKeys));
            } else {
                // キーワード検索で該当するエンティティがなければ、この後のクエリを発行する必要がないので、ここで検索を終了とする。
                return calendar;
            }
        }

        TimeZone timezone = TimeZoneLocator.get();
        List<Nicolive> nicolives = query.asList();
        for (Nicolive nicolive : nicolives) {
            PropertyList properties = new PropertyList();
            properties.add(new Summary(nicolive.getTitle()));
            String description;
            try {
                description =
                        HtmlRemoveUtil.removeHtml(nicolive
                            .getDescription()
                            .getValue());
            } catch (SAXException e1) {
                description = "";
            } catch (IOException e1) {
                description = "";
            }
            java.util.Calendar c = DateUtil.toCalendar(nicolive.getOpenTime());
            c.setTimeZone(timezone);
            properties.add(new Description(description));
            properties.add(new DtStart(new DateTime(c.getTime()), true));
            properties.add(new DtEnd(new DateTime(c.getTime()), true));
            try {
                URI uri = new URI(nicolive.getLink().getValue());
                properties.add(new Url(uri));
            } catch (URISyntaxException e) {
                LOGGER.warning(e.getMessage());
            }

            VEvent event = new VEvent(properties);
            calendar.getComponents().add(event);
        }

        return calendar;
    }

    /**
     * GoogleCalendarAPI用Flowを取得する。
     * @return GoogleCalendarAPI用Flow
     */
    public AuthorizationCodeFlow createNewGoogleCalendarApiFlow() {
        return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            GoogleApiKeyUtil.getClientId(),
            GoogleApiKeyUtil.getClientSecret(),
            Collections.singleton(CalendarScopes.CALENDAR)).setCredentialStore(
            new AppEngineCredentialStore()).build();
    }

    /**
     * Google Calendar API用Clientを取得する。
     * @return Google Calendar API用Client
     * @throws NullPointerException パラメータがnullの場合。
     */
    public com.google.api.services.calendar.Calendar createGoogleCalendarClient(
            User user) {
        if (user == null) {
            throw new NullPointerException("user is null.");
        }
        return createGoogleCalendarClientFromUserId(user.getUserId());
    }

    /**
     * Google Calendarのカレンダー一覧を取得する。
     * @return Google Calendarのカレンダー一覧
     * @throws IOException 
     * @throws NullPointerException パラメータがnullの場合。
     */
    public CalendarList getGoogleCalendarList(User user) throws IOException {
        com.google.api.services.calendar.Calendar client =
                createGoogleCalendarClient(user);
        if (client == null) {
            return null;
        }
        com.google.api.services.calendar.Calendar.CalendarList.List listRequest =
                client.calendarList().list();
        listRequest.setFields("items(id,summary)");
        listRequest.setMinAccessRole("writer");
        return listRequest.execute();
    }

    /**
     * 連携対象のGoogle Calendarを登録する。
     * @param myCalendar {@link MyCalendar}
     * @return 登録後のKey
     * @throws NullPointerException パラメータmyCalendarまたは{@link MyCalendar#getCalendarId()}がnullの場合。
     * @throws AssertionError ログインしていない場合。
     */
    public Key putMyCalendar(MyCalendar myCalendar) {
        if (myCalendar == null) {
            throw new NullPointerException("myCalendar is null.");
        }
        if (myCalendar.getCalendarId() == null) {
            throw new NullPointerException("calendarId is null.");
        }
        User user = UserServiceFactory.getUserService().getCurrentUser();
        if (user == null) {
            throw new AssertionError("Require authorized.");
        }
        MyCalendarMeta mc = MyCalendarMeta.get();
        MyCalendar storedmyCalendar =
                Datastore
                    .query(MyCalendar.class)
                    .filter(mc.user.equal(user))
                    .asSingle();
        MyCalendar entity;
        if (storedmyCalendar == null) {
            // データストアにデータが存在しなければ、新規にデータを作る。
            entity = myCalendar;
            entity.setUser(user);
        } else {
            // データストアにデータが存在するなら、データを上書きする。
            entity = storedmyCalendar;
            CopyOptions options = new CopyOptions();
            options.exclude("key", "user");
            BeanUtil.copy(myCalendar, entity, options);
        }
        return Datastore.put(entity);
    }

    /**
     * 連携対象のGoogleCalendarを取得する。
     */
    public MyCalendar getCurrentMyCalendar() {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        if (user == null) {
            return null;
        }
        MyCalendarMeta mc = MyCalendarMeta.get();
        return Datastore.query(mc).filter(mc.user.equal(user)).asSingle();
    }

    /**
     * Google Calendar連携をやめる。
     */
    public void disConnectMyCalendar() {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        if (user != null) {
            // Access tokenが格納されているAppEngineCredentialStoreを削除する。
            new AppEngineCredentialStore().delete(user.getUserId(), null);
            MyCalendarMeta mc = MyCalendarMeta.get();
            MyCalendar myCalendar =
                    Datastore.query(mc).filter(mc.user.equal(user)).asSingle();
            if (myCalendar != null) {
                myCalendar.setDisabled(true);
                Datastore.put(myCalendar);
            }
        }
    }

    /**
     * {@link MyCalendar}のリストを取得する。
     * @return {@link MyCalendar}のリスト
     */
    public List<MyCalendar> getMyCalendars() {
        MyCalendarMeta mc = MyCalendarMeta.get();
        return Datastore.query(mc).filter(mc.disabled.equal(false)).asList();
    }

    /**
    * {@link MyCalendar}のキーリストを取得する。
     * @return {@link MyCalendar}のキーリスト
     */
    public List<Key> getMyCalendarKeys() {
        MyCalendarMeta mc = MyCalendarMeta.get();
        return Datastore.query(mc).filter(mc.disabled.equal(false)).asKeyList();
    }

    /**
     * マイカレンダー（Google Calendar）にニコニコ生放送の放送予定日をインポートする。
     * @param myCalendarKey {@link MyCalendar}のキー
     * @throws IOException マイカレンダー（Google Calendar）へのインポートに失敗した場合
     */
    public void importToMyCalendar(Key myCalendarKey) throws IOException {
        MyCalendar myCalendar =
                Datastore.getOrNull(MyCalendar.class, myCalendarKey);
        if (myCalendar == null) {
            return;
        }
        com.google.api.services.calendar.Calendar client =
                createGoogleCalendarClient(myCalendar.getUser());
        if (client != null) {
            NicoliveMeta n = NicoliveMeta.get();
            org.joda.time.DateTime d = new org.joda.time.DateTime();
            d = d.minusWeeks(1);
            ModelQuery<Nicolive> query =
                    Datastore.query(n).filter(
                        n.openTime.greaterThanOrEqual(d.toDate()));
            if (myCalendar.getKeyword() != null
                    && 0 < myCalendar.getKeyword().length()) {
                List<Key> keywordKeys =
                        getKeywordKeys(Arrays.asList(myCalendar
                            .getKeyword()
                            .split(" ")));
                if (0 < keywordKeys.size()) {
                    query = query.filterInMemory(n.key.in(keywordKeys));
                } else {
                    // キーワード検索で該当するエンティティがなければ、この後のクエリを発行する必要がないので、ここで検索を終了とする。
                    return;
                }
            }
            MyCalendarLogMeta ml = MyCalendarLogMeta.get();
            MyCalendarLog myCalendarLog =
                    Datastore
                        .query(ml)
                        .filter(
                            ml.user.equal(myCalendar.getUser()),
                            ml.calendarId.equal(myCalendar.getCalendarId()))
                        .asSingle();
            if (myCalendarLog == null) {
                myCalendarLog = new MyCalendarLog();
                myCalendarLog.setUser(myCalendar.getUser());
                myCalendarLog.setCalendarId(myCalendar.getCalendarId());
                myCalendarLog.setNicoliveKeys(new ArrayList<Key>());
            }
            List<Key> importedNicoliveKeys = new LinkedList<Key>();
            List<Key> allNicoliveKeys = new LinkedList<Key>();
            List<Nicolive> nicolives = query.asList();
            for (Nicolive nicolive : nicolives) {
                allNicoliveKeys.add(nicolive.getKey());
                if (!myCalendarLog
                    .getNicoliveKeys()
                    .contains(nicolive.getKey())) {
                    Event event = new Event();
                    // カレンダーのSummaryに生放送のタイトルを設定する。
                    event.setSummary(nicolive.getTitle());
                    // カレンダーのDescriptionに生放送のURLと説明文を設定する。
                    try {
                        event.setDescription(nicolive.getLink().getValue()
                                + "¥n"
                                + HtmlRemoveUtil.removeHtml(nicolive
                                    .getDescription()
                                    .getValue()));
                    } catch (SAXException e) {
                        LOGGER.warning(e.getMessage());
                    } catch (IOException e) {
                        LOGGER.warning(e.getMessage());
                    }
                    java.util.Calendar c =
                            DateUtil.toCalendar(nicolive.getOpenTime());
                    // 生放送会場日時をcom.google.api.client.util.DateTimeに変換する。
                    TimeZone timezone = TimeZoneLocator.get();
                    c.setTimeZone(timezone);
                    com.google.api.client.util.DateTime datetime =
                            new com.google.api.client.util.DateTime(c.getTime());
                    // カレンダーのStartに生放送会場日時を設定する。
                    EventDateTime start = new EventDateTime();
                    start.setDateTime(datetime);
                    start.setTimeZone(timezone.getID());
                    event.setStart(start);
                    // カレンダーのEndに生放送会場日時を設定する。
                    EventDateTime end = new EventDateTime();
                    end.setTimeZone(timezone.getID());
                    end.setDateTime(datetime);
                    event.setEnd(end);

                    if (AppEngineUtil.isProduction()) {
                        try {
                            client
                                .events()
                                .insert(myCalendar.getCalendarId(), event)
                                .execute();
                        } catch (IOException e) {
                            // 途中でインポートに失敗した場合は、成功した分までMyCalendarLogに記録して、次回のインポートの対象から外すようにする。
                            if (0 < importedNicoliveKeys.size()) {
                                myCalendarLog.getNicoliveKeys().addAll(
                                    importedNicoliveKeys);
                                Datastore.putAsync(myCalendarLog);
                            }
                            if (myCalendar.isNotifyErrorMail()) {
                                sendImportErrorMail(myCalendar);
                            }
                            myCalendar.setDisabled(true);
                            Datastore.putAsync(myCalendar);
                            throw e;
                        }
                    }
                    importedNicoliveKeys.add(nicolive.getKey());
                }
            }
            // 今回インポート対象にした範囲のNicoliveをMyCalendarLogに記録して、次回のインポート対象から外すようにする。
            myCalendarLog.setNicoliveKeys(allNicoliveKeys);
            Datastore.putAsync(myCalendarLog);
        }
    }
}
