package com.alibaba.chaosblade.box.service.model.loadtest;

import lombok.Data;

/**
 * 文件上传数据
 *
 * @author ZhengMingZhuo
 */
@Data
public class FileUploadData {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    private byte[] fileContent;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 构造函数
     *
     * @param fileName 文件名
     * @param fileContent 文件内容
     * @param contentType 内容类型
     */
    public FileUploadData(String fileName, byte[] fileContent, String contentType) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileSize = fileContent != null ? fileContent.length : 0;
        this.contentType = contentType;
    }

    /**
     * 检查文件是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    /**
     * 获取文件扩展名
     *
     * @return 文件扩展名
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
