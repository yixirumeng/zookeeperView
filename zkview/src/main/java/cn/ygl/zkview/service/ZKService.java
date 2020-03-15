package cn.ygl.zkview.service;

import java.util.Map;

public interface ZKService {
    void setPathData(String path, String data);
    void deletePath(String path);
    String getPathData(String path);
    void copyZKData(String srcZk, String targetZk, String path);
    Map<String, String> getDatas(String path);
    void setZkAddr(String addr);
    boolean childListener(String path, String op);
}
