package mainframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;


public class Main extends MFrame implements KeyListener{

	Toolkit tk = Toolkit.getDefaultToolkit(); 	//이미지를 불러오기 위한 툴킷
	String img;
	int cnt=0;
	int he=0;
	int select = 1;
	Graphics buffi;
	Image buffImg;
	Image background=tk.getImage("img/main_background.gif");
	ImageIcon a = new ImageIcon("img/main_background.gif");
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		//프레임이 윈도우에 표시괼때 위치를 세팅하기 위함.
	
	
	
	public Main() {
		buffImg= createImage(a.getIconWidth(),a.getIconHeight());
		buffi = buffImg.getGraphics();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		//프레임이 윈도우에 표시괼때 위치를 세팅하기 위함.
		setTitle("Menu");
		setSize(a.getIconWidth(), a.getIconHeight());
		int focus_X = (int)(screen.getWidth() / 2 - a.getIconWidth() / 2);
		int focus_Y = (int)(screen.getHeight() / 2 - a.getIconHeight() / 2);
		setLocation(focus_X, focus_Y); //프레임을 화면에 배치
		this.addKeyListener(this);
		repaint();
	}
		
	@Override
	public void paint(Graphics g) {
		buffi.drawImage(background, 0, 0, this);
		buffi.setColor(Color.WHITE);
		//buffi.setFont(new Font("돋움", Font.BOLD, 50));
		//buffi.drawString("Project 1",155, 230);
		buffi.setFont(new Font("돋움", Font.PLAIN, 15));
		if(he==0)
			buffi.drawImage(tk.getImage("img/button_story_.gif"), 130, 280, this);
		else
			buffi.drawImage(tk.getImage("img/button_story.gif"), 130, 280, this);
		
		if(he==80)
			buffi.drawImage(tk.getImage("img/button_vs_.gif"), 130, 360, this);
		else
			buffi.drawImage(tk.getImage("img/button_vs.gif"), 130, 360, this);
		
		if(he==160)
			buffi.drawImage(tk.getImage("img/button_meet_.gif"), 130, 440, this);
		else
			buffi.drawImage(tk.getImage("img/button_meet.gif"), 130, 440, this);
		
		if(he==240)
			buffi.drawImage(tk.getImage("img/button_hello_.gif"), 130, 520, this);
		else
			buffi.drawImage(tk.getImage("img/button_hello.gif"), 130, 520, this);
		
		//buffi.drawRect(130, 280+select*he, 280, 65);
		g.drawImage(buffImg, 0, 0, this);
	}
		
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			he+=80; 
			if(he>240) he=0;
			if(he<0) he=240;
			repaint();
			break;
		case KeyEvent.VK_UP:
			he-=80; 
			if(he>240) he=0;
			if(he<0) he=240;
			repaint();
			break;
		case KeyEvent.VK_ENTER:
			if(he==0) {dispose(); new Story();}				
			if(he==80) {dispose(); new VSmode();}				
			if(he==160) {dispose(); new Meet();}		
			if(he==240) {dispose(); new Hello();}			
			break;
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

