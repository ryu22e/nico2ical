package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.ryu22e.nico2cal.meta.NicoliveIndexMeta;
import org.ryu22e.nico2cal.meta.NicoliveMeta;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.model.NicoliveIndex;
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
public class GenerateNicoliveIndexControllerTest extends ControllerTestCase {

    /**
     * 
     */
    private List<Key> testDataKeys = new LinkedList<Key>();

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

        // テストデータを登録する。
        testDataKeys.clear();
        List<Nicolive> nicolives = new LinkedList<Nicolive>();
        for (int i = 0; i < 10; i++) {
            Nicolive nicolive = new Nicolive();
            nicolive.setTitle("テスト" + i);
            nicolive.setDescription(new Text("テスト説明文" + i));
            DateTime datetime = new DateTime();
            datetime = datetime.minusHours(i);
            nicolive.setOpenTime(datetime.toDate());
            nicolive.setLink(new Link("http://ryu22e.org/" + i));
            nicolives.add(nicolive);
        }
        testDataKeys.addAll(Datastore.put(nicolives));
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
    public void バリデーションのテスト_キーが指定されていない() throws Exception {
        tester.start("/GenerateNicoliveIndex");
        GenerateNicoliveIndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(401));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 全文検索用インデックスを作成する() throws Exception {
        assertThat(testDataKeys.size(), not(0));
        List<String> keysString = new LinkedList<String>();
        for (Key key : testDataKeys) {
            keysString.add(Datastore.keyToString(key));
        }
        tester.paramValues("keys[]", keysString.toArray(new String[0]));
        tester.start("/GenerateNicoliveIndex");
        GenerateNicoliveIndexController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(200));
        assertThat(tester.getDestinationPath(), is(nullValue()));

        NicoliveIndexMeta ni = NicoliveIndexMeta.get();

        List<NicoliveIndex> indexes1 =
                Datastore.query(ni).filter(ni.keyword.equal("テスト")).asList();
        assertThat(indexes1, is(notNullValue()));
        List<Key> indexKeys1 = new LinkedList<Key>();
        for (NicoliveIndex index1 : indexes1) {
            indexKeys1.add(index1.getNicoliveKey());
        }
        assertThat(indexKeys1, hasItems(testDataKeys.toArray(new Key[0])));

        List<NicoliveIndex> indexes2 =
                Datastore.query(ni).filter(ni.keyword.equal("説明")).asList();
        assertThat(indexes2, is(notNullValue()));
        List<Key> indexKeys2 = new LinkedList<Key>();
        for (NicoliveIndex index2 : indexes2) {
            indexKeys2.add(index2.getNicoliveKey());
        }
        assertThat(indexKeys2, hasItems(testDataKeys.toArray(new Key[0])));

        List<NicoliveIndex> indexes3 =
                Datastore.query(ni).filter(ni.keyword.equal("文")).asList();
        assertThat(indexes3, is(notNullValue()));
        List<Key> indexKeys3 = new LinkedList<Key>();
        for (NicoliveIndex index3 : indexes3) {
            indexKeys3.add(index3.getNicoliveKey());
        }
        assertThat(indexKeys3, hasItems(testDataKeys.toArray(new Key[0])));
    }
}
