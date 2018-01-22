
package enemy;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.RepaintManager;

import character.Hero;

//적군 기본 클래스
public class Enemy extends Thread{ //쓰레드를 상속받아서 적군은 적군 알아서 움직이도록 한다.
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//이미지를 불러오기 위한 툴킷

	//적군의 기본 좌표생성
	protected Point enemy_Point;
	
	//움직일 방향 좌우 랜덤
	protected boolean move_Site; //true 이면 좌측 false이면 우측
	protected double randomValue; //랜덤 값 추출
	
	//경계 구역 범위
	protected int left_Bound_Site;
	protected int right_Bound_Site;
	protected int bottom_Bound_Site;
	
	
	//적을 발견 못했을때는 false, 발견했을때는 true
	protected boolean find_Hero;
	
	//적 영웅 탐지 사각형 왼쪽 위 범위
	protected int range_Site_Width_Left_Point;
	protected int range_Site_Height_Top_Point;
	
	
	//적 영웅 탐지 사각형 오른쪽 밑 아래쪽 범위
	protected int range_Site_Width_Right_Point;
	protected int range_Site_Height_Bottom_Point;
	
	//적군의 가로 두께 및 세로두께
	protected int width;
	protected int height;
	
	//적군의 생명력
	protected int enemy_HP;
	protected int boss_enemy_HP;
	protected int enemy_HPinit;
	protected int boss_enemy_HPinit;
	
	//적군 추락 시작 true 일때
	protected boolean down_Start;
	
	//중력 가속도
	private int g = 1;
	private int gSum;
	
	boolean resumed;
	
	//스레드 생성
	Thread th;
	
	protected Image Enemy_pic;
	protected ImageIcon Enemy_icon;

	//적군도 히어로의 위치 정보및 정보를 알아야 한다.
	protected Hero hero;
	public void set_Hero_Information(Hero hero){
		this.hero = hero;
	}
	
	public Enemy(int left_Bound_Site, int right_Bound_Site, int bottom_Bound_Site) { //생성될 곳 좌표도 받아와야함
		
		Enemy_pic = tk.getImage(this.getClass().getResource("/img/walker1.gif"));
		Enemy_icon = new ImageIcon(this.getClass().getResource("/img/walker1.gif"));

		//워커 둘레
		width = Enemy_icon.getIconWidth();
		height = Enemy_icon.getIconHeight();
		
		int a = 80;
		width = a*Enemy_icon.getIconWidth()/Enemy_icon.getIconHeight();
		height = a;
		
		//경계구역 범위 초기 설정
		init_Bound_Site(left_Bound_Site, right_Bound_Site, bottom_Bound_Site);
		
		//적 영웅 탐지 우측 아래쪽 사각형 범위 지정 
		range_Site_Width_Right_Point = 300;
		range_Site_Height_Bottom_Point = 50;
		
		
		//down_Start 가 true 이가 되면 추락 시작
		down_Start = false;
		
		
		//일단 기본적으로 100,600 에 생성
		enemy_Point = new Point(right_Bound_Site, bottom_Bound_Site);
		
		
		//적 영웅 탐지 사각형 범위 생성 위치값 동일
		range_Site_Width_Left_Point = enemy_Point.x;
		range_Site_Height_Top_Point = enemy_Point.y;
		
		
		randomValue = Math.random();
		if(randomValue < 0.5){
			move_Site = true;	//좌측으로 이동
			
		}else {
			move_Site = false; //우측으로 이동
			
		}
		
		find_Hero = false; //적영웅을 발견했을때 true 로 변경
		
		th = new Thread(this); 	  //스레드 생성
		th.start(); 		  //스레드 시작
	}
	
	//몬스터가 히어로 쫒는 것을 종료했을때 탐색 범위를 원위치 시킨다, 몬스터 발판 변경후 경계지역 초기화
	public void init_Range_Site(int enemy_Point_X, int enemy_Point_Y){
		//range_Site_Width_Right_Point = 300;
		//range_Site_Height_Bottom_Point = 50;
		
		range_Site_Width_Left_Point = enemy_Point_X;
		range_Site_Height_Top_Point = enemy_Point_Y - gSum;
	}
	
	
	
	public void init_Bound_Site(int left_Bound_Site, int right_Bound_Site, int bottom_Bound_Site){ 
		//몬스터가 튕겨 나가거나 주인공 캐릭을 쫓다가 다른 바닥으로 갔을때 한번더 초기화 해야한다.
		this.left_Bound_Site = left_Bound_Site;
		this.right_Bound_Site = right_Bound_Site;
		this.bottom_Bound_Site = bottom_Bound_Site;
	}
	
	//영웅을 발견했을때
	public void set_Find_Hero(int hero_X_Point){
		find_Hero = true;
	}
	//영웅을 놓쳤을때
	public void set_Not_Find_Hero(){
		find_Hero = false;
	}
	
	public void set_Enemy_Image(String img) {
		Enemy_pic = tk.getImage(this.getClass().getResource(img));
		Enemy_icon = new ImageIcon(img);
	}
	
	//기본 영웅을 발견 못했을때 좌우로 경계 서듯 움직이고 있어야함 //synchronized 해당 함수가 작동하는 동안 동기화를 수행한다.
	public void enemy_Move(){ //적의 경계 범위는 바닥 정보의 범위 값을 가져와서 계산한다.

		//적영웅을 발견하지 못했을때 기본 경계 구역을 돌아다닌다.
		if(!find_Hero){
				if(move_Site){	//좌측으로 이동
					enemy_Point.x -= 5;
				}else {			//우측으로 이동
					enemy_Point.x += 5;
				}
				
				//좌우로 움직일 범위 설정
				if((left_Bound_Site) >= enemy_Point.x){
					move_Site = false;
					
				}else if((right_Bound_Site) <= enemy_Point.x){
					move_Site = true;
				}
				
		}else { //적 영웅을 발견하게 되면 적 영웅을 추격한다.
			
		}
	}
	
