package com.rookiefly.commons.directory;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JList;

/**
 * 代码来源  http://git.oschina.net/J_Sky/Direetory-Helper
 * 目录比较同步助手。
 * @version1.1.1
 * @author J_Sky 
 * 本程序只实现了一些微不足道的功能，旨在学习JAVA。
 * 希望本程序能为初学者提供一些帮助，希望和一样在学习JAVA的过程中遇到迷惑的人会遇见光明。 JAVA C# php 易语言 交流
 * Q群：186960527
 * 
 * 2015-10-24 做一些更新：
 * 把程序的功能拆分出来，降低了程序的耦合性，把功能分离出来。
 * 创建了目录比较器类，方便后继功能的扩展。
 * 创建了文件复制线程类。
 * 优化了同步助手GUI中的一些错误。
 * 修复了一个重要的错误！
 *
 */
public class DirCompare {

	private JFrame frame;
	/**
	 * 目标目录BBB
	 */
	private JTextField dir2;
	/**
	 * 源目录AAA
	 */
	private JTextField dir1;

	/**
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 *            要设置的 frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * 底部状态条
	 */
	private JLabel lb;
	/**
	 * 比较目录返回集合AAA
	 */
	private Vector<File> vector_a = new Vector<File>();
	/**
	 * 被比较目录返回集合BBB
	 */
	private Vector<File> vector_b = new Vector<File>();
	/**
	 * 比较目录Jlist
	 */
	private JList list_a;

	/**
	 * @return list_a
	 */
	public JList getList_a() {
		return list_a;
	}

	/**
	 * @param list_a
	 *            要设置的 list_a
	 */
	public void setList_a(JList list_a) {
		this.list_a = list_a;
	}

	/**
	 * @return list_b
	 */
	public JList getList_b() {
		return list_b;
	}

	/**
	 * @param list_b
	 *            要设置的 list_b
	 */
	public void setList_b(JList list_b) {
		this.list_b = list_b;
	}

	/**
	 * 被比较目录Jlist
	 */
	private JList list_b;
	/**
	 * 比较包括子目录
	 */
	private JCheckBox c_isSubDir;
	/**
	 * 比较目录
	 */
	private JCheckBox c_dir;

