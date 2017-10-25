package weapon;

import java.awt.Point;

import javax.swing.ImageIcon;

//권총
public class FireBack extends Weapon{

      public FireBack(Point character_Point, boolean bullet_Side_LEFT_RIGHT) {
         
         super(character_Point, bullet_Side_LEFT_RIGHT);
         //피스톨의 기본 공격력
         bullet_Power = 1;
         bullet_time = 1;
         bullet_Speed = 5;
         Weapon_pic = tk.getImage("img/wave.gif");
         Weapon_icon = new ImageIcon("img/wave.gif");
         weapon_Width=0;
         weapon_Height=0;
      }
      
      //권총의 날아가는 방향을 계산 좌축 또는 우측
      public void pistol_Move(boolean direction){
         //참이면 왼쪽, 거짓이면 오른쪽 으로 이동
         int i=2;
         if(weapon_Width>80||weapon_Width<-80) i=0;
         bullet_Point.y-= i/2;
         weapon_Height+=i;
         if(direction){ //좌측
            bullet_Point.x -= bullet_Speed;
            weapon_Width-=i;
         }else{// 우측
            bullet_Point.x += bullet_Speed;
            weapon_Width+=i;
         }
         
      }
      
   }