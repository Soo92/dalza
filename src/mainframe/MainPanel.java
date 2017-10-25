package mainframe;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;


import javax.swing.ImageIcon;

import mainframe.MemberBean;
import mainframe.MemberMgr;

public class MainPanel extends MFrame implements ActionListener{
	
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//이미지를 불러오기 위한 툴킷

	Image buffImg;
	Graphics buffi;
	Image background = tk.getImage("img/login.gif");
	ImageIcon a = new ImageIcon("img/login.gif");

	TextField idTx, pwTx;
	Label idl, pwl, msgl;
	Button logBtn, makeBtn, offBtn;
	MemberMgr mgr;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String id;
	
	

	public MainPanel() {
		mgr = new MemberMgr();
		buffImg = createImage(a.getIconWidth(),a.getIconHeight());
		buffi = buffImg.getGraphics();
		setTitle("LOGIN");
		setSize(a.getIconWidth(), a.getIconHeight()+75);
					
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		//프레임이 윈도우에 표시괼때 위치를 세팅하기 위함.
		int focus_X = (int)(screen.getWidth() / 2 - 400 / 2);
		int focus_Y = (int)(screen.getHeight() / 2 - 450 / 2);
		setLayout(null);
		setLocation(focus_X, focus_Y); //프레임을 화면에 배치

		idl = new Label("ID");
		pwl = new Label("PASS");
		idTx = new TextField("aaa");
		pwTx = new TextField("1234");
		logBtn = new Button("로그인");
		makeBtn = new Button("회원가입");
		offBtn = new Button("오프라인");
		msgl = new Label("ID와 PASS을 입력하세요.");
		idl.setBounds(150, 180, 50, 20);
		idTx.setBounds(200, 180, 100, 20);
		pwl.setBounds(150, 210, 50, 20);
		pwTx.setBounds(200, 210, 100, 20);
		logBtn.setBounds(125, 250, 60, 30);
		makeBtn.setBounds(190, 250, 60, 30);
		offBtn.setBounds(255, 250, 60, 30);
		msgl.setBounds(155, 280, 150, 40);
		logBtn.addActionListener(this);
		makeBtn.addActionListener(this);
		offBtn.addActionListener(this);;
		add(idl);
		add(idTx);
		add(pwl);
		add(pwTx);
		add(logBtn);
		add(makeBtn);
		add(offBtn);
		add(msgl);

		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		try {
			buffi.drawImage(background, 0, 0, this);			
			buffi.setColor(Color.DARK_GRAY);
			buffi.setFont(new Font("돋움", Font.BOLD, 50));
			buffi.drawString("회식", 170, 150);
		} catch (Exception e) {
		}
		g.drawImage(buffImg, 0, 0, this);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		msgl.setForeground(Color.red);
		MemberBean bean = new MemberBean();
			if (obj == logBtn) { 
				// 중복 체크
				bean.setId(idTx.getText());
				bean.setPass(pwTx.getText());
				if(mgr.checkID(bean)) {
					msgl.setText("ID를 확인해주세요.");
				}else if(mgr.checkPass(bean)){
					msgl.setText("PW를 확인해주세요.");
				}else	{
					dispose();
					new Main();
				}
			}

			else if (obj == makeBtn) {
				bean.setId(idTx.getText());
				bean.setPass(pwTx.getText());
				if(mgr.checkID(bean)) {
					msgl.setForeground(Color.BLACK);
					msgl.setText(bean.getId()+"님 환영합니다!");
					mgr.insertMember(bean);
				}else {
					msgl.setText("사용 중인 ID입니다.");
				}
			}
	}		
	public static void main(String[] args) {
		new MainPanel();
	}
}

