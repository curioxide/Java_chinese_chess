package ui;

import entity.AllChessPieces;
import entity.AllOperationSquares;
import entity.ChessPiece;
import entity.OperationSquare;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class MainUI {

    private AllChessPieces allChessPieces;
    private AllOperationSquares allOperationSquares;

    private JFrame mainFrame;   //主窗口

    private JLayeredPane panelManager;  //Panel层管理器

    private JPanel buttonArea;  //按键区
    private JPanel statusArea;    //棋局状态区
    private JPanel chessBoardLayer;  //棋盘层
    private JPanel operationLayer;   //操作层
    private JPanel chessPiecesLayer;    //棋子层

    private final JButton repentButton = new JButton("悔棋");
    private final JButton peaceButton = new JButton("求和");
    private final JButton restartButton = new JButton("重开");
    private final JButton saveButton = new JButton("保存当前对战");
    private final JButton menuButton = new JButton("主菜单");

    private JLabel chessBoardContent;    //棋盘层内容
    private JLabel selectCircle;    //选择框
    private JLabel chosenCircle;    //已选中框
    private JLabel chessStatus;    //棋局状态

    private ArrayList<ContentedLabel> chessPieceLabels;
    private ArrayList<ContentedLabel> operationSquareLabels;
    private ArrayList<Integer> ableIndexList;
    private ArrayList<Integer> moveHistory;
    private ArrayList<ContentedLabel> eatenPiece;

    private final Random random = new Random();

    private ContentedLabel theChessPieceLabel;    //将落子地

    private boolean reEntry = false;    //是否返回过主界面标记
    private int mode;    //游戏模式
    private int actionSide;    //执棋方
    private int machineSide;    //执棋阵营
    private int prePieceIndex;    //选中的棋子的索引
    private int clickCount;    //鼠标点击次数记录
    private int repentType;    //悔棋种类
    private boolean whetherBegin;    //棋局是否开始或结束
    private ImageIcon ableDotPic;    //标记点

    public void initialiseVar() {

        chessPieceLabels = new ArrayList<>();
        operationSquareLabels = new ArrayList<>();
        ableIndexList = new ArrayList<>();
        moveHistory = new ArrayList<>();
        eatenPiece = new ArrayList<>();

        ImageIcon chessBoardPic = new ImageIcon("res/1.jpg");
        chessBoardPic.setImage(chessBoardPic.getImage().getScaledInstance(720, 750, Image.SCALE_DEFAULT));//图片自适应窗口大小

        ImageIcon selectPic = new ImageIcon("res/2.png");
        selectPic.setImage(selectPic.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));//图片自适应窗口大小

        ableDotPic = new ImageIcon("res/4.png");
        ableDotPic.setImage(ableDotPic.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));//图片自适应窗口大小

        mainFrame.setLayout(new BorderLayout());

        panelManager = new JLayeredPane();
        chessBoardLayer = new JPanel();
        operationLayer = new JPanel();
        chessPiecesLayer = new JPanel();
        buttonArea = new JPanel();
        statusArea = new JPanel();

        selectCircle = new JLabel(selectPic);
        chosenCircle = new JLabel(selectPic);
        chessBoardContent = new JLabel(chessBoardPic);
        chessStatus = new JLabel("对局未开始");

        allOperationSquares = new AllOperationSquares();
        allOperationSquares.setOperationSquares(new ArrayList<>());
        allOperationSquares.initial();

        allChessPieces = new AllChessPieces();
        allChessPieces.setChessPieces(new ArrayList<>());
        allChessPieces.initial("original.dat");

        whetherBegin = false;
    }

    public void initialLists() {

        for(OperationSquare operationSquare : allOperationSquares.getOperationSquares()) {

            ContentedLabel aOperationSquare = new ContentedLabel(operationSquare.getPic(), operationSquare.getLocationNum());
            aOperationSquare.setOpaque(false);
            operationLayer.add(aOperationSquare);
            aOperationSquare.setBounds(operationSquare.getLocationX(), operationSquare.getLocationY(), 45, 45);
            aOperationSquare.addMouseListener(new MouseUIReaction(operationSquare.getLocationNum(), 1));
            operationSquare.getPic().setImage(operationSquare.getPic().getImage().getScaledInstance(aOperationSquare.getWidth(), aOperationSquare.getHeight(), Image.SCALE_DEFAULT));//图片自适应窗口大小
            operationSquareLabels.add(aOperationSquare);
        }

        for(int index = 0; index < allOperationSquares.getOperationSquares().size(); index++) {

            chessPieceLabels.add(null);
        }

        for(ChessPiece chessPiece : allChessPieces.getChessPieces()) {

            ContentedLabel aChess = new ContentedLabel(chessPiece.getPic(), chessPiece.getLocationNum(), chessPiece);
            aChess.setOpaque(false);
            chessPiecesLayer.add(aChess);
            aChess.setBounds(allOperationSquares.getOperationSquares().get(chessPiece.getLocationNum()).getLocationX(),
                    allOperationSquares.getOperationSquares().get(chessPiece.getLocationNum()).getLocationY(), 45, 45);
            chessPiece.getPic().setImage(chessPiece.getPic().getImage().getScaledInstance(aChess.getWidth(), aChess.getHeight(), Image.SCALE_DEFAULT));//图片自适应窗口大小
            chessPieceLabels.set(chessPiece.getLocationNum(), aChess);
        }
    }

    public void mainFrame() {

        mainFrame = new JFrame("中国象棋");    //主窗口
        mainFrame.setSize(732, 850);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void menuUI() {

        mainFrame.setLayout(null);

        JLabel title = new JLabel(new ImageIcon("res/5.png"));
        title.setVisible(true);
        title.setBounds(166, 100, 400, 300);
        mainFrame.add(title);

        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(266, 300, 200, 450);
        menuPanel.setVisible(true);
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 40));
        Font buttonF = new Font("隶书", Font.PLAIN, 25);
        JButton start = new JButton("双人对战");
        JButton pve = new JButton("人机对战");
        JButton loadLast = new JButton("载入上次对局");
        JButton quit = new JButton("退出");
        start.setFont(buttonF);
        pve.setFont(buttonF);
        loadLast.setFont(buttonF);
        quit.setFont(buttonF);
        start.setPreferredSize(new Dimension(200, 48));
        pve.setPreferredSize(new Dimension(200, 48));
        loadLast.setPreferredSize(new Dimension(200, 48));
        quit.setPreferredSize(new Dimension(200, 48));
        start.addMouseListener(new MouseUIReaction(9));
        pve.addMouseListener(new MouseUIReaction(10));
        quit.addMouseListener(new MouseUIReaction(11));
        loadLast.addMouseListener(new MouseUIReaction(7));
        menuPanel.add(start);
        menuPanel.add(pve);
        menuPanel.add(loadLast);
        menuPanel.add(quit);
        mainFrame.add(menuPanel);

        JLabel backGround = new JLabel(new ImageIcon("res/6.jpg"));
        backGround.setVisible(true);
        backGround.setBounds(-100, -20, 900, 900);
        mainFrame.add(backGround);

        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public void gameFrame() {

        initialiseVar();
        initialLists();

        JOptionPane.showMessageDialog(null, "单击鼠标左键可选中棋子,点击蓝点标记处即可落子;单击鼠标右键可以取消选中棋子.", "操作方法", JOptionPane.PLAIN_MESSAGE);

        mainFrame.setSize(732, 890);
        mainFrame.addMouseListener(new MouseUIReaction(2));

        //Panel层管理器设置
        panelManager.setOpaque(false);
        panelManager.setVisible(true);
        mainFrame.add(panelManager, BorderLayout.CENTER);

        //棋盘层设置
        chessBoardLayer.setLayout(null);
        chessBoardLayer.setBounds(0, 0, 720, 750);
        chessBoardContent.setBounds(0, 0, 720, 750);
        chessBoardLayer.add(chessBoardContent);
        panelManager.add(chessBoardLayer, 2, 0);

        //棋子层设置
        chessPiecesLayer.setLayout(null);
        chessPiecesLayer.setBounds(0, 0, 720, 750);
        chessPiecesLayer.setOpaque(false);
        panelManager.add(chessPiecesLayer, 3, 0);

        //操作层设置
        operationLayer.setBounds(0, 0, 720, 750);
        operationLayer.setLayout(null);
        operationLayer.setOpaque(false);
        operationLayer.add(selectCircle);    //给棋子添加选择框
        operationLayer.add(chosenCircle);
        panelManager.add(operationLayer, 4, 0);

        //按键区设置
        Font buttonF = new Font("楷体", Font.BOLD, 16);
        buttonArea.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 13));
        buttonArea.setVisible(true);
        repentButton.setPreferredSize(new Dimension(80, 40));
        peaceButton.setPreferredSize(new Dimension(80, 40));
        restartButton.setPreferredSize(new Dimension(80, 40));
        saveButton.setPreferredSize(new Dimension(140, 40));
        menuButton.setPreferredSize(new Dimension(90, 40));
        repentButton.setFont(buttonF);
        peaceButton.setFont(buttonF);
        restartButton.setFont(buttonF);
        saveButton.setFont(buttonF);
        menuButton.setFont(buttonF);

        buttonArea.add(repentButton);
        buttonArea.add(peaceButton);
        buttonArea.add(restartButton);
        buttonArea.add(saveButton);
        buttonArea.add(menuButton);

        if(!reEntry) {
            repentButton.addMouseListener(new MouseUIReaction(3));
            peaceButton.addMouseListener(new MouseUIReaction(4));
            restartButton.addMouseListener(new MouseUIReaction(5));
            saveButton.addMouseListener(new MouseUIReaction(6));
            menuButton.addMouseListener(new MouseUIReaction(8));
        }
        mainFrame.add(buttonArea, BorderLayout.SOUTH);

        statusArea.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 3));
        chessStatus.setFont(new Font("楷体", Font.BOLD, 24));
        chessStatus.setVisible(true);
        statusArea.add(chessStatus);
        mainFrame.add(statusArea, BorderLayout.NORTH);

        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    /**
     * 电脑走棋
     */
    public void machineMove() {

        if(actionSide == machineSide) {

            ableIndexList = new ArrayList<>();

            int theIndex;
            do {
                do {
                    theIndex = random.nextInt(90);
                } while(chessPieceLabels.get(theIndex) == null
                        || chessPieceLabels.get(theIndex).getContent().getSide() != actionSide);

                prePieceIndex = theIndex;
                theChessPieceLabel = chessPieceLabels.get(theIndex);
                whetherLegal();
            } while(ableIndexList.isEmpty());
            movePiece(ableIndexList.get(random.nextInt(ableIndexList.size())));
            if(actionSide == 1) {
                actionSide = 0;
                chessStatus.setText("红方执棋");
                chessStatus.setForeground(Color.red);
            } else {
                actionSide = 1;
                chessStatus.setText("黑方执棋");
                chessStatus.setForeground(Color.black);
            }
            theChessPieceLabel = null;
            chosenCircle.setVisible(false);
            clickCount = 0;
            for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                operationSquareLabels.get(ableIndex).removeAll();
            }
            operationLayer.updateUI();
            chosenCircle.setVisible(false);
            operationLayer.updateUI();
        }
    }

    /**
     * 判断走棋是否合法
     */
    public void whetherLegal() {

        int preSide = chessPieceLabels.get(prePieceIndex).getContent().getSide();
        int ableVUIndex;
        int ableHLIndex;
        int ableHRIndex;
        int ableVDIndex;
        switch (chessPieceLabels.get(prePieceIndex).getContent().getType()) {
            //将
            case 0 :
                boolean obstacle = false;
                if(preSide == 1) {
                    for(int obIndex = prePieceIndex + 9; obIndex < 90; obIndex++) {
                        if(((obIndex % 9) == (prePieceIndex % 9))
                                && chessPieceLabels.get(obIndex) != null
                                && chessPieceLabels.get(obIndex).getContent().getType() != 0) {
                            obstacle = true;
                            break;
                        }
                        if(chessPieceLabels.get(obIndex) != null
                                && ((obIndex % 9) == (prePieceIndex % 9))
                                && chessPieceLabels.get(obIndex).getContent().getType() == 0
                                && chessPieceLabels.get(obIndex).getContent().getSide() != preSide) {
                            break;
                        }
                    }
                } else {
                    for(int obIndex = prePieceIndex - 9; obIndex >= 0; obIndex--) {
                        if(((obIndex % 9) == (prePieceIndex % 9))
                                && chessPieceLabels.get(obIndex) != null
                                && chessPieceLabels.get(obIndex).getContent().getType() != 0) {
                            obstacle = true;
                            break;
                        }
                        if(chessPieceLabels.get(obIndex) != null
                                && ((obIndex % 9) == (prePieceIndex % 9))
                                && chessPieceLabels.get(obIndex).getContent().getType() == 0
                                && chessPieceLabels.get(obIndex).getContent().getSide() != preSide) {
                            break;
                        }
                    }
                }
                for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                    int tDev = Math.abs(ableIndex - prePieceIndex);
                    if((chessPieceLabels.get(ableIndex) == null
                            || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                            && ((chessPieceLabels.get(ableIndex) != null
                            && chessPieceLabels.get(ableIndex).getContent().getSide() != preSide
                            && chessPieceLabels.get(ableIndex).getContent().getType() == 0
                            && !obstacle && ((ableIndex % 9) == (prePieceIndex % 9)))
                            || ((tDev == 1 || tDev == 9) && (ableIndex != prePieceIndex)
                            && ((preSide == 1 && ((ableIndex <= 5 && ableIndex >= 3) || (ableIndex <= 14 && ableIndex >= 12) || (ableIndex <= 23 && ableIndex >= 21)))
                            || (preSide == 0 && ((ableIndex <= 68 && ableIndex >= 66) || (ableIndex <= 77 && ableIndex >= 75) || (ableIndex <= 86 && ableIndex >= 84))))))) {
                        predictPosition(ableIndex);
                    }
                }
                break;
            //士
            case 1 :
                if(prePieceIndex == 13 || prePieceIndex == 76) {
                    for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                        int tDev = Math.abs(ableIndex - prePieceIndex);
                        if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                                && ((tDev == 1 || tDev == 9 || tDev == 10 || tDev == 8) && (ableIndex != prePieceIndex)
                                && ((preSide == 1 && ((ableIndex <= 5 && ableIndex >= 3) || (ableIndex <= 14 && ableIndex >= 12) || (ableIndex <= 23 && ableIndex >= 21)))
                                || (preSide == 0 && ((ableIndex <= 68 && ableIndex >= 66) || (ableIndex <= 77 && ableIndex >= 75) || (ableIndex <= 86 && ableIndex >= 84)))))) {
                            predictPosition(ableIndex);
                        }
                    }
                } else if(prePieceIndex == 3 || prePieceIndex == 5 || prePieceIndex == 21 || prePieceIndex == 23 || prePieceIndex == 84 || prePieceIndex == 86 || prePieceIndex == 68 || prePieceIndex == 66) {
                    for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                        int tDev = Math.abs(ableIndex - prePieceIndex);
                        if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                                && ((tDev == 1 || tDev == 9 || tDev == 10 || tDev == 8) && (ableIndex != prePieceIndex)
                                && ((preSide == 1 && ((ableIndex <= 5 && ableIndex >= 3) || (ableIndex <= 14 && ableIndex >= 12) || (ableIndex <= 23 && ableIndex >= 21)))
                                || (preSide == 0 && ((ableIndex <= 68 && ableIndex >= 66) || (ableIndex <= 77 && ableIndex >= 75) || (ableIndex <= 86 && ableIndex >= 84)))))) {
                            predictPosition(ableIndex);
                        }
                    }
                } else {
                    for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                        int tDev = Math.abs(ableIndex - prePieceIndex);
                        if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                                && ((tDev == 1 || tDev == 9) && (ableIndex != prePieceIndex)
                                && ((preSide == 1 && ((ableIndex <= 5 && ableIndex >= 3) || (ableIndex <= 14 && ableIndex >= 12) || (ableIndex <= 23 && ableIndex >= 21)))
                                || (preSide == 0 && ((ableIndex <= 68 && ableIndex >= 66) || (ableIndex <= 77 && ableIndex >= 75) || (ableIndex <= 86 && ableIndex >= 84)))))) {
                            predictPosition(ableIndex);
                        }
                    }
                }
                break;
            //象
            case 2 :
                for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                    int rtDev = ableIndex - prePieceIndex;
                    if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                            && (((rtDev == 16 && chessPieceLabels.get(prePieceIndex + 8) == null && (ableIndex / 9 - prePieceIndex / 9 == 2) && (ableIndex % 9 - prePieceIndex % 9 == -2))
                            || (rtDev == -16 && chessPieceLabels.get(prePieceIndex - 8) == null && (ableIndex / 9 - prePieceIndex / 9 == -2) && (ableIndex % 9 - prePieceIndex % 9 == 2))
                            || (rtDev == 20 && chessPieceLabels.get(prePieceIndex + 10) == null && (ableIndex / 9 - prePieceIndex / 9 == 2) && (ableIndex % 9 - prePieceIndex % 9 == 2))
                            || (rtDev == -20 && chessPieceLabels.get(prePieceIndex - 10) == null && (ableIndex / 9 - prePieceIndex / 9 == -2) && (ableIndex % 9 - prePieceIndex % 9 == -2)))
                            && ((preSide == 1 && ableIndex <= 44) || (preSide == 0 && ableIndex >= 45)))) {
                        predictPosition(ableIndex);
                    }
                }
                break;
            //马
            case 3 :
                for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                    int rtDev = ableIndex - prePieceIndex;
                    if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                            && (((rtDev == 11) && (chessPieceLabels.get(prePieceIndex + 1) == null) && (ableIndex / 9 - prePieceIndex / 9 == 1) && (ableIndex % 9 - prePieceIndex % 9 == 2))
                            || ((rtDev == -11) && (chessPieceLabels.get(prePieceIndex - 1) == null) && (ableIndex / 9 - prePieceIndex / 9 == -1) && (ableIndex % 9 - prePieceIndex % 9 == -2))
                            || ((rtDev == 7) && (chessPieceLabels.get(prePieceIndex - 1) == null) && (ableIndex / 9 - prePieceIndex / 9 == 1) && (ableIndex % 9 - prePieceIndex % 9 == -2))
                            || ((rtDev == -7) && (chessPieceLabels.get(prePieceIndex + 1) == null) && (ableIndex / 9 - prePieceIndex / 9 == -1) && (ableIndex % 9 - prePieceIndex % 9 == 2))
                            || ((rtDev == 17) && (chessPieceLabels.get(prePieceIndex + 9) == null) && (ableIndex / 9 - prePieceIndex / 9 == 2) && (ableIndex % 9 - prePieceIndex % 9 == -1))
                            || ((rtDev == -17) && (chessPieceLabels.get(prePieceIndex - 9) == null) && (ableIndex / 9 - prePieceIndex / 9 == -2) && (ableIndex % 9 - prePieceIndex % 9 == 1))
                            || ((rtDev == 19) && (chessPieceLabels.get(prePieceIndex + 9) == null) && (ableIndex / 9 - prePieceIndex / 9 == 2) && (ableIndex % 9 - prePieceIndex % 9 == 1))
                            || ((rtDev == -19) && (chessPieceLabels.get(prePieceIndex - 9) == null) && (ableIndex / 9 - prePieceIndex / 9 == -2) && (ableIndex % 9 - prePieceIndex % 9 == -1)))) {
                        predictPosition(ableIndex);
                    }
                }
                break;
            //车
            case 4 :
                ableVUIndex = 0;
                ableHLIndex = 0;
                ableHRIndex = 89;
                ableVDIndex = 89;
                for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                    if((ableIndex / 9 == prePieceIndex / 9) || ableIndex % 9 == prePieceIndex % 9) {
                        if(chessPieceLabels.get(ableIndex) != null && ableIndex > ableVUIndex && ableIndex <= prePieceIndex - 9) {
                            ableVUIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex > ableHLIndex && ableIndex / 9 == prePieceIndex / 9 && ableIndex < prePieceIndex) {
                            ableHLIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex < ableHRIndex && ableIndex / 9 == prePieceIndex / 9 && ableIndex > prePieceIndex) {
                            ableHRIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex < ableVDIndex && ableIndex >= prePieceIndex + 9) {
                            ableVDIndex = ableIndex;
                        }
                    }
                }
                for(int ableIndex = ableVUIndex; ableIndex <= ableVDIndex; ableIndex++) {
                    if((ableIndex != prePieceIndex) 
                            && (chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                            && ((ableIndex % 9 == prePieceIndex % 9) || (ableIndex / 9 == prePieceIndex / 9 && ableIndex <= ableHRIndex && ableIndex >= ableHLIndex))) {
                        predictPosition(ableIndex);
                    }
                }
                break;
            //炮
            case 5 :
                ableVUIndex = 0;
                ableHLIndex = 0;
                ableHRIndex = 89;
                ableVDIndex = 89;
                for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                    if((ableIndex / 9 == prePieceIndex / 9) || ableIndex % 9 == prePieceIndex % 9) {
                        if(chessPieceLabels.get(ableIndex) != null && ableIndex > ableVUIndex && ableIndex <= prePieceIndex - 9) {
                            ableVUIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex > ableHLIndex && ableIndex / 9 == prePieceIndex / 9 && ableIndex < prePieceIndex) {
                            ableHLIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex < ableHRIndex && ableIndex / 9 == prePieceIndex / 9 && ableIndex > prePieceIndex) {
                            ableHRIndex = ableIndex;
                        } else if(chessPieceLabels.get(ableIndex) != null && ableIndex < ableVDIndex && ableIndex >= prePieceIndex + 9) {
                            ableVDIndex = ableIndex;
                        }
                    }
                }
                for(int ableIndex = ableVUIndex + 1; ableIndex <= ableVDIndex - 1; ableIndex++) {
                    if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                            && (ableIndex != prePieceIndex)
                            && ((ableIndex % 9 == prePieceIndex % 9) || (ableIndex / 9 == prePieceIndex / 9 && ableIndex < ableHRIndex && ableIndex > ableHLIndex))) {
                        predictPosition(ableIndex);
                    }
                }
                int tarIndex = 0;
                for(int ableIndex = 0; ableIndex <= ableVUIndex - 9; ableIndex++) {
                    if((ableIndex % 9 == prePieceIndex % 9)
                            && (chessPieceLabels.get(ableIndex) != null && chessPieceLabels.get(ableIndex).getContent().getSide() != preSide && ableIndex > tarIndex)) {
                        tarIndex = ableIndex;
                    }
                }
                if(tarIndex != 0) {
                    predictPosition(tarIndex);
                }
                tarIndex = 0;
                for(int ableIndex = ableVUIndex; ableIndex < ableHLIndex; ableIndex++) {

                    if((ableIndex / 9 == prePieceIndex / 9)
                            && (chessPieceLabels.get(ableIndex) != null && chessPieceLabels.get(ableIndex).getContent().getSide() != preSide && ableIndex > tarIndex)) {
                        tarIndex = ableIndex;
                    }
                }
                if(tarIndex != 0) {
                    predictPosition(tarIndex);
                }
                tarIndex = 90;
                for(int ableIndex = ableVDIndex; ableIndex > ableHRIndex; ableIndex--) {

                    if((ableIndex / 9 == prePieceIndex / 9)
                            && (chessPieceLabels.get(ableIndex) != null && chessPieceLabels.get(ableIndex).getContent().getSide() != preSide && ableIndex < tarIndex)) {
                        tarIndex = ableIndex;
                    }
                }
                if(tarIndex != 90) {
                    predictPosition(tarIndex);
                }
                tarIndex = 90;
                for(int ableIndex = 89; ableIndex >= ableVDIndex + 9; ableIndex--) {
                    if((ableIndex % 9 == prePieceIndex % 9)
                            && (chessPieceLabels.get(ableIndex) != null && chessPieceLabels.get(ableIndex).getContent().getSide() != preSide && ableIndex < tarIndex)) {
                        tarIndex = ableIndex;
                    }
                }
                if(tarIndex != 90) {
                    predictPosition(tarIndex);
                }
                break;
            //兵
            case 6 :
                if(theChessPieceLabel.getContent().getSide() == 1 && prePieceIndex <= 43) {
                    predictPosition(prePieceIndex + 9);
                } else if(theChessPieceLabel.getContent().getSide() == 0 && prePieceIndex >= 44) {
                    predictPosition(prePieceIndex - 9);
                } else {
                    for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                        if((chessPieceLabels.get(ableIndex) == null || chessPieceLabels.get(ableIndex).getContent().getSide() != preSide)
                                && (((preSide == 1) && ((Math.abs(prePieceIndex - ableIndex) == 1 && prePieceIndex / 9 == ableIndex / 9) || (ableIndex - prePieceIndex == 9)))
                                || ((preSide == 0) && ((Math.abs(prePieceIndex - ableIndex) == 1 && prePieceIndex / 9 == ableIndex / 9) || (ableIndex - prePieceIndex == -9))))) {
                            predictPosition(ableIndex);
                        }
                    }
                }
                break;
            default :
                JOptionPane.showMessageDialog(null, "操作有误", "警告", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }

    /**
     * 把棋子可走的位置在棋盘上标记出来
     *
     * @param ableIndex 可以走的位置的索引
     */
    public void predictPosition(int ableIndex) {

        JLabel positionDot = new JLabel(ableDotPic);
        positionDot.setBounds(-1, -1, 45, 45);
        operationSquareLabels.get(ableIndex).add(positionDot);
        positionDot.setVisible(true);
        operationLayer.updateUI();
        ableIndexList.add(ableIndex);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    /**
     * 移动棋子
     */
    public void movePiece(int index) {

        moveHistory.add(index);
        moveHistory.add(prePieceIndex);
        eatenPiece.add(null);

        if(chessPieceLabels.get(index) == null) {
            chessPieceLabels.set(prePieceIndex, null);
            theChessPieceLabel.setBounds(operationSquareLabels.get(index).getX(), operationSquareLabels.get(index).getY(), 45, 45);
            theChessPieceLabel.setIndex(index);
            chessPieceLabels.set(index, theChessPieceLabel);
            theChessPieceLabel.getContent().setLocationNum(index);
            repentType = 1;
        } else if((chessPieceLabels.get(index) != null) && (chessPieceLabels.get(index).getContent().getSide() != chessPieceLabels.get(prePieceIndex).getContent().getSide())) {
            eatenPiece.set(eatenPiece.size() - 1, chessPieceLabels.get(index));
            chessPiecesLayer.remove(chessPieceLabels.get(index));
            theChessPieceLabel.setBounds(operationSquareLabels.get(index).getX(), operationSquareLabels.get(index).getY(), 45, 45);
            judgeResult(index);
            chessPieceLabels.set(prePieceIndex, null);
            chessPieceLabels.set(index, null);
            theChessPieceLabel.setIndex(index);
            chessPieceLabels.set(index, theChessPieceLabel);
            theChessPieceLabel.getContent().setLocationNum(index);
            repentType = 2;
        }
        moveHistory.add(repentType);
        repentType = 0;
    }

    /**
     * 判断胜负
     */
    public void judgeResult(int index) {
        if(chessPieceLabels.get(index).getContent().getSide() == 1
                && chessPieceLabels.get(index).getContent().getType() == 0) {
            chosenCircle.setVisible(false);
            for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                operationSquareLabels.get(ableIndex).removeAll();
            }
            operationLayer.updateUI();
            chosenCircle.setVisible(false);
            JOptionPane.showMessageDialog(null, "红方胜", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            chessStatus.setText("对局结束");
            JOptionPane.showMessageDialog(null, "可以点击悔棋按钮以复盘,重新开始请点击重开按钮", "提示", JOptionPane.PLAIN_MESSAGE);
            whetherBegin = false;
        } else if(chessPieceLabels.get(index).getContent().getSide() == 0
                && chessPieceLabels.get(index).getContent().getType() == 0) {
            chosenCircle.setVisible(false);
            for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                operationSquareLabels.get(ableIndex).removeAll();
            }
            operationLayer.updateUI();
            chosenCircle.setVisible(false);
            JOptionPane.showMessageDialog(null, "黑方胜", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            chessStatus.setText("对局结束");
            JOptionPane.showMessageDialog(null, "可以点击悔棋按钮以复盘,重新开始请点击重开按钮", "提示", JOptionPane.PLAIN_MESSAGE);
            whetherBegin = false;
        }
    }

    /**
     * 保存
     *
     * @throws IOException IO异常
     */
    public void save(String fileName) throws IOException {

        File boardFile = new File(fileName);
        try(BufferedWriter dataWriter = new BufferedWriter(new FileWriter(boardFile))) {
            dataWriter.write(actionSide + "\n");
            for (ContentedLabel eachPiece : chessPieceLabels) {
                if (eachPiece != null) {
                    dataWriter.write(eachPiece.getContent().getiD() + "_" + eachPiece.getContent().getLocationNum() + "\n");
                } else {
                    dataWriter.write("none" + "\n");
                }
            }
        }
        JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 重置参数
     */
    public void reset() {

        clickCount = 0;
        theChessPieceLabel = null;
        prePieceIndex = -1;
    }

    /**
     * 走棋外围工作
     *
     * @param e 鼠标事件
     */
    public void movePreparation(MouseEvent e, int index) {

        if(e.getButton() == MouseEvent.BUTTON3) {
            reset();
            chosenCircle.setVisible(false);
            for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                operationSquareLabels.get(ableIndex).removeAll();
            }
            operationLayer.updateUI();
        } else {
            if(clickCount == 0 && chessPieceLabels.get(index) != null) {
                ableIndexList = new ArrayList<>();
                if(chessPieceLabels.get(index).getContent().getSide() == actionSide) {
                    theChessPieceLabel = chessPieceLabels.get(index);
                    prePieceIndex = index;
                    clickCount++;
                    whetherLegal();
                }
            }
            if(clickCount == 1) {
                chosenCircle.setBounds(theChessPieceLabel.getX() - 1, theChessPieceLabel.getY() - 1, 47, 47);
                chosenCircle.setVisible(true);
            }
            if(clickCount == 2 && theChessPieceLabel != null) {
                boolean moveFlag = false;
                for(int i : ableIndexList) {
                    if(i == index) {
                        movePiece(index);
                        moveFlag = true;
                        if(actionSide == 0) {
                            actionSide = 1;
                            chessStatus.setText("黑方执棋");
                            chessStatus.setForeground(Color.black);
                        } else {
                            actionSide = 0;
                            chessStatus.setText("红方执棋");
                            chessStatus.setForeground(Color.red);
                        }
                        if(mode == 1 && whetherBegin && actionSide == machineSide) {
                            machineMove();
                        }
                        break;
                    }
                }
                if(moveFlag) {
                    theChessPieceLabel = null;
                    chosenCircle.setVisible(false);
                    clickCount = 0;
                    for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
                        operationSquareLabels.get(ableIndex).removeAll();
                    }
                    operationLayer.updateUI();
                    chosenCircle.setVisible(false);
                }
            }
            if(clickCount == 1 && theChessPieceLabel != null) {
                clickCount++;
            }
        }
    }

    /**
     * 重开
     *
     * @param fileName 原始棋盘布局文件
     */
    public void restart(String fileName) {

        clickCount = 0;
        allChessPieces.setChessPieces(new ArrayList<>());
        allChessPieces.initial(fileName);
        chessPiecesLayer.removeAll();
        chessPiecesLayer.updateUI();
        chessPieceLabels = new ArrayList<>();
        initialLists();
        String[] options = {"红方", "黑方"};
        if(mode == 1) {
            int yourSide = JOptionPane.showOptionDialog(null,
                    "请选择你方阵营, 如不选择将由电脑随机决定",
                    "提示",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            if(yourSide == 0) {
                machineSide = 1;
            } else if(yourSide == 1) {
                machineSide = 0;
            } else if(yourSide == -1) {
                if(random.nextBoolean()) {
                    machineSide = 1;
                    JOptionPane.showMessageDialog(null, "未选择,随机为红方", "准备对局", JOptionPane.PLAIN_MESSAGE);
                } else {
                    machineSide = 0;
                    JOptionPane.showMessageDialog(null, "未选择,随机为黑方", "准备对局", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
        actionSide = 0;
        chessStatus.setText("红方执棋");
        chessStatus.setForeground(Color.red);
        if(actionSide == machineSide && mode == 1) {
            machineMove();
        }
        moveHistory = new ArrayList<>();
        eatenPiece = new ArrayList<>();
    }

    /**
     * 悔棋
     */
    public void repent() {

        if(!moveHistory.isEmpty()) {
            int repentPre = moveHistory.get(moveHistory.size() - 3);
            int repentCurrent = moveHistory.get(moveHistory.size() - 2);
            chessPieceLabels.get(repentPre).getContent().setLocationNum(repentCurrent);
            chessPieceLabels.set(repentCurrent, chessPieceLabels.get(repentPre));
            chessPieceLabels.get(repentPre).setBounds(operationSquareLabels.get(repentCurrent).getX(), operationSquareLabels.get(repentCurrent).getY(), 45, 45);
            chessPieceLabels.get(repentPre).setIndex(repentCurrent);
            if(moveHistory.get(moveHistory.size() - 1) == 1) {
                chessPieceLabels.set(repentPre, null);
            } else if(moveHistory.get(moveHistory.size() - 1) == 2) {
                chessPiecesLayer.add(eatenPiece.get(eatenPiece.size() - 1));
                chessPieceLabels.set(repentPre, eatenPiece.get(eatenPiece.size() - 1));
            }
            moveHistory.remove(moveHistory.size() - 1);
            moveHistory.remove(moveHistory.size() - 1);
            moveHistory.remove(moveHistory.size() - 1);
            eatenPiece.remove(eatenPiece.size() - 1);
            if(whetherBegin) {
                if(actionSide == 0) {
                    actionSide = 1;
                    chessStatus.setText("黑方执棋");
                    chessStatus.setForeground(Color.black);
                } else {
                    actionSide = 0;
                    chessStatus.setText("红方执棋");
                    chessStatus.setForeground(Color.red);
                }
            }
            if(actionSide == machineSide && mode == 1 && whetherBegin) {
                String[] options = {"是", "否"};
                int choice = JOptionPane.showOptionDialog(null,
                        "电脑即将落子,是否继续悔棋",
                        "提示",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]);
                if(choice == 0) {
                    repent();
                } else {
                    machineMove();
                }
            }
        }
    }

    /**
     * 取消棋子选中状态
     */
    public void cancelMove() {

        reset();
        chosenCircle.setVisible(false);
        for(int ableIndex = 0; ableIndex < 90; ableIndex++) {
            operationSquareLabels.get(ableIndex).removeAll();
        }
        operationLayer.updateUI();
    }

    /**
     * 鼠标UI交互类
     */
    private class MouseUIReaction extends MouseAdapter {

        private final int type;
        private final int index;

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3) {
                cancelMove();
            } else {
                switch (type) {
                    case 1 :
                        if(whetherBegin) {
                            movePreparation(e, index);
                        }
                        break;
                    case 2 :
                        if(e.getButton() == MouseEvent.BUTTON3 && whetherBegin) {
                            cancelMove();
                        }
                        break;
                    case 3 :
                        cancelMove();
                        repent();
                        break;
                    case 4 :
                        if(whetherBegin) {
                            cancelMove();
                            chessStatus.setText("对局未开始,请点击重开按钮开始对局");
                            chessStatus.setForeground(Color.red);
                            JOptionPane.showMessageDialog(null, "和解", "游戏结束", JOptionPane.PLAIN_MESSAGE);
                            JOptionPane.showMessageDialog(null, "可以点击悔棋按钮以复盘,重新开始请点击重开按钮", "提示", JOptionPane.PLAIN_MESSAGE);
                            whetherBegin = false;
                        }
                        break;
                    case 5 :
                        cancelMove();
                        chessStatus.setText("对局未开始,请点击重开按钮开始对局");
                        chessStatus.setForeground(Color.red);
                        restart("original.dat");
                        whetherBegin = true;
                        break;
                    case 6 :
                        try {
                            cancelMove();
                            if(whetherBegin) {
                                if(mode == 1) {
                                    save("data/boardFilePVE.dat");
                                } else {
                                    save("data/boardFile.dat");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "对局已结束,保存失败", "提示", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 7 :
                        mainFrame.getContentPane().removeAll();
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                        String[] options = {"双人", "人机"};
                        mode = JOptionPane.showOptionDialog(null,
                                "请选择载入的存档类型",
                                "提示",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);
                        if(mode != -1) {
                            gameFrame();
                            clickCount = 0;
                            whetherBegin = true;
                            allChessPieces.setChessPieces(new ArrayList<>());
                            chessPieceLabels = new ArrayList<>();
                            moveHistory = new ArrayList<>();
                            eatenPiece = new ArrayList<>();
                            chessPiecesLayer.removeAll();
                            chessPiecesLayer.updateUI();
                            File data = null;
                            if(mode == 1) {
                                data = new File("data/boardFilePVE.dat");
                                allChessPieces.initial("boardFilePVE.dat");
                            } else if(mode == 0) {
                                data = new File("data/boardFile.dat");
                                allChessPieces.initial("boardFile.dat");
                            }
                            initialLists();
                            try {
                                if(data != null) {
                                    try (BufferedReader dataReader = new BufferedReader(new FileReader(data))) {
                                        actionSide = Integer.parseInt(dataReader.readLine());
                                    }
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            if(actionSide == 1) {
                                chessStatus.setText("黑方执棋");
                                chessStatus.setForeground(Color.black);
                                machineSide = 0;
                            } else {
                                chessStatus.setText("红方执棋");
                                chessStatus.setForeground(Color.red);
                                machineSide = 1;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "未选择,载入失败", "提示", JOptionPane.WARNING_MESSAGE);
                            mainFrame.getContentPane().removeAll();
                            menuUI();
                            SwingUtilities.updateComponentTreeUI(mainFrame);
                        }
                        break;
                    case 8 :
                        mainFrame.getContentPane().removeAll();
                        menuUI();
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                        reEntry = true;
                        break;
                    case 9 :
                        mode = 0;
                        mainFrame.getContentPane().removeAll();
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                        gameFrame();
                        restart("original.dat");
                        whetherBegin = true;
                        break;
                    case 10 :
                        mode = 1;
                        mainFrame.getContentPane().removeAll();
                        SwingUtilities.updateComponentTreeUI(mainFrame);
                        gameFrame();
                        restart("original.dat");
                        whetherBegin = true;
                        machineMove();
                        break;
                    case 11 :
                        mainFrame.dispose();
                        System.exit(0);
                        break;
                    default :
                        JOptionPane.showMessageDialog(null, "操作有误", "警告", JOptionPane.WARNING_MESSAGE);
                        break;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

            if(type == 1) {
                selectCircle.setVisible(true);
                selectCircle.setOpaque(false);
                selectCircle.setBounds(operationSquareLabels.get(index).getX() - 1, operationSquareLabels.get(index).getY() - 1, 47, 47);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

            if(type == 1) {
                selectCircle.setVisible(false);
            }
        }

        /**
         * 鼠标UI互动一类构造函数
         *
         * @param index 类编号
         * @param type  互动类型
         */
        public MouseUIReaction(int index, int type) {

            this.index = index;
            this.type = type;
        }

        /**
         * 鼠标UI互动二类构造函数
         *
         * @param type 互动类型
         */
        public MouseUIReaction(int type) {

            this.index = -1;
            this.type = type;
        }
    }

    public static void main(String[] args) {

        MainUI game = new MainUI();
        game.mainFrame();
        game.menuUI();
    }
}