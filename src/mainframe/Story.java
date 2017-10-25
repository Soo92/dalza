package mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import character.Hero;
import enemy.Bat;
import enemy.Biggle;
import enemy.Boss;
import enemy.Enemy;
import enemy.Enemy_bullet;
import enemy.Walker;
import enemy.Walker2;
import enemy.Walker3;
import mapData.Block;
import mapData.Stage;
import weapon.FireBack;
import weapon.Mine;
import weapon.Pistol;
import weapon.Weapon;
import zetcode.Breakout;

//메인 화면 만들기위한 클래스
class Story extends JFrame implements KeyListener, Runnable, ActionListener{
	int framew=700, frameh= 700; 	//화면 크기
	int buffx=0, buffy=0, buffw=700, buffh=700;
	int cnt=0, chatt=0, bulletch=0, slowtime,enemy_bulletch = 0;
	int stage_Num = 0; 	//현재 나의 스테이지
	String chat="";
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//이미지를 불러오기 위한 툴킷
	Image menu1,menu2,menu3,menu4;
	Image buffImage;	//더블 버퍼링용 이미지
	Graphics buffg;		
	Thread th,th2; 			//스레드 생성
	Hero mainCh;		//히어로 생성
	Enemy enemy;	//기본 적군 생성
	Stage stage;	//스테이지를 생성한다
	boolean end_Stage;	//다음 스테이지로 넘어 갈 것인가.
	boolean clear_Stage;	//다음 스테이지로 넘어 갈 것인가.
	boolean attack,mine_attack,dash_attack;	//공격중인가 공격중이 아닌가
	int weapon_Number;	//무기 교체 할 때 번호
	int option=50;
	int dash_damage=3;
	int a=20,b=0,c=0,d=0;
	int score=0;
	boolean jump;	//점프를 실행할 것인가 ? true 이면 실행한다.
	boolean resumed=false;
	boolean slowEffect;
	boolean NPCContact=false;   //다음 스테이지로 넘어 갈 것인가.
	boolean portal=true;
	
	int npcx, npcy;
	
	TextArea ta;
	TextField tf;
	Panel pa;
	Color caa=new Color(0f,0f,0f,.0f );
	Color ccc = new Color(0f,0f,0f,.5f);
	
	//공격 기본생성자
	Weapon weapon; 
	Point pistol_Point;
	
	ArrayList bullet_List = new ArrayList<Weapon>(); 	//다수의 권총(알)을 담을 어레이 리스트
	ArrayList enemy_List = new ArrayList<Enemy>();	//다수의 적군을 담을 어레이 리스트
	
	public Story() {
		init();
		setTitle("Shot");		//프레임의 이름을 설정
		setSize(framew, frameh); //프레임의 크기
			
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		//프레임이 윈도우에 표시괼때 위치를 세팅하기 위함.

		Panel pa = new Panel();                                    //채팅 판넬
	    pa.setBackground(Color.green);
	    pa.setLayout(new BorderLayout());
	    pa.add(ta = new TextArea(),BorderLayout.NORTH);
	    ta.setVisible(false);
	    ta.setEditable(false);
	    ta.setFocusable(false);
	    pa.add(tf = new TextField());
	    tf.setFocusable(false);
	    tf.addActionListener(this);
	
	    this.add(pa,BorderLayout.SOUTH);

		this.add(pa,BorderLayout.SOUTH);
		
		//프레임을 모니터 화면 정중앙에 배치 시키기 위해 좌표 값을 계산.
		int focus_X = (int)(screen.getWidth() / 2 - framew / 2);
		int focus_Y = (int)(screen.getHeight() / 2 - frameh / 2);
		
		setLocation(focus_X, focus_Y); //프레임을 화면에 배치
		setResizable(false);		   //프레임의 크기를 임의로 변경못하도록 설정
		setVisible(true);			   //프레임을 눈에 보이게 띄움	
	}

