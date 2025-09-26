package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.annotation.SwaggerDoc;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagCreatingRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagSearchRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.TagException;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.TagService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.tag.TagVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author sunju */
@RestController
@Api(description = "标签")
@SwaggerDoc
public class TagController extends BaseController {

  @Resource private TagService tagService;

  @PostMapping("SearchTags")
  @ApiOperation(value = "搜索标签")
  public RestResponse<List<TagVO>> search(
      @LoginUser ChaosUser user, @RequestBody TagSearchRequest tagSearchRequest) {
    Integer tagType = tagSearchRequest.getType();
    if (tagType == null) {
      tagSearchRequest.setType(TagDO.TAG_TYPE_EXPERIMENT);
    }
    return Optional.of(
            tagService.search(tagSearchRequest).getResult().stream()
                .map(TagVO::from)
                .collect(Collectors.toList()))
        .map(RestResponseUtil::okWithData)
        .orElse(RestResponseUtil.okWithData(Lists.newArrayList()));
  }

  @PostMapping("ListExperimentTags")
  @ApiOperation(value = "搜索标签")
  public RestResponse<List<String>> listExperimentTags(
      @LoginUser ChaosUser user, @RequestBody TagSearchRequest tagSearchRequest) {
    return RestResponseUtil.initWithServiceResponse(
        tagService.listExperimentTags(tagSearchRequest.getUserId()));
  }

  /**
   * 经验库标签搜索
   *
   * @param user
   * @param tagSearchRequest
   * @return
   */
  @PostMapping("ListUserTagsByType")
  @ApiOperation(value = "搜索标签")
  public RestResponse<List<String>> listUserTagsByType(
      @LoginUser ChaosUser user, @RequestBody TagSearchRequest tagSearchRequest) {
    tagSearchRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        tagService.listUserTagsByType(tagSearchRequest));
  }

  /**
   * 经验库标签搜索
   *
   * @param user
   * @param tagSearchRequest
   * @return
   */
  @PostMapping("ListExpertiseTags")
  @ApiOperation(value = "搜索标签")
  public RestResponse<Set<String>> listExpertiseTags(
      @LoginUser ChaosUser user, @RequestBody TagSearchRequest tagSearchRequest) {
    tagSearchRequest.setUser(user);
    return RestResponseUtil.initWithServiceResponse(
        tagService.listUserAndAdminTags(tagSearchRequest));
  }

  @ApiOperation(value = "新建标签")
  @PostMapping("/AddTag")
  public RestResponse<String> add(
      @LoginUser ChaosUser user, @RequestBody TagCreatingRequest request) throws TagException {
    String tagId = tagService.addTag(request).getResult();
    return RestResponseUtil.okWithData(tagId);
  }
}
