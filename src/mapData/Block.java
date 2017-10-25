package mapData;

import java.awt.Image;
import java.awt.Point;

public class Block {
	
	private Image block_image;
	
	//박스가 놓일 좌상단 포인트
	private Point block_Point;
	
	//넓이
	private int width;
	
	//캐릭터와 접척 여부
	private boolean contact;
	
	//높이
	private int height;
	
	private String effect;
	
	
	//좌상단 포인트와 넓이 높이 를 가져와 블록을 생성한다.
	public Block(Point left_Top_Point, int width, int height) {
		
		//생성시 입력받은 좌 상단 좌쵸를 가져온다.
		block_Point = new Point(left_Top_Point.x, left_Top_Point.y);

		//넓이와 높이를 가져온다.
		this.width = width;
		this.height = height;
		
		//접촉여부
		contact = false;
		
	}
	
	//리턴으로 객체를 하지 않는 이유는 private 보안상의 이유로
	
	public Block(Point left_Top_Point, int width, int height, String effect) {
		
		//생성시 입력받은 좌 상단 좌쵸를 가져온다.
		block_Point = new Point(left_Top_Point.x, left_Top_Point.y);

		//넓이와 높이를 가져온다.
		this.width = width;
		this.height = height;
		
		this.effect = effect;
		//접촉여부
		contact = false;
	}


	//좌상단 포인트와 넓이 높이를 리턴한다.
	public Point get_Left_Top_Point(){
		return block_Point;
	}
	
	//넓이를 반환한다.
	public int get_Width(){
		return width;
	}
	
	//높이를 반환한다.
	public int get_Height(){
		return height;
	}
	
	public String get_effect(){
		return effect;
	}
	public void set_effect(String effectt){
	      effect= effectt;
	   }

	//접촉시에 true
	public void set_Contect_T(){
		contact = true;
	}
	
	public void set_Contect_F(){
		contact = false;
	}
	
	public boolean get_Set_Contect(){
		return contact;
	}

	public void set_block_Y_Point(int set_Y_Point){
		block_Point.y = set_Y_Point;
	}
	
	public void set_block_X_Point(int set_X_Point){
		block_Point.x = set_X_Point;
	   }
}


