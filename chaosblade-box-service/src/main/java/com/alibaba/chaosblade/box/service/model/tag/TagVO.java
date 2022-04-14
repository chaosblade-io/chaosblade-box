package com.alibaba.chaosblade.box.service.model.tag;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagVO {

    String tagId;
    String label;

    public static TagVO from(String tag) {
        TagVO vo = new TagVO();
        vo.setLabel(tag);
        return vo;
    }

}
