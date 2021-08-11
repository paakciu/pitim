package top.paakciu.pitim.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.paakciu.pitim.server.handler.SimpleServerHandler;


import java.util.Scanner;


public class NettyServer {

    //定义后续每条连接的数据读写
    public static void setServerBootstrapChildHandler(ServerBootstrap serverBootstrap){
        //定义后续每条连接的数据读写，业务处理逻辑，这里的NioSocketChannel是netty对NIO类型连接的抽象，如Socket
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            //每条客户端连接--
            protected void initChannel(NioSocketChannel nioSocketChannel) {
                System.out.println("客户机接入");
                //添加逻辑处理器
                nioSocketChannel.pipeline()
                        .addLast(new SimpleServerHandler())
                        ;
            }
        });
    }

    public static void main(String[] args)
    {
        System.out.println("请输入你要监听的端口：(4000-4500)");
        Scanner sc=new Scanner(System.in);
        int port=sc.nextInt();
        INSTANCE.startListening(port);
    }
    //单例模式
    public static final NettyServer INSTANCE =new NettyServer();


    public void startListening(int port){
        //监听欢迎端口-线程组
        NioEventLoopGroup bossGroup =new NioEventLoopGroup();
        //处理每条链接数据读写的线程组
        NioEventLoopGroup workerGroup =new NioEventLoopGroup();
        //服务器启动-引导类
        ServerBootstrap serverBootstrap =new ServerBootstrap();
        //核心配置部分
        serverBootstrap
                //配置两大线程组
                .group(bossGroup,workerGroup)
                //指定IO模型 如果要设定成BIO的模型，则改成OioServerSocketChannel.class，NIO则是NioServerSocketChannel
                .channel(NioServerSocketChannel.class);

        //设置客户机连接处理器链
        setServerBootstrapChildHandler(serverBootstrap);
        //设置服务器监听处理器
        setServerBootstrapHandler(serverBootstrap);
        //设置额外设置
        setServerBootstrapExtraConfig(serverBootstrap);
        //绑定端口，监听开始
        bind(serverBootstrap,port,0);
    }

    //handler方法，指定服务器启动过程中的一些逻辑
    public static void setServerBootstrapHandler(ServerBootstrap serverBootstrap){
        //handler方法，指定服务器启动过程中的一些逻辑，一般用不到，NioServerSocketChannel是Nio的服务器连接的抽象，如serverSocket
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            //服务端--
            protected void initChannel(NioServerSocketChannel nioServerSocketChannel)  {
                System.out.println("服务器启动中");
            }
        });
    }
    //其他配置部分
    public static void setServerBootstrapExtraConfig(ServerBootstrap serverBootstrap){
        //其他配置部分
        serverBootstrap
                //给Server连接维护一个map
                //.attr(AttributeKey.newInstance("serverName"), "nettyServer")
                //给每条连接连接维护map
                //.childAttr(clientKey, "随便输入点东西看看行不行")
                //存放已经三次握手的请求的队列的最大长度
                .option(ChannelOption.SO_BACKLOG, 1024)
                //开启TCP底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //实时性设置，开则有数据发送马上发送，关泽减少发送次数
                .childOption(ChannelOption.TCP_NODELAY, true)
        ;
    }
    public static void bind(ServerBootstrap serverBootstrap,int port,int time)
    {
        final int next=time+1;
        if(time>5){
            System.out.println("端口监听失败，停止服务，请检查端口是否被占用并更换端口！");
            return;
        }
        //Lambda写法
        serverBootstrap.bind(port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("端口["+port+"]第"+time+"次监听成功！");
            }
            else{
                System.out.println("端口["+port+"]第"+time+"次监听失败！");
                //重新连接，端口自动增一重试
                //但服务器一般不建议更换端口，所以监听失败便停止
                bind(serverBootstrap, port,next);
            }
        });
    }
}
