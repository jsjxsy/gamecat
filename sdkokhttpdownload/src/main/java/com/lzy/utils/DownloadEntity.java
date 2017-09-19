package com.lzy.utils;

import java.io.Serializable;

/**
 * Created by zhan on 17-2-24.
 *
 * 下载实体类
 */
public class DownloadEntity implements Serializable{
    private String url;//下载so地址
    private String fileName;//文件名
    private String filePath;//文件保存路径
    private String bundleNam;//包名
    private ToUpdateInter toUpdateInter;//更新so包回调
    private String md5;//文件md5值
    private int retrycount;//失败后重新下载次数
    private String oldVersion;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public int getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(int retrycount) {
        this.retrycount = retrycount;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public ToUpdateInter getToUpdateInter() {
        return toUpdateInter;
    }

    public void setToUpdateInter(ToUpdateInter toUpdateInter) {
        this.toUpdateInter = toUpdateInter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBundleNam() {
        return bundleNam;
    }

    public void setBundleNam(String bundleNam) {
        this.bundleNam = bundleNam;
    }
}
