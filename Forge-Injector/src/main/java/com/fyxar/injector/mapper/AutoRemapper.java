package com.fyxar.injector.mapper;

import com.fyxar.injector.mapper.enums.MappingVersion;
import com.fyxar.injector.mapper.mappings.Mapping;
import com.fyxar.injector.mapper.mappings.impl.ClassMapping;
import com.fyxar.injector.mapper.mappings.impl.FieldMapping;
import com.fyxar.injector.mapper.mappings.impl.MethodMapping;
import org.objectweb.asm.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AutoRemapper {

    //TODO: make the code look fucking cleaner, please god save me

    public static byte[] remap(MappingVersion version, byte[] classBytes) throws Exception {
        MappingList list = new MappingList(version);

        for(Mapping m : list) {
            ClassReader cr = new ClassReader(classBytes);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                    return new MethodVisitor(Opcodes.ASM7, methodVisitor) {
                        @Override
                        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                            if(m instanceof ClassMapping) {
                                ClassMapping cm = (ClassMapping) m;
                                if (owner.equals(cm.getDeobfName().replace(".class", ""))) {
                                    super.visitFieldInsn(opcode, cm.getObfName().replace(".class", ""), name, desc);
                                } else {
                                    super.visitFieldInsn(opcode, owner, name, desc);
                                }
                            }else if(m instanceof FieldMapping) {
                                FieldMapping fm = (FieldMapping) m;
                                if (owner.equals(fm.getDeobfOwner().replace(".class", ""))
                                && name.equals(fm.getDeobfName())) {
                                    super.visitFieldInsn(opcode, fm.getObfOwner(), fm.getObfName(), desc);
                                } else {
                                    super.visitFieldInsn(opcode, owner, name, desc);
                                }
                            }else {
                                super.visitFieldInsn(opcode, owner, name, desc);
                            }
                        }

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                            if(m instanceof ClassMapping) {
                                ClassMapping cm = (ClassMapping) m;
                                if (owner.equals(cm.getDeobfName().replace(".class", ""))) {
                                    super.visitMethodInsn(opcode, cm.getObfName().replace(".class", ""), name, descriptor, isInterface);
                                } else {
                                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                }
                            }else if(m instanceof MethodMapping) {
                                MethodMapping mm = (MethodMapping) m;
                                if (owner.equals(mm.getDeobfName().replace(".class", "")) && name.equals(mm.getDeobfName())) {
                                    super.visitMethodInsn(opcode, mm.getObfOwner(), mm.getObfName(), descriptor, isInterface);
                                } else {
                                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                }
                            }else {
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                            }
                        }
                    };
                }
            };

            cr.accept(classVisitor, 0);
            return cw.toByteArray();
        }

        return classBytes;
    }
	
}
