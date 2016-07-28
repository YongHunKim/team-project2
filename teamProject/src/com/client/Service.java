package com.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import com.db.DBConn;


public class Service extends WindowAdapter implements ActionListener,Runnable{
	private Login login;	
	static Service service;
	Connection conn = DBConn.getConnection();
	PreparedStatement psmt;
	ResultSet result;
	String myid;
	BufferedReader in;
	OutputStream out;
	Socket s;
	
	
	public Service(Login login) {
		this.login = login;
		service = this;
	}
	
	
	public static Service getInstance() {
		return service;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == login.ok){
			try {
				String id = login.idTF.getText().toLowerCase().trim();
				String pw = login.pwTF.getText().toLowerCase().trim();
			
				String sql = "SELECT ID FROM MEMBER WHERE LOWER(MEMBER.ID)='"+id+"' AND "
						+ "LOWER(MEMBER.PW)='"+pw+"'";
			
				psmt = conn.prepareStatement(sql);
				
				result = psmt.executeQuery();
				
				if(result.next()){
					System.out.println("성공");
					JOptionPane.showMessageDialog(login, "로그인에 성공했습니다.");
				}else{
					JOptionPane.showMessageDialog(login, "로그인에 실패했습니다.");
					System.out.println("실패");
					return;
				}
				//result.close();
				//psmt.close();
				//conn.close();
				connectProcess();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == login.join){
			login.join_frame.setVisible(true);
		}
		else if(e.getSource() == login.cancel){
			System.exit(0);
		}
		else if(e.getSource() == login.join_frame.join_cancel){
			login.join_frame.id_tf.setText("");
			login.join_frame.pw_tf.setText("");
			login.join_frame.phone_tf.setText("");
			login.join_frame.name_tf.setText("");
			login.join_frame.setVisible(false);
		}
		else if(e.getSource() == login.join_frame.jungbok){
			try {
				String id = login.join_frame.id_tf.getText().trim();
				String sql = "SELECT ID FROM MEMBER";
				psmt = conn.prepareStatement(sql);
				result = psmt.executeQuery();
				int check = 0;
				
				if(result != null){
					while(result.next()){
						if(id.equalsIgnoreCase(result.getString("ID"))){
							check++;
						}
					}
				}
				if(check>0){
					JOptionPane.showMessageDialog(login, "중복된 아이디 입니다.");
				}else{
					JOptionPane.showMessageDialog(login, "사용 가능한 아이디 입니다.");
				}
				//result.close();
				//psmt.close();
				//conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == login.join_frame.join_ok){
			try {
				String id = login.join_frame.id_tf.getText().trim();
				String name = login.join_frame.name_tf.getText().trim();
				String pw = login.join_frame.pw_tf.getText().trim();
				String phone = login.join_frame.phone_tf.getText().trim();
				
				String sql = "INSERT INTO MEMBER(IDX,ID,NAME,PW,PHONE) "
						+ "VALUES(SEQ_MEMBER.NEXTVAL,'"+id+"','"+name+"','"+pw+"','"+phone+"')";
				
				//System.out.println(sql);
			
				psmt = conn.prepareStatement(sql);
				
				result = psmt.executeQuery();
				
				if(result!=null){
					JOptionPane.showMessageDialog(login, "회원가입에 성공하였습니다.");
				}
				
				//result.close();
				//psmt.close();
				//conn.close();
				login.join_frame.setVisible(false);
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == login.wr.b4){
			login.wr.setVisible(false);
			login.idTF.setText("");
			login.pwTF.setText("");
			login.setVisible(true);
		}
		else if(e.getSource() == login.wr.b1){
			login.mr.setVisible(true);
		}
		else if(e.getSource() == login.wr.chat_tf) {
			globalSendProcess();
		}
		else if(e.getSource() == login.mr.b2){
			login.mr.tf.setText("");
			login.mr.setVisible(false);
		}
		else if(e.getSource() == login.mr.b1){
			String rname = login.mr.tf.getText().trim();
			if(rname.length()<1){
				JOptionPane.showMessageDialog(login, "방 이름을 입력하세요.");
				login.mr.tf.requestFocus();
				return;
			}
			String temp = "";
			for (int i = 0; i < login.wr.model1.getRowCount(); i++) {
				temp = login.wr.model1.getValueAt(i, 0).toString();
				if(rname.equals(temp)){
					JOptionPane.showMessageDialog(login, "이미 존재하는 방입니다.\n 다시 입력하세요.");
					login.mr.tf.setText("");
					login.mr.tf.requestFocus();
					return;
				}
			}
			//공개 비공개
			String state = "", pwd="";
			if(login.mr.rb1.isSelected()){
				state="공개";
				pwd=" ";
			}else{
				state="비공개";
				pwd=new String(login.mr.pf.getPassword());				
			}
			
			
			//입력된 데이터 서버로 전송		
			login.wr.list.removeAll();
			login.wr.chat_area.setText("");
			login.wr.setVisible(false);
			//Room tmp1 = new Room(myid,rname);
			//login.room = tmp1;
			login.room.setBounds(200,200,400,300);
			login.room.setVisible(true);
			//login.wr.list.remove(myid);
			send(Protocol.MKROOM+"||"+rname+"/"+state);//방 만들기 메시지
			login.mr.setVisible(false);
		}
		else if(e.getSource() == login.room.cancel){
			login.room.setVisible(false);
			login.wr.setVisible(true);
		}
		else if(e.getSource() == login.wr.b2){
			String joinRoom = login.wr.roomName;
			login.wr.list.removeAll();
			login.wr.chat_area.setText("");
			login.wr.setVisible(false);
			//Room tmp2 = new Room(myid,joinRoom);
			//login.room = tmp2;
			login.room.setBounds(200,200,400,300);
			login.room.setVisible(true);
			send(Protocol.ROOMJOIN+"||"+myid+"||"+joinRoom);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		closeProcess();
	}

	private void closeProcess() {
		login.wr.chat_area.setText("");
		login.wr.list.removeAll();
		send(Protocol.DISCONNECT + "||");
		login.wr.setVisible(false);
	}


//	1. 전송할 메세지 얻기(유효성검사) >> 지우기
//	2. 서버에 메세지를 전송(200||메세지)
	private void globalSendProcess() {
		String msg = login.wr.chat_tf.getText().trim();
		login.wr.chat_tf.setText("");
		if(msg.isEmpty())
			return;
		send(Protocol.ALL_MESSAGE + "||" + msg);
	}	
	
//	1. ip, 대화명 get (유효성 검사)
//	2. 서버에 접속(Socket생성)
//	3. login창 닫기, chat창 열기
//	4. in, out 생성
//	5. server에 내 정보(대화명) 전송
//	6. thread 시작
	private void connectProcess() {
		myid = login.idTF.getText().trim();
		
		if(myid.isEmpty()){
			JOptionPane.showMessageDialog(login, "아이디를 입력하세요.");
		}
		if(login.pwTF.getText().isEmpty()){
			JOptionPane.showMessageDialog(login, "비밀번호를 입력하세요.");
		}
		
		try {
			s = new Socket("127.0.0.1", 9876);
			login.wr.setVisible(true);
			login.setVisible(false);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = s.getOutputStream();
			send(Protocol.CONNECT + "||" + myid);
			new Thread(this).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void send(String msg) {
		try {
			out.write((msg + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				String msg = in.readLine();//protocol||각기능에 해당하는 message
				System.out.println("클라이언트가 받은 메세지 : " + msg);
				StringTokenizer st = new StringTokenizer(msg, "||");
				String[] array = msg.split("||");
				int protocol = Integer.parseInt(st.nextToken());
				switch(protocol) {
				case Protocol.CONNECT : {//100||접속자대화명
					String tmp = st.nextToken();//접속자대화명					
					if(tmp.equals(myid))
						tmp += "(me)";
					else
						login.wr.chat_area.append("[알림] " + tmp + "님이 대화에 참여했습니다.\n");
					login.wr.list.add(tmp);
				}break;
				case Protocol.ALL_MESSAGE : {//200||안효인] 안녕하세요
					String tmp = st.nextToken();//안효인] 안녕하세요
					login.wr.chat_area.append(tmp + "\n");
				}break;
				case Protocol.ClIENTLIST : {
					login.wr.list.removeAll();
					while(st.hasMoreTokens()){
						String tmp = st.nextToken();
						if(tmp == myid)
							login.wr.list.add(tmp+"(me)");
						else
							login.wr.list.add(tmp);
					}
				}break;
				case Protocol.CLIENTLISTROOM : {
					login.room.list.removeAll();
					while(st.hasMoreTokens()){
						String tmp = st.nextToken();
						if(tmp == myid)
							login.room.list.add(tmp+"(me)");
						else
							login.room.list.add(tmp);
					}
				}break;
				case Protocol.ROOMLIST : {
					/*Object inwon = login.mr.box.getSelectedItem();
					Object[] rowData = {rname,state,inwon};
					login.wr.model1.addRow(rowData);*/
					int len = login.wr.model1.getRowCount();
					for (int i = 0; i < len; i++) {
						login.wr.model1.removeRow(i);
					}
					while(st.hasMoreTokens()){
						String tmp = st.nextToken();
						String[] data = tmp.split("/");
						Object[] rowData = {data[0],data[1],data[2]};
						login.wr.model1.addRow(rowData);
					}
				}break;
				case Protocol.ROOMNUM : {
					//int len = login.wr.model1.getRowCount();
					int i = 0; 
					while(st.hasMoreTokens()){
						String tmp = st.nextToken();
						login.wr.model1.setValueAt(tmp, i, 2);
						i++;
					}
				}break;
				/*case Protocol.PAPER : {
					String from = st.nextToken();//쪽지 보낸 사람
					String letter = st.nextToken();//쪽지 내용
					Paper paper = new Paper();
					paper.to.setText(myid);
					paper.from.setText(from);
					paper.letter.setText(letter.replaceAll("}ahi{", "\n"));
					paper.card.show(paper.south1, "answer");
					paper.setVisible(true);
				}break;*/
				case Protocol.DISCONNECT : {//900||나갈사람대화명
					String tmp = st.nextToken();
					if(!tmp.equals(myid)) {//나가는사람이 아니라면.
						login.wr.chat_area.append("[알림] " + tmp + "님이 접속종료하였습니다.\n");
						login.wr.list.remove(tmp);
					} else {
						in.close();
						out.close();
						s.close();
						flag = false;
						System.exit(0);
					}
				}break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				flag = false;
			}
		}		
	}

	
}
