package com.rookiefly.test.commons.concurrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockDemo {
	public static void main(String[] args) {
		try {
			File f = new File("D:" + File.separator + "lock.txt");
			FileOutputStream output = null;
			output = new FileOutputStream(f, true);
			FileChannel fc = output.getChannel();
			FileLock fLock = fc.tryLock();
			if (null != fLock) {
				System.out.println(f.getName() + " 文件被独占锁锁定30秒");
				try {
					Thread.sleep(30000);// 线程休眠3w毫秒
					fLock.release(); // 释放锁定
					System.out.println("文件锁定被解除");
				} catch (InterruptedException e) {
					System.out.println("线程休眠时发生了中断异常");
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
