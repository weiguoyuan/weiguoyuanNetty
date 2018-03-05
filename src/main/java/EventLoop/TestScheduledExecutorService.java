package EventLoop;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 2018/3/2.
 * java.util.concurrent.Executors包添加的用于调度命令在指定延迟之后运行或者周期性执行
 */
public class TestScheduledExecutorService {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);//创建10个线程去执行周期任务 也可以创建单一线程有那个方法
    ScheduledFuture<?> future = executorService.schedule(new Runnable() {
        public void run() {
            System.out.println("60 seconds later executor");
        }
    },60, TimeUnit.SECONDS);
}
