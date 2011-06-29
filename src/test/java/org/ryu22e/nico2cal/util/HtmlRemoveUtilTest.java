package org.ryu22e.nico2cal.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class HtmlRemoveUtilTest {
    /**
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void コンストラクタのテスト() throws Exception {
        Constructor<?>[] constructors = HtmlRemoveUtil.class.getConstructors();
        assertThat(constructors.length, is(1));
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void 文字列中のHTMLタグを除去する_パラメータがnull() throws Exception {

        assertThat(HtmlRemoveUtil.removeHtml(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void 文字列中のHTMLタグを除去する() throws Exception {
        String result =
                HtmlRemoveUtil
                    .removeHtml("<a href=\"http://ryu22e.org/\">ryu22.org</a><h1>test</h1>これはテストです。<br /> ほげほげ");
        assertThat(result, is("ryu22.orgtestこれはテストです。 ほげほげ"));
    }

}
