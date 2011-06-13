package org.ryu22e.nico2cal.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author ryu22e
 *
 */
public final class NicoliveRssServiceTest extends AppEngineTestCase {

    private NicoliveRssService service = new NicoliveRssService();

    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }
}
