package com.rookiefly.commons.directory;

import jodd.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;


/**
 * 代码来源  http://git.oschina.net/J_Sky/Direetory-Helper
 * 复制文件的线程。
 * @version1.0.0
 * @author J_sky
 *
 */
public class CopyThread extends Thread {
	/**
	 * 源目录比较结果集AAA
	 */
	private Vector<File> v_a;

	/**
	 * 源目录路径字符创
	 */
	private String dir_a;
	/**
	 * 目标目录路径字符创
	 */
	private String dir_b;
	/**
	 * 复制进度状态栏，用来打印一些复制信息。
	 */
	private JLabel clb;
	/**
	 * 错误日志文本框，用来打印错误日志。
	 */
	private JTextArea cta;
	/**
	 * 进度条
	 */
	private JProgressBar progressBar;

/**
 * 复制线程的构造器
 * @param name 线程名称
 * @param va 需要复制的数据
 * @param dira 源目录名称字符串
 * @param dirb 目标目录名称字符串
 * @param clb 状态栏打印
 * @param cta 日志打印
 * @param prob 进度条
 */
	public CopyThread(String name, Vector<File> va,
			String dira, String dirb, JLabel clb, JTextArea cta,
			JProgressBar prob) {
		super(name);
		this.v_a = va;
		this.dir_a = dira;
		this.dir_b = dirb;
		this.clb = clb;
		this.cta= cta;
		this.progressBar = prob;

	}

	// 线程主体
	public void run() {
		//统计文件及目录数目，方便查看复制进度。

		//开始复制
		scopy(statistics(v_a), dir_a, dir_b);
	}

	//递归统计统计文件及目录数目，方便查看复制进度。
	private Vector<File> statistics(Vector<File> v_a) {
		System.out.println("统计需要复制的文件。"+v_a.size());
		Vector<File>tempvecFiles = new Vector<File>();//临时存放结果的数组。
		for (File file : v_a) {
			//如果此文件为目录
			if (file.isDirectory()) {
//				返回此目录的文件集合
				File[] files = file.listFiles();
				if (files.length > 0) {
					tempvecFiles.addAll(statistics(arrToVector(files)));//递归开始
				}else {
					tempvecFiles.add(file);//如果目录为空，则添加到vector中。
				}
			}else {
				tempvecFiles.add(file);//如果为文件直接添加到vector中。
			}
		}
		return tempvecFiles;
	}

	/**
	 * 同步复制所有文件的方法
	 * @param v_a
	 * @param dir1
	 * @param dir2
	 */
	private void scopy(Vector<File> v_a,  String dir1,String dir2) {
		System.out.println("开始复制文件。");
		cta.setText("");

		int kk = 0; // 错误数。
		int mm = v_a.size();
		System.out.println("需要复制的文件数："+v_a.size());
		progressBar.setValue(0);// 还原进度条为0
		progressBar.setMaximum(mm);// 设置最大总数为要复制文件的最大数。
		int nn = 0;// 已经复制的文件和目录总数。
		for (int i = 0; i < v_a.size(); i++) {
			
			String cpath = v_a.get(i).getPath().replace(dir1, dir2);//交换根目录，创建目标文件的路径，用来准备复制。
			File file = new File(cpath);
			if (v_a.get(i).isFile()) {
				try {
					System.out.println(v_a.get(i).getPath());
					System.out.println(v_a.get(i).getPath());
					FileUtil.copyFile(v_a.get(i), file);
					System.out.println("复制文件：" + v_a.get(i) + "+++++" + v_a.get(i).getPath());
					clb.setText("复制文件：" + v_a.get(i) + "到目录" + dir2);
					cta.append("复制文件：" + v_a.get(i) + "到目录" + dir2 + "\r\n");
					nn++;
					progressBar.setValue(nn);
				} catch (java.io.FileNotFoundException e1) {
					// System.out.println(e1);
					cta.append(e1 + "\r\n");
					kk++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (v_a.get(i).isDirectory()) {
				try {
					FileUtil.copyDir(v_a.get(i), file);
					System.out.println("复制目录：" + v_a.get(i) + "+++++" + v_a.get(i).getPath());
					clb.setText("复制目录：" + v_a.get(i) + "到目录" + dir2);
					cta.append("复制目录：" + v_a.get(i) + "到目录" + dir2 + "\r\n");
					nn++;
					progressBar.setValue(nn);
				} catch (java.io.FileNotFoundException e2) {
					// System.out.println(e2);
					cta.append(e2 + "\r\n");
					kk++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		
		clb.setText("文件同步完毕哦！");
		JOptionPane.showMessageDialog(null, "复制目录及文件完毕！\r\n共有：" + mm
				+ "个文件和目录需要同步。同步结果为：" + nn + "个。\r\n文件复制错误：" + kk + "个");
	}
	
	/**
	 * 将目录数组转换成动态数组vector
	 * @param files 目录文件数组
	 * @return vector目录文件集合数组
	 */
	private Vector<File> arrToVector(File[] files){
		
		Vector<File>vector = new Vector<File>();//定义动态数组。
		for (File file : files) {
			vector.add(file);
		}
		return vector;
		
	}

}
