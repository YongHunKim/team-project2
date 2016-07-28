package com.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;

public class WaitingRoom extends JFrame implements ItemListener, AdjustmentListener{
	JTable table1;
	DefaultTableModel model1,model2;
	List list = new List();
	JTextArea chat_area;
	JTextField chat_tf = new JTextField();
	JButton b1,b2,b3,b4;
	JLabel whom = new JLabel();
	JScrollBar jsB;
	String roomName;
	
	
	public WaitingRoom() {
		String[][] row1 = new String[0][3];
		String[] col1 = {"방이름","공개/비공개","인원"};
		setTitle("대기실");
		model1 = new DefaultTableModel(row1, col1);
		table1 = new JTable(model1);		
		JScrollPane js1 = new JScrollPane(table1);
		
		
		chat_area = new JTextArea();
		JScrollPane js2 = new JScrollPane(chat_area);
		JScrollPane js3 = new JScrollPane(list);
		whom.setEnabled(false);
		whom.setFont(new java.awt.Font("SansSerif", 0, 12));
		whom.setRequestFocusEnabled(false);
		whom.setBounds(new Rectangle(7, 278, 61, 27));
		
		b1 = new JButton("방만들기");
		b2 = new JButton("방들어가기");
		b3 = new JButton("쪽지보내기");
		b4 = new JButton("나가기");
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 2,5,5));
		
		p.add(b1);p.add(b2);
		p.add(b3);p.add(b4);
		
		//레이아웃 추가
		setLayout(null);
		js1.setBounds(10,15,500,350);
		js2.setBounds(10,370,500,150);
		js3.setBounds(515,15,265,350);
		chat_tf.setBounds(10,530,500,30);
		//box.setBounds(680, 320, 100, 30);
		p.setBounds(515, 470, 265, 80);
		setSize(800, 600);
		list.addItemListener(this);
		table1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(table1.getSelectedRow() > -1){
					int select = table1.getSelectedRow();
					roomName = (String) table1.getValueAt(select, 0);
					roomName+= "/"+table1.getValueAt(select, 1);
				}
			}
		});		
		add(js1);add(js2);add(js3);
		add(chat_tf);
		add(p);
		
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		jsB.setValue(jsB.getMaximum() );
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		String item = list.getSelectedItem();
		System.out.println(item);
		whom.setText(item);
	}

	
}
