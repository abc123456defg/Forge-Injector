package com.fyxar.injector.mapper;

import com.fyxar.injector.mapper.enums.MappingVersion;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MapperTransformer implements ClassFileTransformer {

    //TODO - uh, allow user to choose different versions

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            return AutoRemapper.remap(MappingVersion.BountifulUpdate, classfileBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classfileBuffer;
    }

}
