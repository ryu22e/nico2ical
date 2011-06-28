package org.ryu22e.nico2cal.controller;

import java.util.List;
import java.util.logging.Logger;

import org.ryu22e.nico2cal.service.NicoliveRssService;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * ニコニコ生放送RSSからデータストアのデータを生成するコントローラー。
 * @author ryu22e
 *
 */
public final class GenerateNicoliveController extends Controller {

    /**
     * 
     */
    private static final Logger LOGGER = Logger
        .getLogger(GenerateNicoliveController.class.getName());

    /**
     * 
     */
    private static final int SUBLIST_SIZE = 10;

    /**
     * @see NicoliveRssService
     */
    private NicoliveRssService nicoliveRssService = new NicoliveRssService();

    /**
     * @see NicoliveService
     */
    private NicoliveService nicoliveService = new NicoliveService();

    /**
     * 全文検索用インデックスを作成するTaskQueueを追加する。
     * @param keys NicoliveのキーのList
     */
    private void addTaskQueue(List<Key> keys) {
        if (0 < keys.size()) {
            TaskOptions options =
                    TaskOptions.Builder
                        .withUrl("/GenerateNicoliveIndex")
                        .method(Method.POST);
            for (Key key : keys) {
                options = options.param("keys[]", Datastore.keyToString(key));
            }

            QueueFactory.getQueue("generate-nicoliveindex").add(options);
        }
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        LOGGER.info("BEGIN: " + this.getClass().getName());

        SyndFeed feed = nicoliveRssService.getFeed();
        List<Key> keys = nicoliveService.put(feed);
        if (0 < keys.size()) {
            LOGGER.info("Generated " + keys.size() + " entities.");
            // 全文検索用インデックスを作成する。
            if (SUBLIST_SIZE < keys.size()) {
                int fromIndex = 0;
                // 全てのデータを一つのTaskQueueに渡すと時間がかかりすぎるので、幾つかに分割する。
                while (fromIndex < keys.size()) {
                    int toIndex = fromIndex + SUBLIST_SIZE;
                    if (toIndex < keys.size()) {
                        toIndex = keys.size() - fromIndex;
                    }
                    List<Key> subList = keys.subList(fromIndex, toIndex);
                    addTaskQueue(subList);

                    fromIndex = toIndex + 1;
                }
            } else {
                addTaskQueue(keys);
            }
        }

        LOGGER.info("END: " + this.getClass().getName());
        return null;
    }
}
