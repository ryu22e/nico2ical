package org.ryu22e.nico2cal.controller.myCalendar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

/**
 * @author ryu22e
 *
 */
public class IndexControllerTest extends ControllerTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void 表示確認() throws Exception {
        tester.environment.setEmail("dummy@gmail.com");
        tester.start("/myCalendar/index");
        IndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is("/myCalendar/index.jsp"));
    }
}
