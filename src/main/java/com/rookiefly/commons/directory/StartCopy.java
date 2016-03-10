package com.rookiefly.commons.directory;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JProgressBar;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * 代码来源  http://git.oschina.net/J_Sky/Direetory-Helper
 * 文件同步窗口。
 * @version 1.0.0
 * @author J_Sky
 *
 */
public class StartCopy extends JFrame {

	private JPanel contentPane;
	private JRadioButton RButton;
	private JRadioButton RButton_1;
	/**
	 * 源目录比较结果集AAA
	 */
	private Vector<File> v_a;
	/**
	 * 目标目录比较结果集AAA
	 */
	private Vector<File> v_b;
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
	private JProgressBar progressBar;

	/**
	 * @return v_a
	 */
	public Vector<File> getV_a() {
		return v_a;
	}

	/**
	 * @param v_a
	 *            要设置的 v_a
	 */
	public void setV_a(Vector<File> v_a) {
		this.v_a = v_a;
	}

	/**
	 * @return v_b
	 */
	public Vector<File> getV_b() {
		return v_b;
	}

	/**
	 * @param v_b
	 *            要设置的 v_b
	 */
	public void setV_b(Vector<File> v_b) {
		this.v_b = v_b;
	}

	/**
	 * @return dir_a
	 */
	public String getDir_a() {
		return dir_a;
	}

	/**
	 * @param dir_a
	 *            要设置的 dir_a
	 */
	public void setDir_a(String dir_a) {
		this.dir_a = dir_a;
	}

	/**
	 * @return dir_b
	 */
	public String getDir_b() {
		return dir_b;
	}

	/**
	 * @param dir_b
	 *            要设置的 dir_b
	 */
	public void setDir_b(String dir_b) {
		this.dir_b = dir_b;
	}
	
	/**
	 * @return 是否选择了顺序同步
	 */
	public boolean getRButton() {
		return RButton.isSelected();
	}

	/**
	 * @return 是否选择了反向同步
	 */
	public boolean getRButton_1() {
		return RButton_1.isSelected();
	}

	/**
	 * 开始复制同步程序。
	 * 
	 * @throws IOException
	 */

	/**
	 * Create the frame.
	 */
	public StartCopy() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});
		try {
			String kk = UIManager.getSystemLookAndFeelClassName();// 获得当前系统样式名称字符串。
			UIManager.setLookAndFeel(kk);// 加载样式。
			SwingUtilities.updateComponentTreeUI(this);// 应用给当前的窗口。
		} catch (Exception e) {
			// TODO: handle exception
		}
		setTitle("准备同步比较结果......");
		setVisible(true);
		setResizable(false);
		// setAlwaysOnTop(true);
		setBounds(300, 300, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		JLabel lblNewLabel = new JLabel(
				"准备复制文件同步目录，请选择同步方向。");
		lblNewLabel.setForeground(SystemColor.inactiveCaptionText);
		panel.add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "同步选项",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_1.add(panel_3, BorderLayout.NORTH);

		RButton = new JRadioButton("顺序同步");
		RButton.setToolTipText("按着原始目录同步目标目录的方向复制不相同的文件。");
		RButton.setSelected(true);
		panel_3.add(RButton);

		RButton_1 = new JRadioButton("反向同步");
		RButton_1.setToolTipText("按着从目标目录复制到原始目录中不同的文件。");
		panel_3.add(RButton_1);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("ok");
		btnNewButton.setToolTipText("开始复制同步文件。");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (RButton.isSelected()) {
					System.out.println("准备创建线程开始复制"+v_a.size());
					CopyThread copy = new CopyThread("copy", v_a, dir_a, dir_b, clb, cta, progressBar);
					copy.start();
					
				}
				if (RButton_1.isSelected()) {
					System.out.println("准备创建线程开始复制"+v_b.size());
					CopyThread copy1 = new CopyThread("copy", v_b, dir_b, dir_a, clb, cta, progressBar);
					copy1.start();
				}
			}
		});
		panel_2.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("cancel");
		btnNewButton_1.setToolTipText("取消");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel_2.add(btnNewButton_1);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(RButton);
		buttonGroup.add(RButton_1);

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		progressBar.setForeground(SystemColor.activeCaption);
		progressBar.setToolTipText("复制进度");
		progressBar.setStringPainted(true);
		progressBar.setEnabled(false);
		
		panel_4.add(progressBar, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_4.add(scrollPane, BorderLayout.CENTER);
		
		cta = new JTextArea();
		cta.setText("操作日志，如果发生同步错误这里做一些错误记录。\r\n");
		scrollPane.setViewportView(cta);

		clb = new JLabel("点击ok开始复制文件.");
		panel_4.add(clb, BorderLayout.SOUTH);
	}


	
}
