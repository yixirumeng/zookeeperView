package cn.ygl.zkview.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @ClassName CuratorTests
 * @Author Yang Guangliang
 * @Date 2020/3/14 17:20
 **/
public class CuratorTests {
    private CuratorFramework client = null;

    @Before
    public void init (){
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(policy)
                .build();
        client.start();
    }

    @Test
    public void createPath(){
        try {
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/test");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void setPathData(){
        try{
            client.setData().forPath("/test", "test".getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Test
    public void createOrSetData(){
        try{
            client.create().orSetData().creatingParentsIfNeeded()
                    .forPath("/test/curator/0", "curator".getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void getPathData(){
        try{
            System.err.println(new String(
                    client.getData().forPath("/test/curator/1")
            ));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
