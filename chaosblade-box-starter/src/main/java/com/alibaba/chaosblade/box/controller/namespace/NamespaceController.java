package com.alibaba.chaosblade.box.controller.namespace;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.common.infrastructure.domain.namespace.Namespace;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.NamespaceService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author sunju
 *
 */
@RestController
public class NamespaceController extends BaseController {

    @Autowired
    @Qualifier(value = "cloudNamespaceImpl")
    private NamespaceService namespaceService;

    @PostMapping("QueryNamespaceList")
    public RestResponse<List<Namespace>> getUserNamespace(@LoginUser ChaosUser user) {
        if (null == user) {
            return RestResponseUtil.okWithData(Lists.newArrayList());
        }
        List<Namespace> namespaces = namespaceService.getUserNamespaces(user.getUserId());
        return RestResponseUtil.okWithData(namespaces);
    }

    @PostMapping("CreateNamespace")
    public RestResponse<Namespace> createNamespace(@LoginUser ChaosUser user, @RequestBody Map<String, String> params)
        throws ChaosException {
        String name = params.get("Name");

        if (null == user || Strings.isNullOrEmpty(name)) {
            return RestResponseUtil.okWithData(null);
        }
        return RestResponseUtil.okWithData(
            namespaceService.addNamespace(StringEscapeUtils.escapeHtml4(name), StringEscapeUtils.escapeHtml4(user.getUserId()),""));
    }

    @PostMapping("DeleteNamespace")
    public RestResponse<Boolean> deleteNamespace(@LoginUser ChaosUser user, @RequestBody Map<String, String> params) {
        String name = params.get("NamespaceId");

        if (null == user || Strings.isNullOrEmpty(name)) {
            return RestResponseUtil.okWithData(false);
        }

        return RestResponseUtil.okWithData(
            namespaceService.deleteNamespace(name, user.getUserId()));
    }

}
