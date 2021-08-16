package top.paakciu.pitim.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.paakciu.pitim.common.constant.IMConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class NettyClient {
    private static final int MAX_RETRY = IMConfig.ClientConnectionRetry;

    public void startConnection(String host,int port)
    {
        //线程组
        NioEventLoopGroup workerGroup =new NioEventLoopGroup();
        //引导类
        Bootstrap bootstrap =new Bootstrap();
        //核心配置
        bootstrap
                //指定线程模型
                .group(workerGroup)
                //指定IO类型为NIO
                .channel(NioSocketChannel.class)
        ;
        //指定IO的处理逻辑
        setBootstrapHandler(bootstrap);
        setBootstrapExtraConfig(bootstrap);
        //启动连接
        connect(bootstrap, host, port,MAX_RETRY);
    }
    /**
     * 配置逻辑处理器的责任链
     * 后续处理节点都在里面添加addLast
     * @param bootstrap
     * @return
     */
    private Bootstrap setBootstrapHandler(Bootstrap bootstrap){
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            //连接初始化
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                //接口处理初始化
                //-----------------
                //todo,开始连接，操作需异步，其实同步也行，启动慢点而已
                System.out.println("正在初始化...");
                //-----------------
                //这里是责任链模式，然后加入逻辑处理器
                socketChannel.pipeline()
                        //插入测试处理器
                        .addLast(new SimpleClientHandler())
                        ;
            }
        });
        return bootstrap;
    }

    //额外配置
    private Bootstrap setBootstrapExtraConfig(Bootstrap bootstrap){
        //额外的配置
        bootstrap
                // 设置TCP底层属性
                //连接的超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启TCP底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //是否开启Nagle，即高实时性（true）减少发送次数（false）
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

    //建立连接
    private void connect (Bootstrap bootstrap,String host,int port,int retry)
    {
        Map<Boolean, Consumer<Future<?>>> action=new HashMap<>();
        action.put(false,future->{
            //这里应该要有个随机退避算法
            //接口处理连接失败
            //--------------------
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接失败！正在重试");
            //--------------------

            if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
                return;
            }
            // 第几次重连
            int order = (MAX_RETRY - retry) + 1;
            // 此次重连的间隔时间
            int delay = 1 << order;
            System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
            //使用计划任务来实现退避重连算法
            bootstrap.config().group().schedule(
                    () -> connect(bootstrap, host, port, retry - 1)
                    ,delay
                    ,TimeUnit.SECONDS
            );
        });
        action.put(true,future->{
            Channel channel = ((ChannelFuture) future).channel();
            // 连接成功之后，启动控制台线程
            //--------------------
            //todo,这里应当使用线程，使操作变成异步，以免阻塞欢迎连接
            System.out.println("连接成功！");
            //--------------------
        });
        bootstrap
                .connect(host, port)
                .addListener(future -> {
                    action.get(future.isSuccess()).accept(future);
                });
    }

    public static void main(String[] args) {
        NettyClient nettyClient=new NettyClient();
        nettyClient.startConnection(IMConfig.HOST,IMConfig.PORT);
    }

}
