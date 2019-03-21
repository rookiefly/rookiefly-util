package com.rookiefly.commons.jar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashSet;

public class TraceVersion {
    private static final String VERSION = getVersion(TraceVersion.class, "0.4.0");
    private static final Logger logger = LoggerFactory.getLogger(TraceVersion.class);

    private TraceVersion() {
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void checkDuplicate(Class<?> cls) {
        checkDuplicate(cls.getName().replace('.', '/') + ".class");
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            String e = cls.getPackage().getImplementationVersion();
            if (e == null || e.length() == 0) {
                e = cls.getPackage().getSpecificationVersion();
            }

            if (e == null || e.length() == 0) {
                CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
                if (codeSource == null) {
                    logger.info("No codeSource for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
                } else {
                    String file = codeSource.getLocation().getFile();
                    if (file != null && file.length() > 0 && file.endsWith(".jar")) {
                        file = file.substring(0, file.length() - 4);
                        int i = file.lastIndexOf(47);
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }

                        i = file.indexOf(45);
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }

                        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
                            i = file.indexOf(45);
                            if (i < 0) {
                                break;
                            }

                            file = file.substring(i + 1);
                        }

                        e = file;
                    }
                }
            }

            return e != null && e.length() != 0 ? e : defaultVersion;
        } catch (Throwable throwable) {
            logger.error("return default version, ignore exception " + throwable.getMessage(), throwable);
            return defaultVersion;
        }
    }

    public static void checkDuplicate(String path) {
        try {
            Enumeration e = TraceVersion.class.getClassLoader().getResources(path);
            HashSet files = new HashSet();

            while (e.hasMoreElements()) {
                URL url = (URL) e.nextElement();
                if (url != null) {
                    String file = url.getFile();
                    if (file != null && file.length() > 0) {
                        files.add(file);
                    }
                }
            }

            if (files.size() > 1) {
                logger.error("Duplicate class " + path + " in " + files.size() + " jar " + files);
            }
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }

    }

    static {
        checkDuplicate(TraceVersion.class);
    }
}
