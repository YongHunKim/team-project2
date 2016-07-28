package com.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MakeRoom extends JFrame implements ActionListener{
	
	JLabel la1,la2,la3,la4;
	JTextField tf;
	JRadioButton rb1,rb2;
	JPasswordField pf;
	JComboBox box;
	JButton b1,b2;
	
	public MakeRoom() {
		la1 = new JLabel("방이름");
		la2 = new JLabel("상태");
		la3 = new JLabel("비밀번호");
		la4 = new JLabel("인원");
		
		tf = new JTextField();
		pf = new JPasswordField();
		
		rb1 = new JRadioButton("공개");
		rb2 = new JRadioButton("비공개");
		
		//비밀번호가 비공개시에만 활성화
		la3.setVisible(false);
		pf.setVisible(false);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		rb1.setSelected(true);
		
		b1 = new JButton("방만들기");
		b2 = new JButton("취소");
		
		box = new JComboBox();
		for (int i = 2; i <= 6; i++) {
			box.addItem(i+"명");
		}
		
		//배치
		setLayout(null);
		la1.setBounds(10, 15, 50, 30);
		tf.setBounds(65, 15, 150, 30);
		
		la2.setBounds(10, 50, 50, 30);
		rb1.setBounds(65, 50, 70, 30);
		rb2.setBounds(140, 50, 70, 30);
		
		la3.setBounds(50, 85, 60, 30);
		pf.setBounds(115, 85, 100, 30);
		
		la4.setBounds(10, 120, 50, 30);
		box.setBounds(65, 120, 100, 30);
		
		JPanel p = new JPanel();
		p.add(b1);
		p.add(b2);
		p.setBounds(10, 155, 195, 35);
		
		add(la1);add(tf);
		add(la2);add(rb1);add(rb2);
		add(la3);add(pf);
		add(la4);add(box);
		add(p);
		
		setSize(240, 235);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		rb1.addActionListener(this);
		rb2.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == rb1){
			la3.setVisible(false);
			pf.setVisible(false);
			pf.setText("");
		}
		if(e.getSource() == rb2){
			la3.setVisible(true);
			pf.setVisible(true);
			pf.setText("");
			pf.requestFocus();
		}
	}
	
	public static void main(String[] args) {
		new MakeRoom();
	}

}
