/**
 * 
 */
package org.ryu22e.nico2cal.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author ryu22e
 *
 */
public final class StartWeekTest {
    /**
     * @throws Exception
     */
    @Test
    public void startWeekからDateに変換する() throws Exception {
        DateTime now = new DateTime();

        // １週間前のDateオブジェクトを取得できる。
        Date result1 = StartWeek.WEEK_1.toDate();
        assertThat(result1, is(notNullValue()));
        DateTime expected1 = now.minusWeeks(1);
        DateTime week1 = new DateTime(result1);
        assertThat(week1.getYear(), is(expected1.getYear()));
        assertThat(week1.getMonthOfYear(), is(expected1.getMonthOfYear()));
        assertThat(week1.getDayOfMonth(), is(expected1.getDayOfMonth()));

        // ２週間前のDateオブジェクトを取得できる。
        Date result2 = StartWeek.WEEK_2.toDate();
        assertThat(result2, is(notNullValue()));
        DateTime expected2 = now.minusWeeks(2);
        DateTime week2 = new DateTime(result2);
        assertThat(week2.getYear(), is(expected2.getYear()));
        assertThat(week2.getMonthOfYear(), is(expected2.getMonthOfYear()));
        assertThat(week2.getDayOfMonth(), is(expected2.getDayOfMonth()));

        // ３週間前のDateオブジェクトを取得できる。
        Date result3 = StartWeek.WEEK_3.toDate();
        assertThat(result3, is(notNullValue()));
        DateTime expected3 = now.minusWeeks(3);
        DateTime week3 = new DateTime(result3);
        assertThat(week3.getYear(), is(expected3.getYear()));
        assertThat(week3.getMonthOfYear(), is(expected3.getMonthOfYear()));
        assertThat(week3.getDayOfMonth(), is(expected3.getDayOfMonth()));

        // ４週間前のDateオブジェクトを取得できる。
        Date result4 = StartWeek.WEEK_4.toDate();
        assertThat(result4, is(notNullValue()));
        DateTime expected4 = now.minusWeeks(4);
        DateTime week4 = new DateTime(result4);
        assertThat(week4.getYear(), is(expected4.getYear()));
        assertThat(week4.getMonthOfYear(), is(expected4.getMonthOfYear()));
        assertThat(week4.getDayOfMonth(), is(expected4.getDayOfMonth()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void intからStartWeekに変換する() throws Exception {
        assertThat(StartWeek.parse(1), is(StartWeek.WEEK_1));
        assertThat(StartWeek.parse(2), is(StartWeek.WEEK_2));
        assertThat(StartWeek.parse(3), is(StartWeek.WEEK_3));
        assertThat(StartWeek.parse(4), is(StartWeek.WEEK_4));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ParseException.class)
    public void intからStartWeekに変換する_intが不正な値() throws Exception {
        StartWeek.parse(0);
    }
}
