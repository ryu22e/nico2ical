package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.model.NicoliveIndex;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Key;

/**
 * @author ryu22e
 *
 */
public final class DeleteOldNicoliveIndexControllerTest extends
        ControllerTestCase {
    /**
     * 
     */
    private List<Key> testDataKeys = new LinkedList<Key>();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        NamespaceManager.set("test");

        // テストデータを登録する。
        DateTime datetime = new DateTime();
        List<NicoliveIndex> indexes = new LinkedList<NicoliveIndex>();
        for (int i = 0; i < 1500; i++) {
            NicoliveIndex index = new NicoliveIndex();
            index.setKeyword("テスト");
            index.setOpenTime(datetime.minusDays(i).toDate());
            indexes.add(index);
        }
        testDataKeys.addAll(Datastore.put(indexes));

    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public void tearDown() throws Exception {
        // テストデータを削除する。
        if (0 < testDataKeys.size()) {
            Datastore.delete(testDataKeys);
        }

        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test
    public void 古い全文検索用インデックスを削除する() throws Exception {
        tester.start("/DeleteOldNicoliveIndex");
        DeleteOldNicoliveIndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));

        DateTime datetime = new DateTime();
        NicoliveIndexMeta ni = NicoliveIndexMeta.get();
        int count =
                Datastore
                    .query(ni)
                    .filter(
                        ni.openTime.lessThanOrEqual(datetime
                            .minusDays(31)
                            .toDate()))
                    .count();
        assertThat(count, is(0));
    }
}
