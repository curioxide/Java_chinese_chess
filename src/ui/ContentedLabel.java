package ui;

import entity.ChessPiece;

import javax.swing.*;

public class ContentedLabel extends JLabel {

    private int index;
    private ChessPiece content;

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {

        this.index = index;
    }

    public ChessPiece getContent() {

        return content;
    }

    public ContentedLabel(ImageIcon imageIcon, int index, ChessPiece content) {

        super(imageIcon);
        this.index = index;
        this.content = content;
    }

    public ContentedLabel(ImageIcon imageIcon, int index) {

        super(imageIcon);
        this.index = index;
    }
}