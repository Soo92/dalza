package enemy;

import javax.swing.ImageIcon;

import character.Hero;

//워커 클래스
public class Enemy_bullet extends Walker3{

	//lock_Trans = 태세 변환을 위한 불린 공격/경계 등
	private boolean lock_Trans;
	
	public Enemy_bullet(int left_Site, int right_Site, int bottom_Site) {
		super(left_Site, right_Site, bottom_Site);
		Enemy_pic = tk.getImage("img/bullet.gif");
		Enemy_icon = new ImageIcon("img/bullet.gif");
		
		width = Enemy_icon.getIconWidth();
		height = Enemy_icon.getIconHeight();

		int a=30;
		width = a*Enemy_icon.getIconWidth()/Enemy_icon.getIconHeight();
		height = a;
		
		// TODO Auto-generated constructor stub
		//적 영웅 탐지 사각형 범위 지정
		range_Site_Width_Right_Point = 250;
		range_Site_Height_Bottom_Point = 50;

		lock_Trans = true;
		//워커의 기본 생명력 50
		enemy_HP = 1;
	}
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@오버 라이딩
		//경계 구역 사각형 값 리턴
		public int get_Range_Site_Width_Right_Point(){
			return range_Site_Width_Right_Point;
		}
		
		//경계 구역 사각형 값 리턴
		public int get_Range_Site_Height(){
			return range_Site_Height_Bottom_Point;
		}

		public void set_Y(int a){
			enemy_Point.y=a;
		}
	
		/*//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
		public void set_Range_Site_Width_Right_Point(int range_Site_Width_Right_Point){
			this.range_Site_Width_Right_Point = range_Site_Width_Right_Point;
		}
		
		//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
		public void set_Range_Site_Height_Bottom_Point(int range_Site_Height_Bottom_Point){
			this.range_Site_Height_Bottom_Point = range_Site_Height_Bottom_Point;
		}*/
		
			//몬스터가 히어로 쫒는 것을 종료했을때 탐색 범위를 원위치 시킨다.
			public void init_Range_Site(){
				range_Site_Width_Right_Point = 250;
				range_Site_Height_Bottom_Point = 50;
				
				range_Site_Width_Left_Point = enemy_Point.x;
				range_Site_Height_Top_Point = enemy_Point.y;
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
						/*public void set_Range_Site_Width_Left_Point(int range_Site_Width_Left_Point){
							this.range_Site_Width_Left_Point = range_Site_Width_Left_Point;
						}
						
						//경계 구역 사각형 값 을 증가시킨다. 영웅을 발견했을때 범위를 증가시킨다.
						public void set_Range_Site_Height_Top_Point(int range_Site_Top_Height){
							this.range_Site_Height_Top_Point = range_Site_Top_Height;
						}*/
		
		//경계값 한번만 증가 시키기위한 플래그
		boolean plusOnce = true;
		//영웅을 발견했을때
		public void set_Find_Hero(int hero_X_Point){
			find_Hero = true; 
			/*if(hero_X_Point > enemy_Point.x){
				move_Site = false;
			}else {
				move_Site = true;
			}*/
		}
		//영웅을 놓쳤을때
		public void set_Not_Find_Hero(){
			find_Hero = false;
		}
		//피격 판정시 뒤로 밀려남 효과
		public void knockback(boolean flag){
			//flag 가 true 이면 왼쪽으로 전환
			if(flag){
				enemy_Point.x += 5;
			}else {
				enemy_Point.x -= 5;
			}
			//넉백위치에서 다시금 경계 사각형을 그린다.
			range_Site_Width_Left_Point = enemy_Point.x;
		}
		
		public void enemy_Move(){ //적의 경계 범위는 바닥 정보의 범위 값을 가져와서 계산한다.
			//적영웅을 발견하지 못했을때 기본 경계 구역을 돌아다닌다.
			if(!find_Hero){
					if(move_Site){	//좌측으로 이동
						enemy_Point.x -= 15;
					}else {			//우측으로 이동
						enemy_Point.x += 15;
					}
					//좌우로 움직일 범위 설정 적군이 영웅을 발견 못했을때 경계 범위를 블럭 전체로 잡는다.
					if((left_Bound_Site + 10) >= enemy_Point.x){
						move_Site = true;
					}else if((right_Bound_Site + 20) <= enemy_Point.x){
						move_Site = false;
					}
					if(!lock_Trans){
						init_Range_Site(); //적 영웅 공격 상태가 아닐때 원상 복귀
						lock_Trans = true; //태세 변환을 위한 불린
					}
					//탐색 범위를 계속 해서 동기화 시켜 주어야한다.
					range_Site_Width_Left_Point = enemy_Point.x;
					//range_Site_Height_Top_Point = enemy_Point.y;
			}else { //적 영웅을 발견하게 되면 적 영웅을 추격한다.
				//range_Site_Height_Top_Point = enemy_Point.y-100;	//에서 
				//range_Site_Height_Bottom_Point = 150; //까지 600- 450  500 - 450 enemy_Point.y - 450
				//System.out.println(range_Site_Height_Bottom_Point);
				//탐색 범위를 계속 해서 동기화 시켜 주어야한다.
				//range_Site_Width_Left_Point = enemy_Point.x;
				//태세 변환시 초기화 한번
				if(lock_Trans){
					set_Range_Site_Width_Right_Point(300); //공격 태세일때 경계값 범위가 늘어난다.
					//set_Range_Site_Height(100); //동문
					lock_Trans = false;
				}
					//이동 속도 증가
					if(move_Site){	//좌측으로 이동
						if(hero.get_Hero_X_Point()+30 <= enemy_Point.x){ //좌측으로 캐릭터 보다 전진 하지 못하도록 = 캐릭터를 쫓아 가도록
						enemy_Point.x -= 10;
						}
					}else {			//우측으로 이동
						if(hero.get_Hero_X_Point()-30 >= enemy_Point.x){
						enemy_Point.x += 10;
						}
					}
					//좌우로 움직일 범위 설정 적군의 공격범위에서는 끝까지 따라오도록
				/*	if((left_Bound_Site) >= enemy_Point.x){
						move_Site = false;
					}else if((right_Bound_Site) <= enemy_Point.x){
						move_Site = true;
					}*/
			}
		}
}