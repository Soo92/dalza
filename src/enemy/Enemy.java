
package enemy;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.RepaintManager;

import character.Hero;

//���� �⺻ Ŭ����
public class Enemy extends Thread{ //�����带 ��ӹ޾Ƽ� ������ ���� �˾Ƽ� �����̵��� �Ѵ�.
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//�̹����� �ҷ����� ���� ��Ŷ

	//������ �⺻ ��ǥ����
	protected Point enemy_Point;
	
	//������ ���� �¿� ����
	protected boolean move_Site; //true �̸� ���� false�̸� ����
	protected double randomValue; //���� �� ����
	
	//��� ���� ����
	protected int left_Bound_Site;
	protected int right_Bound_Site;
	protected int bottom_Bound_Site;
	
	
	//���� �߰� ���������� false, �߰��������� true
	protected boolean find_Hero;
	
	//�� ���� Ž�� �簢�� ���� �� ����
	protected int range_Site_Width_Left_Point;
	protected int range_Site_Height_Top_Point;
	
	
	//�� ���� Ž�� �簢�� ������ �� �Ʒ��� ����
	protected int range_Site_Width_Right_Point;
	protected int range_Site_Height_Bottom_Point;
	
	//������ ���� �β� �� ���εβ�
	protected int width;
	protected int height;
	
	//������ ������
	protected int enemy_HP;
	protected int boss_enemy_HP;
	protected int enemy_HPinit;
	protected int boss_enemy_HPinit;
	
	//���� �߶� ���� true �϶�
	protected boolean down_Start;
	
	//�߷� ���ӵ�
	private int g = 1;
	private int gSum;
	
	boolean resumed;
	
	//������ ����
	Thread th;
	
	protected Image Enemy_pic;
	protected ImageIcon Enemy_icon;

	//������ ������� ��ġ ������ ������ �˾ƾ� �Ѵ�.
	protected Hero hero;
	public void set_Hero_Information(Hero hero){
		this.hero = hero;
	}
	
	public Enemy(int left_Bound_Site, int right_Bound_Site, int bottom_Bound_Site) { //������ �� ��ǥ�� �޾ƿ;���
		
		Enemy_pic = tk.getImage("img/walker1.gif");
		Enemy_icon = new ImageIcon("img/walker1.gif");

		//��Ŀ �ѷ�
		width = Enemy_icon.getIconWidth();
		height = Enemy_icon.getIconHeight();
		
		int a = 80;
		width = a*Enemy_icon.getIconWidth()/Enemy_icon.getIconHeight();
		height = a;
		
		//��豸�� ���� �ʱ� ����
		init_Bound_Site(left_Bound_Site, right_Bound_Site, bottom_Bound_Site);
		
		//�� ���� Ž�� ���� �Ʒ��� �簢�� ���� ���� 
		range_Site_Width_Right_Point = 300;
		range_Site_Height_Bottom_Point = 50;
		
		
		//down_Start �� true �̰� �Ǹ� �߶� ����
		down_Start = false;
		
		
		//�ϴ� �⺻������ 100,600 �� ����
		enemy_Point = new Point(right_Bound_Site, bottom_Bound_Site);
		
		
		//�� ���� Ž�� �簢�� ���� ���� ��ġ�� ����
		range_Site_Width_Left_Point = enemy_Point.x;
		range_Site_Height_Top_Point = enemy_Point.y;
		
		
		randomValue = Math.random();
		if(randomValue < 0.5){
			move_Site = true;	//�������� �̵�
			
		}else {
			move_Site = false; //�������� �̵�
			
		}
		
		find_Hero = false; //�������� �߰������� true �� ����
		
		th = new Thread(this); 	  //������ ����
		th.start(); 		  //������ ����
	}
	
