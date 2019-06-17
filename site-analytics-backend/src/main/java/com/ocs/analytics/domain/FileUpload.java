package com.ocs.analytics.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Representation of the characteristics of a file that was uploaded to our web application.
 *
 * @author Bas Piepers
 */
@DataObject
public class FileUpload {
    private String fileName;
    private String uploadedFileName;
    private Long size;
    private String charset;
    private String encoding;
    private String contentType;

    public FileUpload(JsonObject jsonObject) {
        this.fileName = jsonObject.getString("fileName");
        this.uploadedFileName = jsonObject.getString("uploadedFileName");
        this.size = jsonObject.getLong("size");
        this.charset = jsonObject.getString("charset");
        this.encoding = jsonObject.getString("encoding");
        this.contentType = jsonObject.getString("contentType");
    }

    private FileUpload(String fileName, String uploadedFileName, Long size, String charset, String encoding, String contentType) {
        this.fileName = fileName;
        this.uploadedFileName = uploadedFileName;
        this.size = size;
        this.charset = charset;
        this.encoding = encoding;
        this.contentType = contentType;
    }

    public static final FileUpload from(io.vertx.reactivex.ext.web.FileUpload fileUpload) {
        return new FileUpload(fileUpload.fileName(), fileUpload.uploadedFileName(), fileUpload.size(),
                fileUpload.charSet(), fileUpload.contentTransferEncoding(), fileUpload.contentType());
    }

    public String getFileName() {
        return fileName;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public Long getSize() {
        return size;
    }

    public String getCharset() {
        return charset;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileUpload that = (FileUpload) o;

        return uploadedFileName != null ? uploadedFileName.equals(that.uploadedFileName) : that.uploadedFileName == null;
    }

    @Override
    public int hashCode() {
        return uploadedFileName != null ? uploadedFileName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "fileName='" + fileName + '\'' +
                ", uploadedFileName='" + uploadedFileName + '\'' +
                ", size=" + size +
                ", charset='" + charset + '\'' +
                ", encoding='" + encoding + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
