import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Saatvik
 */
class Input {

    private String move_type;
    private String my_color;
    private double time_left;
    private ArrayList<String> board;

    public Input() {
        board = new ArrayList<>();
    }

    public String getMoveType() {
        return move_type;
    }

    public void setMoveType(String type) {
        move_type = type;
    }

    public String getColor() {
        return my_color;
    }

    public void setColor(String color) {
        my_color = color;
    }

    public double getTimeLeft() {
        return time_left;
    }

    public void setTimeLeft(double time) {
        time_left = time;
    }

    public ArrayList<String> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<String> input_board) {
        board = new ArrayList<String>(input_board);
    }
}
public class Tester {
    private int BOARD_SIZE = 16;
    private double remainingTimeP1 = 300, remainingTimeP2 = 300;
    private ArrayList<String> makeNewBoard(ArrayList<String> board, int old_x, int old_y, int new_x, int new_y){
        if(old_x == new_x && old_y == new_y){
            return board;
        }
        char ch1 = board.get(old_x).charAt(old_y);
        char ch2 = board.get(new_x).charAt(new_y);
        ArrayList<String> newBoard = new ArrayList();
        String S1, S2 = "";
        int Flag;
        if(old_x != new_x){
            S1 = board.get(old_x).substring(0, old_y) + ch2 + board.get(old_x).substring(old_y + 1);
            S2 = board.get(new_x).substring(0, new_y) + ch1 + board.get(new_x).substring(new_y + 1);
            Flag = 1;
        } else {
            if(old_y < new_y){
                S1 = board.get(old_x).substring(0, old_y) + ch2 + 
                        board.get(old_x).substring(old_y + 1, new_y) + ch1 + 
                        board.get(old_x).substring(new_y + 1);
            } else {
                S1 = board.get(old_x).substring(0, new_y) + ch1 + 
                        board.get(old_x).substring(new_y + 1, old_y) + ch2 + 
                        board.get(old_x).substring(old_y + 1);
            }
            Flag = 2;
        }
        for(int x = 0; x< BOARD_SIZE; x++){
            if(x == old_x || x == new_x){
                if(Flag == 1){
                    if(x == old_x){
                        newBoard.add(S1);
                    } else if(x == new_x){
                        newBoard.add(S2);
                    }
                } else if(Flag == 2){
                    newBoard.add(S1);
                }
            } else {
                newBoard.add(board.get(x));
            }
        }
        return newBoard;
    }
    private Input getData() throws FileNotFoundException, IOException {
        File fin = new File("input.txt");
        Input dataObj;
        ArrayList<String> board;
        try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
            String data;
            int lineNumber = 0;
            dataObj = new Input();
            board = new ArrayList<>();
            while ((data = br.readLine()) != null && lineNumber < 20){
                lineNumber++;
                switch (lineNumber) {
                    case 1:
                        if (data.toLowerCase().equals("single")) {
                            dataObj.setMoveType("SINGLE");
                        } else {
                            dataObj.setMoveType("GAME");
                        }
                        break;
                    case 2:
                        if (data.toLowerCase().equals("white")) {
                            dataObj.setColor("WHITE");
                        } else {
                            dataObj.setColor("BLACK");
                        }
                        break;
                    case 3:
                        float val = Float.parseFloat(data);
                        dataObj.setTimeLeft(val);
                        break;
                    default:
                        board.add(data);
                }
            }
        }
        dataObj.setBoard(board);
        return dataObj;
    }
    private Boolean isGameWon(ArrayList<String> board, String myC){
        int opp_start, opp_end, opp_sum;
        char myColor, opp_color;
        if("WHITE".equals(myC)){
            opp_start = 11; opp_end = BOARD_SIZE; opp_sum = 24; myColor = 'B'; opp_color = 'W';
        } else {
            opp_start = 0; opp_end = 5; opp_sum = 6; myColor = 'W'; opp_color = 'B';
        }
        int myColor_points = 0, opColor_points = 0;
        for(int i = opp_start; i < opp_end; i++){
            for(int j = opp_start; j < opp_end; j++){
                if ((opp_sum == 6 && i + j < opp_sum) || (opp_sum == 24 && i + j > opp_sum)){
                    if(board.get(i).charAt(j) == myColor){
                        myColor_points++;
                    } else if(board.get(i).charAt(j) == opp_color){
                        opColor_points++;
                    }
                }
            }
        }
        if(myColor_points > 0 && myColor_points + opColor_points == 19){
            return true;
        }
        return false;
    }
    private ArrayList<String> getOutput(Input ipt) throws FileNotFoundException, IOException{
        File fin = new File("output.txt");
        ArrayList<String> board = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fin));
        String data, lastline = "";
        int lineNumber = 0;
        int old_x = 0, old_y = 0, new_x = 0, new_y = 0;
        while ((data = br.readLine()) != null){
            lastline = String.valueOf(data);
            if(lineNumber == 0){
                String str = data.split(" ")[1];
                String[] temp = str.split(",");
                old_x = Integer.parseInt(temp[0]);
                old_y = Integer.parseInt(temp[1]);
            }
            lineNumber++;
        }
        br.close();
        String str = lastline.split(" ")[2];
        String[] temp = str.split(",");
        new_x = Integer.parseInt(temp[0]);
        new_y = Integer.parseInt(temp[1]);
        board = makeNewBoard(ipt.getBoard(), old_y, old_x, new_y, new_x);
        return board;
    }
    private void changeInput(Input ipt, double remainingTime) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input.txt"), "utf-8"));
        if("WHITE".equals(ipt.getColor())){
            ipt.setColor("BLACK");
        } else{
            ipt.setColor("WHITE");
        }
        bw.write(ipt.getMoveType() + "\n");
        bw.write(ipt.getColor() + "\n");
        double time = Math.round(remainingTime * 100.0) / 100.0;
        bw.write(time + "\n");
        for(String i : ipt.getBoard()){
            bw.write(i);
            bw.write("\n");
        }
        bw.close();
    }
    public static void main(String args[]) throws FileNotFoundException, IOException, InterruptedException{
        Tester tt = new Tester();
        Input ipt;
        int cnt2 = 0, cnt3 = 0;
        double startTimeP1, startTimeP2, endTimeP1, endTimeP2, timeTakenP1, timeTakenP2;
        ArrayList<String> board = new ArrayList<>();
        while(tt.remainingTimeP1 > 0 && tt.remainingTimeP2 > 0){
            if (tt.remainingTimeP1 > 0){
                cnt2++;
                // Get input file
                ipt = tt.getData();
                startTimeP1 = System.currentTimeMillis();
                homework.main(args);
                endTimeP1 = System.currentTimeMillis();
                timeTakenP1 = (endTimeP1 - startTimeP1)/1000.0;
                tt.remainingTimeP1 -= timeTakenP1;
                // Get new Board
                board = tt.getOutput(ipt);
                ipt.setBoard(board);
                // Change the input file according to the new board
                tt.changeInput(ipt, tt.remainingTimeP2);
                for(String i : board){
                    System.out.println(i);
                }
                if(tt.isGameWon(ipt.getBoard(), ipt.getColor())){
                    System.out.println("Player 1 Won in " + cnt2 + " steps");
                    break;
                }
                System.out.println("Player 1 done");
            }
            if(tt.remainingTimeP2 > 0){
                cnt3++;
                // Get input file
                ipt = tt.getData();
                // record time for player 2
                startTimeP2 = System.currentTimeMillis();
                homework.main(args);
                endTimeP2 = System.currentTimeMillis();
                timeTakenP2 = (endTimeP2 - startTimeP2)/1000.0;
                tt.remainingTimeP2 -= timeTakenP2;
                // Get new Board
                board = tt.getOutput(ipt);
                ipt.setBoard(board);
                // Change the input file according to the new board
                tt.changeInput(ipt, tt.remainingTimeP1);
                for(String i : board){
                    System.out.println(i);
                }
                if(tt.isGameWon(ipt.getBoard(), ipt.getColor())){
                    System.out.println("Player 2 Won in " + cnt3 + " steps");
                    break;
                }
                System.out.println("Player 2 done");
            }
        }
        if(tt.remainingTimeP1 <= 0)   {
            System.out.println("Time of Player 1 ended " + cnt2 + " " + cnt3);
        } else if(tt.remainingTimeP2 <= 0){
            System.out.println("Time of Player 2 ended " + cnt2 + " " + cnt3);
        }
    }
}
