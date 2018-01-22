package weapon;

import java.awt.Point;

import javax.swing.ImageIcon;

//권총
public class Pistol extends Weapon{

		public Pistol(Point character_Point, boolean bullet_Side_LEFT_RIGHT) {
			
			super(character_Point, bullet_Side_LEFT_RIGHT);
			
			//피스톨의 기본 공격력
			bullet_Power = 1;
			bullet_time = 1;
			bullet_Speed = 10;
			Weapon_pic = tk.getImage(this.getClass().getResource("/img/M_bullet.gif"));
			Weapon_icon = new ImageIcon(this.getClass().getResource("/img/M_bullet.gif"));		}
		
		//권총의 날아가는 방향을 계산 좌축 또는 우측
		public void pistol_Move(boolean direction){
			//참이면 왼쪽, 거짓이면 오른쪽 으로 이동
			if(direction){ //좌측
				bullet_Point.x -= bullet_Speed;
			}else{// 우측
				bullet_Point.x += bullet_Speed;
			}
		}
	}
