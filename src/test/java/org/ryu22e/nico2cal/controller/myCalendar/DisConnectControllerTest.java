package org.ryu22e.nico2cal.controller.myCalendar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author ryu22e
 *
 */
public final class DisConnectControllerTest extends ControllerTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void GoogleCalendar連携をやめる() throws Exception {
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

        tester.start("/myCalendar/disConnect");
        DisConnectController controller = tester.getController();
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
        tester.environment.setEmail("dummy@gmail.com");
        Map<String, Object> envAttributes = new HashMap<String, Object>();
        envAttributes.put(
            "com.google.appengine.api.users.UserService.user_id_key",
            "42");
        tester.environment.setAttributes(envAttributes);

        tester.start("/myCalendar/disConnect");
        DisConnectController controller = tester.getController();
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
    }
}
