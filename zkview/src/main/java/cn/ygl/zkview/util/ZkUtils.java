package cn.ygl.zkview.util;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

public class ZkUtils {

    private final static int SESSION_TIMEOUT=60000;

    public static CuratorFramework getZkClient(String address){
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(policy)
                .build();
        client.start();
        return client;
    }

    private static void createZkPath(CuratorFramework zk, String path) {
        if(zk == null){
            throw new RuntimeException("zk is null");
        }
        try {
            zk.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    public static void setPathData(CuratorFramework zk, String path, String data){
        if(zk != null){
            try {
                zk.create().orSetData().creatingParentsIfNeeded()
                        .forPath(path, data.getBytes());
            }catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }
    }

    public static boolean checkExists(CuratorFramework zk, String path) throws Exception{
        return zk.checkExists().forPath(path) == null ? false:true;
    }

    public static String getPathData(CuratorFramework zk, String path){
        if(zk == null){
            throw new RuntimeException("zk is null");
        }
        try {
            if(checkExists(zk, path)) {
                byte[] data = zk.getData().forPath(path);
                if (data != null) {
                    return new String(data);
                }
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
        return "";
    }

    public static List<String> getChildNodes(CuratorFramework zk, String path){
        if(zk == null){
            throw new RuntimeException("zk is null");
        }

        try{
            if(checkExists(zk, path)) {
                return zk.getChildren().forPath(path);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public static void deletePath(CuratorFramework zk, String path){
        if(zk == null){
            throw new RuntimeException("zk is null");
        }

        try{
            if(checkExists(zk, path)) {
                zk.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    public static void close(CuratorFramework zk){
            zk.close();
    }
}
