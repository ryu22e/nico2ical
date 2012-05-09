package org.ryu22e.nico2cal.controller.myCalendar;

import java.io.IOException;
import java.util.logging.Logger;

import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;

/**
 * Google Calendarに{@link org.ryu22e.nico2cal.model.Nicolive}の内容を個別の{@link org.ryu22e.nico2cal.model.MyCalendar}にインポートするコントローラー。
 * @author ryu22e
 *
 */
public final class ImportController extends Controller {

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
        Validators v = new Validators(request);
        v.add("myCalendarKey", v.required());
        if (v.validate()) {
            try {
                calendarService.importToMyCalendar(Datastore
                    .stringToKey(param("myCalendarKey")));
            } catch (IllegalArgumentException e) {
                LOGGER.warning(e.getMessage());
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
            }
        }
        return null;
    }
}
