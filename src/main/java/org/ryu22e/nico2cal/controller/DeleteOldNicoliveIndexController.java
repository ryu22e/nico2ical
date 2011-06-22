package org.ryu22e.nico2cal.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/**
 * 古い全文検索用インデックスをを削除するコントローラー。
 * @author ryu22e
 *
 */
public final class DeleteOldNicoliveIndexController extends Controller {

    /**
     * 
     */
    private static final Logger LOGGER = Logger
        .getLogger(DeleteOldNicoliveIndexController.class.getName());

    /**
     * 
     */
    private static final int MINUS_DAYS = 31;

    /**
     * 
     */
    private NicoliveService nicoliveService = new NicoliveService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        LOGGER.info("BEGIN: " + this.getClass().getName());

        DateTime datetime = new DateTime();
        DateTime from = datetime.minusDays(MINUS_DAYS);
        nicoliveService.deleteOldIndex(from.toDate());
        if (LOGGER.isLoggable(Level.INFO)) {
            DateTimeFormatter df =
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            LOGGER.info("NicoliveIndex(before " + from.toString(df)
                    + ") was deleted.");
        }

        LOGGER.info("END: " + this.getClass().getName());
        return null;
    }
}