	//���Ͱ� ����� �i�� ���� ���������� Ž�� ������ ����ġ ��Ų��, ���� ���� ������ ������� �ʱ�ȭ
	public void init_Range_Site(int enemy_Point_X, int enemy_Point_Y){
		//range_Site_Width_Right_Point = 300;
		//range_Site_Height_Bottom_Point = 50;
		
		range_Site_Width_Left_Point = enemy_Point_X;
		range_Site_Height_Top_Point = enemy_Point_Y - gSum;
	}
	
	
	
	public void init_Bound_Site(int left_Bound_Site, int right_Bound_Site, int bottom_Bound_Site){ 
		//���Ͱ� ƨ�� �����ų� ���ΰ� ĳ���� �Ѵٰ� �ٸ� �ٴ����� ������ �ѹ��� �ʱ�ȭ �ؾ��Ѵ�.
		this.left_Bound_Site = left_Bound_Site;
		this.right_Bound_Site = right_Bound_Site;
		this.bottom_Bound_Site = bottom_Bound_Site;
	}
	
	//������ �߰�������
	public void set_Find_Hero(int hero_X_Point){
		find_Hero = true;
	}
	//������ ��������
	public void set_Not_Find_Hero(){
		find_Hero = false;
	}
	
	public void set_Enemy_Image(String img) {
		Enemy_pic = tk.getImage(img);
		Enemy_icon = new ImageIcon(img);
	}
	
	//�⺻ ������ �߰� �������� �¿�� ��� ���� �����̰� �־���� //synchronized �ش� �Լ��� �۵��ϴ� ���� ����ȭ�� �����Ѵ�.
	public void enemy_Move(){ //���� ��� ������ �ٴ� ������ ���� ���� �����ͼ� ����Ѵ�.

		//�������� �߰����� �������� �⺻ ��� ������ ���ƴٴѴ�.
		if(!find_Hero){
				if(move_Site){	//�������� �̵�
					enemy_Point.x -= 5;
				}else {			//�������� �̵�
					enemy_Point.x += 5;
				}
				
				//�¿�� ������ ���� ����
				if((left_Bound_Site) >= enemy_Point.x){
					move_Site = false;
					
				}else if((right_Bound_Site) <= enemy_Point.x){
					move_Site = true;
				}
				
		}else { //�� ������ �߰��ϰ� �Ǹ� �� ������ �߰��Ѵ�.
			
		}
	}
	
	//��� ���� �簢�� �� ����
	public int get_Range_Site_Width_Right_Point(){
		return range_Site_Width_Right_Point;
	}
	
	//��� ���� �簢�� �� ����
	public int get_range_Site_Height_Bottom_Point(){
		return range_Site_Height_Bottom_Point;
	}
	
	//��� ���� �簢�� �� �� ������Ų��. ������ �߰������� ������ ������Ų��.
	public void set_Range_Site_Width_Right_Point(int range_Site_Width_Right_Point){
		this.range_Site_Width_Right_Point = range_Site_Width_Right_Point;
	}
	
	//��� ���� �簢�� �� �� ������Ų��. ������ �߰������� ������ ������Ų��.
	public void set_Range_Site_Height_Bottom_Point(int range_Site_Height_Bottom_Point){
		this.range_Site_Height_Bottom_Point = range_Site_Height_Bottom_Point;
	}
	
			
	//��� ���� �簢�� ���� ��� �� ��ȯ
	public int get_Range_Site_Width_Left_Point(){
		return range_Site_Width_Left_Point;
	}
	
	//��� ���� �簢�� �� ����
	public int get_range_Site_Height_Top_Point(){
		return range_Site_Height_Top_Point;
	}
	
	//��� ���� �簢�� �� �� ������Ų��. ������ �߰������� ������ ������Ų��.
	public void set_Range_Site_Width_Left_Point(int range_Site_Width_Left_Point){
		this.range_Site_Width_Left_Point = range_Site_Width_Left_Point;
	}
	
	//��� ���� �簢�� �� �� ������Ų��. ������ �߰������� ������ ������Ų��.
	public void set_Range_Site_Height_Up_Point(int range_Site_Top_Height){
		this.range_Site_Height_Top_Point = range_Site_Top_Height;
	}
	
