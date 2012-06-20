package org.ryu22e.nico2cal.controller.help;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

/**
 * ヘルプを表示させるコントローラー。
 * @author ryu22e
 *
 */
public final class IndexController extends Controller {

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        return forward("index.jsp");
    }
}
