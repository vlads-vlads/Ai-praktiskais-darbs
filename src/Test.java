import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class State {
    int minMaxValue;
    int pace;
    int score;
    int level;
    State [] children;
    public State (int pace, int score, int level, State [] children) {
        this.pace = pace;
        this.score = score;
        this.level = level;
        this.children = children;
    }

    public void setMinMaxValue(int value) {
        minMaxValue = value;
    }
    public int getMinMaxValue() {
        return minMaxValue;
    }

    public int getPace() {
        return pace;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public State[] getChildren() {
        return children;
    }

    public boolean isTerminal () {
        boolean isTerminal = false;
        if (this.getChildren().length == 0) isTerminal = true;
        return isTerminal;
    }

    @Override
    public String toString() {
        return "State{" +
                "pace=" + pace +
                ", score=" + score +
                ", level=" + level +
                '}';
    }
}

class Player {
    static int MOVECOUNT = 0;
    public State makeMove(int pace, State currentState) {
        State newState = currentState;
        State [] children = currentState.getChildren();

        if(newState.isTerminal())
            return newState;
        else if (newState.getChildren().length == 1) {
            MOVECOUNT++;
            return children[0];
        }
        for (State state : currentState.getChildren()) {
            if (state.getScore() + pace == currentState.getScore()) {
                newState = state;
            }
        }
        MOVECOUNT++;
        return newState;
    }


}



class Ai {
    static int MOVECOUNT = 0;
    public void minimax(ArrayList<State> board) {

        for (State state : board) {
            if (state.isTerminal()) {
                if (state.getLevel() == 0)
                    state.setMinMaxValue(1);
                if (state.getLevel() == 1)
                    state.setMinMaxValue(-1);
                if (state.getLevel() == 2)
                    state.setMinMaxValue(1);
            }
        }


        int value;
        for (State state : board) {
            if (state.getChildren().length == 2 && state.getLevel() == 1 || state.getLevel() == 3 || state.getLevel() == 5 || state.getLevel() == 7) {
                State[] newBoard = state.getChildren();
                value = Math.max(newBoard[0].getMinMaxValue(), newBoard[1].getMinMaxValue());
                state.setMinMaxValue(value);
            } else if (state.getChildren().length == 1 && state.getLevel() == 1 || state.getLevel() == 3 || state.getLevel() == 5 || state.getLevel() == 7) {
                State[] newBoard = state.getChildren();
                value = newBoard[0].getMinMaxValue();
                state.setMinMaxValue(value);
            } else if (state.getChildren().length == 2 && state.getLevel() == 2 || state.getLevel() == 4 || state.getLevel() == 6) {
                State[] newBoard = state.getChildren();
                value = Math.min(newBoard[0].getMinMaxValue(), newBoard[1].getMinMaxValue());
                state.setMinMaxValue(value);
            } else if (state.getChildren().length == 1 && state.getLevel() == 2 || state.getLevel() == 4 || state.getLevel() == 6) {
                State[] newBoard = state.getChildren();
                value = newBoard[0].getMinMaxValue();
                state.setMinMaxValue(value);
            }
        }
    }

    public State makeMove1(State currentState) {
        if(currentState.isTerminal())
            return currentState;
        State newState = currentState;
        int randomChoice;
        State[] children = currentState.getChildren();

        if(children.length == 1)
            newState = children[0];
        else if (children[0].getMinMaxValue() == children[1].getMinMaxValue()) {
            randomChoice = (int) (Math.random() * 2);
            newState = children[randomChoice];
        } else if (children[0].getMinMaxValue() > children[1].getMinMaxValue()) {
            newState = children[0];
        } else if (children[0].getMinMaxValue() < children[1].getMinMaxValue()) {
            newState = children[1];
        }
        MOVECOUNT++;
        return newState;
    }
    public State makeMove2(State currentState) {
        if(currentState.isTerminal())
            return currentState;
        State newState = currentState;
        int randomChoice;
        State[] children = currentState.getChildren();

        if(children.length == 1)
            newState = children[0];
        else if (children[0].getMinMaxValue() == children[1].getMinMaxValue()) {
            randomChoice = (int) (Math.random() * 2);
            newState = children[randomChoice];
        } else if (children[0].getMinMaxValue() < children[1].getMinMaxValue()) {
            newState = children[0];
        } else if (children[0].getMinMaxValue() > children[1].getMinMaxValue()) {
            newState = children[1];
        }
        MOVECOUNT++;
        return newState;
    }
}


class Gui implements ActionListener {
    Player p;
    Ai ai;
    State currentState;
    static State finalState;
    boolean gameMode;

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JFrame frame;
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;


    public boolean getGameMode() {
        return gameMode;
    }

    public void setGameMode(boolean gameMode) {
        this.gameMode = gameMode;
    }

    public Gui(State state) {
        gameMode = true;
        finalState = state;
        currentState = state;
        p = new Player();
        ai = new Ai();
        frame = new JFrame();

        label1 = new JLabel("Who will start?");
        button3 = new JButton("Change game mode");

        label3 = new JLabel("Available actions :");
        button1 = new JButton("2");
        button2 = new JButton("3");


        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);


        label2 = new JLabel("Current number: " + currentState.getScore());

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        panel.add(label1);
        panel.add(button3);
        panel.add(label3);
        panel.add(button1);
        panel.add(button2);





        panel.add(label2);




        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Our Gui");
        frame.pack();
        frame.setVisible(true);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == button1) {
            if(currentState.getScore() >= 2) {
                if (gameMode) {
                    State newState = p.makeMove(2, currentState);
                    label2.setText("Current number: " + newState.getScore());
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                    }
                    JOptionPane.showMessageDialog(null, "Ai's turn");
                    newState = ai.makeMove2(newState);
                    label2.setText("Current number: " + newState.getScore());
                    setCurrentState(newState);
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                    }
                } else {

                    State newState = p.makeMove(2, currentState);
                    label2.setText("Current number: " + newState.getScore());

                    if (!newState.isTerminal()) {
                        JOptionPane.showMessageDialog(null, "Ai's turn");
                        newState = ai.makeMove1(newState);
                        setCurrentState(newState);
                        label2.setText("Current number: " + newState.getScore());
                    }
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT || Player.MOVECOUNT == Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                        JOptionPane.showMessageDialog(null, "Ai is going first! Your turn!");
                        newState = ai.makeMove1(currentState);
                        setCurrentState(newState);
                        label2.setText("Current number: " + currentState.getScore());
                    }
                }
            }
        }
        else if (e.getSource() == button2) {
            if(currentState.getScore() >= 3) {
                if (gameMode) {
                    State newState = p.makeMove(3, currentState);
                    label2.setText("Current number: " + newState.getScore());
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                    }
                    JOptionPane.showMessageDialog(null, "Ai's turn");
                    newState = ai.makeMove2(newState);
                    label2.setText("Current number: " + newState.getScore());
                    setCurrentState(newState);
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                    }
                } else {

                    State newState = p.makeMove(3, currentState);
                    label2.setText("Current number: " + newState.getScore());

                    if (!newState.isTerminal()) {
                        JOptionPane.showMessageDialog(null, "Ai's turn");
                        newState = ai.makeMove1(newState);
                        setCurrentState(newState);
                        label2.setText("Current number: " + newState.getScore());
                    }
                    if (newState.isTerminal()) {
                        if (Player.MOVECOUNT > Ai.MOVECOUNT || Player.MOVECOUNT == Ai.MOVECOUNT)
                            JOptionPane.showMessageDialog(null, "Player won!");
                        else JOptionPane.showMessageDialog(null, "Ai Won");
                        Player.MOVECOUNT = 0;
                        Ai.MOVECOUNT = 0;
                        setCurrentState(finalState);
                        label2.setText("Current number: " + finalState.getScore());
                        JOptionPane.showMessageDialog(null, "Ai is going first! Your turn!");
                        newState = ai.makeMove1(currentState);
                        setCurrentState(newState);
                        label2.setText("Current number: " + currentState.getScore());
                    }
                }
            }
        }
        else if(e.getSource() == button3) {
            setGameMode(!gameMode);
            Player.MOVECOUNT = 0;
            Ai.MOVECOUNT = 0;
            setCurrentState(finalState);
            label2.setText("Current number: " + currentState.getScore());
            if(gameMode == false) {
                JOptionPane.showMessageDialog(null, "Ai is going first! Your turn!");
                State newState = ai.makeMove1(currentState);
                setCurrentState(newState);
                label2.setText("Current number: " + currentState.getScore());
                if (newState.isTerminal()) {
                    if (Ai.MOVECOUNT > Player.MOVECOUNT)
                        JOptionPane.showMessageDialog(null, "Ai won!");
                    else JOptionPane.showMessageDialog(null, "Player Won");
                    Player.MOVECOUNT = 0;
                    Ai.MOVECOUNT = 0;
                    setCurrentState(finalState);
                    label2.setText("Current number: " + finalState.getScore());
                }
            }
        }
    }
}

