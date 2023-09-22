package entity;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllChessPieces {

    private ArrayList<ChessPiece> chessPieces;

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void setChessPieces(ArrayList<ChessPiece> chessPieces) {
        this.chessPieces = chessPieces;
    }

    public void initial(String fileName) {
        try {
            File data = new File("data/" + fileName);
            try(BufferedReader dataReader = new BufferedReader(new FileReader(data))) {
                while (true) {
                    String piece = dataReader.readLine();
                    if (piece == null) {
                        break;
                    }
                    if (!piece.equals("1") && !piece.equals("0") && !piece.equals("none")) {
                        String[] infoPart = piece.split("_");
                        String iD = infoPart[0];
                        String locationNum = infoPart[1];
                        chessPieces.add(new ChessPiece(Integer.parseInt(locationNum),
                                Character.getNumericValue(iD.charAt(1)),
                                Character.getNumericValue(iD.charAt(0)),
                                new ImageIcon("res/" + iD + ".png"), iD));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}