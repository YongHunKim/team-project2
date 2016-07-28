package com.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


class Room extends Frame implements Cloneable{
	TextArea ta;
	List list;
	TextField tf;
	JButton cancel;
	String id;
	String rn;
	
	Room(){
		ta = new TextArea();
		ta.enable(false);
		ta.setBackground(Color.WHITE);
		ta.setForeground(Color.black);
		list = new List();
		tf = new TextField();
		cancel = new JButton("나가기");

		Panel p1 = new Panel();
		p1.setLayout(new BorderLayout());
		Panel p2 = new Panel();
		p2.setLayout(new BorderLayout());
		
		p1.add(ta);
		p1.add(list, "East");
		p2.add(tf);
		p2.add(cancel, "East");
		add(p1);
		add(p2, "South");
	}
	Room(String name, String rn) {
		super(name+"님의 방"+"   방제목 :"+rn);
		this.rn=rn;
		id=name;
		ta = new TextArea();
		ta.enable(false);
		ta.setBackground(Color.WHITE);
		ta.setForeground(Color.black);
		list = new List();
		tf = new TextField();
		cancel = new JButton("나가기");

		Panel p1 = new Panel();
		p1.setLayout(new BorderLayout());
		Panel p2 = new Panel();
		p2.setLayout(new BorderLayout());
		
		p1.add(ta);
		p1.add(list, "East");
		p2.add(tf);
		p2.add(cancel, "East");
		add(p1);
		add(p2, "South");
		
	}
	public Room getRoom(){
		Room cloned = null;
		
		try {
			cloned=(Room)clone(); // 다운 캐스팅
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cloned;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}