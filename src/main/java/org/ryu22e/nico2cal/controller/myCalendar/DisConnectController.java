package org.ryu22e.nico2cal.controller.myCalendar;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

/**
 * Google Calendarとの連携をやめるコントローラー。
 * @author ryu22e
 *
 */
public final class DisConnectController extends Controller {

    /**
     * 
     */
    private CalendarService calendarService = new CalendarService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        calendarService.disConnectMyCalendar();

        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "ok");

        JsonWriter writer = new JsonWriter(response.getWriter());
        TypeToken<Map<String, Object>> typeToken =
                new TypeToken<Map<String, Object>>() {
                };
        Type type = typeToken.getType();
        new Gson().toJson(map, type, writer);

        return null;
    }
}
