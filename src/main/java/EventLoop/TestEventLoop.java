package EventLoop;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 2018/3/2.
 * ScheduledExecutorService需要额外创建线程去执行任务 如果有大量的任务执行 这是一个瓶颈
 * EventLoop 扩展了 ScheduledExecutorService
 * 执行这个任务的线程 是所有的IO操作和事件都由已经分配给了当前Channel以及EventLoop的那个Thread来处理
 * 没有了线程间额外的上下文的切换 EventLoop将负责处理一个Channel的整个生命周期内的所有事件 也从ChannelHandler的线程安全中解脱出来
 */
public class TestEventLoop {

    public void channelActive(ChannelHandlerContext ctx){
        Channel channel =ctx.channel();

        ScheduledFuture<?> future = channel.eventLoop().schedule(new Runnable() {
            public void run() {
                System.out.println("60 seconds later executor");
            }
        },60, TimeUnit.SECONDS);//经过60s Runnable将由Channel的EventLoop执行

        ScheduledFuture<?> future1 = channel.eventLoop().scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("60 seconds later executor");
            }
        },60,60,TimeUnit.SECONDS);//每隔60s Runnable将由Channel的EventLoop执行一次
        future1.cancel(false);//取消该任务
    }

}
