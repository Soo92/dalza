package weapon;

import java.awt.Point;

import javax.swing.ImageIcon;

//권총
public class Mine extends Weapon{

		public Mine(Point character_Point, boolean bullet_Side_LEFT_RIGHT) {
			
			super(character_Point, bullet_Side_LEFT_RIGHT);

			Weapon_pic = tk.getImage(this.getClass().getResource("/img/shit.gif"));
			Weapon_icon = new ImageIcon(this.getClass().getResource("/img/shit.gif"));
			
			//피스톨의 기본 공격력
			bullet_Power = 20;
			weapon_Height =70;
			weapon_Width =70;
			bullet_time = 5;
			
			bullet_Point = new Point(character_Point.x+35, character_Point.y+43);
			
		}
		
		//권총의 날아가는 방향을 계산 좌축 또는 우측
		public void pistol_Move(boolean direction){
			//참이면 왼쪽, 거짓이면 오른쪽 으로 이동
			int a = bullet_Point.x;
			if(direction){ //좌측
				a -= 10;
			}else{// 우측
				a += 10;
			}
			
		}
		
	}
