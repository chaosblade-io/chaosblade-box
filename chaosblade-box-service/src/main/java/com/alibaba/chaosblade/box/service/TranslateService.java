package com.alibaba.chaosblade.box.service;

import java.util.Map;

public interface TranslateService {

    public Map<String, String> loadTranslateJson();

    public String translateToEn(String cn);
}
