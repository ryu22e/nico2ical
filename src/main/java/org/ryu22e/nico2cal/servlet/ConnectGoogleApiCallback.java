package org.ryu22e.nico2cal.servlet;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ryu22e.nico2cal.model.MyCalendar;
import org.ryu22e.nico2cal.service.CalendarService;
import org.ryu22e.nico2cal.util.GoogleApiKeyUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.extensions.appengine.http.urlfetch.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;

/**
 * @author ryu22e
 *
 */
public final class ConnectGoogleApiCallback extends
        AbstractAppEngineAuthorizationCodeCallbackServlet {

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
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,
            Credential credential) throws ServletException, IOException {
        MyCalendar myCalendar = calendarService.getCurrentMyCalendar();
        if (myCalendar != null) {
            myCalendar.setDisabled(false);
            calendarService.putMyCalendar(myCalendar);
        }
        resp.sendRedirect("/myCalendar");
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    protected void onError(HttpServletRequest req, HttpServletResponse resp,
            AuthorizationCodeResponseUrl errorResponse)
            throws ServletException, IOException {
        resp.sendRedirect("/myCalendar");
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

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
            new UrlFetchTransport(),
            new JacksonFactory(),
            GoogleApiKeyUtil.getClientId(),
            GoogleApiKeyUtil.getClientSecret(),
            Collections.singleton(CalendarScopes.CALENDAR)).setCredentialStore(
            new AppEngineCredentialStore()).build();
    }

}
