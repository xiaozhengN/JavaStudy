package cache.custom;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-17 23:23:51
 */
@RestController
@RequestMapping("/custom-cache-ops")
@Api(tags = "自定义缓存先关操作")
public class LruCacheController {
    @Autowired
    private CacheKeyLruPool cacheKeyLruPool;

    /**
     * 查询自定义缓存配置信息
     *
     * @return 自定义缓存配置信息
     */
    @ApiModelProperty("获取自定义缓存配置信息")
    @GetMapping("/query-conf")
    public LruPoolDTO getLruPoolConf() {
        return LruPoolDTO.builder()
                .standardCostTime(CacheKeyLruPool.standardCostTime)
                .cacheSize(CacheKeyLruPool.cacheSize)
                .size(cacheKeyLruPool.size.get())
                .build();
    }

    /**
     * 更新自定义缓存配置
     *
     * @param lruPoolDTO  自定义缓存配置
     */
    @ApiModelProperty("配置自定义缓存")
    @PutMapping("/update-conf")
    public void setLruPoolConf(@RequestBody LruPoolDTO lruPoolDTO) {
        // 容量配置
        if (lruPoolDTO.cacheSize != null) {
            CacheKeyLruPool.cacheSize = lruPoolDTO.cacheSize;
        }
        // 时间阈值配置
        if (lruPoolDTO.standardCostTime != null) {
            CacheKeyLruPool.standardCostTime = lruPoolDTO.standardCostTime;
        }
    }

    /**
     * 查询自定义缓存的Key
     *
     * @return 自定义缓存的Key
     */
    @ApiOperation("获取自定义缓存的Key")
    @GetMapping("/query-cache-key")
    public Set<String> getLruPoolKey() {
        return cacheKeyLruPool.keyToNode.keySet();
    }

    /**
     * 清空缓存, 不传key, 清楚所有
     *
     * @param key 需要清空的Key
     */
    @ApiOperation("清空缓存")
    @DeleteMapping("/clear-cache")
    public void clearLruPool(@RequestParam(required = false) String key) {
        if (key == null) {
            cacheKeyLruPool.clearAll();
        } else {
            cacheKeyLruPool.clearByKey(key);
        }
    }

    /**
     * 自定义缓存配置信息数据传输对象
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class LruPoolDTO {
        @ApiModelProperty("阈值")
        public Integer standardCostTime;

        @ApiModelProperty("缓存链表最大容量")
        public Integer cacheSize;

        @ApiModelProperty("目前使用量")
        public Integer size;
    }
}