	/**
	 * 比较文件
	 */
	private JCheckBox c_file;
	/**
	 * 比较修改文件时间
	 */
	private JCheckBox c_fileTime;
	/**
	 * 比较文件K值大小
	 */
	private JCheckBox c_fileSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DirCompare window = new DirCompare();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DirCompare() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		try {
			String kk = UIManager.getSystemLookAndFeelClassName();// 获得当前系统样式名称字符串。
			UIManager.setLookAndFeel(kk);// 加载样式。
			SwingUtilities.updateComponentTreeUI(frame);// 应用给当前的窗口。
		} catch (Exception e) {
			// TODO: handle exception
		}
		frame.setBounds(280, 250, 700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setResizable(false);
		frame.setTitle("目录比较工具1.1.1---By J_sky QQ：285911 ");

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "目标目录",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel.add(panel_1, BorderLayout.CENTER);

		dir2 = new JTextField();
		dir2.setEditable(false);
		dir2.setEnabled(false);
		dir2.setToolTipText("\u76EE\u6807\u76EE\u5F55\u5730\u5740\u680F\u3002");
		dir2.setText("");
		dir2.setColumns(60);
		panel_1.add(dir2);

		JButton button = new JButton("选择....");
		button.setToolTipText("\u8BF7\u9009\u62E9\u76EE\u6807\u76EE\u5F55\u3002");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 选择目标目录
				JFileChooser chooser2 = new JFileChooser();
				chooser2.setToolTipText("选择目标目录");
				chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int rt = chooser2.showOpenDialog(frame);
				if (rt == JFileChooser.APPROVE_OPTION) {
					dir2.setText(chooser2.getSelectedFile().getPath());
					lb.setText("目标目录：" + chooser2.getSelectedFile().getPath()
							+ "如果两个目录都已经选择好，可以开始比较了o(∩_∩)o ");
				}
			}
		});
		panel_1.add(button);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "原始目录",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panel.add(panel_2, BorderLayout.NORTH);

		dir1 = new JTextField();
		dir1.setEditable(false);
		dir1.setEnabled(false);
		dir1.setToolTipText("\u539F\u59CB\u76EE\u5F55\u5730\u5740\u680F");
		dir1.setText("");
		dir1.setColumns(60);
		panel_2.add(dir1);

		JButton button_1 = new JButton("选择....");
		button_1.setToolTipText("\u8BF7\u9009\u62E9\u539F\u59CB\u76EE\u5F55\u3002");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 获取源目录
				JFileChooser chooser1 = new JFileChooser();
				chooser1.setToolTipText("请选择源目录");
				chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int rt = chooser1.showOpenDialog(frame);
				if (rt == JFileChooser.APPROVE_OPTION) {
					dir1.setText(chooser1.getSelectedFile().getPath());
					lb.setText("源目录：" + chooser1.getSelectedFile().getPath()
							+ "请选择目标目录！");

				}
			}
		});
		panel_2.add(button_1);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setBorder(new TitledBorder(null, "目录比较选项",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		c_isSubDir = new JCheckBox("包含子目录");
		c_isSubDir
				.setToolTipText("比较目录是否包含子目录？");
		c_isSubDir.setSelected(true);
		panel_4.add(c_isSubDir);

		c_dir = new JCheckBox("比较目录");
		c_dir.setToolTipText("是否比较目录中的目录？");
		c_dir.setSelected(true);
		panel_4.add(c_dir);

		c_file = new JCheckBox("比较文件");
		c_file.setToolTipText("是否比较文件？");
		c_file.setSelected(true);
		panel_4.add(c_file);

		c_fileSize = new JCheckBox("比较文件大小");
		c_fileSize
				.setToolTipText("发现相同文件是否比较文件的大小不同？");
		panel_4.add(c_fileSize);

		c_fileTime = new JCheckBox("比较时间新旧");
		c_fileTime
				.setToolTipText("功能暂时没有实现。");
		c_fileTime.setEnabled(false);
		panel_4.add(c_fileTime);

		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setLayout(new GridLayout(1, 1, 0, 0));

		JButton btnNewButton = new JButton("开始比较");
		btnNewButton
				.setToolTipText("点击开始同步原始目录与目标目录中结构不同。");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 判断目录为空！
				if ("".equals(dir1.getText())) {
					showmg("请选择原始目录");
				} else if ("".equals(dir2.getText())) {
					showmg("请选择目标目录");
				} else {
					// 开始比较目录及文件。
					starCompare();
				}

			}
		});
		panel_5.add(btnNewButton);

		JButton btnNewButton_3 = new JButton("同步文件");
		btnNewButton_3
				.setToolTipText("点击开始同步目录中的不同文件与目录。");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stratCopy();
			}


		});
		panel_5.add(btnNewButton_3);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("原始目录", null, scrollPane, null);

		list_a = new JList();
		scrollPane.setViewportView(list_a);

		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("目标目录", null, scrollPane_1, null);

		list_b = new JList();
		scrollPane_1.setViewportView(list_b);

		lb = new JLabel("请选择原始目录和要比较的新目录.....");
		frame.getContentPane().add(lb, BorderLayout.SOUTH);

	}

	/**
	 * 开始比较目录
	 */
	public void starCompare() {
		int start = (int) System.currentTimeMillis();
		System.out.println("开始比较目录的不同---------------------------------------------------------------");

		ExecutorService exs = Executors.newCachedThreadPool();// 创建线程池。
		// 创建两个有返回值的线程任务
		FileCompare fc1 = new FileCompare(new File(dir1.getText()), new File(
				dir2.getText()), c_dir.isSelected(), c_isSubDir.isSelected(), c_file.isSelected(), c_fileSize.isSelected());
		FileCompare fc2 = new FileCompare(new File(dir2.getText()), new File(
				dir1.getText()),c_dir.isSelected(), c_isSubDir.isSelected(), c_file.isSelected(), c_fileSize.isSelected());
		// 执行线程并获取Future对象。
		Future<Vector<File>> f1 = exs.submit(fc1);
		Future<Vector<File>> f2 = exs.submit(fc2);

		try {
			vector_a = f1.get();//
			vector_b = f2.get();
			list_a.setListData(f1.get());//添加到列表框里
			list_b.setListData(f2.get());
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		int end = (int) System.currentTimeMillis();
		// System.out.println("比较索引耗时：" + (end - start) + "毫秒");
		JOptionPane.showMessageDialog(null, "目录比较完毕！此次比较耗时：" + (end - start)
				+ "毫秒！\r\n有关比较结果请关注结果列表框o(∩_∩)o ");
	}

	private void showmg(String mg) {
		JOptionPane.showMessageDialog(frame, mg);
	}
	
	/**
	 * 创建线程开始复制文件
	 */
	private void stratCopy() {
		// TODO 自动生成的方法存根
		// 开始同步文件 创建复制同步窗口，获取相关对象，发送需要同步的数据到复制窗口。
		
		
		if (vector_a.size()>0 || vector_b.size() >0) {
			System.out.println("开始同步文件 创建复制同步窗口，获取相关对象，发送需要同步的数据到复制窗口。"+vector_a.size()+vector_b.size());
			StartCopy sCopy = new StartCopy();
			sCopy.setV_a(vector_a);//
			sCopy.setV_b(vector_b);
			sCopy.setDir_a(dir1.getText());
			sCopy.setDir_b(dir2.getText());
			lb.setText("准备开始同步文件o(∩_∩o");
		}else {
			JOptionPane.showMessageDialog(frame, "没有比较数据怎么同步？");
		}

		
	}
}
