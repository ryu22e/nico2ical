/**
 * 
 */
package org.ryu22e.nico2cal.service;

import java.util.Date;
import java.util.List;

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
    private List<String> keywords;

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

}
