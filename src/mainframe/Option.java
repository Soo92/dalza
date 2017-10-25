package mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import character.Hero;
import enemy.Enemy;
import enemy.Walker;
import mapData.Block;
import mapData.Stage;
import weapon.Pistol;
import weapon.Weapon;

//���� ȭ�� ��������� Ŭ����
class Option extends JFrame implements KeyListener, Runnable, ActionListener{
	int framew=700, frameh= 700; 	//ȭ�� ũ��
	int buffx=0, buffy=0, buffw=700, buffh=700;
	int cnt=0, chatt=0;
	int stage_Num = 0; 	//���� ���� ��������
	String chat="";
	Toolkit tk = Toolkit.getDefaultToolkit(); 	//�̹����� �ҷ����� ���� ��Ŷ
	Image hero_Png;
	Image bullet_Png;
	Image walker_Png;
	Image buffImage;	//���� ���۸��� �̹���
	Graphics buffg;		
	Thread th; 			//������ ����
	Hero mainCh;		//����� ����
	Enemy enemy;	//�⺻ ���� ����
	Stage stage;	//���������� �����Ѵ�
	boolean end_Stage;	//���� ���������� �Ѿ� �� ���ΰ�.
	boolean clear_Stage;	//���� ���������� �Ѿ� �� ���ΰ�.
	boolean attack;	//�������ΰ� �������� �ƴѰ�
	int weapon_Number;	//���� ��ü �� �� ��ȣ
	boolean jump;	//������ ������ ���ΰ� ? true �̸� �����Ѵ�.
	
	TextArea ta;
	TextField tf;
	Panel pa;

	//���� �⺻������
	Weapon weapon; 
	Point pistol_Point;
	
	ArrayList bullet_List = new ArrayList<Weapon>(); 	//�ټ��� ����(��)�� ���� ��� ����Ʈ
	ArrayList enemy_List = new ArrayList<Enemy>();	//�ټ��� ������ ���� ��� ����Ʈ
	
	public Option() {
		init();
		setTitle("Shot");		//�������� �̸��� ����
		setSize(framew, frameh); //�������� ũ��
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();		//�������� �����쿡 ǥ�ñ��� ��ġ�� �����ϱ� ����.

		Panel pa = new Panel();   											//ä�� �ǳ�
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
		
		//�������� ����� ȭ�� ���߾ӿ� ��ġ ��Ű�� ���� ��ǥ ���� ���.
		int focus_X = (int)(screen.getWidth() / 2 - framew / 2);
		int focus_Y = (int)(screen.getHeight() / 2 - frameh / 2);
		
		setLocation(focus_X, focus_Y); //�������� ȭ�鿡 ��ġ
		setResizable(false);		   //�������� ũ�⸦ ���Ƿ� ������ϵ��� ����
		setVisible(true);			   //�������� ���� ���̰� ���	
	}

	@Override							// ä��â �Է� ++ Ű���� event comma ��ǲ ����
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
		//���α׷��� ���������� �����ϵ��� ����� �ݴϴ�.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//���ΰ��� �⺻ �̹��� ����
		hero_Png = tk.getImage("img/hero_1.png");
		bullet_Png = tk.getImage("img/Bullet_img.png");
		//���� �̹���
		walker_Png = tk.getImage("img/walker_img.png");
		//���ΰ� ����
		mainCh = new Hero();
		
		//�������� true �� �Ǹ� ���� ���������� �Ѿ
		end_Stage = true;
		
		addKeyListener(this); //Ű���� �̺�Ʈ ����
		th = new Thread(this); 	  //������ ����
		th.start(); 		  //������ ����
		
		//�������� 1 ����
		stage = new Stage();
		
