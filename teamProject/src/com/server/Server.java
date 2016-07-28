package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import com.client.Protocol;


public class Server implements Runnable{
	ArrayList<ChatClient> list = new ArrayList<ChatClient>();
	ServerSocket ss;
	HashMap<String, ArrayList<ChatClient>> map = new HashMap<String, ArrayList<ChatClient>>();
	ArrayList<ChatClient> arraylist =new ArrayList<ChatClient>();
	ArrayList<ChatClient> peoplelist = new ArrayList<ChatClient>();
	
	public Server() {
		try {
			ss = new ServerSocket(9876);
			System.out.println("클라이언트 접속 대기중...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean flag = true;
		while(flag){
			try {
				Socket s = ss.accept();
				System.out.println("클라이언트 접속 "+s);
				new ChatClient(s).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ChatClient extends Thread{
		String name;
		BufferedReader in;
		OutputStream out;
		Socket s;
		
		public ChatClient(Socket socket) {
			this.s = socket;
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = s.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			boolean flag = true;
			while(flag) {
				try {
					String msg = in.readLine();//protocol|| 각 기능에 해당하는 message
					System.out.println("서버가 받은 메시지 : " + msg);
					String[] array = msg.split("||");
					StringTokenizer st = new StringTokenizer(msg, "||");
					int protocol = Integer.parseInt(st.nextToken());
					switch(protocol) {
					case Protocol.CONNECT : {//100||id(대화명)
						name = st.nextToken();//id	
						//대화명 전체 사용자에게 전송
						multicast(Protocol.CONNECT + "||" + name);
						//사용자 추가	
						addClient(this);
						//접속자에게 기존 접속자의 정보 전송
						int size = list.size();
						for (int i = 0; i < size; i++) {
							ChatClient cc = list.get(i);
							unicast(Protocol.CONNECT + "||" + cc.name);
						}
						makeRoomlist();
					}break;
					case Protocol.ALL_MESSAGE : {//200||전체메시지
						String tmp = st.nextToken();//메시지
						multicast(Protocol.ALL_MESSAGE + "||" + name + "] " + tmp);
					}break;
					case Protocol.TO_MESSAGE : {//250||귓속말
						String to = st.nextToken();//보내는사람
						String tmp = st.nextToken();//내용
						int size = list.size();
						for (int i = 0; i < size; i++) {
							ChatClient cc = list.get(i);
							if(to.equals(cc.name)) {
								cc.unicast(Protocol.ALL_MESSAGE + "||��" + name + "�� " + tmp);
								break;
							}
						}
					}break;
					case Protocol.PAPER : {
						String to = st.nextToken();//쪽지 보내는 사람
						String tmp = st.nextToken();//쪽지 내용
						int size = list.size();
						for (int i = 0; i < size; i++) {
							ChatClient cc = list.get(i);
							if(to.equals(cc.name)) {
								cc.unicast(Protocol.PAPER + "||" + name + "||" + tmp);
								break;
							}
						}
					}break;
					case Protocol.MKROOM :{
						String roomname = st.nextToken();
						addRoom(roomname,this);
						removeClient(this);
						makeClientList();
						makeClientlistRoom(roomname);
						makeRoomlist();
					}break;
					case Protocol.ROOMJOIN :{
						String id = st.nextToken();
						String roomname = st.nextToken();
						removeClient(this); //대기실에 자기 제외
						makeClientList(); // 다시 대기실 목록 뿌림
						addGuestRoom(roomname, this); // 그 방에 자기를 넣음
						makeClientlistRoom(roomname); // 방에 참여한 접속자리스트 생성
						makeRoomlist();
					}break;
					case Protocol.DISCONNECT : {
						multicast(Protocol.DISCONNECT + "||" + name);
						int size = list.size();
						for (int i = 0; i < size; i++) {
							ChatClient cc = list.get(i);
							if(cc.name.equals(name)) {
								list.remove(i);
								break;
							}
						}
						in.close();
						out.close();
						s.close();
						flag = false;
					}break;
					}//switch end
				} catch (IOException e) {
					e.printStackTrace();
					flag = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(name + "접속자가 사용을 종료했습니다.");
		}

		private void multicast(String msg){
			int size = list.size();
			for (int i = 0; i < size; i++) {
				ChatClient cc = list.get(i);
				cc.unicast(msg);
			}
		}

		private void unicast(String msg) {
			try {
				out.write((msg + "\n").getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void makeClientList(){
			StringBuffer buffer = new StringBuffer(Protocol.ClIENTLIST+"||");
			for(ChatClient c : list){
				buffer.append(c.name+"||");
			}
			multicast(buffer.toString());
		}
		
		private void addRoom(String roomname, ChatClient client) {
			ArrayList<ChatClient> arraylist2 = new ArrayList<ChatClient>();
			arraylist2.add(client);
			map.put(roomname, arraylist2);
			System.out.println("개설된방 :" + roomname);
			System.out.println("사용자수 :" + arraylist2.size());
		}
		
		private void addClient(ChatClient client){
			list.add(client);
			System.out.println("접속자수:" + list.size());
		}
		
		private void removeClient(ChatClient client){
			list.remove(client);
			System.out.println("접속자수:" + list.size());
		}
		
		private void makeClientlistRoom(String rn) throws Exception { // guestlist/홍길동/김길동/이길동/
			ArrayList<ChatClient> list2 = map.get(rn);
			StringBuffer buffer = new StringBuffer(Protocol.CLIENTLISTROOM+"||"); //guestlistRoom/이름/이름2
			peoplelist=list2;
			for (ChatClient c : list2) {
				buffer.append(c.name + "||");
			}
			
			broadcastRoom(rn, buffer.toString()); // 다방 , guestlistRoom/이름/이름2

		}
		
		private void broadcastRoom(String rn, String msg) throws Exception { // 해당방 메시지 날라옴
			ArrayList<ChatClient> list2 = map.get(rn); // arrayList에 등록되어있는 게스트들
			for (ChatClient c : list2) {
				c.unicast(msg); // 이름-guestlistRoom/이름/이름2     이름2-guestlistRoom/이름/이름2
			}
		}
		
		private void makeRoomlist() throws Exception {
			Set<String> roomlist = map.keySet();
			
			StringBuffer buffer = new StringBuffer(Protocol.ROOMLIST+"||");
			for (String t : roomlist) {
				buffer.append(t +"/"+roomlist.size()+"||"); /////사람수 표시해야할꺼같은데
			}
			multicast(buffer.toString());
			Roomnumber(roomlist);
		}
		
		private void Roomnumber(Set<String> roomlist) throws Exception{
			
			StringBuffer buffer2 = new StringBuffer(Protocol.ROOMNUM+"||"); //방에 사람수 
			for(String t : roomlist){
				buffer2.append(map.get(t).size()+"||");
			}
			multicast(buffer2.toString());
		}
		
		void addGuestRoom(String rn, ChatClient client) {
			ArrayList<ChatClient> list2 = map.get(rn);
			list2.add(client);
			System.out.print("방제" + rn + " ,");
			System.out.println("사람수 :" + list2.size());
		}
	}
	
	public static void main(String[] args) {
		new Thread(new Server()).start();
	}
}
