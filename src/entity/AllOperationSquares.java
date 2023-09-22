package entity;

import javax.swing.*;
import java.util.ArrayList;

public class AllOperationSquares {

    private ArrayList<OperationSquare> operationSquares;

    public ArrayList<OperationSquare> getOperationSquares() {
        return operationSquares;
    }

    public void setOperationSquares(ArrayList<OperationSquare> operationSquares) {
        this.operationSquares = operationSquares;
    }

    public void initial() {

        int x = 54;
        int y = 23;
        int index = 0;
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                operationSquares.add(new OperationSquare(new ImageIcon("res/3.png"), index, x, y));
                x += 71;
                index++;
            }
            y += 73;
            x = 54;
        }
        y += 2;
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 9; j++) {
                operationSquares.add(new OperationSquare(new ImageIcon("res/3.png"), index, x, y));
                x += 71;
                index++;
            }
            y += 73;
            x = 54;
        }
    }
}
