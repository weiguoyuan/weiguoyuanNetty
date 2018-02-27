package IO.OIO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by william on 2018/2/27.
 * 使用Netty的oio
 */
public class NettyOioServer {
    public void server(int port)throws Exception{
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(OioServerSocketChannel.class)//使用OioEventLoopGroup以允许阻塞模式
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//指定ChannelInitializer 对于每个已接受的连接都调用它
                        @Override
                        public void initChannel(SocketChannel ch)throws Exception{
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//添加一个ChannelInboundHandlerAdapter以拦截和处理事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx)throws Exception{
                                    ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);//将消息写到客户端 并添加ChannelFutureListener以便消息一写完就关闭连接
                                }
                            });
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();//绑定服务器以接受连接
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();//释放所有资源
        }
    }
}
