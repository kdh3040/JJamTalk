package com.hodo.talkking.Data;

import com.hodo.talkking.Util.CommonFunc;

/**
 * Created by HwanWoong on 2018-01-14.
 */

public class CoomonValueData  {
    public static final long UNIQ_FANCOUNT = 10000000;
    public static final int FIRST_LOAD_MAIN_COUNT = 30;
    public static final int LOAD_MAIN_COUNT = 8;

    public static final int FIRST_LOAD_BOARD_COUNT = 15;
    public static final int LOAD_BOARD_COUNT = 10;
    public static final int REPORT_BOARD_DELETE = 10;

    public static final int MAIN_ACTIVITY_HOME = 0;
    public static final int MAIN_ACTIVITY_CARD = 1;
    public static final int MAIN_ACTIVITY_CHAT = 2;
    public static final int MAIN_ACTIVITY_FAN = 3;
    public static final int MAIN_ACTIVITY_BOARD = 4;

    public static final String GENDER_MAN = "남자";
    public static final String GENDER_WOMAN = "여자";

    public static final String DATE_FORMAT = "yyyyMMddHHmm";
    public static final String BOARD_TODAY_DATE_FORMAT = "HH:mm";
    public static final String BOARD_DATE_FORMAT = "yyyy-MM-dd";

    public static final long UTC_OFFSET = 32400000;
    public static final long DAY_MILLI_SECONDS = 86400000;
    public static final long HOUR_MILLI_SECONDS = 3600000;
    public static final long MIN_MILLI_SECONDS = 60000;
    public static final long SEC_MILLI_SECONDS = 1000;

    public static final int OPEN_BOX_COST = 7;

    public static final int TEXT_MAX_LENGTH_BOARD = 100;
    public static final int TEXT_MAX_LENGTH_CHAT = 100;
    public static final int TEXT_MAX_LENGTH_MAIL = 20;
    public static final int TEXT_MAX_LENGTH_SEND_HONEY = 30;
    public static final int TEXT_MAX_LENGTH_NICKNAME = 8;
    public static final int TEXT_MAX_LENGTH_MEMO = 100;

    public static final int BOARD_WRITE_TIME_MIN = 10;

    public static final Double REF_LAT = 38.910042;
    public static final Double REF_LON = 125.848755;

    public static boolean OFFAPP = true;
}
