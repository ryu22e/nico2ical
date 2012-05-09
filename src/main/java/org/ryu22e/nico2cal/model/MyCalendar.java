package org.ryu22e.nico2cal.model;

import java.io.Serializable;
import java.util.List;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

/**
 * @author ryu22e
 *
 */
@Model(schemaVersion = 1)
public final class MyCalendar implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @Attribute(primaryKey = true)
    private Key key;

    /**
     * 
     */
    @Attribute(version = true)
    private Long version;

    /**
     * 
     */
    private String calendarId;

    /**
     * 
     */
    private User user;

    /**
     * 
     */
    private boolean notifyErrorMail;

    /**
     * 
     */
    private List<String> keywords;

    /**
     * 
     */
    private boolean disabled;

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @return the calendarId
     */
    public String getCalendarId() {
        return calendarId;
    }

    /**
     * @param calendarId the calendarId to set
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the notifyErrorMail
     */
    public boolean isNotifyErrorMail() {
        return notifyErrorMail;
    }

    /**
     * @param notifyErrorMail the notifyErrorMail to set
     */
    public void setNotifyErrorMail(boolean notifyErrorMail) {
        this.notifyErrorMail = notifyErrorMail;
    }

    /**
     * @return the keywords
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    /*
     * (non-Javadoc) {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MyCalendar other = (MyCalendar) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
}
