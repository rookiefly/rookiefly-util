package com.rookiefly.commons.directory;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * 代码来源  http://git.oschina.net/J_Sky/Direetory-Helper
 * 文件及目录迭代比较器。 比较两个目录的结构是否相同，列出不同的文件及目录的数组。
 * @version1.0.0
 * @author J_sky
 *
 */
public class FileCompare implements Callable<Vector<File>> {

	public FileCompare() {

	}
/**
 *  FileCompare 构造器
 * @param dira 源目录Directory （比较目录）
 * @param dirb 目标目录Directory （被比较目录）
 * @param isDir 是否比较目录不同
 * @param isSubDir 是否比较子目录不同
 * @param isFile 是否比较文件不同
 * @param isFileSize 是否比较文件大小不同
 */
	public FileCompare(File dira, File dirb,boolean isDir,boolean isSubDir,boolean isFile,boolean isFileSize) {
		this.Dir_a = dira;
		this.Dir_b = dirb;
		this.isDir=isDir;
		this.isSubDir = isSubDir;
		this.isFile = isFile;
		this.isFileSize= isFileSize;

	}

	/**
	 * 源目录Directory （比较目录）
	 */
	private File Dir_a;

	/**
	 * 目标目录Directory （被比较目录）
	 */
	private File Dir_b;



	/**
	 * 是否比较目录不同
	 */
	private boolean isDir = true;
	/**
	 * 是否比较子目录不同
	 */
	private boolean isSubDir;

	/**
	 * 是否比较文件不同
	 */
	private boolean isFile = true;
	/**
	 * 是否比较文件大小不同
	 */

	private boolean isFileSize;



	/**
	 * @return dir_a
	 */
	public File getDir_a() {
		return Dir_a;
	}

	/**
	 * @param dir_a
	 *            要设置的 dir_a
	 */
	public void setDir_a(File dir_a) {
		Dir_a = dir_a;
	}

	/**
	 * @return dir_b
	 */
	public File getDir_b() {
		return Dir_b;
	}

	/**
	 * @param dir_b
	 *            要设置的 dir_b
	 */
	public void setDir_b(File dir_b) {
		Dir_b = dir_b;
	}





	/**
	 * 目录比较的核心方法 进行比较后，把结果存放在{@link fileVector}比较
	 * @param filea 源目录。
	 * @param fileb 目标目录。
	 */
	private Vector<File> compare(File filea, File fileb) {
		//返回的比较结果数组初始化。
		Vector<File> tempFileVector = new Vector<File>();
		File[] arr_a = filea.listFiles();// 原始目录下的目录及文件数组。
		File[] arr_b = fileb.listFiles();// 目标目录下的文件及数组。
		System.out.println("原始目录下的目录及文件数组数量:"+arr_a.length);
		System.out.println("目标目录下的目录及文件数组数量:"+arr_b.length);

		// 原始目录为空无动作，本循环自然结束。
		if (arr_a.length == 0) {
			System.out.println("原始目录为空无动作，本循环自然结束");
			return tempFileVector;
		}

		// 目标目录为空，原始目录不为空，则直接把结果加入比较结果集中返回结果。
		if (arr_b.length == 0 & arr_a.length != 0) {
			// 把比较数据加入比较结果集合中。
			System.out.println("目标目录为空，原始目录不为空，则直接把结果加入比较结果集中返回结果");
			for (int i = 0; i < arr_a.length; i++) {
				//系统设置了进行目录比较
				System.out.println("系统设置了进行目录比较");
				if (isDir) {

					if (arr_a[i].isDirectory()) {
						System.out.println("发现一个不同的目录");
						tempFileVector.add(arr_a[i]);
					}
				}
				if (isFile) {
					// 系统设置比较文件
					System.out.println("系统设置了比较文件");

					if (arr_a[i].isFile()) {
						System.out.println("发现一个不同的文件");
						tempFileVector.add(arr_a[i]);
					}
				}
			}
			System.out.println("发现原始目录中有" + tempFileVector.size() + "处不同");
			return tempFileVector;

		}

		// 如果原始目录和目标目录都不为空，将进行一些循环比较。
		if (arr_a.length != 0 & arr_b.length != 0) {
			for (File stra : arr_a) {
				// System.out.println(stra.getPath());
				boolean kk = true;// 临时定义比较结果的布尔值。
				for (File strb : arr_b) {
					// System.out.println(strb.getPath());

					// 如果两个file文件名相同,同时为目录或同时为文件，则比较相同，不添加到比较结果中。
					// .equals 比较字符串如果相同
					if (stra.getName().equals(strb.getName())) {

						// 如果文件相同且两个都是目录
						if (stra.isDirectory() & strb.isDirectory()) {
							System.out.println(stra.getPath() + "++比较++"
									+ strb.getPath() + "进行中");
							// 允许比较子目录。
							if (isSubDir) {
								System.out.println("允许比较子目录，递归比较开始");
								tempFileVector.addAll(compare(stra, strb));// 如果两个目录名称相同，应该递归比较其目录下的内容。
								kk = false;// 不添加到结果中。
								break;
							} else {
								System.out.println("禁止了比较子目录");
								kk = false;// 不添加到结果中。
								break;
							}

						}

						// 如果两个文件名相同且都是文件
						if (stra.isFile() & strb.isFile()) {
							// 如果设置了比较文件K值大小。
							if (isFileSize) {
								if (stra.length() == strb.length()) {
									System.out.println("系统设置了比较文件大小K值");
									System.out
											.println(stra.length()
													+ "   KKK值 值 值    "
													+ strb.length());
									kk = false;
									break;// 结束当前循环
								} else {
									kk = true;
									break;
								}
							} else {
								kk = false;// 如果不比较文件K值大小，则不添加到结果中
								break;
							}

						}

					}
				}

				if (kk) {

					if (isDir) {
						if (stra.isDirectory()) {
							System.out.println("系统设置了进行目录比较");
							System.out.println(stra.getPath());// 打印出比较不同的file字符串
							tempFileVector.add(stra);
						}
					}
					if (isFile) {
						if (stra.isFile()) {
							System.out.println("系统设置了文件比较");
							System.out.println(stra.getPath());// 打印出比较不同的file字符串
							tempFileVector.add(stra);// 把比较出不同的
							// 原始目录比较出来的添加到结果集中。
						}
					}
				}
			}
		}
		System.out.println("发现比较结果数：" + tempFileVector.size());
		return tempFileVector;
	}
	@Override
	/***
	 * 线程实现的方法
	 * @return 比较结果（包括不同的目录及文件的数组集合）
	 * @throws Exception
	 */
	
	public Vector<File> call() throws Exception {
		//线程结束后直接返回比较结果集（一个包括比较出不同的文件数组）
		return compare(Dir_a, Dir_b);
	}

}
