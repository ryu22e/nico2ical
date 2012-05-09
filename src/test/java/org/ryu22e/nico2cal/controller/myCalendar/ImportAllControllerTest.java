package org.ryu22e.nico2cal.controller.myCalendar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.users.User;

/**
 * @author ryu22e
 *
 */
public class ImportAllControllerTest extends ControllerTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void run() throws Exception {
        // テストデータの登録。
        List<MyCalendar> myCalendars = new ArrayList<MyCalendar>();
        for (int i = 0; i < 10; i++) {
            MyCalendar myCalendar = new MyCalendar();
            myCalendar.setCalendarId("test" + i);
            myCalendar.setUser(new User(
                "test" + i + "@gmail.com",
                "example.com",
                "test" + i));
            myCalendar.setDisabled(false);
            myCalendars.add(myCalendar);
        }
        Datastore.put(myCalendars);

        tester.start("/myCalendar/importAll");
        ImportAllController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));

        assertThat(tester.tasks.size(), is(10));
    }
}
