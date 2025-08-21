package com.alibaba.chaosblade.box.service.model.loadtest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * JMX文件上传响应
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("JMX文件上传响应")
public class JmxFileUploadResponse {

    @ApiModelProperty("存储文件名（带时间戳与随机后缀）")
    private String fileName;

    @ApiModelProperty("原始文件名")
    private String originalFileName;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件大小（字节）")
    private Long fileSize;

    @ApiModelProperty("上传路径")
    private String uploadPath;

    @ApiModelProperty("可访问URL")
    private String accessUrl;

    @ApiModelProperty("上传时间戳")
    private Long uploadTime;

    @ApiModelProperty("上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date uploadDate;

    /**
     * 从压测引擎响应创建上传响应对象
     *
     * @param engineResponse 压测引擎响应数据
     * @return JMX文件上传响应
     */
    public static JmxFileUploadResponse fromEngineResponse(EngineFileUploadData engineResponse) {
        if (engineResponse == null) {
            return null;
        }

        JmxFileUploadResponse response = new JmxFileUploadResponse();
        response.setFileName(engineResponse.getFileName());
        response.setOriginalFileName(engineResponse.getOriginalFileName());
        response.setFileType(engineResponse.getFileType());
        response.setFileSize(engineResponse.getFileSize());
        response.setUploadPath(engineResponse.getUploadPath());
        response.setAccessUrl(engineResponse.getAccessUrl());
        response.setUploadTime(engineResponse.getUploadTime());
        
        // 转换时间戳为Date对象
        if (engineResponse.getUploadTime() != null) {
            response.setUploadDate(new Date(engineResponse.getUploadTime()));
        }

        return response;
    }

    /**
     * 压测引擎文件上传响应数据结构
     */
    @Data
    public static class EngineFileUploadData {
        private String fileName;
        private String originalFileName;
        private String fileType;
        private Long fileSize;
        private String uploadPath;
        private String accessUrl;
        private Long uploadTime;
    }
}
