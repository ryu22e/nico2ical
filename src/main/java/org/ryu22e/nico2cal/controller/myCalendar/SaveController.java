package org.ryu22e.nico2cal.controller.myCalendar;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ryu22e.nico2cal.controller.HttpStatusConstants;
import org.ryu22e.nico2cal.model.MyCalendar;
import org.ryu22e.nico2cal.service.CalendarService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.BeanUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

/**
 * 連携対象のGoogle Calendarを登録するコントローラー。
 * @author ryu22e
 *
 */
public final class SaveController extends Controller {

    /**
     * 
     */
    private CalendarService calendarService = new CalendarService();

    /**
     * 
     */
    public static final int KEYWORDS_MAX_LENGTH = 25;

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        Validators v = new Validators(request);
        String[] keywords = paramValues("keywords[]");
        v.add("calendarId", v.required());
        v.add("notifyErrorMail", v.required());
        if (!v.validate()
                || (keywords != null && KEYWORDS_MAX_LENGTH < keywords.length)) {
            response.setStatus(HttpStatusConstants.FORBIDDEN);
            return null;
        }
        MyCalendar myCalendar = new MyCalendar();
        BeanUtil.copy(request, myCalendar);
        myCalendar.setKeywords(Arrays.asList(paramValues("keywords[]")));
        myCalendar.setDisabled(false);
        calendarService.putMyCalendar(myCalendar);

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
