package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Assume;
import org.junit.Test;
import org.slim3.tester.ControllerTestCase;
import org.slim3.util.AppEngineUtil;

/**
 * @author ryu22e
 *
 */
public final class LogoutControllerTest extends ControllerTestCase {

    /** 
     * @throws Exception
     */
    @Test
    public void ログインした状態でログアウトする_パラメータ指定なし() throws Exception {
        // ログアウト用URLを取得できない。
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.param("sessionId", tester.request.getSession().getId());
        tester.environment.setEmail("dummy@gmail.com");
        tester.start("/Logout");
        LogoutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(403));
    }

    /**
     * @throws Exception
     */
    @Test
    public void ログインした状態でログアウトする_パラメータが正常な値() throws Exception {
        // ログアウト用URLを取得できる。
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.param("sessionId", tester.request.getSession().getId());
        tester.environment.setEmail("dummy@gmail.com");
        tester.param("destinationURL", "/dummy.html");
        tester.start("/Logout");
        LogoutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.response.getStatus(), is(302));
        assertThat(tester.response.getRedirectPath(), is(notNullValue()));
    }

    @Test
    public void ログインした状態でログアウトする_パラメータが不正な値() throws Exception {
        // ログアウト用URLを取得できない。
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.param("sessionId", tester.request.getSession().getId());
        tester.environment.setEmail("dummy@gmail.com");
        tester.param("destinationURL", "http://example.com/");
        tester.start("/Logout");
        LogoutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(403));
    }

    /**
     * @throws Exception
     */
    @Test
    public void ログインしていない状態でログアウトする() throws Exception {
        // ログアウト用URLを取得できない。
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.param("sessionId", tester.request.getSession().getId());
        tester.param("destinationURL", "/dummy.html");
        tester.start("/Logout");
        LogoutController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.response.getStatus(), is(403));
    }
}
