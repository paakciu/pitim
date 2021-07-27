package top.paakciu.pitim.protocol.codec.codechandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import top.paakciu.pitim.protocol.codec.PacketCodec;
import top.paakciu.pitim.protocol.packet.BasePacket;

import java.util.List;

/**
 * @author paakciu
 * @ClassName: B2MPacketCodecHandler
 * @date: 2021/7/27 23:13
 */
public class B2MPacketCodecHandler extends ByteToMessageCodec<BasePacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BasePacket msg, ByteBuf out) throws Exception {
        PacketCodec.encode(out,msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodec.decode(in));
    }
}
