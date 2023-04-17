package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