public class Test {
    public static void main(String[] args) {



        State s41 = new State(2,1,0, new State [] {});
        State s40 = new State(3,0,0, new State [] {});
        State s39 = new State(2,0,0, new State [] {});
        State s38 = new State(3,1,1, new State [] {});
        State s37 = new State(2,1,1, new State [] {});
        State s36 = new State(3,0,1, new State [] {});
        State s35 = new State(2,0,1, new State [] {});
        State s34 = new State(3,1,2, new State [] {});
        State s33 = new State(2,1,2, new State [] {});
        State s32 = new State(3,0,2, new State [] {});

        State s31 = new State(2,3,1, new State [] {s41, s40});
        State s30 = new State(3,2,1, new State [] {s39});
        State s29 = new State(2,2,1, new State [] {s39});
        State s28 = new State(2,5,2, new State [] {s31, s30});
        State s27 = new State(3,4,2, new State [] {s29, s38});
        State s26 = new State(2,4,2, new State [] {s29, s38});
        State s25 = new State(3,3,2, new State [] {s37, s36});
        State s24 = new State(2,3,2, new State [] {s37, s36});
        State s23 = new State(3,2,2, new State [] {s35});
        State s22 = new State(2,2,2, new State [] {s35});
        State s21 = new State(2,7,3, new State [] {s28, s27});
        State s20 = new State(3,6,3, new State [] {s26, s25});
        State s19 = new State(2,6,3, new State [] {s26, s25});
        State s18 = new State(3,5,3, new State [] {s24, s23});
        State s17 = new State(2,5,3, new State [] {s24, s23});
        State s16 = new State(3,4,3, new State [] {s22, s34});
        State s15 = new State(2,4,3, new State [] {s22, s34});
        State s14 = new State(3,3,3, new State [] {s33, s32});
        State s13 = new State(2,9,4, new State [] {s21, s20});
        State s12 = new State(3,8,4, new State [] {s19, s18});
        State s11 = new State(2,8,4, new State [] {s19, s18});
        State s10 = new State(3,7,4, new State [] {s17, s16});
        State s9 = new State(2,7,4, new State [] {s17, s16});
        State s8 = new State(3,6,4, new State [] {s15, s14});
        State s7 = new State(2,11,5, new State [] {s13, s12});
        State s6 = new State(3,10,5, new State [] {s11, s10});
        State s5 = new State(2,10,5, new State [] {s11, s10});
        State s4 = new State(3,9,5, new State [] {s9, s8});
        State s3 = new State(2,13,6, new State [] {s7, s6});
        State s2 = new State(3,12,6, new State [] {s5, s4});
        State s1 = new State(0,15,7, new State [] {s3, s2});



        ArrayList <State> board = new ArrayList<>();

        board.add(s41);
        board.add(s40);
        board.add(s39);
        board.add(s38);
        board.add(s37);
        board.add(s36);
        board.add(s35);
        board.add(s34);
        board.add(s33);
        board.add(s32);
        board.add(s31);
        board.add(s30);
        board.add(s29);
        board.add(s28);
        board.add(s27);
        board.add(s26);
        board.add(s25);
        board.add(s24);
        board.add(s23);
        board.add(s22);
        board.add(s21);
        board.add(s20);
        board.add(s19);
        board.add(s18);
        board.add(s17);
        board.add(s16);
        board.add(s15);
        board.add(s14);
        board.add(s13);
        board.add(s12);
        board.add(s11);
        board.add(s10);
        board.add(s9);
        board.add(s8);
        board.add(s7);
        board.add(s6);
        board.add(s5);
        board.add(s4);
        board.add(s3);
        board.add(s2);
        board.add(s1);


        Ai ai2 = new Ai();
        ai2.minimax(board);

        Gui gui = new Gui(s1);


    }
}