package com.castify.tv.utils;

import com.castify.tv.models.AdModel;
import com.castify.tv.models.FeedInfo;
import com.castify.tv.models.Graphics;
import com.castify.tv.models.Info;
import com.castify.tv.models.MenuBar;
import com.castify.tv.models.MenuPageItem;
import com.castify.tv.models.PageModel;
import com.castify.tv.models.PlayList;
import com.castify.tv.models.RawFeedInfo;
import com.castify.tv.models.VideoCard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GlobalVars {

    public static final String CHANNEL_URL = "[FEED_URL]";

    /**
     * Models
     */
    public static FeedInfo feedInfo;
    public static RawFeedInfo rawFeedInfo;
    public static MenuBar menuBar;
    public static Info info;
    public static AdModel adModel;
    public static Graphics graphics;
    public static ArrayList<PageModel> pages = new ArrayList<>();
    public static List<VideoCard> allVideos = new ArrayList<>();
    public static ArrayList<PlayList> playList = new ArrayList<>();
    public static ArrayList<PlayList> typeContainers = new ArrayList<>();
    public static ArrayList<PlayList> typeCategories = new ArrayList<>();
    public static ArrayList<PlayList> typeContents = new ArrayList<>();
    public static ArrayList<PlayList> allVideoCategories = new ArrayList<>();
    public static Map<PlayList, ArrayList<VideoCard>> videos = new LinkedHashMap<>(); // Category and its videos
    public static Map<PageModel, ArrayList<MenuPageItem>> menus = new LinkedHashMap<>();

//            (
//            (menuItem, t1) -> {
//                return menuItem.getPage_type_id() - t1.getPage_type_id(); // Ascending.
//            });

    /**
     * Json Main Keys
     */
    public static final String MENU_KEY = "menu";
    public static final String CONTENT_key = "content";
    public static final String INFO_key = "Info";
    public static final String AD_Key = "Ads";
    public static final String GRAPHIC_key = "graphic";
    public static final String PlaylistKey = "playlists";
    public static final String BEACON_key = "beacon";


    /**
     * Bundle Tags
     */
    public static final String selectedVideoToPlayTag = "video_to_play";
    public static final String currentPageModelTag = "pageModel";
    public static final String currentPageID= "currentPageID";
    public static final String searchKeyWord = "searchKeyWord";
    public static final String categoriesTag = "categories";
    public static final String ERR0R_CODE = "errorCode";
    public static final String QUIT_MSG = "quitmsg";

    public static String BEACON_url;


    /**
     * Booleans
     */
    public static boolean hasHomeScreen = false;
    public static boolean showMenu = false;
    public static boolean NO_LIVE_VIDEO = true;


    /**
     * Dialog Constants
     */
    public static final String ARG_TITLE_RES = "arg_title";
    public static final String ARG_POSITIVE_RES = "arg_yes";
    public static final String ARG_NEGATIVE_RES = "arg_no";
    public static final String ARG_ICON_RES = "arg_icon";
    public static final String ARG_DESC_RES = "arg_desc";
    public static final String REQUEST_CODE = "rcode";


    /**
     * Macros
     */
    public static String USER_IP = null;
    public static String DEVICE_INFO = null;
    public static String USER_AGENT = null;
    public static String DEVICE_ID = null;
    public static String COUNTRY = null;
    public static String LANGUAGE = null;
    public static String WIDTH = null;
    public static String HEIGHT = null;
    public static String MAC_ADDRESS = null;
    public static String IDFA = null;
    public static String IFA_TYPE = null;
    public static String ADS_TRACKING = null;
    public static String APP_VERSION = "VVERSIONNAME";


    /**
     * Beacon Events
     */
    public static final String PLAY_VIDEO = "21";
    public static final String PAUSE_VIDEO = "22";
    public static final String CAROUSEL_CLICK = "23";
    public static final String APP_OPEN = "24";
    public static final String EXIT_APP = "25";
    public static final String AD_BREAK = "31";
    public static final String AD_REQUEST = "26";
    public static final String AD_IMPRESSION = "27";
    public static final String AD_COMPLETE = "28";
    public static final String START_VIDEO = "29";
    public static final String ERROR = "30";
    public static final String USER_CLICK = "32";
    public static final String AD_ERROR = "33";



    /**
     * Page IDs -> Now Dynamic
     */
    public static String homePageId = "1";
    public static String searchPageId = "2";
    public static String aboutUsPageId = "3";
    public static String ourStorePageId = "4";
    public static String socialMediaPageId = "5";
    public static String moreChannelsPageId = "6";
    public static String contactUsPageId = "7";
    public static String videoPage = "0";


    /**
     * Category Types
     */
    public static String categoryTypeStory = "story";
    public static String categoryTypeParalaxBox = "paralaxBox";

    /**
     * Dimens
     */
    public static float windowAlignmentOffsetPercent = 19f;
    public static float playerWindowAlignmentOffsetPercent = 16.5f;
    public static int headerPaddingStart = 50;

}