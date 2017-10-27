package mainframe;

public class ChatProtocol {
	
	public static final String ID = "ID";
	//(C->S) ID:aaa
	//(S->C) CHATLIST:aaa;bbb;ccc;ddd;
	
	public static final String CHAT = "CHAT"; 
	//(C->S)CHAT:받는아이디;챗팅내용 ex)CHAT:bbb,밥묵자
	//(S->C)CHAT:보내는아이디;챗팅내용 ex)CHAT:aaa,밥묵자
	
	public static final String CHATALL = "CHATALL";
	//(C->S)CHATALL:챗팅내용
	//(S->C)CHATALL:[보내는아이디]챗팅내용
	
	public static final String MESSAGE = "MESSAGE"; 
	//(C->S)MESSAGE:받는아이디;쪽지내용
	//(S->C)MESSAGE:보내는아이디;쪽지내용 
	
	public static final String CHATLIST = "CHATLIST";
	
	public static final String HEROSET = "HEROSET";
}







