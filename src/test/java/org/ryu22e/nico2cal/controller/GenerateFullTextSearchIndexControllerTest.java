package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

/**
 * @author ryu22e
 *
 */
public final class GenerateFullTextSearchIndexControllerTest extends
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
        testDataKeys.clear();
        for (int i = 0; i < 99; i++) {
            Nicolive nicolinve = new Nicolive();
            nicolinve.setTitle("テスト" + i);
            nicolinve.setDescription(new Text("テスト説明文" + i));
            datetime = datetime.minusHours(i);
            nicolinve.setOpenTime(datetime.toDate());
            nicolinve.setLink(new Link("http://ryu22e.org/" + i));
            Key key = Datastore.put(nicolinve);
            testDataKeys.add(key);
        }
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

        NicoliveIndexMeta ni = NicoliveIndexMeta.get();
        Datastore.delete(Datastore
            .query(ni)
            .filter(ni.keyword.startsWith("テスト"))
            .asKeyList());

        super.tearDown();
    }

    /**
     * @throws Exception
     */
    @Test
    public void 全文検索用インデックスを作成する() throws Exception {
        tester.start("/GenerateFullTextSearchIndex");
        GenerateFullTextSearchIndexController controller =
                tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
