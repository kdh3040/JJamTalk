package com.dohosoft.talkking.Data;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by HwanWoong on 2018-01-14.
 */

public class CoomonValueData  {
    public static final long UNIQ_FANCOUNT = 10000000;
    public static final int HOTMEMBER_LOAD_MAIN_COUNT = 50;
    public static final int FIRST_LOAD_MAIN_COUNT = 30;
    public static final int LOAD_MAIN_COUNT = 15;

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
    public static final int DAILY_CONNECT_CHECK = 1440;

    public static final Double REF_LAT = 38.910042;
    public static final Double REF_LON = 125.848755;

    public static final Double DEF_LAT = 37.475610;
    public static final Double DEF_LON = 127.007094;

    public static final int TEXTCOLOR_MAN = 0xff5fb9e6;
    public static final int TEXTCOLOR_WOMAN = 0xffffafff;

    public static boolean OFFAPP = true;

    public static int CHANGE_NICK_NAME_COST = 500;
    public static int SEND_MSG_COST = 8;

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
    public static  boolean bSetRich = true;
    public static  boolean bSetRecv = false;
    public static  boolean bSetHot = false;


    public static boolean bRefreshSetHot = false;
    public static boolean bRefreshSetNear = false;
    public static boolean bRefreshSetNew = false;
    public static boolean bRefreshSetFan = true;
    public static boolean bRefreshSetRecv = false;


    public static boolean MOD_AddHotMember = false;
    public boolean MOD_Notification = false;


     public static String ADCOLONY_APP_ID = "appb1dde5c7745c4831ac";
     public static String ZONE_ID = "vz446c43de0bbc4d8191";
     public static String TAG = "AdColonyDemo";

    public static String VUNGLE_APP_ID = "5af2a4f90ad26636322d18e2";
    public static String VUNGLE_REFERENCE = "REWARD_2-3054297";


    public static int REWARD_NEW = 50;
    public static int REWARD_DAY = 30;

    public static int REWARD_UNITY = 1;
    public static int REWARD_ADMOB = 1;
    public static int REWARD_ADCOLONY = 1;
    public static int REWARD_VUNGLE = 1;

    //public String DATA_USERS = "Users";
    public String DATA_BOARD = "TestBoard";
    public String DATA_CHATDATA = "TestCharData";
    public String DATA_RECVHONEY = "TestRecvHoneyList";
    public String DATA_SETTING = "TestSetting";
    public String DATA_SIMPLEDATA = "TestSimpleData";
    public String DATA_GENDERLIST = "TestGenderList";
    public String DATA_BLOCKLIST = "TestBlockList";
    public String DATA_BLOCKEDLIST = "TestBlockedList";

    public String DATA_USERINDEX = "UserIdx";

    public String DATA_USERS_BACKUP = "TestBackup";
    public String DATA_USERS = "TestUsers";

    public String DATA_HOTMEMBER_BACKUP = "TestHotMemberBackup";
    public String DATA_HOTMEMBER = "TestHotMember";

    public static int SUBSTATUS_WEEK = 1;
    public static int SUBSTATUS_MONTH = 2;
    public static int SUBSTATUS_YEAR = 3;

    public static int SUBSTATUS_WEEK_COIN = 100;
    public static int SUBSTATUS_MONTH_COIN = 400;
    public static int SUBSTATUS_YEAR_COIN = 5000;

    public static int SUBDATE_WEEK = 60 * 24 * 7;
    public static int SUBDATE_MONTH = 60 * 24 * 30;
    public static int SUBDATE_YEAR = 60 * 24 * 365;

    public static AdRequest adRequest;

    private static CoomonValueData _Instance;

    public static CoomonValueData getInstance() {
        if (_Instance == null)
            _Instance = new CoomonValueData();

        return _Instance;
    }

    private CoomonValueData() {
        adRequest = new AdRequest.Builder().build();
    }

    public long BoardLoadingDate = -1; // 게시판글 몇일전꺼는 안불러오게 하는 변수 -1은 무제한
    public boolean BoardPastLoading = true; // 과거 게시글을 불러 올까?
    public String DownUrl;
    public String ImgUrl;

    public String SignUp = null;
    public String Login = null;
    public String Notice = null;

}
