package com.xuemiao.utils;

import java.text.DecimalFormat;

/**
 * Created by dzj on 10/3/2016.
 */
public class PrecisionUtils {
    public static String transferToSecondDecimal(double d) {
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(d);
    }
}
