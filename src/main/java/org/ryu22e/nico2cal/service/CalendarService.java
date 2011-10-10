package org.ryu22e.nico2cal.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
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

import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.ryu22e.nico2cal.util.HtmlRemoveUtil;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;
import org.slim3.util.DateUtil;
import org.slim3.util.TimeZoneLocator;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * iCalendarファイルを操作するサービスクラス。
 * @author ryu22e
 *
 */
public final class CalendarService {

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
        NicoliveIndexMeta ni = NicoliveIndexMeta.get();

        ModelQuery<Nicolive> query =
                Datastore
                    .query(n)
                    .filter(
                        n.openTime.greaterThanOrEqual(condition.getStartDate()))
                    .sort(n.openTime.getName(), SortDirection.ASCENDING);

        if (condition.getKeywords() != null
                && 0 < condition.getKeywords().size()) {
            List<Key> keywordKeys = new LinkedList<Key>();
            for (String keyword : condition.getKeywords()) {
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

}
