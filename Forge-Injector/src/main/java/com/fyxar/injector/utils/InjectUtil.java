package com.fyxar.injector.utils;

import com.fyxar.injector.Injector;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class InjectUtil {

    public static Class<?> defineClass(Instrumentation instrumentation, byte[] classData, String className) {
        //Eh just define class or inject whatever
        try {
            Method define = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
            define.setAccessible(true);
            Class<?> klass = (Class<?>)define.invoke(getLoader(instrumentation), className, classData, 0, classData.length, null);
            return klass;
        }catch (Exception e) {

        }
        return Object.class;
    }

    private static ClassLoader getLoader(Instrumentation instrumentation) {
        //Here the minecraft loader, forge or vanilla idk
        try {
            Class[] arrclass = instrumentation.getAllLoadedClasses();
            for (Class klass : arrclass) {
                if (klass.getName().equals("net.minecraftforge.common.MinecraftForge")) {
                    return klass.getClassLoader();
                }
            }
        } catch (Exception ex) {
        }
        return Injector.class.getClassLoader();
    }

}
