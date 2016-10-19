package com.xuemiao.lib;

/**
 * Created by root on 16-10-18.
 */
public class FPComDll {
    static {
        System.loadLibrary("FPCom");
    }

    private native boolean Process(String a, String b);

    public boolean Process_java(String a, String b) {
        return this.Process(a, b);
    }

}
