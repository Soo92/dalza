package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

	Vector<ClientThread2> vc;
	ServerSocket server;
	
	public Server() {
		try {
			server = new ServerSocket(8002);
			vc = new Vector<ClientThread2>();
		} catch (Exception e) {
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);//비정상적인 종료
		}
		System.out.println("****************************");
		System.out.println("클라이언트를 기다리고 있습니다.");
		System.out.println("****************************");
		try {
			while(true){	
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
				ct.start();
				vc.add(ct);
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}
	
	//접속된 모든 클라이언트에게 메세지 전송
	public void sendAllMessage(String msg){
		for (int i = 0; i < vc.size(); i++) {
			ClientThread2 ct = vc.get(i);
			ct.sendMessage(msg);
		}
	}

	//접속된 모든 클라이언트에게 메세지 전송
	public void sendAllLocation(String msg){
		for (int i = 0; i < vc.size(); i++) {
			ClientThread2 ct = vc.get(i);
			ct.sendLocation(msg);
		}
	}

	//Vector에 Client를 제거
	public void removeClient(ClientThread2 ct){
		vc.remove(ct);
	}
	
	class ClientThread2 extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id = "익명";
		
		public ClientThread2(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(
								sock.getInputStream()));
				out = new PrintWriter(
						sock.getOutputStream(),true);
				System.out.println(sock+" 접속됨....");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				out.println("사용하실 아이디를 입력하세요.");
				while(true) {
					String line = in.readLine();
					System.out.println(line);
					if(line==null)
						break;
					else
						routine(line);
				}
			} catch (Exception e) {
				removeClient(this);
				sendAllMessage(ChatProtocol2.CHATLIST+
						":"+getIds());
				System.err.println(sock +"["+id+"] 끊어지쁘.");
				//e.printStackTrace();
			}
		}
		
		//Client부터 전송된 Protocol을 분석 메소드
		public void routine(String line){
			System.out.println("line : " + line);
			//CHATALL:오늘은 비가 옵니다. 머가 생각나나요?
			int idx = line.indexOf(':');
			String cmd/*CHATALL*/ = line.substring(0, idx);
			String data/*오늘은...*/ = line.substring(idx+1);
			if(cmd.equals(ChatProtocol2.ID)) {
				//ID:홍길동
				if(data!=null&&data.length()>0) {
					id = data;
					//새로운 접속자 추가 되었기 때문에 리스트 재전송
					sendAllMessage(ChatProtocol2.CHATLIST+
							":"+getIds());
					sendAllMessage(ChatProtocol2.CHATALL+
							":"+"["+id+"]님이 입장하였습니다.");
				}
			}else if(cmd.equals(ChatProtocol2.CHAT)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);
				data = data.substring(idx+1);
				ClientThread2 ct = findClient(cmd);
				if(ct!=null) {
					ct.sendMessage(ChatProtocol2.CHAT+":"+
							"["+id+"(귓)]"+data);
				}else {
					sendMessage(ChatProtocol2.CHAT+":"+
							"["+cmd+"] 접속자가 아닙니다.");
				}
			}else if(cmd.equals(ChatProtocol2.MESSAGE)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);
				data = data.substring(idx+1);
				ClientThread2 ct = findClient(cmd);
				if(ct!=null) {
					ct.sendMessage(ChatProtocol2.MESSAGE+":"+
							 id +";"+data);
				}else {
					//내자신에게 전송
					sendMessage(ChatProtocol2.CHAT+":"+
							"["+cmd+"] 접속자가 아닙니다.");
				}
			}else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL+
						":"+"["+id+"]"+ data);
			}else {sendAllLocation(ChatProtocol2.HEROSET+
					":"+"["+id+"]"+ data);}
		}//routine

		//매개변수로 받은 id값으로 ClientThread2을 찾는다.
		public ClientThread2 findClient(String id){
			ClientThread2 ct = null;
			for (int i = 0; i < vc.size(); i++) {
				ct = vc.get(i);
				if(ct.id.equals(id))
					break;
			}
			return ct;
		}
		
		//접속된 모든 id를 리턴(; 구분 - ex)aaa;bbb;홍길동;)
		public String getIds() {
			String ids = "";
			for (int i = 0; i < vc.size(); i++) {
				ClientThread2 ct = vc.get(i);
				ids+=ct.id+";";
			}
			return ids;
		}
	
		public void sendMessage(String msg){
			out.println(msg);
		}
		
		public void sendLocation(String Location){
			out.println(Location);
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}
}