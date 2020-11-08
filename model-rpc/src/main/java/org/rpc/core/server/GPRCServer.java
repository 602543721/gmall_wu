package org.rpc.core.server;

import org.rpc.core.protocol.RequestProtocol;
import sun.nio.ch.ThreadPool;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC服务提供方实现类
 * 1.暴露调用服务接口
 * 2.启动服务端
 */
public class GPRCServer {
    //定义存储暴露服务列表 类名+实例对象
    Map<String,Object> serverMap = new ConcurrentHashMap<>(32);

    //自己定义的线程池
    ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(8,20,200, TimeUnit.MICROSECONDS,new ArrayBlockingQueue<Runnable>(10));
    /**
     * 暴露服务方法（可以被远程调用的方法，由服务提供者加入）
     * @param interfaceClass  接口的类对象
     * @param instance  要被调用方法的实例对象
     */
    public void publishServiceAPI(Class<?> interfaceClass,Object instance){
        this.serverMap.put(interfaceClass.getName(),instance);
        System.out.println("获取到要暴露的方法");
    }

    /**
     * 开启服务端的网络socket去进行网络通信
     * 启动自定义线程池去执行
     * @param port
     */
    public void start(int port){
        try{
            //创建服务端socket对象
            ServerSocket serverSocket = new ServerSocket();
            //serverSocket绑定端口号
            serverSocket.bind(new InetSocketAddress(port));
            while (true){
                //accept（）让线程进入阻塞，等待端口有值传入，再执行run方法
                poolExecutor.execute(new ServerTask(serverSocket.accept()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private class ServerTask implements Runnable{
        private final Socket socket;
        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        /**
         * run方法，通过再网络传输中获取的协议内容，去调用要执行的方法
         */
        @Override
        public void run() {
            //这里开始，拿到客户端的序列化流
            try(
                 //获取反序列化流
                 ObjectInputStream deserializer = new ObjectInputStream(socket.getInputStream());
                 //获取序列化流
                 ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());

                    ){
                //通过反序列化获取客户端传过来的协议，并从协议中去抽取类名方法名
                RequestProtocol requestProtocol = (RequestProtocol)deserializer.readObject();
                String interfaceClassName = requestProtocol.getIntefaceClassName();
                //根据传过来的接口全名 去暴露方法中找对应的实现类
                Object instance = serverMap.get(interfaceClassName);
                if(instance==null)
                    return;
                //当实现类不为空时，通过反射去获取要执行的方法（通过协议中的方法名+参数列表 找到指定方法）
                Method method = instance.getClass().getDeclaredMethod(requestProtocol.getMethodName(), requestProtocol.getParameterTypes());
                //通过反射去执行方法，传入实例对象instance以及 传输协议中参数的值
                Object result = method.invoke(instance, requestProtocol.getParameterValues());
                serializer.writeObject(result);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
