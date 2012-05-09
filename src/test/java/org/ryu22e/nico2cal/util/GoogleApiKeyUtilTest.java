package org.ryu22e.nico2cal.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ryu22e
 *
 */
public final class GoogleApiKeyUtilTest {

    /**
     * 
     */
    @Test
    public void プロパティファイルからclientIdを取得する() {
        String clientId = GoogleApiKeyUtil.getClientId();
        assertThat(clientId, not(nullValue()));
        assertThat(clientId, not("!clientId!"));
    }

    /**
     * 
     */
    @Test
    public void プロパティファイルからclientSecretを取得する() {
        String clientSecret = GoogleApiKeyUtil.getClientSecret();
        assertThat(clientSecret, not(nullValue()));
        assertThat(clientSecret, not("!clientSecret!"));
    }
}
