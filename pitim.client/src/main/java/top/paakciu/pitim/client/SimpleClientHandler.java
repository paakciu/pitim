package top.paakciu.pitim.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author paakciu
 * @ClassName: SimpleHandler
 * @date: 2021/7/17 22:41
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("进入handler被激活的状态");
        Scanner scanner=new Scanner(System.in);
        new Thread(()->{
            System.out.println("可以输入一句话到服务端中：");
            String str=scanner.next();
            System.out.println(new Date() + ": 客户端写出数据:"+str);
            // 1.获取数据
            ByteBuf buffer = getByteBuf(str,ctx);
            // 2.写数据
            ctx.channel().writeAndFlush(buffer);
        }).start();
    }
    private ByteBuf getByteBuf(String str,ChannelHandlerContext ctx) {
        if(Objects.isNull(str) || "".equals(str)){
            return null;
        }
        byte[] bytes = str.getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
        super.channelRead(ctx, msg);
    }
}
