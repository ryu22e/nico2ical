/**
 * 
 */
package org.ryu22e.nico2cal.controller;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author ryu22e
 *
 */
public enum StartWeek {
    /**
     * １週間前。
     */
    WEEK_1(1),
    /**
     * ２週間前。
     */
    WEEK_2(2),
    /**
     * ３週間前。
     */
    WEEK_3(3),
    /**
     * ４週間前。
     */
    WEEK_4(4);

    /**
     * 
     */
    private int value;

    private StartWeek(int value) {
        this.value = value;
    }

    /**
     * {@link Date}オブジェクトを取得する。
     * @return {@link Date}オブジェクト
     */
    public Date toDate() {
        DateTime datetime = new DateTime();
        return datetime.minusWeeks(value).toDate();
    }

    /**
     * @return value
     */
    private int getValue() {
        return value;
    }

    /**
     * @param value value
     * @return StartWeek
     * @throws ParseException
     */
    public static StartWeek parse(int value) throws ParseException {
        if (WEEK_1.getValue() == value) {
            return WEEK_1;
        } else if (WEEK_2.getValue() == value) {
            return WEEK_2;
        } else if (WEEK_3.getValue() == value) {
            return WEEK_3;
        } else if (WEEK_4.getValue() == value) {
            return WEEK_4;
        } else {
            throw new ParseException("Invalid value.", value);
        }
    }
}
