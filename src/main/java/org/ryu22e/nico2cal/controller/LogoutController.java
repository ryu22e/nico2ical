package org.ryu22e.nico2cal.controller;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * ログアウトするためのコントローラー。
 * @author ryu22e
 *
 */
public final class LogoutController extends Controller {

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public Navigation run() throws Exception {
        UserService userService = UserServiceFactory.getUserService();
        String destinationURL = request.getParameter("destinationURL");
        if (!userService.isUserLoggedIn()
                || (destinationURL == null || !destinationURL.startsWith("/"))) {
            response.setStatus(HttpStatusConstants.FORBIDDEN);
            return null;
        } else {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return redirect(userService.createLogoutURL(destinationURL));
        }
    }
}
