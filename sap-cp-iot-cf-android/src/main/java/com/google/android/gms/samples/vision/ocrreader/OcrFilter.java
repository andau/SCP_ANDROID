package com.google.android.gms.samples.vision.ocrreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrFilter {
    public static String filter(String textvalue, String prefix) {
        Pattern pattern = Pattern.compile(prefix + "\\s*(.*)\\s*");
        Matcher matcher = pattern.matcher(textvalue);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        else return "";
    }
}
