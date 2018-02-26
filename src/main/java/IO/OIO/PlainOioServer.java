package IO.OIO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by william on 2018/2/26.
 * 未使用Netty的阻塞IO
 */
public class PlainOioServer {
    public void serve(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);//将服务器绑定到指定端口
        try {
            for(;;){
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from "+clientSocket);
                new Thread(new Runnable() {//创建一个新的线程来处理该连接
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes("UTF-8"));//将消息写给已连接的客户端
                            out.flush();
                            clientSocket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        finally {
                            try {
                                clientSocket.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();//启动线程
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
