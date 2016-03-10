package com.rookiefly.commons.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * 
 * 
 * @author wanggaoxiang
 * @version $Id: FileDownloadUtils.java, v 0.1 2015年3月17日 下午4:24:23 wanggaoxiang<p>
 */
public class FileDownloadUtils {

    /**
     * 从web根目录的相对目录下载到浏览器
     * 
     * @param request
     * @param response
     * @param fileName 下载的文件名
     * @param baseDir 相对web根目录的目录名，默认upload
     * @throws Exception
     */
    public static void downloadFromWebRoot(HttpServletRequest request, HttpServletResponse response, String fileName,
                                           String baseDir) throws Exception {
        if (baseDir == null) {
            baseDir = FileUploadUtils.DEFAULT_BASE_DIR;
        }
        response.setContentType("application/octet-stream");
        request.setCharacterEncoding("UTF-8");
        String newFileName = new String(fileName.getBytes("UTF-8"), "UTF-8");
        String ctxPath = request.getServletContext().getRealPath("/") + baseDir;
        String downLoadPath = ctxPath + "/" + newFileName;
        long fileLength = new File(downLoadPath).length();
        response.setHeader("Content-disposition", "attachment; filename="
                                                  + new String(newFileName.getBytes("UTF-8"), "UTF-8"));
        response.setHeader("Content-Length", String.valueOf(fileLength));
        FileInputStream fis = new FileInputStream(downLoadPath);
        OutputStream out = response.getOutputStream();
        IOUtils.copy(fis, out);
        fis.close();
    }

    /**
     * 输入流下载到浏览器
     * 
     * @param fileStream 下载的文件输入流,下载完毕后不会关闭流
     * @param response
     * @param fileName 下载文件时生成的文件名称
     * @throws Exception
     */
    public static void downloadFromStream(InputStream fileStream, HttpServletResponse response, String fileName)
                                                                                                                throws Exception {
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        long fileLength = fileStream.available();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(fileLength));
        IOUtils.copy(fileStream, out);
    }

    /**
     * 根据文件服务器上文件的url路径下载文件
     * 
     * @param response
     * @param fileName
     * @param fileUrl
     * @throws Exception
     */
    public static void downloadFromUrl(HttpServletResponse response, String fileName, String fileUrl) throws Exception {
        try {
            InputStream in = extractStreamFromUrl(fileUrl);
            downloadFromStream(in, response, fileName);
        } catch (Exception e) {
            throw e;
        }
    }

    private static InputStream extractStreamFromUrl(String fileUrl) throws MalformedURLException, IOException {
        URL url = new URL(fileUrl);
        InputStream in = null;
        URLConnection uc = null;
        uc = url.openConnection();
        in = uc.getInputStream();
        return in;
    }

    /**
     * 下载文件服务器文件到本地目录
     * 
     * @param localDir 本地目录,目录必须存在
     * @param fileName 本地文件名
     * @param fileUrl 文件url
     * @throws Exception
     */
    public static void downloadFromUrlToLocal(String localDir, String fileName, String fileUrl) throws Exception {
        InputStream in = null;
        FileOutputStream fis = null;
        try {
            in = extractStreamFromUrl(fileUrl);
            File localFile = new File(localDir, fileName);
            fis = new FileOutputStream(localFile);
            IOUtils.copy(in, fis);
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * 下载文件服务器文件到本地目录
     * 
     * @param localDir 本地目录,目录必须存在
     * @param fileUrl 文件url
     * @throws Exception
     * @return 本地文件名称
     */
    public static String downloadFromUrlToLocal(String localDir, String fileUrl) throws Exception {
        String fileName = extractFileNameFromUrl(fileUrl);
        downloadFromUrlToLocal(localDir, fileName, fileUrl);
        return fileName;
    }

    public static String extractFileNameFromUrl(String fileUrl) {
        if (fileUrl.indexOf("?") > 0) {
            return fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?"));
        }
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