	@Override							// 채팅창 입력 ++ 키보드 event comma 인풋 참조
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==tf) {
			chatt=cnt;
			tf.setFocusable(false);
			if("".equals(tf.getText())) return;
			ta.append("user:"+ (chat=(tf.getText()))+"\n");
			tf.setText("");
		}
	}
	
	private void init(){
		//프로그렘이 정상적으로 종료하도록 만들어 줍니다.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menu1 = tk.getImage("img/sub_button_start.gif");
		menu2 = tk.getImage("img/sub_button_help.gif");
		menu3 = tk.getImage("img/sub_button_exit.gif");
		menu4 = tk.getImage("img/sub_button_start_.gif");
		
		//주인공 생성
		mainCh = new Hero();
		
		//스테이지 true 가 되면 다음 스테이지로 넘어감
		end_Stage = true;
		
		addKeyListener(this); //키보드 이벤트 실행
		th = new Thread(this); 	  //스레드 생성
		th.start(); 		  //스레드 시작
		
		//스테이지 1 생성
		stage = new Stage();
		
		attack = false; //공격 상태 설정 
		weapon_Number = 1;//무기의 상태, 기본 1 피스톨
		jump = false; //점프 상태설정
	}
	
	public void paint(Graphics g){
		buffImage = createImage(buffw,buffh);
		buffg = buffImage.getGraphics();
		if(!resumed) {
			update(g);
		}
		else update1(g);
	}
	
	private void update1(Graphics g) {
		//일시정지
		if(resumed) {
			if(a==20)
				g.drawImage(tk.getImage("img/sub_button_start_.gif"), 230, 250, this);
			else
				g.drawImage(tk.getImage("img/sub_button_start.gif"), 230, 250, this);
			
			if(b==20)
				g.drawImage(tk.getImage("img/sub_button_help_.gif"), 230, 320, this);
			else
				g.drawImage(tk.getImage("img/sub_button_help.gif"), 230, 320, this);
			
			if(d==20)
				g.drawImage(tk.getImage("img/sub_button_exit_.gif"), 230, 390, this);
			else
				g.drawImage(tk.getImage("img/sub_button_exit.gif"), 230, 390, this);
		}
	}
	
	public void update(Graphics g){
		//실제로 그려진 그림을 가져온다.
		draw();
		//총알을 그린다.
		draw_Bullet();
		//적군을 그린다.
		draw_Enemy();
		//스테이지를 그린다.
		draw_Stage();
		//화면에 버퍼에 그린 그림을 가져와 그리기
			if((mainCh.get_Hero_X_Point()-framew/3)-113 < 0 ) {
				buffg.drawImage(buffImage,0,(mainCh.get_Hero_Y_Point()-buffh/3)+25, 300, 100, this);	
			}else if((mainCh.get_Hero_X_Point()-framew/3)-113 >= 0){
				buffg.drawImage(buffImage,(mainCh.get_Hero_X_Point()-framew/3)-113,(mainCh.get_Hero_Y_Point()-buffh/3)+25, 300, 100, this);
			}
		
		//4369
		if(mainCh.get_Hero_X_Point()<=framew/2) {                                 //캐릭터가 화면 왼쪽벽 위치
			g.drawImage(buffImage, -10,buffh/3-mainCh.get_Hero_Y_Point(), this);
		}
		else if(mainCh.get_Hero_X_Point()>=buffw-framew/2) {						//캐릭터가 화면 오른쪽벽 위치
			g.drawImage(buffImage, framew-buffw,buffh/3-mainCh.get_Hero_Y_Point(), this);
		}
		else {																		//나머지 상황일때
			g.drawImage(buffImage, framew/2-mainCh.get_Hero_X_Point(),buffh/3-mainCh.get_Hero_Y_Point(), this);
		}
		  g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
		  g.setColor(Color.white); 
	      g.drawString("SCORE: ", framew-300, 100);
	      g.drawString(Integer.toString(score), framew-100, 100);
	      System.out.println(NPCContact);
		
		//클리어
	      if(clear_Stage) {
	          g.drawImage(tk.getImage("img/clear.gif"), (framew/4)+30, (frameh/4)+130, this);
	       }
		
		//낙하
		if((mainCh.get_Hero_X_Point() < (buffx) || 
			mainCh.get_Hero_X_Point() > (buffx+buffw)) ||
			mainCh.get_Hero_Y_Point() > (buffy+buffh)*1.5){
				mainCh=new Hero();
				mainCh.set_Hero_Y_Point(stage.get_Block().get(0).get_Left_Top_Point().y-mainCh.get_Hero_Height());
		}
	}
	
	//실제로 그림들을 그릴 부분
	public void draw(){
		//프레임에 저장된 png 이미지를 그려넣습니다.
		//buffg.drawImage(hero_Png, mainCh.get_Hero_X_Point(), mainCh.get_Hero_Y_Point(), this);
/*		if(mainCh.get_Hero_X_Point()<=framew/2) {                                 //캐릭터가 화면 왼쪽벽 위치
			buffg.drawImage(stage.get_Background(), 0, mainCh.get_Hero_Y_Point()-buffh/2, buffw, buffh, this);
		}
		else if(mainCh.get_Hero_X_Point()>=buffw-framew/2) {						//캐릭터가 화면 오른쪽벽 위치
			buffg.drawImage(stage.get_Background(),  buffw-framew, mainCh.get_Hero_Y_Point()-buffh/2, buffw, buffh, this);
		}
		else {																		//나머지 상황일때
			buffg.drawImage(stage.get_Background(), mainCh.get_Hero_X_Point()-framew/2, mainCh.get_Hero_Y_Point()-buffh/2, buffw, buffh, this);
		}*/
		buffg.drawImage(stage.get_Background(), 0, 0, buffw, buffh, this);
		if(mainCh.get_Face_Side_LFET_RIGHT()) {
			buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()+mainCh.get_Hero_Width(),   mainCh.get_Hero_Y_Point(), -mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		}
		else {
			buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point(),   mainCh.get_Hero_Y_Point(), mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		}
		if((cnt-chatt)<=100&&chatt!=0) {
			if((cnt-chatt)<10)
			this.requestFocus();
			buffg.drawString(chat,mainCh.get_Hero_X_Point()+20, mainCh.get_Hero_Y_Point()-30);
		}
	}
	
	//스테이지 맵 을 그림 (블록)
	public void draw_Stage(){
		//다음 스테이지로 넘어감
		if(end_Stage){
			stage_Num++; //다음 스테이지로 넘어감 //현 위치 초기화 1스테이지
			//스테이지넘버를 한번 반영해서 스테이지를 만든다.
			this.setTitle("Stage"+stage_Num);
			mainCh = new Hero();
			stage.map_Stage(stage_Num);
			//기본 적군 워커 생성
			enemy_Process(stage_Num);
			mainCh.set_Hero_Y_Point(stage.get_Block().get(0).get_Left_Top_Point().y-mainCh.get_Hero_Height());
			end_Stage = false;
		}
				
		//생성된 스테이지의 블록을 그려야함 
		int temp = 0;
		for(int i=0; i<stage.get_Block().size(); i++){
			buffg.setColor(caa);
			buffg.fillRect(stage.get_Block().get(i).get_Left_Top_Point().x,
					stage.get_Block().get(i).get_Left_Top_Point().y,
					stage.get_Block().get(i).get_Width(), 
					stage.get_Block().get(i).get_Height());

			//System.out.println(stage.get_Block().get(i).get_effect());
            for (int j = 0; j < stage.get_Block().get(i).get_Height()/50; j++) {
                for (int j2 = 0; j2 < stage.get_Block().get(i).get_Width()/50; j2++) {
                   buffg.drawImage(tk.getImage("img/"+stage.get_Block().get(i).get_effect()+".jpg"), 
                         stage.get_Block().get(i).get_Left_Top_Point().x+j2*50, 
                         stage.get_Block().get(i).get_Left_Top_Point().y+j*50, 50, 50, this);
                }
             }
             for (int j = 0; j < stage.get_Block().get(i).get_Height()/50; j++) {
                for (int j2 = 0; j2 < stage.get_Block().get(i).get_Width()/50; j2++) {
                   buffg.drawImage(tk.getImage("img/"+stage.get_Block().get(i).get_effect()+".gif"), 
                         stage.get_Block().get(i).get_Left_Top_Point().x+j2*50, 
                         stage.get_Block().get(i).get_Left_Top_Point().y+j*50, 50, 50, this);
                }
             }
             for (int j = 0; j < stage.get_Block().get(i).get_Height()/50; j++) {
                for (int j2 = 0; j2 < stage.get_Block().get(i).get_Width()/50; j2++) {
                   buffg.drawImage(tk.getImage("img/"+stage.get_Block().get(i).get_effect()+".png"), 
                         stage.get_Block().get(i).get_Left_Top_Point().x+j2*50, 
                         stage.get_Block().get(i).get_Left_Top_Point().y+j*50, 50, 50, this);
                }
             }

			
			//충돌 함수 호출 1이면 벽과 캐릭터
			crash_Decide_Block(mainCh, stage.get_Block().get(i));
			//buff이미지 크기조정
			if(buffw<stage.get_Block().get(i).get_Left_Top_Point().x+stage.get_Block().get(i).get_Width())
				buffw=stage.get_Block().get(i).get_Left_Top_Point().x+stage.get_Block().get(i).get_Width()+10;
			if(buffx>stage.get_Block().get(i).get_Left_Top_Point().x)
				buffx=stage.get_Block().get(i).get_Left_Top_Point().x-10;
			if(buffh<stage.get_Block().get(i).get_Left_Top_Point().y+stage.get_Block().get(i).get_Height())
				buffh=stage.get_Block().get(i).get_Left_Top_Point().y+stage.get_Block().get(i).get_Height()+10;
			if(buffy>stage.get_Block().get(i).get_Left_Top_Point().y)
				buffy=stage.get_Block().get(i).get_Left_Top_Point().y-10;
			//캐릭터가 모든 발판을 밟고 있지 않을때 추락중이다.
			if(!stage.get_Block().get(i).get_Set_Contect()){
				temp++;
			}
			//발판 숫자만큼 false 이면 발판을 밝고 있지않음 
			if(temp == stage.get_Block().size()){
				mainCh.auto_Jump_Down();
			}
		}
		for(int i=0; i<stage.get_Item().size(); i++){
			//buffg.setColor(Color.LIGHT_GRAY);
			buffg.fillRect(stage.get_Item().get(i).get_Left_Top_Point().x,
					stage.get_Item().get(i).get_Left_Top_Point().y,
					stage.get_Item().get(i).get_Width(), 
					stage.get_Item().get(i).get_Height());

			if(stage.get_Item().get(i).get_effect()=="Beer")
	            for (int j = 0; j < stage.get_Item().get(i).get_Height()/150; j++) {
	                for (int j2 = 0; j2 < stage.get_Item().get(i).get_Width()/250; j2++) {
	                   buffg.drawImage(tk.getImage("img/"+stage.get_Item().get(i).get_effect()+".png"), 
	                         stage.get_Item().get(i).get_Left_Top_Point().x+j2*250, 
	                         stage.get_Item().get(i).get_Left_Top_Point().y+j*150, 250, 150, this);
	                }
	             }
			//System.out.println(stage.get_Item().get(i).get_effect());
			else {
			buffg.drawImage(tk.getImage("img/"+stage.get_Item().get(i).get_effect()+".png"),stage.get_Item().get(i).get_Left_Top_Point().x,
					stage.get_Item().get(i).get_Left_Top_Point().y,
					stage.get_Item().get(i).get_Width(), 
					stage.get_Item().get(i).get_Height(), this);
			buffg.drawImage(tk.getImage("img/"+stage.get_Item().get(i).get_effect()+".jpg"),stage.get_Item().get(i).get_Left_Top_Point().x,
					stage.get_Item().get(i).get_Left_Top_Point().y,
					stage.get_Item().get(i).get_Width(), 
					stage.get_Item().get(i).get_Height(), this);
			buffg.drawImage(tk.getImage("img/"+stage.get_Item().get(i).get_effect()+".gif"),stage.get_Item().get(i).get_Left_Top_Point().x,
					stage.get_Item().get(i).get_Left_Top_Point().y,
					stage.get_Item().get(i).get_Width(), 
					stage.get_Item().get(i).get_Height(), this);
			}
			//충돌 함수 호출 1이면 벽과 캐릭터
			crash_Decide_Item(mainCh, stage.get_Item().get(i));
			//buff이미지 크기조정
			if(buffw<stage.get_Item().get(i).get_Left_Top_Point().x+stage.get_Item().get(i).get_Width())
				buffw=stage.get_Item().get(i).get_Left_Top_Point().x+stage.get_Item().get(i).get_Width()+10;
			if(buffx>stage.get_Item().get(i).get_Left_Top_Point().x)
				buffx=stage.get_Item().get(i).get_Left_Top_Point().x-10;
			if(buffh<stage.get_Item().get(i).get_Left_Top_Point().y+stage.get_Item().get(i).get_Height())
				buffh=stage.get_Item().get(i).get_Left_Top_Point().y+stage.get_Item().get(i).get_Height()+10;
			if(buffy>stage.get_Item().get(i).get_Left_Top_Point().y)
				buffy=stage.get_Item().get(i).get_Left_Top_Point().y-10;
			//캐릭터가 모든 발판을 밟고 있지 않을때 추락중이다.
		}
	}
	
	public void draw_Bullet(){	//총알을 그리는 함수
		for(int i=0; i<bullet_List.size(); i++){		//총알 개수 만큼 반복하며 그린다.
			weapon = (Weapon) bullet_List.get(i);
			buffg.drawImage(weapon.get_Weapon_Img(),weapon.getPoint().x,  weapon.getPoint().y, weapon.get_Weapon_Width(), weapon.get_Weapon_Height(),this); //사각형으로 일단 대체
			if(weapon instanceof Pistol){ //불릿 리스트에 정보가 피스톨로 변형 가능하다면.
				((Pistol) weapon).pistol_Move( weapon.get_Bullet_Side_LEFT_RIGHT() );				//피스톨 총알 제각기 의 방향성을 가지고 날아간다.
			}

			if(weapon instanceof FireBack){ //불릿 리스트에 정보가 피스톨로 변형 가능하다면.
				((FireBack) weapon).pistol_Move( weapon.get_Bullet_Side_LEFT_RIGHT() );				//피스톨 총알 제각기 의 방향성을 가지고 날아간다.
			}

			if(weapon instanceof Mine){ //불릿 리스트에 정보가 지뢰로 변형 가능하다면.
				((Mine) weapon).pistol_Move( weapon.get_Bullet_Side_LEFT_RIGHT() );				//피스톨 총알 제각기 의 방향성을 가지고 날아간다.
			}
			
			//총알과 적군의 충돌판정 함수 호출 
			for(int j=0; j<enemy_List.size(); j++){
			enemy = (Enemy) enemy_List.get(j);
			crash_Decide_Enemy(weapon, enemy, enemy.get_Move_Site());
			}
			//총알 제거 함수 호출
			remove_Bullet(weapon, i);
		}
	}
	
	//충돌 판정 함수, 충돌은 2중 바운딩렉트로 구한다. 1차 는 큰 사각형 2차는 조각난 사각형과 대조를한다 일단 1차 바운딩 구조만 생각하도록 한다.
	//private int object_Width; //판정의 범위를 연산하기 위한 템프 변수
	//private int object_Height; //판정의 범위를 연산하기 위한 템프 변수
	
	private boolean jump_Up_Lock_Temp = false; //올라가는 도중에는 벽위에 안착 불가능
	private int auto_Jump_Down_Head_Flag = 0; //머리 끼임 현상 방지
	//충돌 체크 맵과 메인 캐릭터 캐릭터 x,y
	public void crash_Decide_Block(Hero hero, Block block){ //what_Object 1 일때 벽과 캐릭터 충돌
		//올라가는중일때 안착 불가
		if(!mainCh.get_Jump_State()){
			jump_Up_Lock_Temp = true;
		}
			if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (block.get_Left_Top_Point().x ) || 
					hero.get_Hero_X_Point() > (block.get_Left_Top_Point().x+block.get_Width()) ||
					(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < block.get_Left_Top_Point().y ||
					hero.get_Hero_Y_Point() > (block.get_Left_Top_Point().y+block.get_Height())){
				//캐릭터가 땅을 밝지 않으면 false 벽을 밝고 있지 않을때는 떯어지도록 
				block.set_Contect_F();
			}else {
				//캐릭터가 땅을 밝으면 true
				block.set_Contect_T();
				//캐릭터가 점프를 하지않고 떨어졌을때 중력 가속도를 초기화 한다.
				mainCh.set_dgSum_Zero();
				//캐릭터의 머리가 벽의 바닥에 닿았을때
				if(hero.get_Hero_Y_Point() >= block.get_Left_Top_Point().y + block.get_Height() - 20){
					if(block.get_effect()=="randombox") {
						for (int i = 0; i < 10; i++) {
							block.set_block_Y_Point(block.get_Left_Top_Point().y-1);
						}
						block.set_effect("randomboxclosed");
						stage.make_item(block.get_Left_Top_Point().x, block.get_Left_Top_Point().y-50, block.get_Width(), block.get_Height(), "coin");
					}
					//System.out.println("머리와 바닥 부딛힘");
					//캐릭터가 벽과 부딛히면 바로 아래쪽으로 떨어짐
					mainCh.set_Jump_Hero_UP_DOWN();
					//점프를 안하고 추락할때 끼었을때 끼지 않도록 해야한다.
					//낀 값의 넓이를 구해서 뺀다.
					auto_Jump_Down_Head_Flag++;
					if(auto_Jump_Down_Head_Flag >= 2){
					mainCh.auto_Jump_Down_Head(block.get_Left_Top_Point().y + block.get_Height() - hero.get_Hero_Y_Point());
					}
				}else 
				//캐릭터의 하단이 발판의 상단이 겹쳤을때 멈출 지점 알려준다.
				if(hero.get_Hero_Y_Point()+hero.get_Hero_Height()  <=  block.get_Left_Top_Point().y + 20){
					//System.out.println("위에 밝고 있음");
				//끼임이 발생하면 연속적인 동작이 발생함으로 연속적인 동작이 발생하였을때 감산을 해준다.
				auto_Jump_Down_Head_Flag = 0;
				//캐릭터가 점프했다가 아래로 내려오는 도중에만 벽 위에 올라 설수 있도록 설정 && 낙하시에 제대로 설수 있도록 설정
				if(jump_Up_Lock_Temp){
					//벽위로 띄었을때 벽위에 안착 시키기위한 함수
					mainCh.set_Hero_Y_Point(block.get_Left_Top_Point().y-hero.get_Hero_Height());
					//캐릭터가 다시 위치 해야 할 곳 지정해줌 && 점프 중이 아닐때만 초기화 할수 있도록 해야한다.
					mainCh.jump_Move_Stop(mainCh.get_Hero_Y_Point());
					jump_Up_Lock_Temp = false;
				}
					//위에 밝을때까지 떨어지다가 밝고 있음 if 문에 들어오면 점프 중지 로 들어온다.
				}//캐릭터 우측과 벽의 좌측이 박았을때
				else if(hero.get_Hero_X_Point() + hero.get_Hero_Width() <= block.get_Left_Top_Point().x + 25){
					//System.out.println("벽의 왼쪽 면 부딛힘");
					//오른쪽으로 전진 못하도록 막아야함
					mainCh.set_CrashBlock(true);
					mainCh.stop_Move_Right(hero.get_Hero_X_Point());
				}
				//캐릭터 좌측과 벽의 우측이 박았을때
				else if(hero.get_Hero_X_Point() >= block.get_Left_Top_Point().x + block.get_Width() - 25){
					//System.out.println("벽의 오른쪽 면 부딛힘");
					//왼쪽으로 전진 못하도록 막아야함
					mainCh.set_CrashBlock(true);
					mainCh.stop_Move_Leftt(hero.get_Hero_X_Point());
				}
				else {
					mainCh.set_CrashBlock(false);
					block.set_Contect_F();//캐릭터가 땅을 밝지 않으면 false 벽을 밝고 있지 않을때는 떯어지도록 
				}
			}
		}
	//적군과 블록의 충돌 검사 떨어지다가 맞물려야한다.
	
	public void crash_Decide_Enemy_Block(Block block, Enemy enemy){
		//떨어지면 작동
		if(enemy.get_Down_Start()){
			//xpoint = 블럭의 x 좌표
			if((block.get_Left_Top_Point().x + block.get_Width()) <= (enemy.get_enemy_Point().x ) || 
					block.get_Left_Top_Point().x >= (enemy.get_enemy_Point().x+enemy.get_Enemy_Width()) ||
					(block.get_Left_Top_Point().y + block.get_Height()) <= enemy.get_enemy_Point().y ||
					block.get_Left_Top_Point().y-1 >= (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
			//enemy.get_Enemy_Exit_Yoint(1000);
				block.set_Contect_F();
		}else{
			//System.out.println("적군 땅에 닿아 있음");
			//닿아 있을때 발판의 위치를 리턴한다, 적군을 블록위에 안착시킨다.
			enemy.get_Enemy_Exit_Yoint(block.get_Left_Top_Point().y  - enemy.get_Enemy_Height() );
			enemy.init_Bound_Site(block.get_Left_Top_Point().x, (block.get_Width() + block.get_Left_Top_Point().x), block.get_Left_Top_Point().y - block.get_Height());
			
			//추적 알고리즘도 같이 떨궈야한다.
			enemy.init_Range_Site(enemy.get_enemy_Point().x, enemy.get_enemy_Point().y);
			
			/*if() {
				enemy.set_CrashBlock(true);
				
			}else if() {
				enemy.set_CrashBlock(true);
				
			}else {
				enemy.set_CrashBlock(false);
				block.set_Contect_F();
				}*/
			}
		}
	}
	
	public void crash_Decide_Item(Hero hero, Block item){
		if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (item.get_Left_Top_Point().x ) || 
				hero.get_Hero_X_Point() > (item.get_Left_Top_Point().x+item.get_Width()) ||
				(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < item.get_Left_Top_Point().y ||
				hero.get_Hero_Y_Point() > (item.get_Left_Top_Point().y+item.get_Height())){
			item.set_Contect_F();
		}else {
			item.set_Contect_T();
			
			if(item.get_effect()=="next") {
				clear_Stage=false;
				end_Stage=true;
			}
			if(item.get_effect()=="finish") {
				clear_Stage=true;
				end_Stage=true;
			}
			if(item.get_effect()=="coin") {
				score+=200;
				stage.get_Item().remove(item);
			}			
			if(item.get_effect()=="NPC_1") {
				//buffg.fillRect( item.get_Left_Top_Point().x,item.get_Left_Top_Point().y, item.get_Width(),item.get_Height());
				NPCContact=true;
				npcx=item.get_Left_Top_Point().x;
				npcy=item.get_Left_Top_Point().y;
			}
			if(item.get_effect()=="NPC_2") {
				//buffg.drawRect(x, y, width, height);
				buffg.fillRect( item.get_Left_Top_Point().x,item.get_Left_Top_Point().y, item.get_Width(),item.get_Height());
				buffg.drawString("안녕", item.get_Left_Top_Point().x,item.get_Left_Top_Point().y);
				buffg.drawString("달자야",1950,680);
			}
			if(item.get_effect()=="NPC_3") {
				//buffg.drawRect(x, y, width, height);
				buffg.drawString("얼마", 2450, 680);
			}
			if(item.get_effect()=="NPC_4") {
				//buffg.drawRect(x, y, width, height);
				buffg.drawString("안남음", 2950, 680);
			}
			if(item.get_effect()=="NPC_5") {
				//buffg.drawRect(x, y, width, height);
				buffg.drawString("ㅗㅗ", 3450, 680);
			}
			 if(item.get_effect()=="thorns") {
				 mainCh.set_Hero_Move_Velocity(3);
				 slowEffect=true;
				 slowtime=cnt;
			 }
			 if(item.get_effect()=="Beer") {
				 mainCh.set_Hero_Move_Velocity(1);
				 mainCh.set_Hero_Jump_Speed(-3);
				 slowEffect=true;
			 }
			 if(item.get_effect()=="S1_portal1") {
				 mainCh.set_Hero_X_Point(5000);
				 mainCh.set_Hero_Y_Point(520);
			  }
			if(item.get_effect()=="S1_portal2") {
				  mainCh.set_Hero_X_Point(1780);
				  mainCh.set_Hero_Y_Point(520);
			  }
			if(item.get_effect()=="S1_portal3") {
				  mainCh.set_Hero_X_Point(5300);
				  mainCh.set_Hero_Y_Point(1050);
			  }
			if(item.get_effect()=="S2_portal1") {
				  mainCh.set_Hero_X_Point(4450);
				  mainCh.set_Hero_Y_Point(1000);
			  }
			if(item.get_effect()=="S2_portal2") {
				  mainCh.set_Hero_X_Point(1800);
				  mainCh.set_Hero_Y_Point(300);
			  }
			if(item.get_effect()=="S2_portal3") {
				  mainCh.set_Hero_X_Point(3350);
				  mainCh.set_Hero_Y_Point(1000);
			  }
			if(item.get_effect()=="S2_portal4") {
				  mainCh.set_Hero_X_Point(720);
				  mainCh.set_Hero_Y_Point(1000);
			  }
			if(item.get_effect()=="S2_portal5") {
				  mainCh.set_Hero_X_Point(700);
				  mainCh.set_Hero_Y_Point(280);
			  }
			if(item.get_effect()=="S2_portal6") {
				  mainCh.set_Hero_X_Point(4650);
				  mainCh.set_Hero_Y_Point(440);
			  }
			if(item.get_effect()=="S3_portal1") {
				  mainCh.set_Hero_X_Point(2450);
				  mainCh.set_Hero_Y_Point(1200);
			  }
			if(item.get_effect()=="S3_portal2") {
				  mainCh.set_Hero_X_Point(2000);
				  mainCh.set_Hero_Y_Point(900);
			  }
			if(portal && item.get_effect()=="S5_portal1") {
				resumed=true;
				portal=false;
				new Breakout();
			  }
			 }
		}
	
	
	//충돌 체크 함수 캐릭터와 총알 등 
	public void crash_Decide_Enemy(Hero hero, Enemy enemy, boolean get_Site){ //get_Site = 탐지구역이 좌측인지 우측인지
		//what_Object = 1 이면 캐릭터, 2 이면 총알
				if(get_Site){ //좌측을 탐지할때. 사각형의 범위가 캐릭터의 좌측부터 시작하기 때문에 넉백 계산시에 캐릭터의 넓이 만큼 경계 범위를 x 축의 범위(width)에 더해주어여한다. 
					//System.out.println("좌측 이동 캐릭터 위치 : " + enemy.get_enemy_Point().x + ", 캐릭터 좌측 시야 : " + (enemy.get_enemy_Point().x - enemy.get_Range_Site_Width()));
					if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point()+hero.get_Hero_Width()) || 
							hero.get_Hero_X_Point() > (enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point()+hero.get_Hero_Width()+enemy.get_Range_Site_Width_Right_Point()) ||
							(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_range_Site_Height_Top_Point() ||
							hero.get_Hero_Y_Point() > (enemy.get_range_Site_Height_Top_Point()+enemy.get_range_Site_Height_Bottom_Point())){
						enemy.set_Not_Find_Hero(); //캐릭터를 찾지 못했을때.
					}else {
						//System.out.println("충돌 판정");
						enemy.set_Find_Hero(mainCh.get_Hero_X_Point()); //캐릭터를 찾았을때
						//캐릭터와 적군이 직접 닿았을때. 좌측방향
						
						  if(enemy instanceof Walker3 && (cnt-enemy_bulletch)>40) {
		                      enemy = new Enemy_bullet(stage.get_Block().get(18).get_Left_Top_Point().x, 
		                               stage.get_Block().get(18).get_Left_Top_Point().x+stage.get_Block().get(18).get_Width()-600, 
		                               stage.get_Block().get(18).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		                      enemy.set_Enemy_Y(enemy.get_enemy_Point().y+100);
		                         enemy_List.add(enemy);
		                         enemy_bulletch=cnt;
		                   }
						
						if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_enemy_Point().x - enemy.get_Enemy_Width()) || 
								hero.get_Hero_X_Point() > (enemy.get_enemy_Point().x - enemy.get_Enemy_Width()+enemy.get_Enemy_Width()) ||
								(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_enemy_Point().y ||
								hero.get_Hero_Y_Point() > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
						}else{
							//좌측방향 넉백
							hero.left_Knock_Back();
						}
					}
				}else { //우측 탐지할때 경계 
					if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_Range_Site_Width_Left_Point() ) || 
							hero.get_Hero_X_Point() > (enemy.get_Range_Site_Width_Left_Point()+enemy.get_Range_Site_Width_Right_Point()) ||
							(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_range_Site_Height_Top_Point() ||
							hero.get_Hero_Y_Point() > (enemy.get_range_Site_Height_Top_Point()+enemy.get_range_Site_Height_Bottom_Point())){
						enemy.set_Not_Find_Hero(); //캐릭터를 찾지 못했을때.
					}else {
						//System.out.println("충돌 판정");
						enemy.set_Find_Hero(mainCh.get_Hero_X_Point()); //캐릭터를 찾았을때
						//우측 면과 영웅이 닿았을때.
						if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_enemy_Point().x ) || 
								hero.get_Hero_X_Point() > (enemy.get_enemy_Point().x + enemy.get_Enemy_Width()) ||
								(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_enemy_Point().y ||
								hero.get_Hero_Y_Point() > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
						}else {
							//우측방향 넉백
							hero.right_Knock_Back();
						}
					}
				}
	}
	//충돌 체크 함수 캐릭터와 총알 등 
	public void crash_Decide_Enemy(Weapon weapon, Enemy enemy, boolean get_Site){ //get_Site = 탐지구역이 좌측인지 우측인지
			if((weapon.getPoint().x+weapon.get_Weapon_Width()) < (enemy.get_enemy_Point().x ) || 
					weapon.getPoint().x > (enemy.get_enemy_Point().x+enemy.get_Enemy_Width()) ||
					(weapon.getPoint().y+weapon.get_Weapon_Height()) < enemy.get_enemy_Point().y ||
					weapon.getPoint().y > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
			}else {
				//System.out.println("충돌 판정");
				//피격시 적군의 에너지를 감소시킨다.
				score+=weapon.get_Bullet_Power()*10;
				enemy.enemy_HP_Down(weapon.get_Bullet_Power());
							//피격된 적군 에너지가 0 이 되면 삭제한다.
				
				if(enemy instanceof Boss) {
					System.out.println(b);
					
					if(enemy.get_Enemy_HP()==140) {
					
						enemy = new Walker3(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-600, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						
					}
					
					if(enemy.get_Enemy_HP()==120) {
						enemy.set_Enemy_Image("img/gorilla2.gif");
						
						enemy = new Walker(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-600, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Walker(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
					}
					
					if(enemy.get_Enemy_HP()==90) {
						enemy.set_Enemy_Image("img/gorilla3.gif");
						
						enemy = new Walker2(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
					}
					if(enemy.get_Enemy_HP()==60) {
						enemy.set_Enemy_Image("img/gorilla4.gif");
						
						enemy = new Bat(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-600, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Biggle(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Bat(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-700, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
					}
					if(enemy.get_Enemy_HP()==30) {
						enemy.set_Enemy_Image("img/gorilla5.gif");
						
						enemy = new Bat(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-700, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Biggle(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Walker2(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Walker(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
						
						enemy = new Walker3(stage.get_Block().get(19).get_Left_Top_Point().x, 
								stage.get_Block().get(19).get_Left_Top_Point().x+stage.get_Block().get(19).get_Width()-300, 
								stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
						enemy_List.add(enemy);
					}
					if(enemy.get_Enemy_HP()<0) {
						stage.make_item(enemy.get_enemy_Point().x,enemy.get_enemy_Point().x, 100, 100,"finish");
						enemy_List.clear();
					}
					
				}
				
				if(enemy.get_Enemy_HP() <= 0 || 
				(enemy.get_enemy_Point().x < (buffx-10) || 
				enemy.get_enemy_Point().x+enemy.get_Enemy_Width() > (buffx+buffw+10)) ||
				enemy.get_enemy_Point().y+enemy.get_Enemy_Height()>buffh-10){
					score+=100;
					enemy_List.remove(enemy);
					}
				//피격된 몬스터를 자신의 방향으로 달려오도록 해야한다. 즉 몬스터를 공격 상태로 변경해야한다.
				if(enemy.get_enemy_Point().x >= mainCh.get_Hero_X_Point()){
					enemy.set_Move_Site(true);
					//적군 넉백효과
					enemy.knockback(true);
				}
				//피격된 몬스터를 자신의 방향으로 달려오도록 해야한다. 즉 몬스터를 공격 상태로 변경해야한다.
				if(enemy.get_enemy_Point().x <= mainCh.get_Hero_X_Point()){
					enemy.set_Move_Site(false);
					//적군 넉백효과
					enemy.knockback(false);
				}
				//넉백 하다가 발판보다 거리가 넘어가게되면 추락 시작
				if(enemy.get_enemy_Point().x >= enemy.get_Right_Bound_Site() ||
						enemy.get_enemy_Point().x + 30 <= enemy.get_Left_Bound_Site()){ //우측으로 떨어지고 좌측으로 떨어지고
					enemy.set_Down_Start_True();
				}
				weapon.set_Remove_Bullet_Choice(); //충돌 되면 총알의 상태를 삭제 상태로
		}
	}
	//적군을 그리는 함수
	public void draw_Enemy(){
		//적군의 수를 반복하여 그린다
		for(int i=0; i<enemy_List.size(); i++){
			enemy = (Enemy) enemy_List.get(i);

			//적 생성
			if(enemy instanceof Walker){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(!enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				if(enemy.get_Enemy_HP()>=enemy.get_Enemy_HPinit()*2/3) {
					System.out.println("aa");
					buffg.setColor(Color.yellow);
					buffg.fillRect(enemy.get_enemy_Point().x+enemy.get_Enemy_Width()/2-enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5/2,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5, 5); //적 에너지 바 위치, 크기
					buffg.setColor(Color.green);
					buffg.fillRect(enemy.get_enemy_Point().x+enemy.get_Enemy_Width()/2-enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5/2,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*(enemy.get_Enemy_HP()-enemy.get_Enemy_HPinit()*2/3)/5, 5); //적 에너지 바 위치, 크기
				}else
				if(enemy.get_Enemy_HP()>=enemy.get_Enemy_HPinit()/3) {
					buffg.setColor(Color.red);
					buffg.fillRect(enemy.get_enemy_Point().x+enemy.get_Enemy_Width()/2-enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5/2,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5, 5); //적 에너지 바 위치, 크기
					buffg.setColor(Color.yellow);
					buffg.fillRect(enemy.get_enemy_Point().x+enemy.get_Enemy_Width()/2-enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5/2,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*(enemy.get_Enemy_HP()-enemy.get_Enemy_HPinit()/3)/5, 5); //적 에너지 바 위치, 크기
				}else {
					buffg.setColor(Color.red);
					buffg.fillRect(enemy.get_enemy_Point().x+enemy.get_Enemy_Width()/2-enemy.get_Enemy_Width()*(enemy.get_Enemy_HPinit()*1/3)/5/2,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/5, 5); //적 에너지 바 위치, 크기
				}
				buffg.setColor(Color.BLACK);
			}
			if(enemy instanceof Walker2){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //적 에너지 바 위치, 크기
				buffg.setColor(Color.BLACK);
			}
			
			if(enemy instanceof Walker3){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //적 에너지 바 위치, 크기
				buffg.setColor(Color.BLACK);
			}
			
			if(enemy instanceof Biggle){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(!enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //적 에너지 바 위치, 크기
				buffg.setColor(Color.BLACK);
			}
			
			if(enemy instanceof Bat){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(!enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //적 에너지 바 위치, 크기
				buffg.setColor(Color.BLACK);
			}
			
			if(enemy instanceof Boss){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //적 에너지 바 위치, 크기
				buffg.setColor(Color.BLACK);
			}
			
			if(enemy instanceof Enemy_bullet){ //에너미중 워커의 객체가 있다면 그려라
				//좌우전환
				if(enemy.get_Move_Site())
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x + enemy.get_Enemy_Width(), enemy.get_enemy_Point().y,-enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
				else
					buffg.drawImage(enemy.get_Enemy_Img(), enemy.get_enemy_Point().x, enemy.get_enemy_Point().y,enemy.get_Enemy_Width(),enemy.get_Enemy_Height(), this);
			}
//			buffg.drawRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y, ((Enemy_bullet) enemy).get_Enemy_Width(),  ((Enemy_bullet) enemy).get_Enemy_Height()); //사각형으로 일단 대체
//			for (int j = 0; j < enemy.get_Enemy_HP()/50; j++) {
//				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10,  (enemy.get_Enemy_Width()*enemy.get_Enemy_HP()-50*j)/10, 5); //적 에너지 바 위치, 크기
//				if(j==0) {
//					buffg.setColor(Color.GREEN);
//				}
//				else if(j==1) {
//					buffg.setColor(Color.YELLOW);
//				}
//				else if(j==2) {
//					buffg.setColor(Color.RED);
//				}
//				else {
//					buffg.setColor(Color.black);
//				}
//			}
//			buffg.setColor(Color.BLACK);
		
			
			//적군을 움직이는 함수 호출 -> 멀티쓰레드로 변경 생성과 동시에 적군 클레스 쓰레드 자동생성
			//캐릭터와 몬스터 충돌판정 함수 호출
			crash_Decide_Enemy(mainCh, enemy, enemy.get_Move_Site());
			//땅하고 적군하고 출동판정 땅에 떨어지고있을때만 하면 되긴 한다.
			for(int j=0; j< stage.get_Block().size(); j++ ){
			crash_Decide_Enemy_Block(stage.get_Block().get(j), enemy);
			}
			//탐지 구역 그리기
			if(enemy.get_Move_Site()){ //좌측으로 갈때
//					buffg.drawRect(enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point(),  enemy.get_range_Site_Height_Top_Point(),
//							enemy.get_Range_Site_Width_Right_Point(), enemy.get_range_Site_Height_Bottom_Point());	
			}else 
			if(!enemy.get_Move_Site()){ //우측으로 갈때
//					buffg.drawRect(enemy.get_Range_Site_Width_Left_Point(),  enemy.get_range_Site_Height_Top_Point(),
//					enemy.get_Range_Site_Width_Right_Point(), enemy.get_range_Site_Height_Bottom_Point());
			}
			enemy.set_Hero_Information(mainCh);
		}
	}
	
	//스테이지가 시작될때마다 적군 숫자를 파라메터로 받고 생성하고 해야 할 듯 하다.
	public void enemy_Process(int stage_Num){
		if(stage_Num == 1){ //1스테이지 일때 몬스터 배치
	         enemy_List.clear();
	         enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
	               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width()-600, 
	               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Walker2(stage.get_Block().get(0).get_Left_Top_Point().x, 
	               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
	               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
	               stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
	               stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
	               stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
	               stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Walker3(stage.get_Block().get(7).get_Left_Top_Point().x, 
		               stage.get_Block().get(7).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
		               stage.get_Block().get(7).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
	         ////////////////////////////////////////////////////////위 쪽에 1스테이지의  워커 부분 추가
	      }
	      if(stage_Num == 2){ //2스테이지 일때 몬스터 배치
	    	  enemy_List.clear();
		         enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
		               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width()-600, 
		               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Walker2(stage.get_Block().get(0).get_Left_Top_Point().x, 
		               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
		               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
		               stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
		               stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
		               stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
		               stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Walker3(stage.get_Block().get(7).get_Left_Top_Point().x, 
			               stage.get_Block().get(7).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
			               stage.get_Block().get(7).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
			         enemy_List.add(enemy);
	         ////////////////////////////////////////////////////////위 쪽에 2스테이지의  워커 부분 추가
	      }

	      if(stage_Num == 3){ //3스테이지 일때 몬스터 배치
	         enemy_List.clear();
	         enemy_List.clear();
	         enemy = new Bat(stage.get_Block().get(0).get_Left_Top_Point().x, 
	               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
	               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Biggle(stage.get_Block().get(1).get_Left_Top_Point().x, 
	               stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
	               stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Biggle(stage.get_Block().get(8).get_Left_Top_Point().x, 
		               stage.get_Block().get(8).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
		               stage.get_Block().get(8).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Biggle(stage.get_Block().get(9).get_Left_Top_Point().x, 
			               stage.get_Block().get(9).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
			               stage.get_Block().get(9).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
			         enemy_List.add(enemy);
	         enemy = new Bat(stage.get_Block().get(4).get_Left_Top_Point().x, 
	               stage.get_Block().get(4).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
	               stage.get_Block().get(4).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Bat(stage.get_Block().get(2).get_Left_Top_Point().x, 
		               stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
		               stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
	         ////////////////////////////////////////////////////////위 쪽에 3스테이지의  워커 부분 추가
	      }
	      
	      if(stage_Num == 4){ //4스테이지 일때 몬스터 배치
	         enemy_List.clear();
	         enemy = new Bat(stage.get_Block().get(0).get_Left_Top_Point().x, 
	               stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
	               stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Biggle(stage.get_Block().get(1).get_Left_Top_Point().x, 
	               stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
	               stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Biggle(stage.get_Block().get(2).get_Left_Top_Point().x, 
	               stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
	               stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	         enemy_List.add(enemy);
	         enemy = new Bat(stage.get_Block().get(4).get_Left_Top_Point().x, 
		               stage.get_Block().get(4).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
		               stage.get_Block().get(4).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
		         enemy_List.add(enemy);
		         enemy = new Bat(stage.get_Block().get(9).get_Left_Top_Point().x, 
			               stage.get_Block().get(9).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
			               stage.get_Block().get(9).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
			         enemy_List.add(enemy);
	      ////////////////////////////////////////////////////////위 쪽에 4스테이지의  워커 부분 추가
	      }

	   if(stage_Num == 5){ //4스테이지 일때 몬스터 배치
	      enemy_List.clear();
	    
	   ////////////////////////////////////////////////////////위 쪽에 4스테이지의  워커 부분 추가
	   }

	if(stage_Num == 6){ //4스테이지 일때 몬스터 배치
	   enemy_List.clear();
	   enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
	         stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
	         stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	   enemy_List.add(enemy);
	   enemy = new Walker2(stage.get_Block().get(1).get_Left_Top_Point().x, 
	         stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
	         stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	   enemy_List.add(enemy);
	   enemy = new Boss(stage.get_Block().get(19).get_Left_Top_Point().x-1000, 
	         stage.get_Block().get(19).get_Left_Top_Point().x + stage.get_Block().get(19).get_Width(), 
	         stage.get_Block().get(19).get_Left_Top_Point().y - 70); //20, width 는 경계 범위 지정, height 는 몬스터가 밝고 있는 블럭의 높이
	   enemy_List.add(enemy);
	////////////////////////////////////////////////////////위 쪽에 6스테이지의  워커 부분 추가
	}
	if(stage_Num==7)
		enemy_List.clear();
	}
	
	public void enemy_move_stop() {
		for(int i=0; i<enemy_List.size(); i++){
			enemy = (Enemy) enemy_List.get(i);
			enemy.enemy_stop(resumed);
		}
	}
	
	//총알을 발사중인가 아닌가 총알을 생성하는 함수
	public void bullet_Process(){
		if(attack){ //참일때 총알을 생성한다.
			if(weapon_Number == 1 && (cnt-bulletch)>5){ //권총일때
				pistol_Point = new Point(mainCh.get_Hero_X_Point()+mainCh.getWidth()/2,mainCh.get_Hero_Y_Point()+mainCh.getHeight()/2); //캐릭터의 좌쵸를 알아와서 위치 정보를 저장
				weapon = new FireBack(pistol_Point, mainCh.get_Face_Side_LFET_RIGHT()); //캐릭터의 좌표 지점에 권총을 생성한다.
				bullet_List.add(weapon); //권총의 공격을 불릿 리스트에 넣는다 블릿 리스틑 Weapon 클래스로 되어있다.
				bulletch=cnt;
				mainCh.set_Hero_Image("img/dalza_woof.gif");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(weapon_Number == 2 && (cnt-bulletch)>5){ //권총2일때
				pistol_Point = new Point(mainCh.get_Hero_X_Point()+mainCh.getWidth()/2,mainCh.get_Hero_Y_Point()+mainCh.getHeight()/2); //캐릭터의 좌쵸를 알아와서 위치 정보를 저장
				weapon = new Pistol(pistol_Point, mainCh.get_Face_Side_LFET_RIGHT()); //캐릭터의 좌표 지점에 권총을 생성한다.
				bullet_List.add(weapon); //권총의 공격을 불릿 리스트에 넣는다 블릿 리스틑 Weapon 클래스로 되어있다.
				bulletch=cnt;
			}
		}
		if(mine_attack){ //참일때 총알을 생성한다.
			if((cnt-bulletch)>50){ 
				pistol_Point = new Point(mainCh.get_Hero_X_Point(),mainCh.get_Hero_Y_Point()); //캐릭터의 좌쵸를 알아와서 위치 정보를 저장
				weapon = new Mine(pistol_Point, mainCh.get_Face_Side_LFET_RIGHT()); //캐릭터의 좌표 지점에 권총을 생성한다.
				bullet_List.add(weapon); //권총의 공격을 불릿 리스트에 넣는다 블릿 리스틑 Weapon 클래스로 되어있다.
				mainCh.set_Hero_Image("img/dalza_dong.gif");
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mine_attack = false;
			}
		}
		
		if(dash_attack){ //참일때 총알을 생성한다.
			dash(mainCh); //캐릭터의 좌표 지점에 권총을 생성한다.
			dash_attack = false;
		}
	}
	
	   public void dash(Hero mainCh) {
		      mainCh.set_Hero_Image("img/dalza_run.gif");
		      for (int i = 0; i < 10; i++) {
		         if(!mainCh.get_Face_Side_LFET_RIGHT()) {
		            mainCh.set_Hero_X_Point(mainCh.get_Hero_X_Point()+30);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point(),   mainCh.get_Hero_Y_Point(), mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()-30,   mainCh.get_Hero_Y_Point(), mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()-60,   mainCh.get_Hero_Y_Point(), mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()-90,   mainCh.get_Hero_Y_Point(), mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		         }
		         else { 
		            mainCh.set_Hero_X_Point(mainCh.get_Hero_X_Point()-30);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()+mainCh.get_Hero_Width(),   mainCh.get_Hero_Y_Point(), -mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()+mainCh.get_Hero_Width()+30,   mainCh.get_Hero_Y_Point(), -mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()+mainCh.get_Hero_Width()+60,   mainCh.get_Hero_Y_Point(), -mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		            buffg.drawImage(mainCh.get_Hero_Img(), mainCh.get_Hero_X_Point()+mainCh.get_Hero_Width()+90,   mainCh.get_Hero_Y_Point(), -mainCh.get_Hero_Width(), mainCh.get_Hero_Height(),this);
		         }
		         try {
		            Thread.sleep(50);
		         } catch (InterruptedException e) {
		         }
		      }
		   }
	//총알 제거 함수
	public void remove_Bullet(Weapon weapon, int i){
		//x축에서 화면 밖으로 나갔을때 삭제
		if(weapon.getPoint().x > buffw-30 || weapon.getPoint().x < 10 
				|| weapon.get_Remove_Bullet_Choice()){
			bullet_List.remove(i);
		}
		if(end_Stage) {
			bullet_List.clear();
		}
	}
	
	//implement Runnable를 통해 생성된 스레드 
	@Override
	public void run() {
		try{
			while(true){
				 //System.out.println(mainCh.get_Hero_Height());
				 //System.out.println(60/mainCh.get_Hero_Height());
				if(!resumed) {
					mainCh.move();//캐릭터의 움직임을 항상 체크한다.
					bullet_Process();//총알 생성 함수 호출
					//점프 실행 매소드
					if(jump){
						mainCh.set_Hero_Jumping();
					}
					mainCh.jump_Move();
					if(slowEffect && cnt-slowtime>20) {
						mainCh.set_Hero_Move_Velocity(10);
						mainCh.set_Hero_Jump_Speed(3);
						slowEffect = false;
					}
				}
				repaint(); //화면을 지웠다 다시 그리기
				Thread.sleep(20); //20milli sec 로 스레드 돌리기
				cnt++;
			}
		}catch (Exception e) {
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//키 핸들러
	@Override
	//키보드가 눌러졌을떼 이벤트 처리하는 곳
	public void keyPressed(KeyEvent e) {

		switch(e.getKeyCode()){
		case KeyEvent.VK_UP :
			if(resumed) {
				if(b==20) {
					b=0;
					a=20;
				}
				//System.out.println(a);
				if(d==20) {
					d=0;
					b=20;
				}
			}
			break;
		case KeyEvent.VK_DOWN :
			if(resumed) {
				if(b==20) {
					b=0;
					d=20;
				}
				if(a==20) {
					a=0;
					b=20;
				}
			}
			break;
		case KeyEvent.VK_LEFT :
			mainCh.set_Hero_X_Left();
			break;
		case KeyEvent.VK_RIGHT :
			mainCh.set_Hero_X_Right();
			break;
		case KeyEvent.VK_A :
			//공격중일때 총알을 생성한다.
			weapon_Number=2;
			attack = true;
			break;
		case KeyEvent.VK_S :
			//공격중일때 총알을 생성한다.
			weapon_Number=1;
			attack = true;
			break;
		case KeyEvent.VK_SPACE :
			//점프 실행한다.
			jump = true;
			break;
		case KeyEvent.VK_D :
			//칼질..
			dash_attack = true;
			break;
		case KeyEvent.VK_F :
			//칼질..
			mine_attack = true;
			break;
		case KeyEvent.VK_ESCAPE:
			resumed=!resumed;
			enemy_move_stop();
			break;
		}
	}

	@Override
	//키보드가 눌러졌다가 때어졌을때 이벤트 처리하는 곳
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP :
			break;
		case KeyEvent.VK_LEFT :
			mainCh.set_Hero_X_Left_Stop();
			break;
		case KeyEvent.VK_RIGHT :
			mainCh.set_Hero_X_Right_Stop();
			break;
		case KeyEvent.VK_A :
			//총알생성을 중지한다..
			attack = false;
			break;
		case KeyEvent.VK_S :
			//총알생성을 중지한다..
			attack = false;
			break;
		case KeyEvent.VK_SPACE :
			//점프 종료
			jump = false;
			break;
		case KeyEvent.VK_COMMA :
			//점프 종료
			ta.setVisible(!ta.isVisible());
			this.revalidate();
			break;
		case KeyEvent.VK_X:
			mainCh.set_Hero_Y_Point(100);
			break;
		case KeyEvent.VK_ENTER:
			if(resumed) {
				if(a==20) {
					resumed=!resumed;
					enemy_move_stop();
				}
				if(b==20) {
				}
				if(c==20) {
				}
				if(d==20) {
					dispose();
					Main menu = new Main();
				}
			}
			else {
				tf.setFocusable(true);
				tf.requestFocus();
				break;
			}
		}
	}

	@Override
	//키보드가 타이핑 될 때 이벤트 처리하는 곳
	public void keyTyped(KeyEvent e) {
	}
	///////////////////////////////////////////////////////////////////////////////////////////
}