package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class GsonPluginsTest {
    private Integer code;
    private String message;
    private Integer ttl;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        private List<TagsDTO> tags;
        private Integer pn;
        private Integer ps;
        private Integer total;

        @NoArgsConstructor
        @Data
        public static class TagsDTO {
            private Integer tagId;
            private String tagName;
            private String cover;
        }
    }
}
