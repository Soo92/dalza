package enemy;

import javax.swing.ImageIcon;

import character.Hero;

//��Ŀ Ŭ����
public class Bat extends Enemy{

   //lock_Trans = �¼� ��ȯ�� ���� �Ҹ� ����/��� ��
   private boolean lock_Trans;
   int a = 10;
   
   public Bat(int left_Site, int right_Site, int bottom_Site) {
      super(left_Site, right_Site, bottom_Site);
      Enemy_pic = tk.getImage("img/walker4.gif");
      Enemy_icon = new ImageIcon("img/walker4.gif");
      
      
      int a=60;
      width = a*Enemy_icon.getIconWidth()/Enemy_icon.getIconHeight();
      height = a;

      // TODO Auto-generated constructor stub
      //�� ���� Ž�� �簢�� ���� ����
      range_Site_Width_Right_Point = 250;
      range_Site_Height_Bottom_Point = 50;

      lock_Trans = true;
      //��Ŀ�� �⺻ ������ 50
      enemy_HP = 50;
   }
   //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@���� ���̵�
      //��� ���� �簢�� �� ����
      public int get_Range_Site_Width_Right_Point(){
         return range_Site_Width_Right_Point;
      }
      
      //��� ���� �簢�� �� ����
      public int get_Range_Site_Height(){
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
      
         //���Ͱ� ����� �i�� ���� ���������� Ž�� ������ ����ġ ��Ų��.
         public void init_Range_Site(){
            range_Site_Width_Right_Point = 250;
            range_Site_Height_Bottom_Point = 50;
            
            range_Site_Width_Left_Point = enemy_Point.x;
            range_Site_Height_Top_Point = enemy_Point.y;
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
                  public void set_Range_Site_Height_Top_Point(int range_Site_Top_Height){
                     this.range_Site_Height_Top_Point = range_Site_Top_Height;
                  }
      
      //��谪 �ѹ��� ���� ��Ű������ �÷���
      boolean plusOnce = true;
      //������ �߰�������
      public void set_Find_Hero(int hero_X_Point){
         find_Hero = true;
         if(hero_X_Point > enemy_Point.x){
            move_Site = false;
         }else {
            move_Site = true;
         }
      }
      //������ ��������
      public void set_Not_Find_Hero(){
         find_Hero = false;
      }
      //�ǰ� ������ �ڷ� �з��� ȿ��
      public void knockback(boolean flag){
         //flag �� true �̸� �������� ��ȯ
         if(flag){
            enemy_Point.x += 5;
         }else {
            enemy_Point.x -= 5;
         }
         //�˹���ġ���� �ٽñ� ��� �簢���� �׸���.
         range_Site_Width_Left_Point = enemy_Point.x;
      }
      
      public void enemy_Move(){ //���� ��� ������ �ٴ� ������ ���� ���� �����ͼ� ����Ѵ�.
         //�������� �߰����� �������� �⺻ ��� ������ ���ƴٴѴ�.
         
         if(enemy_Point.y>500) {
            a=-1;
            //System.out.println("a");
         }
         else if(enemy_Point.y<200) {
            a=+1;
            //System.out.println("b");
         }
         enemy_Point.y+=a;
         //System.out.println(enemy_Point.y);
         
         if(!find_Hero){
               if(move_Site){   //�������� �̵�
                  enemy_Point.x -= 2;
               }else {         //�������� �̵�
                  enemy_Point.x += 2;
               }
               //�¿�� ������ ���� ���� ������ ������ �߰� �������� ��� ������ ���� ��ü�� ��´�.
               if((left_Bound_Site - 10) >= enemy_Point.x){
                  move_Site = false;
                  
               }else if((right_Bound_Site - 20) <= enemy_Point.x){
                  move_Site = true;
               }
               if(!lock_Trans){
                  init_Range_Site(); //�� ���� ���� ���°� �ƴҶ� ���� ����
                  lock_Trans = true; //�¼� ��ȯ�� ���� �Ҹ�
               }
               //Ž�� ������ ��� �ؼ� ����ȭ ���� �־���Ѵ�.
               range_Site_Width_Left_Point = enemy_Point.x;
               range_Site_Height_Top_Point = enemy_Point.y;
         }else { //�� ������ �߰��ϰ� �Ǹ� �� ������ �߰��Ѵ�.
            range_Site_Height_Top_Point = enemy_Point.y-100;   //���� 
            range_Site_Height_Bottom_Point = 150; //���� 600- 450  500 - 450 enemy_Point.y - 450
            //System.out.println(range_Site_Height_Bottom_Point);
            //Ž�� ������ ��� �ؼ� ����ȭ ���� �־���Ѵ�.
            //range_Site_Width_Left_Point = enemy_Point.x;
            //�¼� ��ȯ�� �ʱ�ȭ �ѹ�
            if(lock_Trans){
               set_Range_Site_Width_Right_Point(300); //���� �¼��϶� ��谪 ������ �þ��.
               //set_Range_Site_Height(100); //����
               lock_Trans = false;
            }
            
               //�̵� �ӵ� ����
               if(move_Site){   //�������� �̵�
                  if(hero.get_Hero_X_Point()+30 <= enemy_Point.x){ //�������� ĳ���� ���� ���� ���� ���ϵ��� = ĳ���͸� �Ѿ� ������
                  enemy_Point.x -= 7;
                  }
               }else {         //�������� �̵�
                  if(hero.get_Hero_X_Point()-30 >= enemy_Point.x){
                  enemy_Point.x += 7;
                  }
               }
               //�¿�� ������ ���� ���� ������ ���ݹ��������� ������ ���������
               if((left_Bound_Site) >= enemy_Point.x){
                  move_Site = false;
               }else if((right_Bound_Site) <= enemy_Point.x){
                  move_Site = true;
               }
         }
      }
}