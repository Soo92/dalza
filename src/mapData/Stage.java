package mapData;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Stage {
   Toolkit tk = Toolkit.getDefaultToolkit();    //이미지를 불러오기 위한 툴킷

   //배열리스트에 블록에 대한 정보를 담는다.
   ArrayList block_Array, item_Array;

   Block temp_Block, temp_Item; 
   
   //블록의 좌상단 포인트를 반환하기 위한 임시 변수
   Point temp_Block_Left_Top_Point, temp_Item_Left_Top_Point;
   //블록의 넓이, 높이 임시 변수
   int width, height;
   String effect;
   Graphics buffg;
   int buffx=0, buffy=0, buffw=50, buffh=50;
   
   protected Image Background;
   protected Image Block_pic;
   protected ImageIcon Block_icon;
   
   //스테이지마다 블록의 위치를 지정한다.
   public Stage() {
   block_Array = new ArrayList<Block>();
   temp_Block_Left_Top_Point = new Point(0, 0); //초기화
   //temp_Block = new Block(temp_Block_Left_Top_Point, 0, 0); //블록 어레이이 넣을 블록
   item_Array = new ArrayList<Block>();
   temp_Item_Left_Top_Point = new Point(0, 0); //초기화
   }
   
   //1스테이지
   public void map_Stage(int stage_Num){
      //스테이지 설정
            switch(stage_Num){
               //1스테이지 일때
             case 1:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                stage_Num_1();
            break;
             case 2:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                stage_Num_2();
            break;
             case 3:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                stage_Num_3();
            break;
             case 4:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                stage_Num_4();
            break;
             case 5:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                stage_Num_5();
            break;
             case 6:
                 Background = tk.getImage("img/stage"+stage_Num+".gif");
                 block_Array.clear();
                 item_Array.clear();
                 stage_Num_6();
             break;
             case 7:
                 Background = tk.getImage("img/stage"+stage_Num+".gif");
                 block_Array.clear();
                 item_Array.clear();
                 stage_Num_7();
             break;
            }
   }

   
   public void stage_Num_1(){
	      // x,y,w,h
	      
	      make_block(0,1100,1700,200,"block1");            //b1
	         make_block(0,1300,5350,50,"block1");            // b22 제일 바닥 발판
	         make_block(400,980,150,50,"block1");            //b2
	         make_block(700,880,400,50,"block1");            //b3
	         make_block(1200,810,100,50,"block1");            //b4
	         make_block(1450,770,50,50,"block1");            //b5
	         make_block(1650,750,50,50,"block1");            //b6
	         make_block(1800,630,250,50,"block1");            //b7
	         
	         make_block(1800,940,100,50,"block1");            //b13
	         make_block(2000,940,100,50,"block1");            //b14
	         make_block(2150,940,100,50,"block1");            //b15
	         make_block(2350,940,100,50,"block1");            //b16
	         make_block(2550,940,100,50,"block1");            //b17
	         
	         make_block(2950,940,100,50,"block1");            //b19
	         make_block(3350,940,100,50,"block1");            //b20
	         make_block(3150,1240,100,50,"block1");            //b20
	         make_block(3250,1190,100,50,"block1");            //b20
	         make_block(3350,1140,100,50,"block1");            //b20
	         
	         make_block(3450,1100,800,200,"block1");            //b23
	         make_block(4350,1040,100,50,"block1");            //b24
	         make_block(4550,1100,800,200,"block1");            //b25
	         make_block(3450,720,800,50,"block1");            //b26
	         make_block(3450,70,50,700,"block1");            //b27
	         make_block(3450,20,800,50,"block1");            //b28
	         make_block(3500,200,400,50,"block1");            //b29
	         make_block(3850,300,200,50,"block1");            //b30
	         make_block(3950,420,200,50,"block1");            //b31
	         make_block(4250,20,700,150,"block1");            //b32
	         make_block(4250,570,700,200,"block1");            //b33
	         make_block(4950,20,400,250,"block1");            //b34
	         make_block(4950,620,400,150,"block1");            //b35
	         make_block(5300,270,50,350,"block1");            //b35
	             
	         make_block(750,680,50,50,"randombox");
	         make_block(820,680,50,50,"randombox");
	         make_block(890,680,50,50,"randombox");
	         make_block(960,680,50,50,"randombox");
	         
	         make_block(3520,500,50,50,"randombox");
	         make_block(3605,500,50,50,"randombox");
	         make_block(3690,500,50,50,"randombox");
	         make_block(3775,500,50,50,"randombox");
	         make_block(3860,500,50,50,"randombox");
	         
	         
	         make_item(1700,1150,1750,150,"Beer"); // water         
	         make_item(800,1070,50,30,"thorns");
	         make_item(850,1070,50,30,"thorns");
	         make_item(900,1070,50,30,"thorns");
	         make_item(950,1070,50,30,"thorns");
	         make_item(1000,1070,50,30,"thorns");
	         make_item(1050,1070,50,30,"thorns");                           
	         make_item(1100,1070,50,30,"thorns");
	         make_item(2580,910,50,30,"thorns");
	         
	         
	         
	         
	         
	         
	         make_item(1950,530,100,100,"S1_portal1");
	         make_item(5200,520,100,100,"S1_portal2");      //i28
	        //make_item(0,1000,50,60,"S1_portal3");      //i28
	         make_item(5250,1000,100,100, "next");   //next stage 
	      
	      }
	   
	   public void stage_Num_2(){
	       make_block(0,1100,4700,50,"block1");            //b38
	         make_block(600,400,350,50,"block1");            //b39
	         make_block(550,650,400,50,"block1");            //b40
	         make_block(550,950,400,50,"block1");            //b41
	         make_block(0,800,400,50,"block1");            //b41
	         make_block(0,550,400,50,"block1");            //b41
	         
	         
	         make_block(950,600,300,500,"block1");            //b42
	         make_block(1250,900,500,200,"block1");            //b43
	         make_block(1750,1000,450,100,"block1");            //b44
	         make_block(1750,450,450,100,"block1");            //b45
	         make_block(2200,450,800,200,"block1");            //b46
	         make_block(3000,450,800,300,"block1");            //b47
	         make_block(2100,300,300,50,"block1");            //b48
	         make_block(2500,250,300,50,"block1");            //b50
	         make_block(3000,300,300,50,"block1");            //b49
	         make_block(3900,500,800,50,"block1");            //b53
	         make_block(3250,650,50,450,"block1");            //b54
	         make_block(4700,350,200,800,"block1");            //b54
	         make_block(3950,1000,50,50,"block1");            //b54
	         
	         make_block(2450,850,50,50,"randombox");            //b54
	         make_block(2500,850,50,50,"randombox");            //b54
	         make_block(2550,850,50,50,"randombox");            //b54
	         make_block(2600,850,50,50,"randombox");            //b54
	         make_block(2650,850,50,50,"randombox");            //b54
	         make_block(2700,850,50,50,"randombox");            //b54
	         make_block(2750,850,50,50,"randombox");            //b54
	         make_block(2800,850,50,50,"randombox");            //b54
	         
	         make_block(3500,850,50,50,"randombox");            //b54
	         make_block(3550,850,50,50,"randombox");            //b54
	         make_block(3600,850,50,50,"randombox");            //b54
	         make_block(3650,850,50,50,"randombox");            //b54
	         make_block(3700,850,50,50,"randombox");            //b54
	         make_block(3750,850,50,50,"randombox");            //b54
	         make_block(3800,850,50,50,"randombox");            //b54
	         make_block(3850,850,50,50,"randombox");            //b54
	         
	         
	         
	         make_item(2250,420,50,30,"thorns");            //i68
	         make_item(2300,420,50,30,"thorns");            //i68
	         make_item(2350,420,50,30,"thorns");            //i68
	         make_item(2400,420,50,30,"thorns");            //i68
	         make_item(2450,420,50,30,"thorns");            //i68
	         make_item(2500,420,50,30,"thorns");            //i68
	         make_item(2550,420,50,30,"thorns");            //i68
	         make_item(2600,420,50,30,"thorns");            //i68
	         make_item(2650,420,50,30,"thorns");            //i68
	         make_item(2700,420,50,30,"thorns");            //i68
	         make_item(2750,420,50,30,"thorns");            //i68
	         make_item(2800,420,50,30,"thorns");            //i68
	         make_item(2850,420,50,30,"thorns");            //i68
	         make_item(2900,420,50,30,"thorns");            //i68
	         make_item(2950,420,50,30,"thorns");            //i68
	         make_item(3000,420,50,30,"thorns");            //i68
	         make_item(3050,420,50,30,"thorns");            //i68
	         make_item(3100,420,50,30,"thorns");            //i68
	         
	         
	         make_item(850,1000,100,100,"S2_portal1");            //i68
	         make_item(850,300,100,100,"S2_portal2");            //i68
	         make_item(3150,1000,100,100,"S2_portal3");            //i68
	         make_item(4600,1000,100,100,"S2_portal4");            //i68
	         make_item(1650,350,100,100,"S2_portal5");            //i68
	        //make_item(0,1000,100,100,"S2_portal6");            //i68         
	         
	         make_item(4600,400,100,100, "next");   //next stage
	   }
	   public void stage_Num_3(){
	         make_block(0,1200,500,300,"block1");            //b1
	         make_block(500,1300,4500,200,"block1");         //b2
	         make_block(650,1100,150,50,"block1");         //b3
	         make_block(950,1050,150,50,"block1");         //b4
	         make_block(1250,1000,150,50,"block1");            //b5
	         make_block(1550,950,300,50,"block1");         //b6
	         make_block(2000,1000,250,50,"block1");            //b7
	         make_block(2250,450,50,850,"block1");            //b8
	         make_block(2300,450,2700,50,"block1");            //b9
	         make_block(2850,1000,50,50,"block1");            //b9
	         make_block(4950,450,50,850,"block1");         //b10
	         
	         make_block(2300,1100,400,50,"block1");         //b12
	         make_block(2800,1100,300,50,"block1");         //b13
	         make_block(3150,1100,300,50,"block1");         //b14
	         make_block(3600,1200,300,50,"block1");         //b15
	         make_block(3900,1100,400,50,"block1");         //b16
	         make_block(4400,1000,550,50,"block1");         //b17
	         
	         make_block(4700,950,50,50,"block1");         //b17
	         make_block(4500,850,50,50,"block1");         //b17
	         
	         make_block(4300,800,50,50,"block1");         //b17
	         make_block(4200,950,50,50,"block1");         //b17
	         make_block(4100,900,50,50,"block1");         //b17
	         make_block(4000,950,50,50,"block1");         //b17
	         make_block(3900,900,50,50,"block1");         //b17
	         make_block(3800,850,50,50,"block1");         //b17
	         make_block(3700,800,50,50,"block1");         //b17
	         make_block(3600,750,50,50,"block1");         //b17     
	         make_block(3500,800,50,50,"block1");         //b17
	         make_block(3400,750,50,50,"block1");         //b17
	         make_block(3300,800,50,50,"block1");         //b17
	         make_block(3200,850,50,50,"block1");         //b17
	         make_block(3100,800,50,50,"block1");         //b17
	         make_block(3000,750,50,50,"block1");         //b17
	         make_block(2900,700,50,50,"block1");         //b17
	         make_block(2800,650,50,50,"block1");         //b17
	         make_block(2700,600,50,50,"block1");         //b17  
	         
	         
	         make_block(1600,1100,50,50,"randombox");            //b54
	         make_block(1650,1100,50,50,"randombox");            //b54
	         make_block(1700,1100,50,50,"randombox");            //b54
	         make_block(1750,1100,50,50,"randombox");            //b54
	         make_block(1800,1100,50,50,"randombox");            //b54
	         make_block(1850,1100,50,50,"randombox");            //b54
	         make_block(2350,900,50,50,"randombox");            //b54
	         make_block(2400,900,50,50,"randombox");            //b54
	         make_block(2450,900,50,50,"randombox");            //b54
	         make_block(2500,900,50,50,"randombox");            //b54
	         make_block(2550,900,50,50,"randombox");            //b54
	         make_block(2600,900,50,50,"randombox");            //b54
	         make_block(3900,600,50,50,"randombox");            //b54
	         make_block(3950,600,50,50,"randombox");            //b54
	         make_block(4000,600,50,50,"randombox");            //b54
	         make_block(4050,600,50,50,"randombox");            //b54
	         
	         
	         
	         
	         make_item(1100,1270,50,30, "thorns");      //i22
	         make_item(1150,1270,50,30, "thorns");      //i22
	         make_item(1200,1270,50,30, "thorns");      //i22
	         make_item(1250,1270,50,30, "thorns");      //i22
	         make_item(1300,1270,50,30, "thorns");      //i22
	         make_item(1350,1270,50,30, "thorns");      //i22
	         make_item(1400,1270,50,30, "thorns");      //i22
	         make_item(1450,1270,50,30, "thorns");      //i22
	         make_item(1500,1270,50,30, "thorns");      //i22
	         
	         
	         make_item(2150,900,100,100, "S3_portal1");      //i22
	         make_item(2300,1200,100,100, "S3_portal2");      //i22 
	          
	        make_item(2700,500,100,100, "next");
	   }
	   public void stage_Num_4(){
	      
	        make_block(0,750,400,50,"block1");         //b18
	        make_block(0,1250,5050,50,"block1");         //b18
	         make_block(0,1200,400,50,"block1");         //b19
	         make_block(400,1100,400,50,"block1");         //b19
	         make_block(800,1000,400,50,"block1");         //b19
	         make_block(650,850,1200,50,"block1");         //b20
	         make_block(1700,1150,200,50,"block1");         //b21
	         make_block(1400,1100,200,50,"block1");         //b22
	         make_block(1700,1000,400,50,"block1");         //b23
	         make_block(2100,900,300,50,"block1");         //b24
	         make_block(2400,800,500,500,"block1");         //b24
	         
	         make_block(3100,800,500,500,"block1");         //b24
	         make_block(3750,750,100,50,"block1");         //b24
	         make_block(3950,700,200,50,"block1");         //b24
	         make_block(4250,650,200,50,"block1");         //b24
	         make_block(3800,1150,50,50,"block1");         //b24
	         make_block(3900,1150,50,50,"block1");         //b24
	         make_block(4000,1150,50,50,"block1");         //b24
	         make_block(4100,1100,50,50,"block1");         //b24
	         make_block(4200,1050,50,50,"block1");         //b24
	         make_block(4300,1000,50,50,"block1");         //b24
	         make_block(4400,950,50,50,"block1");         //b24
	         make_block(4500,900,50,50,"block1");         //b24
	         make_block(4600,850,50,50,"block1");         //b24
	         make_block(4700,800,50,50,"block1");         //b24
	         make_block(4800,750,50,50,"block1");         //b24
	         make_block(4900,700,50,50,"block1");         //b24
	         make_block(5000,650,50,50,"block1");         //b24
	      
	                 
	      make_item(4950,550,100,100, "next");
	   }
	   public void stage_Num_5(){
	      make_block(0,1000,5500,50,"block5");         //b24
	      make_block(0,1000,5000,50,"block5");         //b24
	      make_block(3300,900,50,50,"block5");         //b24
	      make_block(3450,800,50,50,"block5");         //b24
	      make_block(3600,700,50,50,"block5");         //b24
	      make_block(3750,600,400,50,"block5");         //b24
	      make_block(4250,500,50,50,"block5");         //b24
	      make_block(4350,400,50,50,"block5");         //b24
	      make_block(4200,250,50,50,"block5");         //b24
	      make_block(4450,300,50,50,"block5");         //b24
	      make_block(4600,200,50,50,"block5");         //b24
	      make_block(4000,200,50,50,"block5");         //b24
	      make_block(3350,100,500,50,"block5");         //b24
	      make_block(4750,100,500,50,"block5");         //b24
	      
	      
	      make_item(3750,300,400,300,"store");
	      make_item(1500,720,100,280,"NPC_1");
	      make_item(1800,720,133,280,"NPC_2");
	      make_item(2100,720,144,280,"NPC_3");
	      make_item(2400,720,131,280,"NPC_4");
	      make_item(2700,720,100,280,"NPC_5");
	      
	      make_item(3350,0,100,100, "S5_portal1");
	      make_item(5150,0,100,100, "next");
	   }
	   public void stage_Num_6(){
	      make_block(0,670,500,50,"block6");            //1
	      make_block(500,950,300,50,"block6");            //1
	      make_block(750,670,500,50,"block6");
	      make_block(0,1100,5000,50,"block6");         
	      make_block(1350,1050,400,50,"block6");            
	      make_block(1800,900,50,50,"block6");            
	      make_block(2000,850,50,50,"block6");            
	      make_block(2200,800,50,50,"block6");            
	      make_block(2400,700,50,50,"block6");            
	      make_block(2600,600,50,50,"block6");            
	      make_block(2800,500,50,50,"block6");            
	      make_block(3000,400,50,50,"block6");            
	      make_block(3150,300,50,50,"block6");      
	            
	      make_block(3400,1000,50,50,"block6");      
	      make_block(3600,850,50,50,"block6");      
	      make_block(3800,700,50,50,"block6");      
	      make_block(3600,550,50,50,"block6");      
	      make_block(3800,400,50,50,"block6");      
	      make_block(4700,1000,50,50,"block6");      
	      make_block(4500,850,50,50,"block6");      
	      make_block(4400,700,50,50,"block6");      
	      make_block(4500,550,50,50,"block6");      
	      make_block(4400,400,50,50,"block6");      
	      make_item(4400,300,100,100,"finish");      
	            
	      
	      make_block(3200,200,100,900,"block6");
	      make_block(5000,200,100,950,"block6");
	   }
	   public void stage_Num_7(){
	      make_block(0,1100,3000,50);
	   }

   //1스테이지
   public void map_VS(int stage_Num){
      //스테이지 설정
            switch(stage_Num){
               //1스테이지 일때
             case 1:
                //Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                VS_1();
            break;
             case 2:
                //Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                VS_2();
            break;
             case 3:
                //Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                VS_3();
            break;
             case 4:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                VS_4();
            break;
             case 5:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                VS_5();
            break;
            }
   }

   
   public void VS_1(){
      // x,y,w,h
      make_block(0,670,1000,30);            //가장 바닥 발판
      make_block(300,450,300,35);            //오른쪽 중간 부분 긴 바닥
      make_block(150,250,270,40);            //가운데 윗 부분 살짝 긴 바닥
      make_block(100,500,100,50);            //1스테이지의 첫 번째 블록
      make_block(500,300,70,30);            //오른쪽 가운데 작은 블록
      make_block(450,600,200,30);
      make_block(50,450,30,30);            //좌측 중간 아래의 작은 바닥
      make_block(100,350,30,20);            //좌측 중간 아래 위의 작은 바닥
      make_block(650,550,30,40);            //가장 오른쪽 아래 작은 사각형 발판
      make_block(600,200,100,40);            //가장 오른쪽 위의 사각형

      make_item(50,600,50, 50);
      make_item(200, 600, 50, 50,"a");
      make_item(600, 150, 50, 50, "next");
   }
   public void VS_2(){
      make_block(0,670,1000,30);            //가장 바닥 발판
      make_block(300,500,300,35);            //오른쪽 중간 부분 긴 바닥
      make_block(150,250,270,40);            //가운데 윗 부분 살짝 긴 바닥
      make_block(100,500,100,50);            //1스테이지의 첫 번째 블록
      make_block(500,300,70,30);            //오른쪽 가운데 작은 블록
      make_block(450,600,200,30);
      make_block(50,450,30,30);            //좌측 중간 아래의 작은 바닥
      make_block(100,350,30,20);            //좌측 중간 아래 위의 작은 바닥
      make_block(650,550,30,40);            //가장 오른쪽 아래 작은 사각형 발판
      make_block(600,200,100,40);            //가장 오른쪽 위의 사각형
      
      make_item(50, 100, 50, 50);
      make_item(600, 150, 50, 50, "next");
   }
   public void VS_3(){
      make_block(0,670,800,30);            //가장 바닥 발판
      make_block(300,450,300,35);            //오른쪽 중간 부분 긴 바닥
      make_block(150,300,270,40);            //가운데 윗 부분 살짝 긴 바닥
      make_block(100,500,100,50);            //1스테이지의 첫 번째 블록
      make_block(500,300,70,30);            //오른쪽 가운데 작은 블록
      make_block(450,800,200,30);
      make_block(50,450,30,30);            //좌측 중간 아래의 작은 바닥
      make_block(100,350,30,20);            //좌측 중간 아래 위의 작은 바닥
      make_block(650,550,30,40);            //가장 오른쪽 아래 작은 사각형 발판
      make_block(600,200,100,40);            //가장 오른쪽 위의 사각형
      
      make_item(50, 100, 50, 50);
      make_item(600, 150, 50, 50, "next");
   }
   public void VS_4(){
      make_block(0,670,500,30);            //가장 바닥 발판
      make_block(350,550,100,35);            //오른쪽 중간 부분 긴 바닥
      make_block(250,500,50,35);            //오른쪽 중간 부분 긴 바닥
      make_block(150,300,270,40);            //가운데 윗 부분 살짝 긴 바닥
      make_block(100,500,100,50);            //1스테이지의 첫 번째 블록
      make_block(400,200,200,30);            //오른쪽 가운데 작은 블록
      make_block(450,800,100,30);
      make_block(50,450,100,30);            //좌측 중간 아래의 작은 바닥
      make_block(100,350,30,20);            //좌측 중간 아래 위의 작은 바닥
      make_block(550,600,30,40);            //가장 오른쪽 아래 작은 사각형 발판
      make_block(750,200,100,40);            //가장 오른쪽 위의 사각형
      
      make_item(50, 100, 50, 50);
      make_item(750, 150, 50, 50, "finish");
   }
   public void VS_5(){
      make_block(0,670,500,30);            //가장 바닥 발판
      
      make_item(50, 100, 50, 50);
      make_item(500, 150, 50, 50, "next");
   }

   
   //1스테이지
   public void map_meet(int stage_Num){
      //스테이지 설정
            switch(stage_Num){
               //1스테이지 일때
             case 1:
                Background = tk.getImage("img/stage"+stage_Num+".gif");
                block_Array.clear();
                item_Array.clear();
                meet_Num_1();
            break;
            }
   }

   
   public void meet_Num_1(){
      // x,y,w,h
      make_block(0,670,1000,30);            //가장 바닥 발판
      make_block(300,450,300,35);            //오른쪽 중간 부분 긴 바닥
      make_block(150,250,270,40);            //가운데 윗 부분 살짝 긴 바닥
      make_block(100,500,100,50);            //1스테이지의 첫 번째 블록
      make_block(500,300,70,30);            //오른쪽 가운데 작은 블록
      make_block(450,600,200,30);
      make_block(50,450,30,30);            //좌측 중간 아래의 작은 바닥
      make_block(100,350,30,20);            //좌측 중간 아래 위의 작은 바닥
      make_block(650,550,30,40);            //가장 오른쪽 아래 작은 사각형 발판
      make_block(600,200,100,40);            //가장 오른쪽 위의 사각형

      make_item(50,600,50, 50,"a");
      make_item(200, 600, 50, 50,"a");
   }

   
   private void paint(Graphics g) {
      for (int i = 0; i < width/50; i++) {
         for (int j = 0; j < height/50; j++) {
            
         }
      }
   }
   
   public void intro(){
	   Background = tk.getImage("img/Hello_Back.gif");
       make_block(0,1200,2000,200,"block1");            //b1
       
       make_block(700,800,50,50,"left");            //b1
       make_block(750,800,50,50,"down");            //b1
       make_block(750,750,50,50,"up");            //b1
       make_block(800,800,50,50,"right");            //b1
       
       
       make_block(900,800,50,50,"a");            //b1
       make_block(950,800,50,50,"s");            //b1
       make_block(1000,800,50,50,"d");            //b1
       make_block(1050,800,50,50,"f");            //b1
       
       make_block(1300,1100,300,100);            //b1
       make_item(1300,1100,300,100,"space");            //b1
 
       
       
       
}
   
   public void make_block(int a, int b, int width, int height) {
      temp_Block_Left_Top_Point.x=a;
      temp_Block_Left_Top_Point.y=b;
      this.effect="blockdef";
      temp_Block = new Block(temp_Block_Left_Top_Point, width, height, this.effect);
      block_Array.add(temp_Block);
      
   }
   
   public void make_block(int a, int b, int width, int height, String effect) {
      temp_Block_Left_Top_Point.x=a;
      temp_Block_Left_Top_Point.y=b;
      this.effect=effect;
      temp_Block = new Block(temp_Block_Left_Top_Point, width, height, this.effect);
      block_Array.add(temp_Block);
   }
   
   public void make_item(int a, int b, int width, int height) {
      temp_Item_Left_Top_Point.x=a;
      temp_Item_Left_Top_Point.y=b;
      this.effect="itemdef";
      temp_Item = new Block(temp_Item_Left_Top_Point, width, height, this.effect);
      item_Array.add(temp_Item);
   }

   public void make_item(int a, int b, int width, int height, String effect) {
      temp_Item_Left_Top_Point.x=a;
      temp_Item_Left_Top_Point.y=b;
      temp_Item = new Block(temp_Item_Left_Top_Point, width, height);
      this.effect=effect;
      temp_Item = new Block(temp_Item_Left_Top_Point, width, height, this.effect);
      item_Array.add(temp_Item);
   }

   public Image get_Background(){
      return Background;
   }

   //생성된 블록을 반환해서 맵을 그린다.
   public ArrayList<Block> get_Block(){
      return block_Array;
   }
   public ArrayList<Block> get_Item(){
      return item_Array;
   }
}