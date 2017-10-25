package zetcode;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Paddle extends Sprite implements Commons {
	
	boolean left=false;

    private int dx;

    public Paddle() {

        ImageIcon ii = new ImageIcon("src/a15.gif");
        image = ii.getImage();

        setX(getWidth());
        i_width = -image.getWidth(null);
        i_heigth = image.getHeight(null);

        resetState();
    }

    public void move() {

        x += dx;

        if (x <= 0) {
            x = 0;
        }

        if (x >= WIDTH - i_width) {
            x = WIDTH - i_width;
        }
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -5;
            if(left) {
    	        setX(getX()+getWidth());
    	        setWidth(-getWidth());
    	        left=false;
            }
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 5;
            if(!left) {
	            setX(getX()+getWidth());
	            setWidth(-getWidth());
	            left=true;
            }
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    private void resetState() {

        x = INIT_PADDLE_X;
        y = INIT_PADDLE_Y;
    }
}