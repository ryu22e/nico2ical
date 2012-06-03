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
public final class LoginControllerTest extends ControllerTestCase {
    /**
     * @throws Exception
     */
    @Test
    public void ログイン画面に遷移する_パラメータが正常な値() throws Exception {
        // ログイン用URLを取得できる。
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.start("/Login");
        LoginController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.response.getStatus(), is(302));
        assertThat(tester.response.getRedirectPath(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void セッション変数に値が設定されている状態でログインするとセッション変数の値がクリアされる() throws Exception {
        Assume.assumeTrue(AppEngineUtil.isServer() == false);
        tester.sessionScope("test", "this is test.");
        tester.start("/Login");
        LoginController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.response.getStatus(), is(302));
        assertThat(tester.response.getRedirectPath(), is(notNullValue()));
        assertThat(tester.sessionScope("test"), is(nullValue()));
    }
}
