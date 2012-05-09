package org.ryu22e.nico2cal.controller.myCalendar;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

/**
 * @author ryu22e
 *
 */
public final class ImportControllerTest extends ControllerTestCase {

    /**
     * @throws Exception
     */
    @Test
    public void run() throws Exception {
        tester.start("/myCalendar/import");
        ImportController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
    }
}
