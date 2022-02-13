package com.hncd.carcontrol.base;

public class Constant {
    public static final boolean IS_DEBUG = true;//是否测试==打印测试日志
    public static  String BASE_URL = "";

    /*传递的zxingconfing*/
    /*与zxinglibrary 的Constant冲突  所以弄过来一样的*/
    public static final int DECODE = 1;
    public static final int DECODE_FAILED = 2;
    public static final int DECODE_SUCCEEDED = 3;
    public static final int LAUNCH_PRODUCT_QUERY = 4;
    public static final int QUIT = 5;
    public static final int RESTART_PREVIEW = 6;
    public static final int RETURN_SCAN_RESULT = 7;
    public static final int FLASH_OPEN = 8;
    public static final int FLASH_CLOSE = 9;
    public static final int REQUEST_IMAGE = 10;
    public static final String CODED_CONTENT = "codedContent";
    public static final String CODED_BITMAP = "codedBitmap";
    public static final String INTENT_ZXING_CONFIG = "zxingConfig";
}
