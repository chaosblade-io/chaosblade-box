package com.alibaba.chaosblade.box.common.app.sdk;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public final class ChaosAppRequest {

    Scope scope;

    Map<String, String> matcher;

    Map<String, String> action = new HashMap<>();

    Map<String, String> userArgs = new HashMap<>();

    Map<String, String> config;

}
