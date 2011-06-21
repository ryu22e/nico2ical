package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.NamespaceManager;

/**
 * @author ryu22e
 *
 */
public final class GenerateNicoliveControllerTest extends ControllerTestCase {

    /**
     * 
     */
    private void clearDataStore() {
        NicoliveMeta n = NicoliveMeta.get();
        Datastore.delete(Datastore.query(n).asKeyList());
        NicoliveIndexMeta ni = NicoliveIndexMeta.get();
        Datastore.delete(Datastore.query(ni).asKeyList());
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        NamespaceManager.set("test");

        clearDataStore();
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void tearDown() throws Exception {
        clearDataStore();

        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test
    public void run() throws Exception {
        tester.start("/GenerateNicolive");
        GenerateNicoliveController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));

        NicoliveMeta n = NicoliveMeta.get();
        assertThat(Datastore.query(n).count(), not(0));
    }
}
