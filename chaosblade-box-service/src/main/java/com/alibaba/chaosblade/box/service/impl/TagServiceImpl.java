package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagCreatingRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagSearchRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.TagException;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.TagRepository;
import com.alibaba.chaosblade.box.service.TagService;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sunju
 *
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Autowired
    private TagManager tagManager;



    @Override
    public Response<String> addTag(TagCreatingRequest request) throws TagException {
        if (Strings.isNullOrEmpty(request.getName())) {
            throw new TagException(
                CommonErrorCode.P_REQUIRE_FIELD_MISSING,
                "Insert tag failed. Miss required field[name] for tag."
            );
        }

        if (Strings.isNullOrEmpty(request.getCode())) {
            throw new TagException(
                CommonErrorCode.P_REQUIRE_FIELD_MISSING,
                "Insert tag failed. Miss required field[code] for tag."
            );
        }

        TagDO tag = new TagDO();
        tag.setName(request.getName());
        tag.setCode(request.getCode());
        tag.setType(request.getType());
        tag.setUserId(request.getUserId());
        boolean result;
        try {
            result = tagRepository.add(tag);
        } catch (Exception e) {
            throw new TagException(CommonErrorCode.B_ADD_TAG_FAILED, "Insert tag to DB failed.", e);
        }

        if (!result) {
            throw new TagException(CommonErrorCode.B_ADD_TAG_FAILED, "Insert tag to DB failed.");
        }

        return Response.okWithData(tag.getTagId());
    }

    @Override
    public Response<List<String>> listExperimentTags(String userId) {
        return Response.okWithData(tagManager.listExperimentTags(userId));
    }

    @Override
    public Response<List<String>> listUserTagsByType(TagSearchRequest searchRequest) {
        return Response.okWithData(tagManager.listUserTagsByType(searchRequest));
    }

    @Override
    public Response<List<String>> search(TagSearchRequest tagSearchRequest) {
        return Response.okWithData(tagManager
            .searchTagsByUserId(tagSearchRequest.getUserId(), tagSearchRequest.getType(), tagSearchRequest.getKey()));
    }

    @Override
    public Response<Set<String>> listUserAndAdminTags(TagSearchRequest tagSearchRequest) {
        Set<String> result = new HashSet<>();
        result.addAll(tagManager.searchTagsByUserId(tagSearchRequest.getUserId(), TagDO.TAG_TYPE_EXPERTISE, tagSearchRequest.getKey()));
        result.addAll(tagManager.getAdminExpertiseTags());
        return Response.okWithData(result);
    }
}