	//경계 구역 사각형 값 리턴
	public int get_Range_Site_Width_Right_Point(){
		return range_Site_Width_Right_Point;
	}
	
	//경계 구역 사각형 값 리턴
	public int get_range_Site_Height_Bottom_Point(){
		return range_Site_Height_Bottom_Point;
	}
	
	//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
	public void set_Range_Site_Width_Right_Point(int range_Site_Width_Right_Point){
		this.range_Site_Width_Right_Point = range_Site_Width_Right_Point;
	}
	
	//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
	public void set_Range_Site_Height_Bottom_Point(int range_Site_Height_Bottom_Point){
		this.range_Site_Height_Bottom_Point = range_Site_Height_Bottom_Point;
	}
	
			
	//경계 구역 사각형 좌측 상단 값 반환
	public int get_Range_Site_Width_Left_Point(){
		return range_Site_Width_Left_Point;
	}
	
	//경계 구역 사각형 값 리턴
	public int get_range_Site_Height_Top_Point(){
		return range_Site_Height_Top_Point;
	}
	
	//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
	public void set_Range_Site_Width_Left_Point(int range_Site_Width_Left_Point){
		this.range_Site_Width_Left_Point = range_Site_Width_Left_Point;
	}
	
	//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
	public void set_Range_Site_Height_Up_Point(int range_Site_Top_Height){
		this.range_Site_Height_Top_Point = range_Site_Top_Height;
	}
	
	public Image get_Enemy_Img(){
		return Enemy_pic; 
	}

	//적군 경계범위 리턴 좌측값
	public int get_Left_Bound_Site(){
		return left_Bound_Site;
	}
	//우측값
	public int get_Right_Bound_Site(){
		return right_Bound_Site;
	}
		
	//적군 가로 세로 반환
	public int get_Enemy_Width(){
		return width;
	}
	
	public int get_Enemy_Height(){
		return height;
	}
	
	//내가 보고 있는 방향 리턴 
	public boolean get_Move_Site(){ //true = 좌측
		return move_Site;
	}
	
	//저장된 적군의위치를 리턴한다.
	public Point get_enemy_Point(){
		return enemy_Point;
	}
	
	//적군 움직임 쓰레드
	public void enemy_stop(boolean a) {
		this.resumed = a;
	}

	public void run() {
		try{
			while(true){
				if (!resumed) {
					enemy_Move();
				}
				//적군 발판에서 밀려서 땅으로 떨어지는것
				if(down_Start){
					enemy_Down_Algorithm();
				}
				Thread.sleep(20); //20milli sec 로 스레드 돌리기
			}
		}catch (Exception e) {
		}
	}
	
	//적군 추락 알고리즘
	public void enemy_Down_Algorithm(){
		 //낙하 과속 방지
		 if(gSum >= 15){
			 gSum = 15;
		 }
		 //떨어지면서 확인을 해야한다.
		 gSum += g;
		 enemy_Point.y += gSum;
		 //다른 발판에 닿았을때 추락을 중지해야 하며 다시금 경계위치를 지정해야한다.
		 if(End_Y_Point < enemy_Point.y){ //이때 중지
			 //적군과 바닥이 맞닿는 지점을 적군의 y 좌표로 설정한다. [적군 착지하면 블록위에 안착시킨다.]
			 enemy_Point.y = End_Y_Point;
			 //추락을 중지하고 중력 가속도를 0 으로 초기화
			 set_Down_Start_False();
			 //닿은 사각형 정보 가져와야함
			 //3개 값을 입력 경계시작 위치, 종료위치, y 포인트
			 //init_Bound_Site();
		 }
	}
	
	int End_Y_Point = 1000;
	//적군이 새롭게 놓이 y 포인트 받아
	public void get_Enemy_Exit_Yoint(int End_Y_Point){
		this.End_Y_Point = End_Y_Point;
	}
	
	//적군이 발판에서 벗어나면 추락시작
	public void set_Down_Start_True(){
		down_Start = true;
	}
	public void set_Down_Start_False(){
		//여기서 발판의 정보를 가져와서 뿌려야함
		End_Y_Point = 1000; //초기화
		gSum = 0;
		down_Start = false;
	}
	public boolean get_Down_Start(){
		return down_Start;
	}
	
	//공격으로 몬스터 방향 전환
	public void set_Move_Site(boolean flag){
		//flag 가 ture 이면 왼쪽으로 전환
		if(flag){
			move_Site = true;
		}else {
			move_Site = false;
		}
	}
	
	//피격 판정시 뒤로 밀려남 효과
			public void knockback(boolean flag){
				//flag 가 ture 이면 왼쪽으로 전환
				if(flag){
					enemy_Point.x += 15;
				}else {
					enemy_Point.x -= 15;
				}
			}
	
	//적군의 피가 줄어듬
	public void enemy_HP_Down(int gun_Damage){
		enemy_HP-=gun_Damage;
		//System.out.println(enemy_HP);
	}
	
	//적군 에너지 체크하기
	public int get_Enemy_HP(){
		return enemy_HP;
	}
	
	//적군 에너지 체크하기
	public void set_Enemy_Y(int x){
		enemy_Point.y=x;
	}

	//보스 HP 체크
		public int get_boss_Enemy_HP(){
			return boss_enemy_HP;
		}
		
		public int get_Enemy_HPinit(){
			return enemy_HPinit;
		}
		
		public int get_boss_Enemy_HPinit(){
			return enemy_HPinit;
		}
}
