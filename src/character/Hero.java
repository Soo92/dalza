package character;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Hero extends JPanel{
   Toolkit tk = Toolkit.getDefaultToolkit();    //이미지를 불러오기 위한 툴킷
   //히어로 기본 좌표생성
   private Point Hero_Point;
   
   private int x_Point;
   private int y_Point;
   
   //히어로의 두께
   private int hero_Height;
   private int hero_Width;
   private int move= 10;
   private int JumpSpeed =3;

   //히어로 무빙(부드러운 움직임 표현)
   boolean x_Flag_Left;
   boolean y_Flag_Left;
   boolean x_Flag_Right;
   boolean y_Flag_Right;
   
   //점프시
   boolean jump_Hero;
   //점프시 올라가는중인지 가라 앉는 중 인지 확인 true 이면 점프중
   boolean jump_Hero_UP_DOWN;
   //점프 시작과 함께 기본 캐릭터의 도착지점 Y를 체크하기위한 플래그
   boolean jump_Hero_Stop_Point;
   //도착지점의 y좌표를 지정할 변수
   int jump_Hero_Stop_Point_Y;
   
   protected String ImgUrl;
   protected boolean CrashBlock = false;
   protected Image Hero_pic;
   protected ImageIcon Hero_icon;

   //캐릭터가 보고있는 방향 좌측일때 true 우측 일때 false
   boolean face_Side_LEFT_RIGHT;
   
   //캐릭터 점프시 중력가속도
   int g;
   int gSum;
   int dgSum;
   
   
   //점프 스케줄링 점프 스케줄링 이 어느정도 도달해야 다시 띌수 있도록 조정
   int jump_Time_Schedule;
   
   
   //히어로 기본 생성자
   public Hero() {
    ImgUrl = "img/dalza_run.gif";
  	Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_run.gif"));
	Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_run.gif"));
      
      x_Point = 100;
      y_Point = 620;
      Hero_Point = new Point(x_Point,y_Point);

      //히어로 폭
      int a=100;
      hero_Width = Hero_icon.getIconWidth()/Hero_icon.getIconHeight()*a;
      hero_Height = a;
      
      x_Flag_Left = false;
      x_Flag_Right = false;
      y_Flag_Left = false;
      y_Flag_Right = false;
      
      face_Side_LEFT_RIGHT = false; //캐릭터 생성과 동시에 우측 방향
      
      jump_Hero = false;//점프키 누르면 점프 하는 명령어 실행
      jump_Hero_UP_DOWN = true; //올라가는중
      jump_Hero_Stop_Point = true; //true 일때 한번 실행
      
      g = 1;//중력가속도
      gSum = 15;
      dgSum = 0;//낙하 가속도
      
      //점프 스케줄링 0 으로 초기화
      jump_Time_Schedule = 0;
   }
   
   public String get_Hero_ImgUrl(){
      return ImgUrl; 
   }

   public Image get_Hero_Img(){
      return Hero_pic; 
   }
   
   public void set_Hero_Image(String img) {
		Hero_pic = tk.getImage(this.getClass().getResource(img));
		Hero_icon = new ImageIcon(Hero_pic);
	}
   
   //히어로 폭 반환
   public int get_Hero_Width(){
      return hero_Width; 
   }
   //히어로 높이 반환
   public int get_Hero_Height(){
      return hero_Height;
   }
   
   //히어로 넉백 적군이 영웅을 피격했을때. 좌측으로 넉백
   public void left_Knock_Back(){
      x_Point -= 10;
   }
   //우측으로 넉백
   public void right_Knock_Back(){
      x_Point += 10;
   }
   
   public void move(){  //synchronized 해당 함수가 작동하는 동안 동기화를 수행한다.
      //좌우 움직임 컨트롤
	   if(x_Flag_Left){
			Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_run.gif"));
			Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_run.gif"));
			x_Point -= move;
		}
		if(x_Flag_Right){
			Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_run.gif"));
			Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_run.gif"));
			x_Point += move;
		}
		if(!x_Flag_Left&&!x_Flag_Right) {
			//System.out.println("a");
			Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_normal.gif"));
			Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_normal.gif"));
		}
   }
   
   //캐릭터 점프 무빙
   public void jump_Move(){ //캐릭터의 Y축 값을 가져와서 Y축과 동일 해지거나 작아질때까지 감소시키고 Y축 값을 리턴 또는 중간에 블럭을 밟으면 블럭의 값을 가져와 일치시킨다.
      
      //jump_Time_Schedule 가 일정 수 이상일때 점프 가능
      jump_Time_Schedule++;
      
      
      if(jump_Hero){
         //스톱 지정 지정하기
         if(jump_Hero_Stop_Point){
            jump_Hero_Stop_Point = false;
         }
         
         //gSum이 특정 숫자에 도달했을때 방향 역전
          if(gSum == 0){
             jump_Hero_UP_DOWN = !jump_Hero_UP_DOWN;
          }
          
          //낙하 과속 방지
          if(gSum >= 15){
             gSum = 15;
          }
          
          //올라가는중
          if(jump_Hero_UP_DOWN){
             gSum -= g;
             y_Point -= gSum+JumpSpeed;
            Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_jump.gif"));
			Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_jump.gif"));
			
          }else{ //내려가는중
             gSum += g;
             y_Point += gSum;
             Hero_pic = tk.getImage(this.getClass().getResource("/img/dalza_run.gif"));
			 Hero_icon = new ImageIcon(this.getClass().getResource("/img/dalza_run.gif"));
          }
      }
   }
   
   //점프를 멈출 시점을 정해준다.
   public void jump_Move_Stop(int y_Stop){
      jump_Hero_Stop_Point_Y = y_Stop; //맨 처음 캐릭터가 점프후 도착할 Y좌표
      //점프가 중지되어야 할 시점 //점프가 다시 가능하도록 초기화
       if(y_Point >= jump_Hero_Stop_Point_Y){         //초기값으로 받은 y 포인터가 캐릭터보다 아래이면 중지
          y_Point = jump_Hero_Stop_Point_Y;      
          jump_Hero = false;                     //Jump_Move 매소드 안으로 못들어오게 한다. 점프 중지
          jump_Hero_Stop_Point = true;            //점프 중지후 다시 멈출 Y 포인터를 받을수 있도록 초기화
          jump_Hero_UP_DOWN = true;    //중력 가속도 증감 조절 하는 불린
          g = 1;
          gSum = 15;
       }
   }
   
   //벽을 밝고 있지 않으면 땅으로 떨어져야 한다.
   public void auto_Jump_Down(){
      //점프하는 순간이 아니면서 캐릭터가 블럭과 맞닿아있지 않을때
      if(!jump_Hero){ //jump_Hero = 점프 중일때
         if(dgSum>=15)dgSum = 15;
          y_Point += dgSum;
          dgSum += g;
          //System.out.println(dgSum);
          jump_Hero_UP_DOWN = false; //점프 하지않고 떨어지는 중에도 안착할 수 있도록 함
      }else dgSum = 0;
   }
   
   //머리가 끼어있으면 아래로 떨어뜨린다.
   public void auto_Jump_Down_Head(int minus_Num){
       y_Point += minus_Num+1;
   }
   
   //우측으로 못들가게함
   public void stop_Move_Right(int x_Temp){
      //x_Point = x_Temp;
      //우측 벽 에 부딪혀서 오른쪽으로 제어 불가능
      x_Flag_Right = false;
      //왼쪽으로 -5 만큼씩 밀어줘야 벽으로 안들어가짐
      x_Point -= 10;
      //y_Point 는 약간의 버그 때문에 추가하였음, 몹한테 부딛히면 벽에 붙을 수 있기때문에 미세하게 움직이도록 함
     // y_Point -= 5;
      
   }
   
   //좌측으로 못들가게함
   public void stop_Move_Leftt(int y_Temp){
         //x_Point = x_Temp;
         //좌측 벽 에 부딪혀서 오른쪽으로 제어 불가능
         x_Flag_Left = false;
         //오른쪽으로 + 5 만큼씩 밀어줘야 벽으로 안들어가짐
         x_Point += 10;
         //y_Point 는 약간의 버그 때문에 추가하였음, 몹한테 부딛히면 벽에 붙을 수 있기때문에 미세하게 움직이도록 함
         //y_Point -= 5;
   }
   
   //캐릭터가 점프를 하지않고 떨어졌을때 중력 가속도를 초기화 한다.
   public void set_dgSum_Zero(){
      dgSum=0;
   }
   
   //벽하고 충돌시 바로 아래로 떨러지도록 하는 함수
   public void set_Jump_Hero_UP_DOWN(){
      dgSum=0;
      jump_Hero_UP_DOWN = false;
   }
   
   //점프 키를 눌렀을때 점프 를 실행하도록 설정함
   public void set_Hero_Jumping(){
      if(jump_Time_Schedule >= 15){   
         if(!jump_Hero){ //점프중일때는 점프가 불가능함
         jump_Hero = true;
         //점프 스케줄 초기화
         jump_Time_Schedule = 0;
         }      
      }
   }
   
   //캐릭터가 점프 중인지 아닌지 반환한다
   public boolean get_Jump_State(){
      return jump_Hero_UP_DOWN; //true = 올라가는중
   }
   
   public void set_CrashBlock(Boolean CrashBlockin){
	      CrashBlock=CrashBlockin; //true = 올라가는중
	}
   public void get_CrashBlock(Boolean CrashBlockin){
	      CrashBlock=CrashBlockin; //true = 올라가는중
	}
   //주인공 x 축 리턴
   public int get_Hero_X_Point(){
      return x_Point;
   }
   //주인공 y 축 리턴
   public int get_Hero_Y_Point(){
      return y_Point;
   }

   public Point get_Hero_Point(){
      Hero_Point = new Point(x_Point,y_Point);
      return Hero_Point;
   }
   
   public Boolean get_Jump_Hero(){
	      return jump_Hero;
	   }

   //캐릭터가 처다보고 있는 방향 리턴
   public boolean get_Face_Side_LFET_RIGHT(){
      return face_Side_LEFT_RIGHT;
   }
   
   //주인공 점프후 착지하면 블록위에 안착시킨다
   public void set_Hero_Y_Point(int set_Y_Point){
      y_Point = set_Y_Point;
   }
   
   public void set_Hero_X_Point(int set_X_Point){
      x_Point = set_X_Point;
   }

   //주인공 좌측이동
   public void set_Hero_X_Left(){
      x_Flag_Left = true;
      face_Side_LEFT_RIGHT = true; //좌측 방향
   }
   
   //주인공 우측 이동
   public void set_Hero_X_Right(){
      x_Flag_Right = true;
      face_Side_LEFT_RIGHT = false; //우측 방향
   }
   //주인공 정지
   public void set_Hero_X_Left_Stop(){
      x_Flag_Left = false;
   }
   //주인공 정지
   public void set_Hero_X_Right_Stop(){
      x_Flag_Right = false;
   }
   public void set_Hero_Jump_Speed(int JumpS){
	     JumpSpeed = JumpS; 
	   }
   public void set_Hero_Move_Velocity(int velocity){
       move = velocity;
}
}