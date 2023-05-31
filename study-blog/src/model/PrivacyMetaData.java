package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * PrivacyMetaData模型
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2023-04-17 21:51:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivacyMetaData {
    /**
     * APP名称
     */
    private String appName;

    /**
     * APP版本
     */
    private String appVersion;

    /**
     * APPOwner
     */
    private String appOwner;

    /**
     * APP状态
     */
    private AppStatusEnum appStatus;

    /**
     * 扩展字段
     */
    private Map<String, String> extend;

    @AllArgsConstructor
    @NoArgsConstructor
    public enum AppStatusEnum {
        ON_SHELF("STATUS_001", "已上架"),

        NOT_ON_SHELF("STATUS_002", "未上架");

        /**
         * 状态码
         */
        private String code;

        /**
         * 状态名称
         */
        private String statusName;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        PrivacyMetaData privacyMetaData = new PrivacyMetaData();
        Map<String, String> extndMap = new HashMap<>();
        extndMap.put("1", "1");
        extndMap.put("2", "2");
        privacyMetaData.setExtend(extndMap);
        Field[] fields = privacyMetaData.getClass().getDeclaredFields();
        // 判断某个对象是否有指定字段
        for (Field field : fields) {
            if (field.getName().equals("extend")) {
                Field declaredField = privacyMetaData.getClass().getDeclaredField("extend");
                declaredField.setAccessible(true);
                Object extendObj = declaredField.get(privacyMetaData);
                if (extendObj instanceof Map) {
                    Map<Object, Object> extendObj1 = (Map<Object, Object>) extendObj;
                }
            }
        }
    }
}
