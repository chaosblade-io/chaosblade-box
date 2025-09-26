package com.alibaba.chaosblade.box.common.sdk.parser;

import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import java.io.IOException;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/** @author Changjun Xiao */
public abstract class BaseYamlService implements YamlService {

  protected InputStream inputStream;

  @Override
  public void close() {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
      }
    }
  }

  @Override
  public ChaosModels parse(String source) {
    try {
      Yaml yaml = new Yaml(new Constructor(ChaosModels.class));
      inputStream = getInputStream(source);
      if (inputStream == null) {
        return null;
      }
      return yaml.load(inputStream);
    } finally {
      close();
    }
  }
}
