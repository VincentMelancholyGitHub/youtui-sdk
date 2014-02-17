package cn.bidaround.youtui.social.activity;

/**
 * Description: created by qyj on January 7, 2014
 */
public class YoutuiConstants {

	// 文件保存路径
	public static final String FILE_SAVE_PATH = "/youtui/";

	// 新浪微博的参数
	public static final String SINA_WEIBO_APP_ID = "2502314449";
	public static final String SINA_WEIBO_REDIRECT_URL = "http://yt.bidaround.cn/weiboBindResponse";
	public static final String SINA_WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	// 腾讯的参数(包括腾讯微博、QQ空间、定向分享)
	public static final String TENCENT_APP_ID = "100566655";
	public static final String TENCENT_SCOPE = "all";

	// 微信的参数
	public static final String WEIXIN_APP_ID = "wxbc9e6010cf85c3e4";
	public static final int WEIXIN_TIMELINE_SUPPORTED_VERSION = 0x21020001;// 微信4.2以上支持分享至朋友圈

	// 人人的参数
	public static final String RENREN_APP_ID = "244110";
	public static final String RENREN_API_KEY = "b1a80ac1aa694090bfb9aa3a590f2161";
	public static final String RENREN_SECRET_KEY = "506ccdbda36046d197801e79c4ebba23";
	public static final String RENREN_SCOPE = "read_user_blog read_user_photo read_user_status read_user_album read_user_comment read_user_share publish_blog publish_share send_notification photo_upload status_update create_album publish_comment publish_feed";

	// package name
	public static final String PACKAGE_NAME_SINA_WEIBO = "com.sina.weibo";
	public static final String PACKAGE_NAME_TENCENT_QQ = "com.tencent.mobileqq";
	public static final String PACKAGE_NAME_RENREN = "com.renren.mobile.android";
	public static final String PACKAGE_NAME_WEIXIN = "com.tencent.mm";

	// request code
	public static final int SINA_WEIBO_REQUEST_CODE = 100;
	public static final int TENCENT_WEIBO_REQUEST_CODE = 101;
	public static final int TENCENT_QZONE_REQUEST_CODE = 102;
	public static final int TENCENT_DIRECTIONAL_SHARE_REQUEST_CODE = 103;
	public static final int WEIXIN_SHARE_REQUEST_CODE = 104;
	public static final int RENREN_REQUEST_CODE = 105;

	// result
	public static final int RESULT_SUCCESSFUL = -1;
	public static final int RESULT_CANCEL = 0;
	public static final int RESULT_ERROR = 1;
	public static final int RESULT_SIGNATURE_ERROR = 2;
	public static final int APP_NOT_EXIST = 3;
}
