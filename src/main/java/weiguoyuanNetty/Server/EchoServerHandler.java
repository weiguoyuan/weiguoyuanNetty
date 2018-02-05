package weiguoyuanNetty.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by william on 2018/2/2.
 * 事件触发对应的channelRead channelReadComplete exceptionCaught
 * 在这些方法里写应用的逻辑 这样来解耦应用和网络 这是netty核心
 */
@ChannelHandler.Sharable//标示一个ChannelHandler可以被多个Channel安全地分享
public class EchoServerHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        ByteBuf in = (ByteBuf)msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));//输出消息到控制台
        ctx.write(in);//将接受到的消息写给发送者 而不是冲刷出站消息
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);//将消息冲刷到远程节点,并关闭该Channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
