package org.ryu22e.nico2cal.controller.myCalendar;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.ryu22e.nico2cal.model.MyCalendar;
import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.util.AppEngineUtil;

import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * マイカレンダーのトップページを表示させるコントローラー。
 * @author ryu22e
 *
 */
public final class IndexController extends Controller {

    /**
     * 
     */
    public static final Logger LOGGER = Logger.getLogger(IndexController.class
        .getName());

    /**
     * 
     */
    private CalendarService calendarService = new CalendarService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {

        boolean storedCredential;
        if (AppEngineUtil.isProduction()) {
            CalendarList feed = null;
            try {
                feed =
                        calendarService
                            .getGoogleCalendarList(UserServiceFactory
                                .getUserService()
                                .getCurrentUser());
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
            }
            if (feed != null) {
                requestScope("calendars", feed.getItems());
                storedCredential = true;
            } else {
                storedCredential = false;
            }
        } else {
            CalendarListEntry entry1 = new CalendarListEntry();
            entry1.setId("test1");
            entry1.setSummary("テスト1");
            CalendarListEntry entry2 = new CalendarListEntry();
            entry2.setId("test2");
            entry2.setSummary("テスト2");
            CalendarListEntry entry3 = new CalendarListEntry();
            entry3.setId("test3");
            entry3.setSummary("テスト3");
            requestScope("calendars", Arrays.asList(entry1, entry2, entry3));
            storedCredential = true;
        }
        MyCalendar myCalendar = calendarService.getCurrentMyCalendar();
        if (myCalendar != null) {
            requestScope("isDisabled", myCalendar.isDisabled());
            requestScope("notifyErrorMail", myCalendar.isNotifyErrorMail());
            requestScope("calendarSummaries", myCalendar.getCalendarId());
            requestScope(
                "keywords",
                myCalendar.getKeywords().toArray(new String[0]));
        }
        requestScope("storedCredential", storedCredential);
        return forward("index.jsp");
    }
}
