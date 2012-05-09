package org.ryu22e.nico2cal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryu22e.nico2cal.service.CalendarService;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;

/**
 * @author ryu22e
 *
 */
public final class ConnectGoogleApi extends
        AbstractAppEngineAuthorizationCodeServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private CalendarService calendarService = new CalendarService();

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // do stuff
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return calendarService.createNewGoogleCalendarApiFlow();
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    protected String getRedirectUri(HttpServletRequest req)
            throws ServletException, IOException {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath("/oauth2callback");
        return url.build();
    }

}
