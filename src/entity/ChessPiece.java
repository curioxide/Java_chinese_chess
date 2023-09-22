package entity;

import javax.swing.*;

public class ChessPiece {

    private int locationNum;
    private int type;
    private int side;
    private ImageIcon pic;
    private String iD;

    public int getLocationNum() {
        return locationNum;
    }

    public void setLocationNum(int locationNum) {
        this.locationNum = locationNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public ImageIcon getPic() {
        return pic;
    }

    public void setPicAddress(ImageIcon pic) {
        this.pic = pic;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public boolean equals(Object object) {
        return object instanceof ChessPiece && ((ChessPiece) object).getSide() == this.getSide();
    }

    public ChessPiece(int locationNum, int type, int side, ImageIcon pic, String iD) {

        this.locationNum = locationNum;
        this.type = type;
        this.side = side;
        this.pic = pic;
        this.iD = iD;
    }
}