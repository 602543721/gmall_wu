package org.rpc.core.protocol;

import java.io.Serializable;

/**
 * PRC的传输协议
 * 网络中我们要进行对象的传输，是要实现序列化的
 */
public class RequestProtocol implements Serializable {
    //接口全名称
    private String intefaceClassName;
    //调用方法名
    private String methodName;
    //参数类型列表
    private Class<?>[] parameterTypes;
    //参数值列表
    private Object[] parameterValues;

    public String getIntefaceClassName() {
        return intefaceClassName;
    }

    public void setIntefaceClassName(String intefaceClassName) {
        this.intefaceClassName = intefaceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }
}
