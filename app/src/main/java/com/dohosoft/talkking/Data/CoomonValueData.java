package com.dohosoft.talkking.Data;

/**
 * Created by HwanWoong on 2018-01-14.
 */

public class CoomonValueData  {
    public static final long UNIQ_FANCOUNT = 10000000;
    public static final int FIRST_LOAD_MAIN_COUNT = 60;
    public static final int LOAD_MAIN_COUNT = 60;

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
    public static final String BOARD_DATE_FORMAT = "yy-MM-dd";
    public static final String MAIL_DATE_FORMAT = "yy-MM-dd HH:mm";

    public static final String DATE_MONTH_DAY = "MM/dd\n";
    public static final String DATE_HOURS_MIN = "HH:mm";
    public static final String CHAT_DATE_TIME_COLOR_MONTH_DAY = "#9e9e9e";
    public static final String CHAT_DATE_TIME_COLOR_HOURS_MIN = "#9e9e9e";

    public static final long UTC_OFFSET = 32400000;
    public static final long DAY_MILLI_SECONDS = 86400000;
    public static final long HOUR_MILLI_SECONDS = 3600000;
    public static final long MIN_MILLI_SECONDS = 60000;
    public static final long SEC_MILLI_SECONDS = 1000;

    public static final int OPEN_BOX_COST = 7;

    public static final int TEXT_MAX_LENGTH_BOARD = 200;
    public static final int TEXT_MAX_LENGTH_CHAT = 100;
    public static final int TEXT_MAX_LENGTH_MAIL = 20;
    public static final int TEXT_MAX_LENGTH_SEND_HONEY = 30;
    public static final int TEXT_MAX_LENGTH_NICKNAME = 8;
    public static final int TEXT_MAX_LENGTH_MEMO = 100;

    public static final int BOARD_WRITE_TIME_MIN = 10;

    public static final Double REF_LAT = 38.910042;
    public static final Double REF_LON = 125.848755;

    public static final Double DEF_LAT = 37.475610;
    public static final Double DEF_LON = 127.007094;

    public static final int TEXTCOLOR_MAN = 0xff5fb9e6;
    public static final int TEXTCOLOR_WOMAN = 0xffffafff;

    public static boolean OFFAPP = true;

    public static int CHANGE_NICK_NAME_COST = 50;

    public static boolean bMySet_DownUrl = false;
    public static  boolean bMySet_Image = false;
    public static  boolean bMySet_BoardLoad = false;
    public static  boolean bMySet_Card = false;
    public static  boolean bMySet_Fan = false;
    public static  boolean bMySet_Send =false;
    public static  boolean bMySet = true;

    public static  boolean bMyLoc = true;
    public static  boolean bSetNear = false;
    public static  boolean bSetNew = false;
    public static  boolean bSetRich =false;
    public static  boolean bSetRecv = false;

    private static CoomonValueData _Instance;

    public static CoomonValueData getInstance() {
        if (_Instance == null)
            _Instance = new CoomonValueData();

        return _Instance;
    }

    private CoomonValueData() {

    }

    public long BoardLoadingDate = -1; // 게시판글 몇일전꺼는 안불러오게 하는 변수 -1은 무제한
    public boolean BoardPastLoading = true; // 과거 게시글을 불러 올까?
    public String DownUrl;
    public String ImgUrl;

    public String SignUp = null;
    public String Login = null;
    public String Notice = null;

}
