package org.ryu22e.nico2cal.rome.module;

import com.sun.syndication.feed.module.ModuleImpl;

/**
 * ニコニコ生放送RSS用のRomeモジュール。
 * @author ryu22e
 *
 */
public final class NicoliveModule extends ModuleImpl {
    /**
     * 
     */
    private static final long serialVersionUID = 1930411314660770745L;

    /**
     * URI.
     */
    public static final String URI = "http://live.nicovideo.jp/";

    /**
     * 
     */
    private String openTime;

    /**
     * 
     */
    private String startTime;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String premiumOnly;

    /**
     * コンストラクタ。
     */
    public NicoliveModule() {
        super(NicoliveModule.class, URI);
    }

    /**
     * コンストラクタ。
     * @param beanClass beanClass
     * @param uri URI
     */
    protected NicoliveModule(Class<?> beanClass, String uri) {
        super(beanClass, uri);
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    public Class<?> getInterface() {
        return NicoliveModule.class;
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    public void copyFrom(Object obj) {
        NicoliveModule module = (NicoliveModule) obj;
        module.openTime = this.openTime;
        module.startTime = this.startTime;
        module.type = this.type;
        module.password = this.password;
        module.premiumOnly = this.premiumOnly;
    }

    /**
     * @return the open_time
     */
    public String getOpenTime() {
        return openTime;
    }

    /**
     * @param openTime the open_time to set
     */
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    /**
     * @return the start_time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the start_time to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the premium_only
     */
    public String getPremiumOnly() {
        return premiumOnly;
    }

    /**
     * @param premiumOnly the premium_only to set
     */
    public void setPremiumOnly(String premiumOnly) {
        this.premiumOnly = premiumOnly;
    }

}
