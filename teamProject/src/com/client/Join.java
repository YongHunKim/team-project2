package com.client;

import java.awt.*;
import javax.swing.*;

public class Join extends JFrame{
	JButton join_ok = new JButton("가입");
	JButton join_cancel = new JButton("취소");
	JButton jungbok = new JButton("중복확인");
	JLabel join = new JLabel("회  원  가  입");
	JLabel name = new JLabel("이         름 : ");
	JLabel id = new JLabel("아   이   디 : ");
	JLabel pw = new JLabel("비 밀 번 호 : ");
	JLabel phone = new JLabel("핸드폰번호 : ");
	JTextField name_tf = new JTextField();
	JTextField id_tf = new JTextField();
	JTextField pw_tf = new JTextField();
	JTextField phone_tf = new JTextField();
	
	public Join() {
		init();
	}
	
	public void init(){
		setTitle("회원 가입");
		setLayout(new GridLayout(6, 1));
		add(join);
		JPanel panel1 = new JPanel(new GridLayout(1, 2));
		panel1.add(name);
		panel1.add(name_tf);
		JPanel panel2 = new JPanel(new GridLayout(1, 3));
		panel2.add(id);
		panel2.add(id_tf);
		panel2.add(jungbok);
		JPanel panel3 = new JPanel(new GridLayout(1, 2));
		panel3.add(pw);
		panel3.add(pw_tf);
		JPanel panel4 = new JPanel(new GridLayout(1, 2));
		panel4.add(phone);
		panel4.add(phone_tf);
		JPanel panel5 = new JPanel();
		panel5.add(join_ok);
		panel5.add(join_cancel);
		add(panel1);add(panel2);add(panel3);add(panel4);add(panel5);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(300,300,300,300);
	}
}
