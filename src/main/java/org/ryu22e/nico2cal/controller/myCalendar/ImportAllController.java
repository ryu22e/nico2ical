package org.ryu22e.nico2cal.controller.myCalendar;

import java.util.List;
import java.util.logging.Logger;

import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * Google Calendarに{@link org.ryu22e.nico2cal.model.Nicolive}の内容を全ての{@link org.ryu22e.nico2cal.model.MyCalendar}インポートするコントローラー。
 * @author ryu22e
 *
 */
public final class ImportAllController extends Controller {

    /**
     * 
     */
    public static final Logger LOGGER = Logger
        .getLogger(ImportAllController.class.getName());

    /**
     * 
     */
    private CalendarService calendarService = new CalendarService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        LOGGER.info("BEGIN: " + this.getClass().getName());

        List<Key> myCalendarKeys = calendarService.getMyCalendarKeys();
        for (Key key : myCalendarKeys) {
            TaskOptions options =
                    TaskOptions.Builder.withUrl("/myCalendar/import").method(
                        Method.POST);
            options =
                    options.param("myCalendarKey", Datastore.keyToString(key));
            QueueFactory.getQueue("mycalendar-import").add(options);
        }

        LOGGER.info("END: " + this.getClass().getName());
        return null;
    }
}
