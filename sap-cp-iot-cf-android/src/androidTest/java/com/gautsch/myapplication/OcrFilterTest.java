package com.gautsch.myapplication;

import com.google.android.gms.samples.vision.ocrreader.OcrFilter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OcrFilterTest {
    @Test
    public void filterForSerialnumberTest() throws Exception {
        assertEquals("", OcrFilter.filter("", ""));
        assertEquals("123", OcrFilter.filter("SERIAL NO. 123", "SERIAL NO."));
        assertEquals("", OcrFilter.filter("SERIAL 123", "SERIAL NO."));

    }
}