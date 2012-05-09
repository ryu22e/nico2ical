/**
 * 
 */
package org.ryu22e.nico2cal.filter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

/**
 * @author ryu22e
 *
 */
public final class CsrfTokenCookieFilterTest extends ControllerTestCase {

    /**
     * 
     */
    private CsrfTokenCookieFilter filter = new CsrfTokenCookieFilter();

    /**
     * @throws Exception
     */
    @Test
    public void CookieにCSRF対策用トークンが設定されている() throws Exception {
        assertThat(filter, is(notNullValue()));

        filter.doFilter(tester.request, tester.response, tester.filterChain);

        String cookieCsrfToken = null;
        for (Cookie cookie : tester.response.getCookies()) {
            if (cookie.getName().equals("csrftoken")) {
                cookieCsrfToken = cookie.getValue();
            }
        }
        assertThat(cookieCsrfToken, is(tester.request.getSession().getId()));
    }
}
