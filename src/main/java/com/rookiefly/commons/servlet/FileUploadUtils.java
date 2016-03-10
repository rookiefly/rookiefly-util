package com.rookiefly.commons.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传的工具类
 * 
 * @author wanggaoxiang
 * @version $Id: FileUploadUtils.java, v 0.1 2015年3月19日 下午3:36:02 wanggaoxiang<p>
 */
public class FileUploadUtils {
    //默认大小 50M
    public static final long   DEFAULT_MAX_SIZE = 52428800;
    //默认上传的地址
    public static final String DEFAULT_BASE_DIR = "upload";
    //默认的本地目录
    public static final String DEFAULT_LOCAL_DIR = "/taxi/file";

    /**
     * 上传文件到本地绝对目录
     * 
     * @param file 
     *              上传文件
     * @param localDir 
     *              本地绝对目录
     * @return
     * @throws Exception
     */
    public static final String upload2LocalDir(MultipartFile file, String localDir) throws Exception {
        if (localDir == null) {
            localDir = DEFAULT_LOCAL_DIR;
        }
        String filename = extractFilename(file, localDir);
        File localFile = new File(filename);
        file.transferTo(localFile);
        return filename;
    }
    
    /**
     * 上传文件到web项目根目录的相对目录
     * 
     * @param request
     * @param file 
     *              上传文件
     * @param baseDir 
     *              web项目根目录
     * @return
     * @throws Exception
     */
    public static final String upload(HttpServletRequest request, MultipartFile file, String baseDir) throws Exception {
        if (baseDir == null) {
            baseDir = DEFAULT_BASE_DIR;
        }
        String filename = extractFilename(file, baseDir);
        File desc = getAbsoluteFile(extractUploadDir(request), filename);
        file.transferTo(desc);
        return filename;
    }

    /**
     * 
     * 
     * @param uploadDir
     * @param filename
     * @return
     * @throws IOException
     */
    private static final File getAbsoluteFile(String uploadDir, String filename) throws IOException {
        if (uploadDir.endsWith("/")) {
            uploadDir = uploadDir.substring(0, uploadDir.length() - 1);
        }
        if (filename.startsWith("/")) {
            filename = filename.substring(0, uploadDir.length() - 1);
        }
        File desc = new File(uploadDir + "/" + filename);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 
     * 
     * @param file
     * @param baseDir
     * @return
     * @throws UnsupportedEncodingException
     */
    public static final String extractFilename(MultipartFile file, String baseDir) throws UnsupportedEncodingException {
        String filename = file.getOriginalFilename();
        int slashIndex = filename.indexOf("/");
        if (slashIndex >= 0) {
            filename = filename.substring(slashIndex + 1);
        }
        filename = baseDir + "/" + filename;
        return filename;
    }

    /**
     * 
     * 
     * @param request
     * @return
     */
    public static final String extractUploadDir(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }
}