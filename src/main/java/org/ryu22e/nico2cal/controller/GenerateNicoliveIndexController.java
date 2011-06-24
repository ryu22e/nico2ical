package org.ryu22e.nico2cal.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Errors;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;

/**
 * {@link Nicolive}から全文検索用インデックスを作成するコントローラー。
 * @author ryu22e
 *
 */
public final class GenerateNicoliveIndexController extends Controller {

    /**
     * 
     */
    private static final Logger LOGGER = Logger
        .getLogger(GenerateNicoliveIndexController.class.getName());

    /**
     * 
     */
    private static final int UNAUTHORIZED = 401;

    /**
     * @see NicoliveService
     */
    private NicoliveService nicoliveService = new NicoliveService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        LOGGER.info("BEGIN: " + this.getClass().getName());

        Validators v = new Validators(request);
        v.add("keys[]", v.required());
        if (!v.validate()) {
            response.setStatus(UNAUTHORIZED);
            if (LOGGER.isLoggable(Level.WARNING)) {
                Errors errors = v.getErrors();
                for (int i = 0; i < errors.size(); i++) {
                    LOGGER.warning(errors.get(i));
                }
            }
        } else {
            String[] keysString = request.getParameterValues("keys[]");
            for (String key : keysString) {
                Nicolive nicolive =
                        nicoliveService.find(Datastore.stringToKey(key));
                if (nicolive != null) {
                    nicoliveService.createIndex(nicolive);
                }
            }
        }

        LOGGER.info("END: " + this.getClass().getName());
        return null;
    }
}
