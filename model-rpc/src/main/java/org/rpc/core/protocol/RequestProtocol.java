package org.rpc.core.protocol;

/**
 * 
 */
public class RequestProtocol {
    private String intefaceClassName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] patameterValues;

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

    public Object[] getPatameterValues() {
        return patameterValues;
    }

    public void setPatameterValues(Object[] patameterValues) {
        this.patameterValues = patameterValues;
    }
}
