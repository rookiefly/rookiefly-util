package com.rookiefly.commons.agent;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;

import java.security.ProtectionDomain;

/**
 * description: Transformer
 * author: xiaobai
 * data: 2016/8/30
 */
public class NewTransformer implements ClassFileTransformer {


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if (!className.equals("com.pnfsoftware.jeb.client.AbstractClientContext")) return null;
        try {
            ClassPool classPool = ClassPool.getDefault();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
            CtClass clazz = classPool.makeClass(byteArrayInputStream);
            CtMethod[] methods = clazz.getMethods();
            for (CtMethod method : methods) {
                if (method.getName().equals("ping")) {
                    return fixMethod(clazz, method.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] fixMethod(CtClass clazz, String oldmethodName) {
        try {
            CtMethod method = clazz.getDeclaredMethod(oldmethodName);
            StringBuilder bodyStr = new StringBuilder();
            bodyStr.append("{");
            bodyStr.append("System.out.println(\"开始热补丁\");\n");
            bodyStr.append("return 0;");
            bodyStr.append("}");
            method.setBody(bodyStr.toString());
            return clazz.toBytecode();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}