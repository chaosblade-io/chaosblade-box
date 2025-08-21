package com.alibaba.chaosblade.box.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Multipart/form-data 构建工具类
 *
 * @author ZhengMingZhuo
 */
public class MultipartFormDataBuilder {

    private static final String BOUNDARY_PREFIX = "--";
    private static final String LINE_SEPARATOR = "\r\n";
    
    private final String boundary;
    private final ByteArrayOutputStream outputStream;

    public MultipartFormDataBuilder() {
        this.boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replace("-", "");
        this.outputStream = new ByteArrayOutputStream();
    }

    /**
     * 添加文件字段
     *
     * @param fieldName 字段名
     * @param fileName 文件名
     * @param contentType 内容类型
     * @param fileContent 文件内容
     * @return this
     * @throws IOException IO异常
     */
    public MultipartFormDataBuilder addFileField(String fieldName, String fileName, 
                                                String contentType, byte[] fileContent) throws IOException {
        // 写入边界
        outputStream.write((BOUNDARY_PREFIX + boundary + LINE_SEPARATOR).getBytes(StandardCharsets.UTF_8));
        
        // 写入Content-Disposition头
        String contentDisposition = String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s",
                fieldName, fileName, LINE_SEPARATOR);
        outputStream.write(contentDisposition.getBytes(StandardCharsets.UTF_8));
        
        // 写入Content-Type头
        String contentTypeHeader = String.format("Content-Type: %s%s", contentType, LINE_SEPARATOR);
        outputStream.write(contentTypeHeader.getBytes(StandardCharsets.UTF_8));
        
        // 写入空行
        outputStream.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
        
        // 写入文件内容
        outputStream.write(fileContent);
        
        // 写入换行
        outputStream.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
        
        return this;
    }

    /**
     * 添加文本字段
     *
     * @param fieldName 字段名
     * @param value 字段值
     * @return this
     * @throws IOException IO异常
     */
    public MultipartFormDataBuilder addTextField(String fieldName, String value) throws IOException {
        // 写入边界
        outputStream.write((BOUNDARY_PREFIX + boundary + LINE_SEPARATOR).getBytes(StandardCharsets.UTF_8));
        
        // 写入Content-Disposition头
        String contentDisposition = String.format("Content-Disposition: form-data; name=\"%s\"%s",
                fieldName, LINE_SEPARATOR);
        outputStream.write(contentDisposition.getBytes(StandardCharsets.UTF_8));
        
        // 写入空行
        outputStream.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
        
        // 写入字段值
        outputStream.write(value.getBytes(StandardCharsets.UTF_8));
        
        // 写入换行
        outputStream.write(LINE_SEPARATOR.getBytes(StandardCharsets.UTF_8));
        
        return this;
    }

    /**
     * 构建最终的multipart数据
     *
     * @return multipart数据字节数组
     * @throws IOException IO异常
     */
    public byte[] build() throws IOException {
        // 写入结束边界
        outputStream.write((BOUNDARY_PREFIX + boundary + BOUNDARY_PREFIX + LINE_SEPARATOR).getBytes(StandardCharsets.UTF_8));
        
        return outputStream.toByteArray();
    }

    /**
     * 获取Content-Type头值
     *
     * @return Content-Type头值
     */
    public String getContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    /**
     * 关闭输出流
     */
    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            // 忽略关闭异常
        }
    }
}
