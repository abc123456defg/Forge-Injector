package com.fyxar.injector;

import com.fyxar.injector.mapper.AutoRemapper;
import com.fyxar.injector.mapper.MapperTransformer;
import com.fyxar.injector.mapper.enums.MappingVersion;
import com.fyxar.injector.utils.ForgeUtil;
import com.fyxar.injector.utils.InjectUtil;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Injector {

    private static MapperTransformer transformer;

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(transformer = new MapperTransformer());
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        if (args == null || args.isEmpty()) {
            System.err.println("Please input a specify class file");
            return;
        }

        String filePath = args.trim();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        if (!file.getName().toLowerCase().endsWith(".jar")) {
            System.err.println("Not a JAR file: " + filePath);
            return;
        }

        try {
            processJarFile(file, instrumentation, "xd", "wow");
        } catch (Exception e) {
            e.printStackTrace();
        }

        instrumentation.removeTransformer(transformer);
    }

    private static void processJarFile(File file, Instrumentation instrumentation, Object... o) throws IOException, IllegalAccessException, InstantiationException {
        Object mainObj = null;
        Class<?> mainKlass = null;

        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').replaceAll(".class$", "");
                    byte[] classData = getClassData(jarFile, entry);

                    System.out.println(className);

                    Class<?> definedClass = InjectUtil.defineClass(instrumentation, classData, className);
                    if (ForgeUtil.isForgeMain(definedClass)) {
                        Object defined = definedClass.newInstance();
                        //System.out.println("Found the forge mod main class!");
                        mainObj = defined;
                        mainKlass = definedClass;
                    }
                }
            }
        }

        //It would be useless if we not init the main class after inject classes
        boolean isInited = false;
        if (mainKlass != null && mainObj != null) {
            Method method = ForgeUtil.getInitMethod(mainKlass);

            if (method != null) {
                method.setAccessible(true);

                try {
                    //Eh idk run the init method?
                    //Object event = ForgeUtil.getForgeInitClass(instrumentation).
                            //getDeclaredConstructor(o.getClass()).newInstance((Object) "Why not?");
                    Object realEvent = null;
                    method.invoke(mainObj, realEvent);
                    isInited = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] getClassData(JarFile jarFile, JarEntry entry) throws IOException {
        try (InputStream inputStream = jarFile.getInputStream(entry);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
}