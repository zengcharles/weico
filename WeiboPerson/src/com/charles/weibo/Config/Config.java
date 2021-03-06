package com.charles.weibo.Config;

public class Config {
	
	//网站后台基地址
	public static String HOST = "https://api.weibo.com/2";  
	
	//测试APP 更新URL
	public static String UPDATE_APP_URL = "http://125.88.125.201:877/themes/softs/iCare.apk";
	
  	
	 //服务器Action地址
	public static String loginUsreInfoAction = HOST+"/users/show.json";				 		//登陆用户信息
    public static String friendsNewWeiboAction = HOST+"/statuses/home_timeline.json";	 	//当前登录用户及其所关注用户的最新微博
    public static String commentDataAction = HOST+"/comments/show.json";	 				//獲取單條微博的評論列表
    public static String commentAction = HOST+"/comments/create.json";  					// 評論單條微博
    public static String commentRepAction = HOST+"/comments/reply.json";					// 回复某条评论
    public static String groupAction = HOST+"/friendships/groups.json";						// 獲取用戶分組				
    public static String commentListAction = HOST+"/comments/to_me.json";		    	 	//获取当前登录用户的最新评论包括接收到的与发出的		
    public static String nearByPlaceAction = HOST+"/place/nearby/pois.json";		 	 	//獲取附近地點
    
    //定义请求方式字符
  	public static String GET = "GET";
  	public static String POST = "POST";
    
	//SOCKET时间设置
  	public static final int REQUEST_TIMEOUT = 30 * 1000;	//请求超时时间
  	//public static final int SO_TIMEOUT = 30 * 1000; 		// socket超时时间
	public static final int SO_TIMEOUT = 60 * 1000; 		// socket超时时间
  	public static final int taskCount =5;
  	
  	
  //网络查询结果状态	
    public static final String QUERY_STATUS_OK="ok";
    public static final String QUERY_STATUS_ERROR="err";
    public static final String QUERY_STATUS_INVALID="invalid";
    
  	 // 默认响应信息
    public static String expressErrorMsg ="{\"status\":\"err\",errMsg:\"快递查询接口维护中···\", \"expressInfo\":[]}";
    public static String drugsErrorMsg ="{\"status\":\"err\",errMsg:\"药品查询接口维护中···\", \"drugsInfo\":{}}";
    public static String drugsFactErrorMsg ="{\"status\":\"err\",errMsg:\"药品/商品真伪查询接口维护中···\", \"drugsValidateInfo\":{}}";
    public static String sameDrugsErrorMsg ="{\"status\":\"err\",errMsg:\"药品查询接口维护中···\", pages:0,\"drugsList\":[]}";
    public static String goodsBarcodeDefaultMsg ="{\"status\":\"err\",errMsg:\"商品库中暂无该物品的条码记录，您可以使用商品名称查询相关商品···\", \"goodsList\":[]}";
    public static String dataErrorMsg = "{\"status\":\"-1\",msg:\"此功能维护中···\", \"datas\":{}}";
  	 
  	//手机查询归属地
   	public static String mobile_endPoint = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
   	public static String mobile_soapAction = "http://WebXml.com.cn/getMobileCodeInfo";
   	public static String mobile_nameSpace = "http://WebXml.com.cn/";
   	public static String mobile_methodName = "getMobileCodeInfo";
   	
   	
    //银联支付参数
  	//“00 ” – 银联正式环境 银联正式环境 银联正式环境
  	// “01 ” – 银联测试环境，该中不发生真实交易
    public static  String serverMode = "01";
    
    //模块权限接口地址
    public static String appModuleRightUrl =HOST+"";
    
  // 药品查询地址
    public static String expressAction ="index.php?r=express/query";
    public static String drugsByBarcodeAction ="index.php?r=drugs/queryinfo";
    public static String drugsByIdAction ="index.php?r=drugs/queryinfo";
    public static String drugsFactAction ="index.php?r=drugs/QueryBoolean";
    public static String goodsAction ="index.php?r=goods/querylist";
    public static String sameDrugsAction ="index.php?r=drugs/querylist";
    public static String sameDrugsByIdAction ="index.php?r=drugs/QueryInfoByDrugID";
    
	/* 版本更新 */
	public static String APK_URL = null; // 新版本下载链接

	public static String newVersionName = null; // 新版本名称

	public static String oldVersionName = "0.00.00"; // 旧版本名称

	public static String newVersionCode = null; // 新版本号

	public static int oldVersion = -1; // 旧版本号

	public static String appName = null; // 应用的名称

}