	public Image get_Enemy_Img(){
		return Enemy_pic; 
	}

	//���� ������ ���� ������
	public int get_Left_Bound_Site(){
		return left_Bound_Site;
	}
	//������
	public int get_Right_Bound_Site(){
		return right_Bound_Site;
	}
		
	//���� ���� ���� ��ȯ
	public int get_Enemy_Width(){
		return width;
	}
	
	public int get_Enemy_Height(){
		return height;
	}
	
	//���� ���� �ִ� ���� ���� 
	public boolean get_Move_Site(){ //true = ����
		return move_Site;
	}
	
	//����� ��������ġ�� �����Ѵ�.
	public Point get_enemy_Point(){
		return enemy_Point;
	}
	
	//���� ������ ������
	public void enemy_stop(boolean a) {
		this.resumed = a;
	}

	public void run() {
		try{
			while(true){
				if (!resumed) {
					enemy_Move();
				}
				//���� ���ǿ��� �з��� ������ �������°�
				if(down_Start){
					enemy_Down_Algorithm();
				}
				Thread.sleep(20); //20milli sec �� ������ ������
			}
		}catch (Exception e) {
		}
	}
	
	//���� �߶� �˰�����
	public void enemy_Down_Algorithm(){
		 //���� ���� ����
		 if(gSum >= 15){
			 gSum = 15;
		 }
		 //�������鼭 Ȯ���� �ؾ��Ѵ�.
		 gSum += g;
		 enemy_Point.y += gSum;
		 //�ٸ� ���ǿ� ������� �߶��� �����ؾ� �ϸ� �ٽñ� �����ġ�� �����ؾ��Ѵ�.
		 if(End_Y_Point < enemy_Point.y){ //�̶� ����
			 //������ �ٴ��� �´�� ������ ������ y ��ǥ�� �����Ѵ�. [���� �����ϸ� �������� ������Ų��.]
			 enemy_Point.y = End_Y_Point;
			 //�߶��� �����ϰ� �߷� ���ӵ��� 0 ���� �ʱ�ȭ
			 set_Down_Start_False();
			 //���� �簢�� ���� �����;���
			 //3�� ���� �Է� ������ ��ġ, ������ġ, y ����Ʈ
			 //init_Bound_Site();
		 }
	}
	
	int End_Y_Point = 1000;
	//������ ���Ӱ� ���� y ����Ʈ �޾�
	public void get_Enemy_Exit_Yoint(int End_Y_Point){
		this.End_Y_Point = End_Y_Point;
	}
	
	//������ ���ǿ��� ����� �߶�����
	public void set_Down_Start_True(){
		down_Start = true;
	}
	public void set_Down_Start_False(){
		//���⼭ ������ ������ �����ͼ� �ѷ�����
		End_Y_Point = 1000; //�ʱ�ȭ
		gSum = 0;
		down_Start = false;
	}
	public boolean get_Down_Start(){
		return down_Start;
	}
	
	//�������� ���� ���� ��ȯ
	public void set_Move_Site(boolean flag){
		//flag �� ture �̸� �������� ��ȯ
		if(flag){
			move_Site = true;
		}else {
			move_Site = false;
		}
	}
	
	//�ǰ� ������ �ڷ� �з��� ȿ��
			public void knockback(boolean flag){
				//flag �� ture �̸� �������� ��ȯ
				if(flag){
					enemy_Point.x += 15;
				}else {
					enemy_Point.x -= 15;
				}
			}
	
	//������ �ǰ� �پ��
	public void enemy_HP_Down(int gun_Damage){
		enemy_HP-=gun_Damage;
		//System.out.println(enemy_HP);
	}
	
	//���� ������ üũ�ϱ�
	public int get_Enemy_HP(){
		return enemy_HP;
	}
	
	//���� ������ üũ�ϱ�
	public void set_Enemy_Y(int x){
		enemy_Point.y=x;
	}

	//���� HP üũ
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