package org.ryu22e.nico2cal.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
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
import org.slim3.datastore.Datastore;

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
    private static final ProdId PROD_ID = new ProdId("nico2cal");

    /**
     * 
     */
    private static final String CALNAME = "ニコニコ生放送";

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

        List<Nicolive> nicolives =
                Datastore
                    .query(n)
                    .filter(
                        n.openTime.greaterThanOrEqual(condition.getStartDate()))
                    .sort(n.openTime.getName(), SortDirection.ASCENDING)
                    .asList();

        if (0 < nicolives.size()) {
            Set<Key> keywordKeys = null;
            if (condition.getKeywords() != null
                    && 0 < condition.getKeywords().size()) {
                keywordKeys = new HashSet<Key>();
                NicoliveIndexMeta ni = NicoliveIndexMeta.get();
                List<NicoliveIndex> indexes =
                        Datastore
                            .query(ni)
                            .filter(ni.keyword.in(condition.getKeywords()))
                            .asList();
                for (NicoliveIndex nicoliveIndex : indexes) {
                    keywordKeys.add(nicoliveIndex.getNicoliveKey());
                }
            }
            for (Nicolive nicolive : nicolives) {
                if (keywordKeys == null
                        || keywordKeys.contains(nicolive.getKey())) {
                    PropertyList properties = new PropertyList();
                    properties.add(new Summary(nicolive.getTitle()));
                    properties.add(new Description(nicolive
                        .getDescription()
                        .getValue()));
                    properties.add(new DtStart(new DateTime(nicolive
                        .getOpenTime())));
                    properties.add(new DtEnd(new DateTime(nicolive
                        .getOpenTime())));
                    try {
                        URI uri = new URI(nicolive.getLink().getValue());
                        properties.add(new Url(uri));
                    } catch (URISyntaxException e) {
                        LOGGER.log(Level.WARNING, e.getMessage());
                    }

                    VEvent event = new VEvent(properties);
                    calendar.getComponents().add(event);
                }
            }
        }

        return calendar;
    }

}
