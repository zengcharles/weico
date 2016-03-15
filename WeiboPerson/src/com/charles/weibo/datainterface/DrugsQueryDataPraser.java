package com.charles.weibo.datainterface;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class DrugsQueryDataPraser implements HttpQuery{

	HttpUtils utils;
	
	private Context context;
	
	public DrugsQueryDataPraser(Context context) {
		super();
		this.context = context;
	}

	private String requestDataByHttpPost( HashMap<String,Object> map){
   	 utils=new HttpUtils(context);
   	 String result = "";
   	HashMap<String,Object> param =new HashMap<String, Object>();
   	 if(Integer.valueOf(map.get("serchType").toString())==0){
   		param.put("drugBarcode", map.get("drugBarcode"));
   		result =utils.getStringDataByHttp(Config.drugsByBarcodeAction, param);
   	 }else{
   		param.put("id", map.get("id"));
   		result =utils.getStringDataByHttp(Config.drugsByIdAction, param);
   	 }
   	 /*
	  * 此处暂时使用硬编码用于开发
	  */
//   	 result ="{\"status\":\"ok\",\"errMsg\":\"\",\"drugsInfo\":{\"drugsId\":760301,\"price\":2.95,\"aliasName\":\"\",\"chineseName\":\"维生素C片\",\"companyName\":\"广东华南药业有限公司\",\"formula\":\"剂型_片剂\",\"maxPrice\":9.5,\"minPrice\":6.6,\"gongneng\":\"用于治疗肠道感染、腹泻\",\"parentid\":2269153,\"titleimg\":\"http://img.39.net/yp/20130916/2/760306.jpg\",\"listimg\":\"http://img.39.net/yp/2010/4/6/T3b2c5d92043.jpg\",\"storage\":\"遮光，密封保存\",\"shelflife\":\"36 个月。\",\"codename\":\"国药准字H44020757\",\"pharmacology\":\"本品对细菌只有微弱的抑菌作用，但对痢疾杆菌、大肠杆菌引起的肠道感染有效。\",\"usage\":\"口服，成人：一次1-3片，一日3次；儿童用量见下表：年龄（岁）体重（公斤）一次用量（片）次数 1-3 10-15 0.5-1 4-6 16-21 1-1.5 一日3次 7-9 22-27 1.5-2 10-12 28-32 2-2.5\",\"adr\":\"口服不良反应较少，偶有恶心、呕吐、皮疹和药热，停药后消失。\",\"interaction\":\"1.含鞣质的中药与本品合用后，由于鞣质是生物碱沉淀剂，二者结合，生成难溶性鞣酸盐沉淀，降低疗效。 2.如与其他药物同时使用可能会发生药物相互作用，详情请咨询医师或药师。\",\"contraindication\":\"溶血性贫血患者及葡萄糖-6-磷酸脱氢酶缺乏患者禁用。\",\"composition\":\"本品每片含主要成分盐酸小檗碱0.05克\",\"childrentaboo\":\"\",\"pregnantwomentaboo\":\"\",\"elderlytaboo\":\"\",\"englishName\":\"Berberine Hydrochloride Tablets\",\"refcorpaddress\":\"东莞市石龙镇西湖工业区信息产业园\",\"refcontacttel\":\"\",\"specificationdate\":\"2010年10月01日\",\"material\":\"\",\"categorycode\":\"感染科用药\",\"drugattribute\":\"本品为黄色片或糖衣片。\",\"standard\":\"《中国药典》2010年版二部\",\"note\":\"1.对本品过敏者、溶血性贫血患者禁用。 2.妊娠期头三个月慎用。 3.如服用过量过出现严重不良反应，请立即就医。 4.当药品性状发生改变时禁止使用。 5.儿童必须在成人监护下使用。 6.请将此药品放在儿童不能接触的地方。\",\"unit\":\"0.1克\",\"drugtype\":1,\"suitablecrowd\":\"\",\"inappropriatecrowd\":\"\",\"depttype\":\"科室用药_感染科用药\",\"Comments\":[],\"drugdetaids\":[237914],\"refspecifications\":[\"0.1gx100片\"],\"SameDrugs\":[{\"_id\":501966,\"namecn\":\"谷维素片\",\"aliascn\":\"\",\"refdrugcompanyname\":\"北京海联制药有限公司\",\"avgprice\":0,\"gongneng\":\"神经官能症、经前期紧张综合征、更年期综合征的镇静助眠。\",\"score\":1},{\"_id\":508200,\"namecn\":\"谷维素片\",\"aliascn\":\"\",\"refdrugcompanyname\":\"上海信谊天平药业有限公司\",\"avgprice\":3,\"gongneng\":\"用于镇静助眠，如神经官能症、月经前期紧张症、更年期综合征的辅助治疗。\",\"score\":4},{\"_id\":771062,\"namecn\":\"谷维素片\",\"aliascn\":\"\",\"refdrugcompanyname\":\"福建省福抗药业股份有限公司\",\"avgprice\":0,\"gongneng\":\"用于周期性精神病及各种神经官能症的辅助治疗\",\"score\":0}]}}";
   	 
   	 if(null!=result&&!"".equalsIgnoreCase(result)){
   		 return result;
   	 }else{
   		 return Config.drugsErrorMsg;
   	 }
    }
	 
	@Override
	public JSONObject query(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		String result= requestDataByHttpPost(map);
		try {
			return new JSONObject(result);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return new JSONObject( Config.drugsErrorMsg);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
			}
		}
		return null;
	}

	@Override
	public void httpCancel() {
		// TODO Auto-generated method stub
		if(null!=utils){
   		 utils.disconnectRequest();
   	    }
	}

}
