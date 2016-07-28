package com.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame{
	
	Join join_frame = new Join();
	WaitingRoom wr = new WaitingRoom();
	MakeRoom mr = new MakeRoom();
	Room room = new Room();
	
	JPanel global = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JLabel id = new JLabel();
	JLabel pw = new JLabel();
	JTextField idTF = new JTextField();
	JTextField pwTF = new JTextField();
	JTextField chatTF = new JTextField();
	JButton cancel = new JButton();
	JButton ok = new JButton();
	JButton join = new JButton();
	Font f = new Font("SansSerif", 0, 12);
	
	public Login() {		
		init();
	}
	
	public void init(){
		setTitle("로그인");
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.getContentPane().setBackground(new Color(249, 255, 255));
		this.getContentPane().setLayout(null);		
		global.setBorder(BorderFactory.createEtchedBorder());
		global.setOpaque(false);
		global.setBounds(new Rectangle(3, 3, 246, 114));
		global.setLayout(gridLayout1);
		gridLayout1.setRows(3);
		gridLayout1.setColumns(1);
		gridLayout1.setVgap(5);
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setOpaque(false);
		jPanel3.setLayout(null);
		jPanel2.setOpaque(false);
		jPanel2.setLayout(null);
		jPanel1.setOpaque(false);
		jPanel1.setLayout(null);
		id.setFont(new java.awt.Font("SansSerif", 0, 12));
		id.setText("  아 이 디  : ");
		id.setBounds(new Rectangle(6, 3, 66, 27));
		pw.setBounds(new Rectangle(6, 0, 66, 27));
		pw.setFont(new java.awt.Font("SansSerif", 0, 12));
		pw.setText("  비밀번호  : ");
		idTF.setNextFocusableComponent(pwTF);
		idTF.setBounds(new Rectangle(78, 3, 163, 27));
		pwTF.setNextFocusableComponent(ok);
		pwTF.setBounds(new Rectangle(78, 0, 163, 27));
		cancel.setFont(new java.awt.Font("SansSerif", 0, 12));
		cancel.setBorder(BorderFactory.createRaisedBevelBorder());
		cancel.setText("종료");
		cancel.setBounds(new Rectangle(160, 2, 60, 26));
		
		ok.setBounds(new Rectangle(95, 2, 60, 26));
		ok.setFont(new java.awt.Font("SansSerif", 0, 12));
		ok.setBorder(BorderFactory.createRaisedBevelBorder());
		ok.setNextFocusableComponent(cancel);
		ok.setText("로그인");
		join.setFont(new java.awt.Font("SansSerif", 0, 12));
		join.setBorder(BorderFactory.createRaisedBevelBorder());
		join.setText("회원가입");
		join.setBounds(new Rectangle(30, 2, 60, 26));
		this.getContentPane().add(global, null);
		global.add(jPanel1, null);
		jPanel1.add(id, null);
		jPanel1.add(idTF, null);
		global.add(jPanel2, null);
		jPanel2.add(pw, null);
		jPanel2.add(pwTF, null);
		global.add(jPanel3, null);
		jPanel3.setLayout(null);
		jPanel3.setLayout(null);
		jPanel3.add(cancel, null);
		jPanel3.add(ok, null);
		jPanel3.add(join,null);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(200,200,269,155);
		setVisible(true);
		
		Service service = new Service(this);
		
		ok.addActionListener(service);
		cancel.addActionListener(service);
		join.addActionListener(service);
		join_frame.join_cancel.addActionListener(service);
		join_frame.jungbok.addActionListener(service);
		join_frame.join_ok.addActionListener(service);
		wr.b1.addActionListener(service);
		wr.b2.addActionListener(service);
		wr.b4.addActionListener(service);
		wr.chat_tf.addActionListener(service);
		mr.b1.addActionListener(service);
		mr.b2.addActionListener(service);
		room.cancel.addActionListener(service);
		
	}
	public static void main(String[] args) {
		new Login();
	}
}
