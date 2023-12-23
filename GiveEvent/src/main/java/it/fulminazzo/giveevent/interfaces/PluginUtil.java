package it.fulminazzo.giveevent.interfaces;

import it.fulminazzo.reflectionutils.objects.ReflObject;
import it.fulminazzo.reflectionutils.utils.ReflUtil;

import java.lang.reflect.Method;

public interface PluginUtil {

    default <O> O invokeMethod(String methodName, Object... args) {
        return new ReflObject<>(this).getMethodObject(methodName, args);
    }

    default Method getMethod(String methodName, Object... args) {
        return ReflUtil.getMethod(this.getClass(), methodName, null, args);
    }
}
