package zetcode;

import javax.swing.ImageIcon;

public class Ball extends Sprite implements Commons {

    private int xdir;
    private int ydir;

    public Ball() {

        xdir = 1;
        ydir = -1;

        ImageIcon ii = new ImageIcon("src/a1.jpg");
        image = ii.getImage();

        i_width = ii.getIconWidth();
        i_heigth = ii.getIconHeight();

        resetState();
    }

    public void move() {
        
        x += xdir;
        y += ydir;

        if (x == 0) {
            setXDir(5);
        }

        if (x == WIDTH - i_width) {
            setXDir(-5);
        }

        if (y == 0) {
            setYDir(5);
        }
    }

    private void resetState() {
        
        x = INIT_BALL_X;
        y = INIT_BALL_Y;
    }

    public void setXDir(int x) {
        xdir = x;
    }

    public void setYDir(int y) {
        ydir = y;
    }

    public int getYDir() {
        return ydir;
    }
}