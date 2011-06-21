package org.ryu22e.nico2cal.controller;

import org.ryu22e.nico2cal.model.Nicolive;
import org.ryu22e.nico2cal.service.NicoliveService;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
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
        Validators v = new Validators(request);
        v.add("keys[]", v.required());
        if (!v.validate()) {
            response.setStatus(UNAUTHORIZED);
            return null;
        }
        String[] keysString = request.getParameterValues("keys[]");
        for (String key : keysString) {
            Nicolive nicolive =
                    nicoliveService.find(Datastore.stringToKey(key));
            if (nicolive != null) {
                nicoliveService.createIndex(nicolive);
            }
        }

        return null;
    }
}
