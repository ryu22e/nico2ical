package org.ryu22e.nico2cal.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

/**
 * @author ryu22e
 *
 */
public class NicoliveTest extends AppEngineTestCase {

    /**
     * 
     */
    private Nicolive model = new Nicolive();

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
