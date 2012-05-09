package org.ryu22e.nico2cal.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Google APIキーを取得するユーティリティクラス。
 * @author ryu22e
 *
 */
public final class GoogleApiKeyUtil {
    /**
     * 
     */
    private static final String BUNDLE_NAME = "private/googleapi";

    /**
     * 
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
        .getBundle(BUNDLE_NAME);

    private GoogleApiKeyUtil() {
    }

    private static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Client IDを取得する。
     * @return Client ID
     */
    public static String getClientId() {
        return getString("clientId");
    }

    /**
     * Client secretを取得する。
     * @return Client secret
     */
    public static String getClientSecret() {
        return getString("clientSecret");
    }
}
