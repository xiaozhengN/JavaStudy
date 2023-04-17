package model;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;


/**
 * @author xiaozhengN 571457082@qq.com
 * @since 2023-04-17 22:01:36
 **/
public class ConvertTest {
    public static void main(String[] args) {
        PrivacyMetaData privacyMetaData = PrivacyMetaData.builder()
                .appName("高德地图")
                .appOwner("张三 00888888")
                .appStatus(PrivacyMetaData.AppStatusEnum.ON_SHELF)
                .appVersion("V1.1.1")
                .build();
        Gson gson = new Gson();
//        String json = gson.toJson(privacyMetaData);
//        System.out.println(json);
//        String json2 = "{\"appName\":\"高德地图\",\"appVersion\":\"V1.1.1\",\"appOwner\":\"张三 00888888\",\"appStatus\":\"已上架\"}";
//        PrivacyMetaData privacyMetaData1 = gson.fromJson(json2, PrivacyMetaData.class);
//        System.out.println(privacyMetaData1);

        String json = JSON.toJSONString(privacyMetaData);
        // {"appName":"高德地图","appOwner":"张三 00888888","appStatus":"ON_SHELF","appVersion":"V1.1.1"}
        System.out.println(json);

        String json2 = "{\"appName\":\"高德地图\",\"appVersion\":\"V1.1.1\",\"appOwner\":\"张三 00888888\",\"appStatus\":\"已上架\"}";
        PrivacyMetaData obj1 = JSON.parseObject(json2, PrivacyMetaData.class);
        // PrivacyMetaData(appName=高德地图, appVersion=V1.1.1, appOwner=张三 00888888, appStatus=null)
        System.out.println(obj1);

        String json3 = "{\"appName\":\"高德地图\",\"appVersion\":\"V1.1.1\",\"appOwner\":\"张三 00888888\",\"appStatus\":\"ON_SHELF\"}";
        PrivacyMetaData obj2 = JSON.parseObject(json3, PrivacyMetaData.class);
        // PrivacyMetaData(appName=高德地图, appVersion=V1.1.1, appOwner=张三 00888888, appStatus=ON_SHELF)
        System.out.println(obj2);

        PrivacyMetaData obj3 = gson.fromJson(json3, PrivacyMetaData.class);
        // PrivacyMetaData(appName=高德地图, appVersion=V1.1.1, appOwner=张三 00888888, appStatus=ON_SHELF)
        System.out.println(obj3);
    }
}
