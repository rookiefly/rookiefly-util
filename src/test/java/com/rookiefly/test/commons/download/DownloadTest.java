package com.rookiefly.test.commons.download;

import com.rookiefly.commons.servlet.FileDownloadUtils;


public class DownloadTest {

    public static void main(String[] args) throws Exception {

        FileDownloadUtils.downloadFromUrlToLocal("D:\\test", "http://test.kuaidadi.com:5100/fs/IMAGE/08/09/51/1/1510908.jpg");
    }

}
