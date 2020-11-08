package org.rpc.core.client;

import org.rpc.core.protocol.RequestProtocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GRPCClient {
    /**
     * 通过动态代理实现对接口的代理类创建
     * 代理类中实现网络通信，完成协议在网络中的发送与接收。
     *
     * @param interfaceClass  接口的类对象，所有的信息都有
     * @param address  网络通信的sockt
     * @param <T>
     * @return
     */
    public static <T>T getRemoteProxy(Class<T> interfaceClass, InetSocketAddress address){
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try(Socket socket = new Socket()){
                            try(
                                    //获取输出流(一个处理流)   序列化流
                                    ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
                                    //获取输入流 反序列化
                                    ObjectInputStream deserializer = new ObjectInputStream(socket.getInputStream());
                                    ){
                                //获取、设置RPC传输协议
                                RequestProtocol requestProtocol = new RequestProtocol();
                                //填充属性
                                requestProtocol.setIntefaceClassName(interfaceClass.getName());
                                requestProtocol.setMethodName(method.getName());
                                requestProtocol.setParameterTypes(method.getParameterTypes());
                                requestProtocol.setParameterValues(args);
                                //序列化协议对象
                                serializer.writeObject(requestProtocol);
                                Object result = deserializer.readObject();

                                return result;

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
    }
}