		attack = false; //���� ���� ���� 
		weapon_Number = 1;//������ ����, �⺻ 1 �ǽ���
		jump = false; //���� ���¼���
	}
	
	public void paint(Graphics g){
		//�������۸� ���� ũ�⸦ ȭ�� ũ��� ���� ����
		buffImage = createImage(buffw,buffh);
		//������ �׷��� ��ü ���
		buffg = buffImage.getGraphics();
		update(g);
	}
	
	public void update(Graphics g){
		//�Ѿ��� �׸���.
		draw_Bullet();
		//������ �׷��� �׸��� �����´�.
		draw();
		//������ �׸���.
		draw_Enemy();
		//���������� �׸���.
		draw_Stage();
		//ȭ�鿡 ���ۿ� �׸� �׸��� ������ �׸���
		g.clearRect(buffx, buffy, buffw, buffh);
		if(mainCh.get_Hero_X_Point()<=framew/2) {                                 //ĳ���Ͱ� ȭ�� ���ʺ� ��ġ
			g.drawImage(buffImage, -10,buffh/2-mainCh.get_Hero_Y_Point(), this);
		}
		else if(mainCh.get_Hero_X_Point()>=buffw-framew/2) {						//ĳ���Ͱ� ȭ�� �����ʺ� ��ġ
			g.drawImage(buffImage, framew-buffw,buffh/2-mainCh.get_Hero_Y_Point(), this);
		}
		else {																		//������ ��Ȳ�϶�
			g.drawImage(buffImage, framew/2-mainCh.get_Hero_X_Point(),buffh/2-mainCh.get_Hero_Y_Point(), this);
		}
		if(clear_Stage) {
			g.drawString("CLEAR", framew/2, frameh/2);
		}
		if((mainCh.get_Hero_X_Point() < (buffx) || 
			mainCh.get_Hero_X_Point() > (buffx+buffw)) ||
			mainCh.get_Hero_Y_Point() > (buffy+buffh)*1.5){
				mainCh=new Hero();
		}
	}
	
	//������ �׸����� �׸� �κ�
	public void draw(){
		//�����ӿ� ����� png �̹����� �׷��ֽ��ϴ�.
		//buffg.drawImage(hero_Png, mainCh.get_Hero_X_Point(), mainCh.get_Hero_Y_Point(), this);
		buffg.drawImage(hero_Png, mainCh.get_Hero_X_Point(),   mainCh.get_Hero_Y_Point(), 30 ,45 ,this);
		buffg.drawRect(mainCh.get_Hero_X_Point(),   mainCh.get_Hero_Y_Point(), 30,  45); //�簢������ �ϴ� ��ü
		if((cnt-chatt)<=100&&chatt!=0) {
			if((cnt-chatt)<10)
			this.requestFocus();
			buffg.drawString(chat,mainCh.get_Hero_X_Point()+20, mainCh.get_Hero_Y_Point()-30);
		}
	}
	
	//�������� �� �� �׸� (����)
	public void draw_Stage(){
		//���� ���������� �Ѿ
		if(end_Stage){
			stage_Num++; //���� ���������� �Ѿ //�� ��ġ �ʱ�ȭ 1��������
			//���������ѹ��� �ѹ� �ݿ��ؼ� ���������� �����.
			this.setTitle("Stage"+stage_Num);
			mainCh = new Hero();
			stage.map_Stage(stage_Num);
			//�⺻ ���� ��Ŀ ����
			enemy_Process(stage_Num);
			end_Stage = false;
		}
				
		//������ ���������� ������ �׷����� 
		int temp = 0;
		for(int i=0; i<stage.get_Block().size(); i++){
			buffg.drawRect(stage.get_Block().get(i).get_Left_Top_Point().x,
					stage.get_Block().get(i).get_Left_Top_Point().y,
					stage.get_Block().get(i).get_Width(), 
					stage.get_Block().get(i).get_Height());
			//�浹 �Լ� ȣ�� 1�̸� ���� ĳ����
			crash_Decide_Block(mainCh, stage.get_Block().get(i));
			//buff�̹��� ũ������
			if(buffw<stage.get_Block().get(i).get_Left_Top_Point().x+stage.get_Block().get(i).get_Width())
				buffw=stage.get_Block().get(i).get_Left_Top_Point().x+stage.get_Block().get(i).get_Width()+10;
			if(buffx>stage.get_Block().get(i).get_Left_Top_Point().x)
				buffx=stage.get_Block().get(i).get_Left_Top_Point().x-10;
			if(buffh<stage.get_Block().get(i).get_Left_Top_Point().y+stage.get_Block().get(i).get_Height())
				buffh=stage.get_Block().get(i).get_Left_Top_Point().y+stage.get_Block().get(i).get_Height()+10;
			if(buffy>stage.get_Block().get(i).get_Left_Top_Point().y)
				buffy=stage.get_Block().get(i).get_Left_Top_Point().y-10;
			//ĳ���Ͱ� ��� ������ ��� ���� ������ �߶����̴�.
			if(!stage.get_Block().get(i).get_Set_Contect()){
				temp++;
			}
			//���� ���ڸ�ŭ false �̸� ������ ��� �������� 
			if(temp == stage.get_Block().size()){
				mainCh.auto_Jump_Down();
			}
		}
		for(int i=0; i<stage.get_Item().size(); i++){
			buffg.fillRect(stage.get_Item().get(i).get_Left_Top_Point().x,
					stage.get_Item().get(i).get_Left_Top_Point().y,
					stage.get_Item().get(i).get_Width(), 
					stage.get_Item().get(i).get_Height());
			//�浹 �Լ� ȣ�� 1�̸� ���� ĳ����
			crash_Decide_Item(mainCh, stage.get_Item().get(i));
			//buff�̹��� ũ������
			if(buffw<stage.get_Item().get(i).get_Left_Top_Point().x+stage.get_Item().get(i).get_Width())
				buffw=stage.get_Item().get(i).get_Left_Top_Point().x+stage.get_Item().get(i).get_Width()+10;
			if(buffx>stage.get_Item().get(i).get_Left_Top_Point().x)
				buffx=stage.get_Item().get(i).get_Left_Top_Point().x-10;
			if(buffh<stage.get_Item().get(i).get_Left_Top_Point().y+stage.get_Item().get(i).get_Height())
				buffh=stage.get_Item().get(i).get_Left_Top_Point().y+stage.get_Item().get(i).get_Height()+10;
			if(buffy>stage.get_Item().get(i).get_Left_Top_Point().y)
				buffy=stage.get_Item().get(i).get_Left_Top_Point().y-10;
			//ĳ���Ͱ� ��� ������ ��� ���� ������ �߶����̴�.
		}
	}
	
	public void draw_Bullet(){	//�Ѿ��� �׸��� �Լ�
		for(int i=0; i<bullet_List.size(); i++){		//�Ѿ� ���� ��ŭ �ݺ��ϸ� �׸���.
			weapon = (Weapon) bullet_List.get(i);
			if(weapon instanceof Pistol){ //�Ҹ� ����Ʈ�� ������ �ǽ���� ���� �����ϴٸ�.
				buffg.drawRect(weapon.getPoint().x,  weapon.getPoint().y, weapon.get_Weapon_Width(), weapon.get_Weapon_Height()); //�簢������ �ϴ� ��ü
				buffg.drawImage(bullet_Png,weapon.getPoint().x,  weapon.getPoint().y, weapon.get_Weapon_Width(), weapon.get_Weapon_Height(),this); //�簢������ �ϴ� ��ü
				((Pistol) weapon).pistol_Move( weapon.get_Bullet_Side_LEFT_RIGHT() );				//�ǽ��� �Ѿ� ������ �� ���⼺�� ������ ���ư���.
			}
			
			//�Ѿ˰� ������ �浹���� �Լ� ȣ�� 
			for(int j=0; j<enemy_List.size(); j++){
			enemy = (Enemy) enemy_List.get(j);
			crash_Decide_Enemy(weapon, enemy, enemy.get_Move_Site());
			}
			//�Ѿ� ���� �Լ� ȣ��
			remove_Bullet(weapon, i);
		}
	}
	
	//�浹 ���� �Լ�, �浹�� 2�� �ٿ����Ʈ�� ���Ѵ�. 1�� �� ū �簢�� 2���� ������ �簢���� �������Ѵ� �ϴ� 1�� �ٿ�� ������ �����ϵ��� �Ѵ�.
	//private int object_Width; //������ ������ �����ϱ� ���� ���� ����
	//private int object_Height; //������ ������ �����ϱ� ���� ���� ����
	
	private boolean jump_Up_Lock_Temp = false; //�ö󰡴� ���߿��� ������ ���� �Ұ���
	private int auto_Jump_Down_Head_Flag = 0; //�Ӹ� ���� ���� ����
	//�浹 üũ �ʰ� ���� ĳ���� ĳ���� x,y
	public void crash_Decide_Block(Hero hero, Block block){ //what_Object 1 �϶� ���� ĳ���� �浹
		//�ö󰡴����϶� ���� �Ұ�
		if(!mainCh.get_Jump_State()){
			jump_Up_Lock_Temp = true;
		}
			if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (block.get_Left_Top_Point().x ) || 
					hero.get_Hero_X_Point() > (block.get_Left_Top_Point().x+block.get_Width()) ||
					(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < block.get_Left_Top_Point().y ||
					hero.get_Hero_Y_Point() > (block.get_Left_Top_Point().y+block.get_Height())){
				//ĳ���Ͱ� ���� ���� ������ false ���� ��� ���� �������� ���������� 
				block.set_Contect_F();
			}else {
				//ĳ���Ͱ� ���� ������ true
				block.set_Contect_T();
				//ĳ���Ͱ� ������ �����ʰ� ���������� �߷� ���ӵ��� �ʱ�ȭ �Ѵ�.
				mainCh.set_dgSum_Zero();
				//ĳ������ �Ӹ��� ���� �ٴڿ� �������
				if(hero.get_Hero_Y_Point() >= block.get_Left_Top_Point().y + block.get_Height() - 20){
					System.out.println("�Ӹ��� �ٴ� �ε���");
					//ĳ���Ͱ� ���� �ε����� �ٷ� �Ʒ������� ������
					mainCh.set_Jump_Hero_UP_DOWN();
					//������ ���ϰ� �߶��Ҷ� �������� ���� �ʵ��� �ؾ��Ѵ�.
					//�� ���� ���̸� ���ؼ� ����.
					auto_Jump_Down_Head_Flag++;
					if(auto_Jump_Down_Head_Flag >= 2){
					mainCh.auto_Jump_Down_Head(block.get_Left_Top_Point().y + block.get_Height() - hero.get_Hero_Y_Point());
					}
				}else 
				//ĳ������ �ϴ��� ������ ����� �������� ���� ���� �˷��ش�.
				if(hero.get_Hero_Y_Point()+hero.get_Hero_Height()  <=  block.get_Left_Top_Point().y + 20){
					//System.out.println("���� ��� ����");
				//������ �߻��ϸ� �������� ������ �߻������� �������� ������ �߻��Ͽ����� ������ ���ش�.
				auto_Jump_Down_Head_Flag = 0;
				//ĳ���Ͱ� �����ߴٰ� �Ʒ��� �������� ���߿��� �� ���� �ö� ���� �ֵ��� ���� && ���Ͻÿ� ����� ���� �ֵ��� ����
				if(jump_Up_Lock_Temp){
					//������ ������� ������ ���� ��Ű������ �Լ�
					mainCh.set_Hero_Y_Point(block.get_Left_Top_Point().y-hero.get_Hero_Height());
					//ĳ���Ͱ� �ٽ� ��ġ �ؾ� �� �� �������� && ���� ���� �ƴҶ��� �ʱ�ȭ �Ҽ� �ֵ��� �ؾ��Ѵ�.
					mainCh.jump_Move_Stop(mainCh.get_Hero_Y_Point());
					jump_Up_Lock_Temp = false;
				}
					//���� ���������� �������ٰ� ��� ���� if ���� ������ ���� ���� �� ���´�.
				}//ĳ���� ������ ���� ������ �ھ�����
				else if(hero.get_Hero_X_Point() + hero.get_Hero_Width() <= block.get_Left_Top_Point().x + 25){
					System.out.println("���� ���� �� �ε���");
					//���������� ���� ���ϵ��� ���ƾ���
					mainCh.stop_Move_Right(hero.get_Hero_X_Point());
				}
				//ĳ���� ������ ���� ������ �ھ�����
				else if(hero.get_Hero_X_Point() >= block.get_Left_Top_Point().x + block.get_Width() - 25){
					System.out.println("���� ������ �� �ε���");
					//�������� ���� ���ϵ��� ���ƾ���
					mainCh.stop_Move_Leftt(hero.get_Hero_X_Point());
				}
				else {
					block.set_Contect_F();//ĳ���Ͱ� ���� ���� ������ false ���� ��� ���� �������� ���������� 
				}
			}
		}
	//������ ������ �浹 �˻� �������ٰ� �¹������Ѵ�.
	
	public void crash_Decide_Enemy_Block(Block block, Enemy enemy){
		//�������� �۵�
		if(enemy.get_Down_Start()){
			//xpoint = ������ x ��ǥ
			if((block.get_Left_Top_Point().x + block.get_Width()) <= (enemy.get_enemy_Point().x ) || 
					block.get_Left_Top_Point().x >= (enemy.get_enemy_Point().x+enemy.get_Enemy_Width()) ||
					(block.get_Left_Top_Point().y + block.get_Height()) <= enemy.get_enemy_Point().y ||
					block.get_Left_Top_Point().y-1 >= (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
			//enemy.get_Enemy_Exit_Yoint(1000);
		}else{
			System.out.println("���� ���� ��� ����");
			//��� ������ ������ ��ġ�� �����Ѵ�, ������ �������� ������Ų��.
			enemy.get_Enemy_Exit_Yoint(block.get_Left_Top_Point().y  - enemy.get_Enemy_Height() );
			enemy.init_Bound_Site(block.get_Left_Top_Point().x, (block.get_Width() + block.get_Left_Top_Point().x), block.get_Left_Top_Point().y - block.get_Height());
			
			//���� �˰����� ���� ���ž��Ѵ�.
			enemy.init_Range_Site(enemy.get_enemy_Point().x, enemy.get_enemy_Point().y);
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
		}
	}
	
	//�浹 üũ �Լ� ĳ���Ϳ� �Ѿ� �� 
	public void crash_Decide_Enemy(Hero hero, Enemy enemy, boolean get_Site){ //get_Site = Ž�������� �������� ��������
		//what_Object = 1 �̸� ĳ����, 2 �̸� �Ѿ�
				if(get_Site){ //������ Ž���Ҷ�. �簢���� ������ ĳ������ �������� �����ϱ� ������ �˹� ���ÿ� ĳ������ ���� ��ŭ ��� ������ x ���� ����(width)�� �����־�Ѵ�. 
					//System.out.println("���� �̵� ĳ���� ��ġ : " + enemy.get_enemy_Point().x + ", ĳ���� ���� �þ� : " + (enemy.get_enemy_Point().x - enemy.get_Range_Site_Width()));
					if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point()+hero.get_Hero_Width()) || 
							hero.get_Hero_X_Point() > (enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point()+hero.get_Hero_Width()+enemy.get_Range_Site_Width_Right_Point()) ||
							(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_range_Site_Height_Top_Point() ||
							hero.get_Hero_Y_Point() > (enemy.get_range_Site_Height_Top_Point()+enemy.get_range_Site_Height_Bottom_Point())){
						enemy.set_Not_Find_Hero(); //ĳ���͸� ã�� ��������.
					}else {
						//System.out.println("�浹 ����");
						enemy.set_Find_Hero(mainCh.get_Hero_X_Point()); //ĳ���͸� ã������
						//ĳ���Ϳ� ������ ���� �������. ��������
						if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_enemy_Point().x - enemy.get_Enemy_Width()) || 
								hero.get_Hero_X_Point() > (enemy.get_enemy_Point().x - enemy.get_Enemy_Width()+enemy.get_Enemy_Width()) ||
								(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_enemy_Point().y ||
								hero.get_Hero_Y_Point() > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
						}else{
							//�������� �˹�
							hero.left_Knock_Back();
						}
					}
				}else { //���� Ž���Ҷ� ��� 
					if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_Range_Site_Width_Left_Point() ) || 
							hero.get_Hero_X_Point() > (enemy.get_Range_Site_Width_Left_Point()+enemy.get_Range_Site_Width_Right_Point()) ||
							(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_range_Site_Height_Top_Point() ||
							hero.get_Hero_Y_Point() > (enemy.get_range_Site_Height_Top_Point()+enemy.get_range_Site_Height_Bottom_Point())){
						enemy.set_Not_Find_Hero(); //ĳ���͸� ã�� ��������.
					}else {
						//System.out.println("�浹 ����");
						enemy.set_Find_Hero(mainCh.get_Hero_X_Point()); //ĳ���͸� ã������
						//���� ��� ������ �������.
						if((hero.get_Hero_X_Point()+hero.get_Hero_Width()) < (enemy.get_enemy_Point().x ) || 
								hero.get_Hero_X_Point() > (enemy.get_enemy_Point().x + enemy.get_Enemy_Width()) ||
								(hero.get_Hero_Y_Point()+hero.get_Hero_Height()) < enemy.get_enemy_Point().y ||
								hero.get_Hero_Y_Point() > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
						}else {
							//�������� �˹�
							hero.right_Knock_Back();
						}
					}
				}
	}
	//�浹 üũ �Լ� ĳ���Ϳ� �Ѿ� �� 
	public void crash_Decide_Enemy(Weapon weapon, Enemy enemy, boolean get_Site){ //get_Site = Ž�������� �������� ��������
			if((weapon.getPoint().x+weapon.get_Weapon_Width()) < (enemy.get_enemy_Point().x ) || 
					weapon.getPoint().x > (enemy.get_enemy_Point().x+enemy.get_Enemy_Width()) ||
					(weapon.getPoint().y+weapon.get_Weapon_Height()) < enemy.get_enemy_Point().y ||
					weapon.getPoint().y > (enemy.get_enemy_Point().y+enemy.get_Enemy_Height())){
			}else {
				//System.out.println("�浹 ����");
				//�ǰݽ� ������ �������� ���ҽ�Ų��.
				enemy.enemy_HP_Down(weapon.get_Bullet_Power());
				System.out.println(weapon.get_Bullet_Power());
				System.out.println(enemy.get_Enemy_HP());
							//�ǰݵ� ���� �������� 0 �� �Ǹ� �����Ѵ�.
				if(enemy.get_Enemy_HP() == 0 || 
				(enemy.get_enemy_Point().x < (buffx-10) || 
				enemy.get_enemy_Point().x+enemy.get_Enemy_Width() > (buffx+buffw+10)) ||
				enemy.get_enemy_Point().y+enemy.get_Enemy_Height()>buffh-10){
					enemy_List.remove(enemy);
				}
				//�ǰݵ� ���͸� �ڽ��� �������� �޷������� �ؾ��Ѵ�. �� ���͸� ���� ���·� �����ؾ��Ѵ�.
				if(enemy.get_enemy_Point().x >= mainCh.get_Hero_X_Point()){
					enemy.set_Move_Site(true);
					//���� �˹�ȿ��
					enemy.knockback(true);
				}
				//�ǰݵ� ���͸� �ڽ��� �������� �޷������� �ؾ��Ѵ�. �� ���͸� ���� ���·� �����ؾ��Ѵ�.
				if(enemy.get_enemy_Point().x <= mainCh.get_Hero_X_Point()){
					enemy.set_Move_Site(false);
					//���� �˹�ȿ��
					enemy.knockback(false);
				}
				//�˹� �ϴٰ� ���Ǻ��� �Ÿ��� �Ѿ�ԵǸ� �߶� ����
				if(enemy.get_enemy_Point().x >= enemy.get_Right_Bound_Site() ||
						enemy.get_enemy_Point().x + 30 <= enemy.get_Left_Bound_Site()){ //�������� �������� �������� ��������
					enemy.set_Down_Start_True();
				}
				weapon.set_Remove_Bullet_Choice(); //�浹 �Ǹ� �Ѿ��� ���¸� ���� ���·�
		}
	}
	//������ �׸��� �Լ�
	public void draw_Enemy(){
		//������ ���� �ݺ��Ͽ� �׸���
		for(int i=0; i<enemy_List.size(); i++){
			enemy = (Enemy) enemy_List.get(i);
			if(enemy instanceof Walker){ //���ʹ��� ��Ŀ�� ��ü�� �ִٸ� �׷���
				buffg.drawImage(walker_Png, enemy.get_enemy_Point().x, enemy.get_enemy_Point().y, this);
				buffg.drawRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y, ((Walker) enemy).get_Enemy_Width(),  ((Walker) enemy).get_Enemy_Height()); //�簢������ �ϴ� ��ü
				buffg.setColor(Color.RED);
				buffg.fillRect(enemy.get_enemy_Point().x,  enemy.get_enemy_Point().y-10, enemy.get_Enemy_Width()*enemy.get_Enemy_HP()/10, 5); //�� ������ �� ��ġ, ũ��
				buffg.setColor(Color.BLACK);
			}
			//������ �����̴� �Լ� ȣ�� -> ��Ƽ������� ���� ������ ���ÿ� ���� Ŭ���� ������ �ڵ�����
			//enemy.enemy_Move();
			//ĳ���Ϳ� ���� �浹���� �Լ� ȣ��
			crash_Decide_Enemy(mainCh, enemy, enemy.get_Move_Site());
			//���ϰ� �����ϰ� �⵿���� ���� ���������������� �ϸ� �Ǳ� �Ѵ�.
			for(int j=0; j< stage.get_Block().size(); j++ ){
			crash_Decide_Enemy_Block(stage.get_Block().get(j), enemy);
			}
			//Ž�� ���� �׸���
			if(enemy.get_Move_Site()){ //�������� ����
					buffg.drawRect(enemy.get_Range_Site_Width_Left_Point() - enemy.get_Range_Site_Width_Right_Point(),  enemy.get_range_Site_Height_Top_Point(),
							enemy.get_Range_Site_Width_Right_Point(), enemy.get_range_Site_Height_Bottom_Point());	
			}else 
			if(!enemy.get_Move_Site()){ //�������� ����
					buffg.drawRect(enemy.get_Range_Site_Width_Left_Point(),  enemy.get_range_Site_Height_Top_Point(),
					enemy.get_Range_Site_Width_Right_Point(), enemy.get_range_Site_Height_Bottom_Point());
			}
			enemy.set_Hero_Information(mainCh);
		}
	}
	
	//���������� ���۵ɶ����� ���� ���ڸ� �Ķ���ͷ� �ް� �����ϰ� �ؾ� �� �� �ϴ�.
	public void enemy_Process(int stage_Num){
		if(stage_Num == 1){ //1�������� �϶� ���� ��ġ
			enemy_List.clear();
			enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
					stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
					stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
					stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
					stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
					stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
					stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			////////////////////////////////////////////////////////�� �ʿ� 1����������  ��Ŀ �κ� �߰�
		}
		if(stage_Num == 2){ //1�������� �϶� ���� ��ġ
			enemy_List.clear();
			enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
					stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
					stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
					stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
					stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
					stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
					stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			////////////////////////////////////////////////////////�� �ʿ� 1����������  ��Ŀ �κ� �߰�
		}

		if(stage_Num == 3){ //1�������� �϶� ���� ��ġ
			enemy_List.clear();
			enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
					stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
					stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
					stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
					stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
					stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
					stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			////////////////////////////////////////////////////////�� �ʿ� 1����������  ��Ŀ �κ� �߰�
		}
		
		if(stage_Num == 4){ //1�������� �϶� ���� ��ġ
			enemy_List.clear();
			enemy = new Walker(stage.get_Block().get(0).get_Left_Top_Point().x, 
					stage.get_Block().get(0).get_Left_Top_Point().x+stage.get_Block().get(0).get_Width(), 
					stage.get_Block().get(0).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(1).get_Left_Top_Point().x, 
					stage.get_Block().get(1).get_Left_Top_Point().x + stage.get_Block().get(1).get_Width(), 
					stage.get_Block().get(1).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
			enemy = new Walker(stage.get_Block().get(2).get_Left_Top_Point().x, 
					stage.get_Block().get(2).get_Left_Top_Point().x + stage.get_Block().get(2).get_Width(), 
					stage.get_Block().get(2).get_Left_Top_Point().y - 70); //20, width �� ��� ���� ����, height �� ���Ͱ� ��� �ִ� ������ ����
			enemy_List.add(enemy);
		////////////////////////////////////////////////////////�� �ʿ� 1����������  ��Ŀ �κ� �߰�
		}
	}
	
	//�Ѿ��� �߻����ΰ� �ƴѰ� �Ѿ��� �����ϴ� �Լ�
	public void bullet_Process(){
		if(attack){ //���϶� �Ѿ��� �����Ѵ�.
			if(weapon_Number == 1){ //�����϶�
				pistol_Point = new Point(mainCh.get_Hero_X_Point(),mainCh.get_Hero_Y_Point()); //ĳ������ ���ݸ� �˾ƿͼ� ��ġ ������ ����
				weapon = new Pistol(pistol_Point, mainCh.get_Face_Side_LFET_RIGHT()); //ĳ������ ��ǥ ������ ������ �����Ѵ�.
				bullet_List.add(weapon); //������ ������ �Ҹ� ����Ʈ�� �ִ´� ���� �����z Weapon Ŭ������ �Ǿ��ִ�.
			}
		}
	}
	
	//�Ѿ� ���� �Լ�
	public void remove_Bullet(Weapon weapon, int i){
		//x�࿡�� ȭ�� ������ �������� ����
		if(weapon.getPoint().x > buffw-30 || weapon.getPoint().x < 10){
			bullet_List.remove(i);
		}
		if(weapon.get_Remove_Bullet_Choice()){
			bullet_List.remove(i);
		}
	}
	
	//implement Runnable�� ���� ������ ������ 
	@Override
	public void run() {
		try{
			while(true){
				mainCh.move();//ĳ������ �������� �׻� üũ�Ѵ�.
				bullet_Process();//�Ѿ� ���� �Լ� ȣ��
				//���� ���� �żҵ�
				if(jump){
					mainCh.set_Hero_Jumping();
				}
				//ĳ���Ͱ� ����ִ� ������ ���̰� �Ǿ���Ѵ�.
				mainCh.jump_Move();
				repaint(); //ȭ���� ������ �ٽ� �׸���
				cnt++;
				Thread.sleep(20); //20milli sec �� ������ ������
			}
		}catch (Exception e) {
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	//Ű �ڵ鷯
	@Override
	//Ű���尡 ���������� �̺�Ʈ ó���ϴ� ��
	public void keyPressed(KeyEvent e) {

		switch(e.getKeyCode()){
		case KeyEvent.VK_UP :
			break;
		case KeyEvent.VK_DOWN :
			break;
		case KeyEvent.VK_LEFT :
			mainCh.set_Hero_X_Left();
			break;
		case KeyEvent.VK_RIGHT :
			mainCh.set_Hero_X_Right();
			break;
		case KeyEvent.VK_A :
			//�������϶� �Ѿ��� �����Ѵ�.
			attack = true;
			break;
		case KeyEvent.VK_S :
			//���� �����Ѵ�.
			jump = true;
			break;
		}
	}

	@Override
	//Ű���尡 �������ٰ� ���������� �̺�Ʈ ó���ϴ� ��
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP :
			break;
		case KeyEvent.VK_DOWN :
			break;
		case KeyEvent.VK_LEFT :
			mainCh.set_Hero_X_Left_Stop();
			break;
		case KeyEvent.VK_RIGHT :
			mainCh.set_Hero_X_Right_Stop();
			break;
		case KeyEvent.VK_A :
			//�Ѿ˻����� �����Ѵ�..
			attack = false;
			break;
		case KeyEvent.VK_S :
			//���� ����
			jump = false;
			break;
		case KeyEvent.VK_COMMA :
			//���� ����
			ta.setVisible(!ta.isVisible());
			this.revalidate();
			this.repaint();
			break;
		case KeyEvent.VK_ENTER:
			tf.setFocusable(true);
			tf.requestFocus();
			break;
		}
	}

	@Override
	//Ű���尡 Ÿ���� �� �� �̺�Ʈ ó���ϴ� ��
	public void keyTyped(KeyEvent e) {
	}
	///////////////////////////////////////////////////////////////////////////////////////////
}