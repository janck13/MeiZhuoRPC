package org.meizhuo.rpc.trace;

import io.netty.channel.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.meizhuo.rpc.core.RPC;

public class HTTPConnectionPool {

    private GenericObjectPool<Channel> pool;

    public HTTPConnectionPool() {
        if (RPC.isTrace()) {
            HTTPConnectionFactory httpConnectionFactory = new HTTPConnectionFactory();
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            //最大空闲连接数
            config.setMaxIdle(RPC.getTraceConfig().getHTTPMaxIdle());
            //最大连接数
            config.setMaxTotal(RPC.getTraceConfig().getHTTPMaxTotal());
            //需要如下设置 才能在borrow的时候触发validateObject校验对象是否可用
            config.setTestOnBorrow(true);
            pool = new GenericObjectPool(httpConnectionFactory, config);
        }
    }

    public Channel getHTTPChannel() throws Exception {
        return pool.borrowObject();
    }

    public void releaseHTTPChannel(Channel channel){
        pool.returnObject(channel);
    }
}
