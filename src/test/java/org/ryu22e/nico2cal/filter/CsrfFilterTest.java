/**
 * 
 */
package org.ryu22e.nico2cal.filter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.ryu22e.nico2cal.exception.CsrfException;
import org.slim3.tester.ControllerTestCase;

/**
 * @author ryu22e
 *
 */
public final class CsrfFilterTest extends ControllerTestCase {

    /**
     * 
     */
    private CsrfFilter filter = new CsrfFilter();

    /**
     * @throws Exception
     */
    @Test(expected = CsrfException.class)
    public void POSTメソッド以外でアクセスする() throws Exception {
        assertThat(filter, is(notNullValue()));

        filter.doFilter(tester.request, tester.response, tester.filterChain);
    }

    /**
     * @throws Exception
     */
    @Test(expected = CsrfException.class)
    public void CSRFトークンの検査_不合格の場合() throws Exception {
        assertThat(filter, is(notNullValue()));

        tester.request.setMethod("POST");
        filter.doFilter(tester.request, tester.response, tester.filterChain);
    }

    /**
     * @throws Exception
     */
    @Test
    public void CSRFトークンの検査_合格の場合() throws Exception {
        assertThat(filter, is(notNullValue()));

        String csrfToken = tester.request.getSession().getId();
        tester.request.setParameter("csrftoken", csrfToken);
        tester.request.setMethod("POST");
        filter.doFilter(tester.request, tester.response, tester.filterChain);
    }

}
