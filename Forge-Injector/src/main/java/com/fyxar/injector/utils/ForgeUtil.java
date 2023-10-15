package com.fyxar.injector.utils;

import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

public class ForgeUtil {

    public static boolean isForgeMain(Class<?> clazz) {
        return getInitMethod(clazz) != null;
    }

    public static Method getInitMethod(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (isInitMethod(method)) {
                return method;
            }
        }

        return null;
    }

    private static boolean isInitMethod(Method method) {
        if ("init".equals(method.getName()) && method.getParameterCount() == 1) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes[0].getName().endsWith("FMLInitializationEvent")) {
                return true;
            }
        }

        return false;
    }

    public static Class<?> getForgeInitClass(Instrumentation instrumentation) throws ClassNotFoundException {
        Class[] arrclass = instrumentation.getAllLoadedClasses();

        for(Class klass : arrclass) {
            if (klass.getName().equals("net.minecraftforge.fml.common.event.FMLInitializationEvent")) {
                return klass;
            }
        }

        return Object.class;
    }

}
