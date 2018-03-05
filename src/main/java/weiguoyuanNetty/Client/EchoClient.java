package weiguoyuanNetty.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by william on 2018/2/2.
 * 客户端使用主机和端口参数来连接远程地址
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)throws Exception{
                            ch.pipeline().addLast(new EchoClientHandler());//当连接创建时 一个EchoClientHandler实例会被安装到(该Channel的)ChannelPipeline中
                        }
                    });
            ChannelFuture f = b.connect().sync();//connect()被调用后BootStrap将会创建一个新的Channel 连接到远程节点并返回一个ChannelFuture 其将会在连接操作完成后接收到通知
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception{
//        if(args.length!=2){
//            System.err.println("Usage: "+EchoClient.class.getSimpleName()+"<host> <port>");
//            return;
//        }
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
        new EchoClient("127.0.0.1",8379).start();
    }
}
