package entity;

import javax.swing.*;

public class OperationSquare {

    private ImageIcon pic;
    private int locationNum;
    private int locationX;
    private int locationY;

    public ImageIcon getPic() {
        return pic;
    }

    public void setPic(ImageIcon pic) {
        this.pic = pic;
    }

    public int getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(int locationNum) {
        this.locationNum = locationNum;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public OperationSquare(ImageIcon pic, int locationNum, int locationX, int locationY) {

        this.pic = pic;
        this.locationNum = locationNum;
        this.locationX = locationX;
        this.locationY = locationY;
    }
}
