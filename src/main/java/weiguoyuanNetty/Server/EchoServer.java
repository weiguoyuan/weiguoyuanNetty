package weiguoyuanNetty.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by william on 2018/2/2.
 * 引导服务器
 * 绑定到服务器将在其上监听并接受传入连接请求的端口
 * 配置Channel 以将有关的入站消息通知给EchoServerHandler实例
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
       /* if(args.length != 1){
            System.err.println("Usage: "+EchoServer.class.getSimpleName()+"<port>");
            return;
        }
        int port = Integer.parseInt(args[0]);*/
        new EchoServer(8379).start();
    }

    public void start()throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();//ShareAble的可以用
        EventLoopGroup group = new NioEventLoopGroup();//用来事件处理 接收处理新的连接 读写数据 使用nio传输
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();//用来绑定服务器
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//将Channel设置为NioServerSocketChannel
                    .localAddress(new InetSocketAddress(port))//本地地址是个选定端口的InetSocketAddress 服务器将绑定这个地址来监听新的连接请求(绑定一个一直被监听的端口)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//当一个新的连接被接受时 一个新的子Channel将会创建ChannelInitializer将EchoServerHandler
                        @Override                                          //实例添加到该Channel的ChannelPipeline中 EchoServerHandler将会收到有关入站消息的通知
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();//异步的绑定服务器 调用sync()方法阻塞当前Thread直到绑定成功 绑定ServerChannel并返回一个ChannelFuture 绑定完成后接到通知 在那之后必须调用Channel.connect()方法来建立连接
            future.channel().closeFuture().sync();//应用程序将会阻塞等待直到服务器的Channel关闭
        }finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup 释放所有资源 包括创建的线程

        }
    }
}
