package org.ryu22e.nico2cal.controller.myCalendar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.Test;
import org.ryu22e.nico2cal.meta.MyCalendarMeta;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author ryu22e
 *
 */
public final class SaveControllerTest extends ControllerTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象のGoogleCalendarを登録する_パラメータを渡していない() throws Exception {
        tester.environment.setEmail("dummy@gmail.com");

        tester.request.setMethod("POST");
        tester.start("/myCalendar/save");
        SaveController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(403));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 連携対象のGoogleCalendarを登録する_パラメータを渡している() throws Exception {
        tester.environment.setEmail("dummy@gmail.com");

        tester.request.setMethod("POST");
        tester.param("calendarId", "test1");
        tester.param("notifyErrorMail", "true");
        tester.param("keyword", "keyword1 keyword2");
        tester.start("/myCalendar/save");
        SaveController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
        String output = tester.response.getOutputAsString();
        assertThat(output, is(notNullValue()));
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, String> json = gson.fromJson(output, type);
        assertThat(json, is(notNullValue()));
        assertThat(json.get("status"), is("ok"));

        User user = UserServiceFactory.getUserService().getCurrentUser();
        MyCalendarMeta mc = MyCalendarMeta.get();
        MyCalendar myCalendar =
                Datastore.query(mc).filter(mc.user.equal(user)).asSingle();
        assertThat(myCalendar, is(notNullValue()));
        assertThat(myCalendar.getCalendarId(), is("test1"));
        assertThat(myCalendar.isNotifyErrorMail(), is(true));
        assertThat(myCalendar.getKeyword(), is(notNullValue()));
        assertThat(myCalendar.getKeyword(), is("keyword1 keyword2"));
    }
}
