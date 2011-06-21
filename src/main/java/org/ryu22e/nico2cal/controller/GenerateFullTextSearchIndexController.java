package org.ryu22e.nico2cal.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.service.NicoliveCondition;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * 全文検索用インデックスを作成するコントローラー。
 * @author ryu22e
 *
 */
public final class GenerateFullTextSearchIndexController extends Controller {

    /**
     * 
     */
    private static final int MINUS_DAYS = 31;

    /**
     * 
     */
    private static final int SUBLIST_SIZE = 10;

    /**
     * 
     */
    private NicoliveService nicoliveService = new NicoliveService();

    /**
     * 全文検索用インデックスを作成するTaskQueueを追加する。
     * @param nicolives NicoliveのList
     */
    private void addTaskQueue(List<Nicolive> nicolives) {
        if (0 < nicolives.size()) {
            TaskOptions options =
                    TaskOptions.Builder
                        .withUrl("/GenerateNicoliveIndex")
                        .method(Method.POST);
            for (Nicolive nicolive : nicolives) {
                options =
                        options.param(
                            "keys[]",
                            Datastore.keyToString(nicolive.getKey()));
            }

            QueueFactory.getQueue("generate-nicoliveindex").add(options);
        }
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        DateTime datetime = new DateTime();

        NicoliveCondition condition = new NicoliveCondition();
        condition.setStartDate(datetime.minusDays(MINUS_DAYS).toDate());
        List<Nicolive> nicolives = nicoliveService.findList(condition);
        if (0 < nicolives.size()) {
            int fromIndex = 0;
            while (fromIndex < nicolives.size()) {
                int toIndex = fromIndex + SUBLIST_SIZE;
                if (toIndex < nicolives.size()) {
                    toIndex = nicolives.size() - fromIndex;
                }
                List<Nicolive> subList = nicolives.subList(fromIndex, toIndex);
                addTaskQueue(subList);

                fromIndex = toIndex + 1;
            }
        }

        return null;
    }
}
