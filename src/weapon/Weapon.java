package weapon;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;


//기본 총알의 부모 클래스
public class Weapon {
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//이미지를 불러오기 위한 툴킷
	
	//총알 좌표
	protected Point bullet_Point;
	//총알 속도
	protected int bullet_Speed;
	//총알의 파워
	protected int bullet_Power; 
	//연사 속도
	protected int bullet_time;
	
	//총알의 두께
	protected int weapon_Width;
	protected int weapon_Height;

	//총알이 날아갈 방향  true:좌 false:우   
	protected boolean bullet_Side_LEFT_RIGHT;
	
	//총알 충돌 판정시 삭제 하기 위함
	private boolean bullet_Remove_Bollean; 
	
	protected Image Weapon_pic;	
	protected ImageIcon Weapon_icon;
	
	//생성시 초기화, 총알 좌표 초기화	
	Weapon(Point character_Point, boolean bullet_Side_LEFT_RIGHT) {
		Weapon_pic = tk.getImage(this.getClass().getResource("/img/M_bullet.gif"));
		Weapon_icon = new ImageIcon(this.getClass().getResource("/img/M_bullet.gif"));

		//총알 의 시작 좌쵸를 전달 받는 캐릭터의 x 좌쵸로 설정
		//총알 의 시작 좌쵸를 전달 받는 캐릭터의 y 좌쵸로 설정
		bullet_Point = new Point(character_Point.x, character_Point.y+30);
		
		weapon_Width = Weapon_icon.getIconWidth();
		weapon_Height = Weapon_icon.getIconHeight();
		
		//총알의 기본 속도 10으로 생성
		bullet_Speed = 10;
		
		//총알의 기본 파워
		bullet_Power = 100;
		bullet_time = 1;
		
		this.bullet_Side_LEFT_RIGHT = bullet_Side_LEFT_RIGHT;
		
		bullet_Remove_Bollean = false;
	}
	
	public Image get_Weapon_Img(){
		return Weapon_pic; 
	}

	//총알 두께 가져오기
	public int get_Weapon_Width(){
		return weapon_Width;
	}
	public int get_Weapon_Height(){
		return weapon_Height;
	}
	
	
	//사라질 총알 
	public void set_Remove_Bullet_Choice(){
		bullet_Remove_Bollean = true;
	}
	//사라질 총알 
	public boolean get_Remove_Bullet_Choice(){
		return bullet_Remove_Bollean;
	}
	
	//총알의 좌표를 리턴
	public Point getPoint(){
		return bullet_Point;
	}
	
	//총알의 좌우측 방향을 린턴ssss
	public boolean get_Bullet_Side_LEFT_RIGHT(){
		return bullet_Side_LEFT_RIGHT;
	}
	
	public int get_Bullet_Power(){
		return bullet_Power;
	}
	
	public int get_Bullet_Time(){
		return bullet_time;
	}
	
}



