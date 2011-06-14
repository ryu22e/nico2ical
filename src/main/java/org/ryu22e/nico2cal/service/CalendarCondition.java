/**
 * 
 */
package org.ryu22e.nico2cal.service;

import java.util.Date;

/**
 * {@link CalendarService#getCalendar(CalendarCondition)}の検索条件を格納するクラス。
 * @author ryu22e
 *
 */
public final class CalendarCondition {
    /**
     * 
     */
    private Date startDate;

    /**
     * 
     */
    private String keyword;

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
