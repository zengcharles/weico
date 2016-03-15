package com.charles.weibo.datainterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class GoodsQueryDataPraser implements HttpQuery {

	HttpUtils utils;
	
	private Context context;
	
	public GoodsQueryDataPraser(Context context) {
		super();
		this.context = context;
	}

	private String searchDataByName(HashMap<String, Object> map) {
		utils = new HttpUtils(context);
		String result = utils.getStringDataByHttp(Config.goodsAction, map);
		/*
		 * 此处暂时使用硬编码用于开发
		 */
//		result = "{\"status\":\"ok\",\"errMsg\":\"\",\"goodsList\":[{\"title\":\"多维维生素C咀嚼片 维C vc 100片 增强免疫力 香橙味 正品特价\",\"nick\":\"多维食品专营店\",\"item_location\":\"\",\"num_iid\":\"21709764375\",\"url\":\"http://item.taobao.com/item.htm?id=21709764375\",\"pic_url\":\"http://img04.taobaocdn.com/bao/uploaded/i4/14810032017743508/T1itZFFfNbXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"70.70\",\"volume\":\"1325\"},{\"title\":\"艾兰得维生素C片 儿童vc咀嚼片2瓶120片 增强免疫力买三送60片\",\"nick\":\"艾兰得旗舰店\",\"item_location\":\"\",\"num_iid\":\"18727511082\",\"url\":\"http://item.taobao.com/item.htm?id=18727511082\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/14618030329262452/T1OOOlXE4bXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"78.00\",\"volume\":\"332\"},{\"title\":\"艾兰得维生素C 美白vc咀嚼片 两瓶120片多口味 抗感冒增强抵抗力\",\"nick\":\"艾兰得旗舰店\",\"item_location\":\"\",\"num_iid\":\"17653494345\",\"url\":\"http://item.taobao.com/item.htm?id=17653494345\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/14618030329282404/T1OOOlXE4bXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"78.00\",\"volume\":\"197\"},{\"title\":\"喜维家 维生素C泡腾片 6只 VC泡腾片 维C泡腾片 德国进口技术包邮\",\"nick\":\"优能食品专营店\",\"item_location\":\"\",\"num_iid\":\"12994309963\",\"url\":\"http://item.taobao.com/item.htm?id=12994309963\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/17609024502383140/T1SphjFX4eXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"88.00\",\"volume\":\"2853\"},{\"title\":\"【买1送2】禾博士维生素C咀嚼片 口味好的维C 每日补充VC 共160片\",\"nick\":\"禾博士旗舰店\",\"item_location\":\"\",\"num_iid\":\"12588692238\",\"url\":\"http://item.taobao.com/item.htm?id=12588692238\",\"pic_url\":\"http://img04.taobaocdn.com/bao/uploaded/i4/18626043061424447/T1V5NbFs8XXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"98.00\",\"volume\":\"783\"},{\"title\":\"康恩贝 维生素C咀嚼片 维生素c 维C VC 香橙味 100片\",\"nick\":\"康恩贝官方旗舰店\",\"item_location\":\"\",\"num_iid\":\"16248578021\",\"url\":\"http://item.taobao.com/item.htm?id=16248578021\",\"pic_url\":\"http://img04.taobaocdn.com/bao/uploaded/i4/10117030558279119/T1D1fkXcXoXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"106.00\",\"volume\":\"5926\"},{\"title\":\"汤臣倍健 维生素C片100片 补充维生素c 官方正品 包邮\",\"nick\":\"汤臣倍健官方旗舰店\",\"item_location\":\"\",\"num_iid\":\"13941196505\",\"url\":\"http://item.taobao.com/item.htm?id=13941196505\",\"pic_url\":\"http://img03.taobaocdn.com/bao/uploaded/i3/11201031286355356/T1sa9NFbBiXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"118.00\",\"volume\":\"526\"},{\"title\":\"佳莱福 益普利生牌正品维生素C咀嚼片 维C VC 香橙味 正品\",\"nick\":\"康健人生食品专营店\",\"item_location\":\"\",\"num_iid\":\"22181319525\",\"url\":\"http://item.taobao.com/item.htm?id=22181319525\",\"pic_url\":\"http://img02.taobaocdn.com/bao/uploaded/i2/17311028917971844/T1mR4_FaBdXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"118.00\",\"volume\":\"70\"},{\"title\":\"GNC维生素c 针叶樱桃柑橘咀嚼片 美国进口180片/瓶\",\"nick\":\"gnc健安喜旗舰店\",\"item_location\":\"\",\"num_iid\":\"16542812713\",\"url\":\"http://item.taobao.com/item.htm?id=16542812713\",\"pic_url\":\"http://img04.taobaocdn.com/bao/uploaded/i4/11629030494601202/T1YZiHFjxaXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"138.00\",\"volume\":\"69\"},{\"title\":\"康恩贝 美白养颜套装 维生素E+维生素C礼盒 （VE120粒+VC100片）\",\"nick\":\"康恩贝官方旗舰店\",\"item_location\":\"\",\"num_iid\":\"12574776935\",\"url\":\"http://item.taobao.com/item.htm?id=12574776935\",\"pic_url\":\"http://img04.taobaocdn.com/bao/uploaded/i4/10117026667557226/T1tLygFcpgXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"186.00\",\"volume\":\"1536\"},{\"title\":\"Lumi维生素c60粒 台湾进口小白丸vc维生素C含丰富VC 买3送276\",\"nick\":\"lumi旗舰店\",\"item_location\":\"\",\"num_iid\":\"22078508819\",\"url\":\"http://item.taobao.com/item.htm?id=22078508819\",\"pic_url\":\"http://img02.taobaocdn.com/bao/uploaded/i2/17036030538602862/T159XrFaxXXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"188.00\",\"volume\":\"63\"},{\"title\":\"[进口正品]健美生天然维生素C 口含片/咀嚼片VC融合橙沫500mg*120\",\"nick\":\"jamieson健美生旗舰店\",\"item_location\":\"\",\"num_iid\":\"16997962660\",\"url\":\"http://item.taobao.com/item.htm?id=16997962660\",\"pic_url\":\"http://img03.taobaocdn.com/bao/uploaded/i3/11905032190267827/T1t_BAFohhXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"197.00\",\"volume\":\"173\"},{\"title\":\"养生堂天然维生素E C套装 VC120+VE90+面膜+EC尝鲜装\",\"nick\":\"养生堂旗舰店\",\"item_location\":\"\",\"num_iid\":\"35446551141\",\"url\":\"http://item.taobao.com/item.htm?id=35446551141\",\"pic_url\":\"http://img02.taobaocdn.com/bao/uploaded/i2/19579030115901556/T1RlzKFexaXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"202.00\",\"volume\":\"14\"},{\"title\":\"美国产安利维生素C/纽崔莱天然VC/180片 美白养颜保健品 金冠正品\",\"nick\":\"dzlixh\",\"item_location\":\"\",\"num_iid\":\"162446185\",\"url\":\"http://item.taobao.com/item.htm?id=162446185\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/10033030301470272/T1BLlBFkdXXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"225.00\",\"volume\":\"795\"},{\"title\":\"养生堂天然维生素E.C套装ve160粒+vc90片维生素ec 美容祛斑 包邮\",\"nick\":\"开心人大药房旗舰店\",\"item_location\":\"\",\"num_iid\":\"14722590587\",\"url\":\"http://item.taobao.com/item.htm?id=14722590587\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/18864030214908818/T1T5cJFXheXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"228.00\",\"volume\":\"595\"},{\"title\":\"[买1送2]养生堂天然维生素c e套装ve160+vc90 c+e 维生素e c正品\",\"nick\":\"康佰保健品专营店\",\"item_location\":\"\",\"num_iid\":\"9847602423\",\"url\":\"http://item.taobao.com/item.htm?id=9847602423\",\"pic_url\":\"http://img02.taobaocdn.com/bao/uploaded/i2/15447030326784807/T1zmSFFh8aXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"228.00\",\"volume\":\"5196\"},{\"title\":\"海南养生堂天然维生素C咀嚼片 VC 维C 208片 正品包邮\",\"nick\":\"七彩日月保健品专营店\",\"item_location\":\"\",\"num_iid\":\"25681668289\",\"url\":\"http://item.taobao.com/item.htm?id=25681668289\",\"pic_url\":\"http://img01.taobaocdn.com/bao/uploaded/i1/11813030378620822/T1v2oQFoVfXXXXXXXX_!!0-item_pic.jpg_90x90.jpg\",\"price\":\"254.00\",\"volume\":\"162\"}]}";

		if (null != result && !"".equalsIgnoreCase(result)) {
			return result;
		} else {
			return Config.drugsErrorMsg;
		}
	}

	private String searchDataByBarcode(HashMap<String, Object> map) {
		utils = new HttpUtils(context);
		String result = utils.getStringDataByHttp(Config.drugsFactAction, map);
		/*
		 * 此处暂时使用硬编码用于开发
		 */
//		result = "{\"status\":\"ok\",\"errMsg\":\"\",\"drugsValidateInfo\":{\"validateType\":\"1\",\"validateResults\":{\"resultUrl\":\"\",\"resultInfo\":{\"companyInfo\":{\"companyName\":\"广东华南药业集团有限公\",\"companyAddr\":\"广东省东莞市石龙镇西湖工业区信息产业园\",\"FirmStatus\":\"有效\"},\"drugsInfo\":{\"drugName\":\"维生素C片\",\"barcode\":\"6910942381270\",\"specification\":\"每瓶100\",\"BrandName\":\"华南\"}}}}}";

		if (null != result && !"".equalsIgnoreCase(result)) {
			return result;
		} else {
			return Config.drugsErrorMsg;
		}
	}

	@Override
	public void httpCancel() {
		// TODO Auto-generated method stub
		if (null != utils) {
			utils.disconnectRequest();
		}
	}

	@Override
	public JSONObject query(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		if (map.containsKey("searchType")) {
			if (Integer.valueOf(map.get("searchType").toString()) == 0) {
				HashMap<String, Object> searchByBarcodeMap = new HashMap<String, Object>();
				searchByBarcodeMap.put("drugBarcode", map.get("goodBarcode"));
				searchByBarcodeMap.put("barcodeType", "1");
				searchByBarcodeMap.put("imei", map.get("imei"));

				String resultBarCode = searchDataByBarcode(searchByBarcodeMap);
				try {
					JSONObject jsonBarcodeValue = new JSONObject(resultBarCode);
					String drugName = "";
					if (Config.QUERY_STATUS_OK
							.equalsIgnoreCase(jsonBarcodeValue
									.getString("status"))) {

						JSONObject drugsValidateInfo = jsonBarcodeValue
								.getJSONObject("drugsValidateInfo");
						JSONObject validateResults = drugsValidateInfo
								.getJSONObject("validateResults");
						JSONObject resultInfo = validateResults
								.getJSONObject("resultInfo");
						if (null != resultInfo) {
							if (resultInfo.has("drugsInfo")
									&& null != resultInfo
											.getJSONObject("drugsInfo")
									&& resultInfo.getJSONObject("drugsInfo")
											.has("drugName")
									&& null != resultInfo.getJSONObject(
											"drugsInfo").getString("drugName")) {
								if (resultInfo.getJSONObject("drugsInfo").has(
										"drugName")
										&& null != resultInfo.getJSONObject(
												"drugsInfo").getString(
												"drugName")) {
									drugName = resultInfo.getJSONObject(
											"drugsInfo").getString("drugName");
								}
							}
						}
						if ("".equalsIgnoreCase(drugName)) {
							return new JSONObject(Config.goodsBarcodeDefaultMsg);
						} else {
							HashMap<String, Object> searchByNameMap = new HashMap<String, Object>();
							try {
								searchByNameMap.put("goodsName",
										URLEncoder.encode(drugName, "utf8"));
							} catch (UnsupportedEncodingException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							String result = searchDataByName(searchByNameMap);
							try {
								return new JSONObject(result);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								try {
									return new JSONObject(
											Config.goodsBarcodeDefaultMsg);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
								}
							}
						}
					} else {
						return new JSONObject(Config.goodsBarcodeDefaultMsg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						return new JSONObject(Config.goodsBarcodeDefaultMsg);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}
				}
			} else {
				HashMap<String, Object> searchByNameMap = new HashMap<String, Object>();
				searchByNameMap.put("goodsName", map.get("goodsName")
						.toString());
				String result = searchDataByName(searchByNameMap);
				try {
					return new JSONObject(result);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						return new JSONObject(Config.goodsBarcodeDefaultMsg);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
		}
		return null;
	}

}
