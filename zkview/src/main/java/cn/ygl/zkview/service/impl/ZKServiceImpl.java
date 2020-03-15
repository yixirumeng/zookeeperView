package cn.ygl.zkview.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.ygl.zkview.service.ZKService;
import cn.ygl.zkview.util.ZkUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.stereotype.Component;

@Component
public class ZKServiceImpl implements ZKService {
    private CuratorFramework zk;
    private PathChildrenCache childrenCache;

    @Override
    public void setZkAddr(String addr){
        if(zk != null){
            ZkUtils.close(zk);
        }
        zk = ZkUtils.getZkClient(addr);
    }

    @Override
    public boolean childListener(String path, String op) {
        try {
            if ("start".equals(op)) {
                childrenCache = new PathChildrenCache(zk, path, true);
                childrenCache.start();
                childrenCache.getListenable().addListener(
                        new PathChildrenCacheListener() {
                            @Override
                            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                                switch (pathChildrenCacheEvent.getType()){
                                    case CHILD_ADDED:
                                        System.err.println("ADD: "+pathChildrenCacheEvent.getData().getPath());
                                        break;
                                    case CHILD_UPDATED:
                                        System.err.println("UPDATE: "+pathChildrenCacheEvent.getData().getPath());
                                        break;
                                    case CHILD_REMOVED:
                                        System.err.println("REMOVE: "+pathChildrenCacheEvent.getData().getPath());
                                        break;
                                        default:
                                            System.err.println("default");
                                            break;
                                }
                            }
                        }
                );
                return true;
            } else {
                childrenCache.close();
                return true;
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public void setPathData(String path, String data) {
        ZkUtils.setPathData(zk, path, data);
    }

    @Override
    public void deletePath(String path) {
        ZkUtils.deletePath(zk, path);
    }

    @Override
    public String getPathData(String path) {
        System.out.printf(path);
        return null;
    }

    @Override
    public void copyZKData(String srcZk, String targetZk, String path) {
        CuratorFramework sZK = ZkUtils.getZkClient(srcZk);
        CuratorFramework tZK = ZkUtils.getZkClient(targetZk);

        copyDataToTargetZK(sZK, tZK, path);

    }

    public void copyDataToTargetZK(CuratorFramework sZK, CuratorFramework tZK, String path){
        String data = ZkUtils.getPathData(sZK, path);
        ZkUtils.setPathData(tZK, path, data);

        List<String> nodes = ZkUtils.getChildNodes(sZK, path);
        for (String node : nodes) {
            copyDataToTargetZK(sZK, tZK, path.equals("/") ? path + node : path + "/" + node);
        }

    }

    @Override
    public Map<String, String> getDatas(String path) {
        Map<String, String> datas = new HashMap<>();
        List<String> nodes = ZkUtils.getChildNodes(zk,path);
        for(String node : nodes){
            String p = "";
            if(path.equals("/")){
                p = path + nodes;
            }else{
                p = path+"/"+node;
            }
            String data = ZkUtils.getPathData(zk, p);
            datas.put(node, data);
        }
        return datas;
    }
}
