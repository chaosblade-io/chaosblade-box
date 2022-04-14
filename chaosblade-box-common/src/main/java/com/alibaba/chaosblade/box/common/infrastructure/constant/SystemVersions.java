package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sunju
 */
public enum SystemVersions {

    LINUX(new String[] {"2.6.3", "3.0", "4.0", "5.0"}),

    WINDOWS(new String[] {"Windows XP", "Windows 7", "Windows 8", "Windows 10"});

    @Getter
    private String[] versions;

    SystemVersions(String[] versions) {
        this.versions = versions;
    }

    public JSONObject json() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("systemName", this.name());
        jsonObject.put("versions", new JSONArray(Arrays.asList(this.getVersions())));
        return jsonObject;
    }

    public static JSON toJson() {
        JSONArray jsonArray = new JSONArray();
        for (SystemVersions value : SystemVersions.values()) {
            jsonArray.add(value.json());
        }
        return jsonArray;
    }

    public static String getLowestVersion(SystemVersions systemVersions) {
        String[] versions = systemVersions.getVersions();
        if (null != versions) {
            return Strings.nullToEmpty(versions[0]);
        }
        return "";
    }

}
