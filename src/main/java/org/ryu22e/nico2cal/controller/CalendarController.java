package org.ryu22e.nico2cal.controller;

import java.net.URLEncoder;
import java.util.Arrays;

import net.fortuna.ical4j.model.Calendar;

import org.ryu22e.nico2cal.service.CalendarCondition;
import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Errors;
import org.slim3.controller.validator.Validators;
import org.slim3.memcache.Memcache;

/**
 * データストアのNicoliveをiCalendarファイルを変換して取得するコントローラー。
 * @author ryu22e
 *
 */
public final class CalendarController extends Controller {

    /**
     * 
     */
    private static final int START_WEEK_MIN = 1;
    /**
     * 
     */
    private static final int START_WEEK_MAX = 4;
    /**
     * 
     */
    private static final int KEYWORD_MAX_LENGTH = 50;
    /**
     * 
     */
    private static final int UNAUTHORIZED = 401;

    /**
     * 
     */
    private static final String ICALENDAR_FILE_NAME = "nico2ical.ics";

    /**
     * @see CalendarService
     */
    private CalendarService calendarService = new CalendarService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        Validators v = new Validators(request);
        v.add(
            "startWeek",
            v.required(),
            v.integerType(),
            v.longRange(START_WEEK_MIN, START_WEEK_MAX));
        v.add("keyword", v.maxlength(KEYWORD_MAX_LENGTH));
        if (!v.validate()) {
            response.setStatus(UNAUTHORIZED);
            Errors errors = v.getErrors();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                sb.append(errors.get(i) + "\n");
            }
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(new String(sb));
        } else {
            response.setContentType("text/calendar;charset=UTF-8");
            response.setHeader("Content-Disposition", "filename=\""
                    + ICALENDAR_FILE_NAME + "\"");

            Integer startWeekNum =
                    Integer.parseInt(request.getParameter("startWeek"));
            StartWeek startWeek = StartWeek.parse(startWeekNum);
            String keyword = request.getParameter("keyword");
            String memcacheKey = "startWeek=" + startWeekNum;
            if (keyword != null) {
                memcacheKey +=
                        "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
            }
            Object cache = Memcache.get(memcacheKey);
            if (cache == null) {
                CalendarCondition condition = new CalendarCondition();
                if (keyword != null) {
                    // 半角スペースで区切られているキーワードは分割して配列にする。
                    condition.setKeywords(Arrays.asList(keyword.split(" ")));
                }
                condition.setStartDate(startWeek.toDate());

                Calendar calendar = calendarService.getCalendar(condition);
                response.getWriter().write(calendar.toString());

                // MemcacheにiCalendarの内容をキャッシュする。
                Memcache.put(memcacheKey, calendar.toString());
            } else {
                // キャッシュがある場合はキャッシュの内容を返す。
                response.getWriter().write((String) cache);
            }

        }
        response.flushBuffer();

        return null;
    }
}
