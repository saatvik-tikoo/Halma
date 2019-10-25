/**
 *
 * @author Saatvik
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue; 

class InputData {

    private int move_type; // 1 = Single or 2 = Game
    private char my_color; // W = White or B = Black
    private double time_left; // Time left for the remaining game
    private ArrayList<String> board;

    public InputData() {
        board = new ArrayList<>();
    }

    public int getMoveType() {
        return move_type;
    }

    public void setMoveType(int type) {
        move_type = type;
    }

    public char getColor() {
        return my_color;
    }

    public void setColor(char color) {
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
        board = (ArrayList<String>) input_board.clone();
    }
}

class Node{
    private ArrayList<String> parent;
    private ArrayList<String> me;
    private ArrayList<Node> children; 
    private int score;
    private int level;
    private ArrayList<Move> moves;
    private char moveTypeToReachHere;
    
    public Node(ArrayList<String> parent, ArrayList<String> me, ArrayList<Node> children, int score, 
            int level, ArrayList<Move> moves, char moveTypeToReachHere){
        this.parent = parent;
        this.me = me;
        this.children = children;
        this.score = score;
        this.level = level;
        this.moves = moves;
        this.moveTypeToReachHere = moveTypeToReachHere;
    }

    public ArrayList<String> getParent() {
        return parent;
    }
    
    public void setParent(ArrayList<String> parent) {
        this.parent = parent;
    }
    
    public ArrayList<String> getMe() {
        return me;
    }
    
    public void setMe(ArrayList<String> me) {
        this.me = me;
    }
    
    public ArrayList<Node> getChildren() {
        return children;
    }
    
    public void addChildren(Node child) {
        this.children.add(child);
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public ArrayList<Move> getMoves() {
        return moves;
    }
    
    public void addMove(Move move) {
        this.moves.add(move);
    }
    
    public char getMoveTypeToReachHere() {
        return moveTypeToReachHere;
    }
    
    public void setMoveTypeToReachHere(char moveTypeToReachHere) {
        this.moveTypeToReachHere = moveTypeToReachHere;
    }
}

class Pair { 
  private final int x; 
  private final int y; 
  public Pair(int x, int y) { 
    this.x = x; 
    this.y = y; 
  }
  public int getX(){
      return x;
  }
  public int getY(){
      return y;
  }
}

class Move{
    private Pair from;
    private Pair to;
    public Move(Pair from, Pair to){
        this.from = from;
        this.to = to;
    }
    public Pair getFrom(){
        return this.from;
    }
    public void setForm(Pair from){
        this.from = from;
    }
    public Pair getTo(){
        return this.to;
    }
    public void setTo(Pair to){
        this.to = to;
    }
}

public class homework {
    private static final int BOARD_SIZE = 16;
    private static final int MIN = -10000;
    private static final int MAX = 10000;
    private int[][] visitedInHops;
    private Boolean utilityFunctionFlag = false;
    // Get Input from File
    private InputData getData() throws FileNotFoundException, IOException {
        File fin = new File("input.txt");
        InputData dataObj;
        ArrayList<String> board;
        try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
            String data;
            int lineNumber = 0;
            dataObj = new InputData();
            board = new ArrayList<>();
            while ((data = br.readLine()) != null && lineNumber < 20){
                lineNumber++;
                switch (lineNumber) {
                    case 1:
                        if (data.toLowerCase().equals("single")) {
                            dataObj.setMoveType(1);
                        } else {
                            dataObj.setMoveType(2);
                        }
                        break;
                    case 2:
                        if (data.toLowerCase().equals("white")) {
                            dataObj.setColor('W');
                        } else {
                            dataObj.setColor('B');
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
    // Change the depth of these according to the time
    private int getMaxDepth(double time_left, int moveType) throws FileNotFoundException, IOException{
        if(moveType == 1){
            return 2;
        } else{
            File fin = new File("calibration.txt");
            if(fin.exists()) {
                long[] timeForEachDepth = new long[10];
                try (BufferedReader br = new BufferedReader(new FileReader(fin))) {
                    timeForEachDepth[0] = Long.parseLong(br.readLine())/1000;
                    timeForEachDepth[1] = Long.parseLong(br.readLine())/1000;
                    timeForEachDepth[2] = Long.parseLong(br.readLine())/1000;

                    if(timeForEachDepth[2] + 7 < time_left && time_left > 300){
                        utilityFunctionFlag = true;
                        return 4;
                    } else if(timeForEachDepth[1] + 3 < time_left && time_left > 290){
                        utilityFunctionFlag = true;
                        return 3;
                    } else if(timeForEachDepth[2] + 7 < time_left && time_left > 100){
                        utilityFunctionFlag = true;
                        return 4;
                    } else if(timeForEachDepth[1] + 3 < time_left && time_left > 10){
                        utilityFunctionFlag = true;
                        return 3;
                    } else{
                        utilityFunctionFlag = false;
                        return 2;
                    }
                }
            } else{
                if(time_left > 300){
                    utilityFunctionFlag = true;
                    return 4;
                } else if(time_left > 290){
                    utilityFunctionFlag = true;
                    return 3;
                } else if(time_left > 100){
                    utilityFunctionFlag = true;
                    return 4;
                } else if(time_left > 15){
                    utilityFunctionFlag = true;
                    return 3;
                } else{
                    utilityFunctionFlag = false;
                    return 2;
                }
            }
        }
    }
    // Generate the current state of Board
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
    //Get the Total Jumps the opposite player can make if position of one point is changed
    private int jumpsOppositePlayerCanMake(ArrayList<String> board, char myColor, char op_color, int i, int j){
        int cnt = 0;
        for(int x = i - 1; x <= i + 1; x++){
            for(int y = j - 1; y <= j + 1; y++){
                 if(x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE
                         && board.get(x).charAt(y) == op_color){
                     if((x == i - 1 && y == j - 1 && i + 1 < BOARD_SIZE && j + 1 < BOARD_SIZE
                             && board.get(i + 1).charAt(j + 1) == '.') ||
                         (x == i - 1 && y == j && i + 1 < BOARD_SIZE
                             && board.get(i + 1).charAt(j) == '.') ||
                         (x == i - 1 && y == j + 1 && i + 1  < BOARD_SIZE && j - 1 >= 0 
                             && board.get(i + 1).charAt(j - 1) == '.') ||
                         (x == i && y == j - 1 && j + 1 < BOARD_SIZE
                             && board.get(i).charAt(j + 1) == '.') ||
                         (x == i && y == j + 1 && j - 1 >= 0
                             && board.get(i).charAt(j - 1) == '.') ||
                         (x == i + 1 && y == j - 1 && i - 1 >= 0 && j + 1 < BOARD_SIZE
                             && board.get(i - 1).charAt(j + 1) == '.') ||
                         (x == i + 1 && y == j && i - 1 >= 0
                             && board.get(i - 1).charAt(j) == '.') ||
                         (x == i + 1 && y == j + 1 && i - 1 >= 0 && j - 1 >= 0
                             && board.get(i - 1).charAt(j - 1) == '.')){
                         cnt++;
                     }
                 }
            }
        }
        return cnt;
    }
    // Get the Score of leaf Nodes
    private int utilityFunction(Node node, char myColor){
        ArrayList<String> board = node.getMe();
        int myNewLoc_x = 0, myNewLoc_y= 0, myOldLoc_x = 0, myOldLoc_y = 0, oldJumpsCount = 0, newJumpsCount = 0;
        if(utilityFunctionFlag){
            myNewLoc_x = node.getMoves().get(node.getMoves().size() - 1).getFrom().getX();
            myNewLoc_y = node.getMoves().get(node.getMoves().size() - 1).getFrom().getY();
            myOldLoc_x = node.getMoves().get(0).getTo().getX();
            myOldLoc_y = node.getMoves().get(0).getTo().getY();
        }
        int result = 0;
        Pair destinationPair;
        char op_color;
        if(myColor == 'W'){
            destinationPair = new Pair(0, 0);
            op_color = 'B';
        } else {
            destinationPair = new Pair(15, 15);
            op_color = 'W';
        }
        if(utilityFunctionFlag){
            oldJumpsCount = jumpsOppositePlayerCanMake(board, myColor,op_color, myOldLoc_x, myOldLoc_y);
            newJumpsCount = jumpsOppositePlayerCanMake(board, myColor, op_color, myNewLoc_x, myNewLoc_y);
        }
        for(int i = 0; i< BOARD_SIZE; i++){
                for(int j = 0; j< BOARD_SIZE ; j++){
                    if (board.get(i).charAt(j) == myColor){
                        result += Math.sqrt(Math.pow((destinationPair.getX() - i), 2) + 
                                    Math.pow((destinationPair.getY() - j), 2));
                    }
                }
            }
        if(utilityFunctionFlag){
            result += (newJumpsCount - oldJumpsCount)*2 ;
        }
        return result;
    }
    // Check if the current board is game winning board
    private Boolean isGameWon(ArrayList<String> board, char myColor){
        int opp_start, opp_end, opp_sum;
        char opp_color;
        if(myColor == 'W'){
            opp_start = 0; opp_end = 5; opp_sum = 6; opp_color = 'B';
        } else {
            opp_start = 11; opp_end = BOARD_SIZE; opp_sum = 24; opp_color = 'W';
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
    // Get how many points are in my camp
    private HashSet<ArrayList<Integer>> getAllCampPointLocations(ArrayList<String> board, char myColor, int start, int end, int sum){
        HashSet<ArrayList<Integer>> hashSet = new HashSet();
        for(int i = start; i < end; i++){
            for(int j = start; j < end; j++){
                if (((sum == 6 && i + j < sum) || (sum == 24 && i + j > sum)) && (myColor == board.get(i).charAt(j))){
                    ArrayList<Integer> temp = new ArrayList();
                    temp.add(i);
                    temp.add(j);
                    hashSet.add(temp);
                }
            }
        }
        return hashSet;
    }
    // Check if the two sets are true according to rule 1b
    private Boolean checkValidity(HashSet<ArrayList<Integer>> orig_set, HashSet<ArrayList<Integer>> t_set, char myColor){
        int m = orig_set.size();
        int n = t_set.size();
        if(m != n){
            return false;
        }
        HashSet<ArrayList<Integer>> temp = new HashSet<>(orig_set);
        HashSet<ArrayList<Integer>> temp1 = new HashSet<>(orig_set);
        temp1.removeAll(t_set);
        t_set.removeAll(temp);
        int[] orig_arr = new int[2];
        int[] t_arr = new int[2];
        if(temp1.size() != 1 || t_set.size() != 1){
            return false;
        }
        for(ArrayList<Integer> i : temp1){
            orig_arr[0] = i.get(0);
            orig_arr[1] = i.get(1);
        }
        for(ArrayList<Integer> i : t_set){
            t_arr[0] = i.get(0);
            t_arr[1] = i.get(1);
        }
        return (myColor == 'B' && orig_arr[0] + orig_arr[1] < t_arr[0] + t_arr[1]) || 
                (myColor == 'W' && orig_arr[0] + orig_arr[1] > t_arr[0] + t_arr[1]);
    }
    // Get all the valid moves for the current board
    private ArrayList<Node> getAllValidChildren(ArrayList<String> parentBoard, char myColor, int moveType) throws IOException{
        ArrayList<Node> children = new ArrayList<>();
        int start, end, sum;
        int opp_start, opp_end, opp_sum;
        if(myColor == 'W'){
            start = 11; end = BOARD_SIZE; sum = 24;
            opp_start = 0; opp_end = 5; opp_sum = 6;
        } else {
            start = 0; end = 5; sum = 6;
            opp_start = 11; opp_end = BOARD_SIZE; opp_sum = 24;
        }
        HashSet<ArrayList<Integer>> orig_set = getAllCampPointLocations(parentBoard, myColor, start, end, sum);
        int count_mycamp = orig_set.size();
        HashSet<ArrayList<Integer>> opp_campMyPoints = getAllCampPointLocations(parentBoard, myColor, opp_start, opp_end, opp_sum);
        int count_oppcamp = opp_campMyPoints.size();
        // If there exists a point in my base camp that can be moved out of base then get those moves
        for(int i = start; i < end; i++){
            for(int j = start; j < end; j++){
                if (((sum == 6 && i + j < sum) || (sum == 24 && i + j > sum)) && (myColor == parentBoard.get(i).charAt(j))){
                    ArrayList<Node> temp = new ArrayList<>();
                    temp.addAll(getSingleMovesFromCurrentPlayer(parentBoard, i, j));
                    visitedInHops = new int[BOARD_SIZE][BOARD_SIZE];
                    visitedInHops[i][j] = 1;
                    temp.addAll(getJumpMovesFromCurrentPlayer(parentBoard, myColor, i, j, new ArrayList<>()));
                    for(Node node : temp){
                        int t_count_mycamp = getAllCampPointLocations(node.getMe(), myColor, start, end, sum).size();
                        if(t_count_mycamp < count_mycamp){
                            if(moveType == 1){
                                putData(node);
                                System.exit(0);
                            }
                            children.add(node);
                        }
                    }
                }
            }
        }
        // If the first one has no moves and move a point in further away from my base 
        if(children.isEmpty()){
            for(int i = start; i < end; i++){
                for(int j = start; j < end; j++){
                    if (((sum == 6 && i + j < sum) || (sum == 24 && i + j > sum)) && (myColor == parentBoard.get(i).charAt(j))){
                        ArrayList<Node> temp = new ArrayList<>();
                        temp.addAll(getSingleMovesFromCurrentPlayer(parentBoard, i, j));
                        visitedInHops = new int[BOARD_SIZE][BOARD_SIZE];
                        visitedInHops[i][j] = 1;
                        temp.addAll(getJumpMovesFromCurrentPlayer(parentBoard, myColor, i, j, new ArrayList<>()));
                        for(Node node : temp){
                            HashSet<ArrayList<Integer>> t_set = getAllCampPointLocations(node.getMe(), myColor, start, end, sum);
                            if(checkValidity(orig_set, t_set, myColor)){
                                if(moveType == 1){
                                    putData(node);
                                    System.exit(0);
                                }
                                children.add(node);
                            }
                        }
                    }
                }
            }
        }
        // If there was no possible move for the base camp points then check for points outside the base camp
        if(children.isEmpty()){
            for(int i = 0; i < BOARD_SIZE; i++){
                for(int j = 0; j < BOARD_SIZE; j++){
                    if(!((myColor == 'W' && (i >= 11 && j >= 11) && (i + j) > 24) ||
                            (myColor == 'B' && (i <= 4 && j <= 4) && (i + j) < 6)) && (myColor == parentBoard.get(i).charAt(j))){
                        ArrayList<Node> temp = new ArrayList<>();
                        temp.addAll(getSingleMovesFromCurrentPlayer(parentBoard, i, j));
                        visitedInHops = new int[BOARD_SIZE][BOARD_SIZE];
                        visitedInHops[i][j] = 1;
                        temp.addAll(getJumpMovesFromCurrentPlayer(parentBoard, myColor, i, j, new ArrayList<>()));
                        for(Node node : temp){
                            int t_count_mycamp = getAllCampPointLocations(node.getMe(), myColor, start, end, sum).size();
                            int t_count_oppcamp = getAllCampPointLocations(node.getMe(), myColor, opp_start, opp_end, opp_sum).size();
                            int temp_flag = 0;
                            if(t_count_mycamp == count_mycamp && t_count_oppcamp >= count_oppcamp){
                                if(moveType == 1){
                                    putData(node);
                                    System.exit(0);
                                }
//                                if(count_oppcamp > 15){
//                                    System.out.println("Hello");
//                                    for(ArrayList<Integer> x : opp_campMyPoints){
//                                        if((node.getMoves().get(0).getFrom().getX() == x.get(0)) &&
//                                                (node.getMoves().get(0).getFrom().getY() == x.get(1))){
//                                            temp_flag = 1;
//                                            break;
//                                        }
//                                    }
//                                    if(temp_flag == 0){
//                                        children.add(node);
//                                    }
//                                } else{
                                    children.add(node);
//                                }
                            }
                        }
                    } 
                }
            }
        }
        return children;
    }
    // Single Moves that will move a point out of base camp
    private ArrayList<Node> getSingleMovesFromCurrentPlayer(ArrayList<String> board, int location_x, int location_y){
        ArrayList<Node> children = new ArrayList<>();
        for(int i = location_x - 1; i <= location_x + 1; i++){
            for(int j = location_y - 1; j <= location_y + 1; j++){
                if(i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE){
                    if (board.get(i).charAt(j) == '.'){
                        ArrayList<Move> moves = new ArrayList<>();
                        moves.add(new Move(new Pair(location_x, location_y), new Pair(i, j)));
                        children.add(new Node(null, makeNewBoard(board, location_x, 
                                location_y, i, j), new ArrayList<>(), 0, 0, moves, 'E'));
                    }
                }
            }
        }
        return children;
    }
    // Jumps that will move a point out of base camp
    private ArrayList<Node> getJumpMovesFromCurrentPlayer(ArrayList<String> board, char color, 
            int location_x, int location_y, ArrayList<Node> children){
        for(int i = location_x - 1; i <= location_x + 1; i++){
            for(int j = location_y - 1; j <= location_y + 1; j++){
                if(i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE){
                    if (board.get(i).charAt(j) != '.'){
                            if ((i == location_x - 1 && j == location_y - 1) && 
                                    (i - 1 >= 0 && j - 1 >= 0 && board.get(i - 1).charAt(j - 1) == '.' && visitedInHops[i - 1][j - 1] == 0)){
                                visitedInHops[i - 1][j - 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j - 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i - 1, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j - 1, children);
                                
                            } else if ((i == location_x - 1 && j == location_y) && (i - 1 >= 0 && board.get(i - 1).charAt(j) == '.' && visitedInHops[i - 1][j] == 0)){
                                visitedInHops[i - 1][j] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i - 1, j), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j, children);
                            } else if ((i == location_x - 1 && j == location_y + 1) && (i - 1 >= 0 && j + 1 < BOARD_SIZE && board.get(i - 1).charAt(j + 1) == '.' && visitedInHops[i - 1][j + 1] == 0)){
                                visitedInHops[i - 1][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j + 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i - 1, j + 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j + 1, children);
                            } else if ((i == location_x && j == location_y - 1) && (j - 1 >= 0 && board.get(i).charAt(j - 1) == '.' && visitedInHops[i][j - 1] == 0)){
                                visitedInHops[i][j - 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i, j - 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i, j - 1, children);
                            } else if ((i == location_x && j == location_y + 1) && (j + 1 < BOARD_SIZE && board.get(i).charAt(j + 1) == '.' && visitedInHops[i][j + 1] == 0)){
                                visitedInHops[i][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i, j + 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i, j + 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i, j + 1, children);
                            } else if ((i == location_x + 1 && j == location_y - 1) && (i + 1 < BOARD_SIZE && j - 1 >= 0 && board.get(i + 1).charAt(j- 1) == '.' && visitedInHops[i + 1][j - 1] == 0)){
                                visitedInHops[i + 1][j - 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j - 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i + 1, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i + 1, j - 1, children);
                            } else if ((i == location_x + 1 && j == location_y) && (i + 1 < BOARD_SIZE && board.get(i + 1).charAt(j) == '.' && visitedInHops[i + 1][j] == 0)){
                                visitedInHops[i + 1][j] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i + 1, j), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i + 1, j, children);
                            } else if ((i == location_x + 1 && j == location_y + 1) && (i + 1 < BOARD_SIZE && j + 1 < BOARD_SIZE && board.get(i + 1).charAt(j + 1) == '.' && visitedInHops[i + 1][j + 1] == 0)){
                                visitedInHops[i + 1][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = (ArrayList) children.get(a).getMoves().clone();
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j + 1)));
                                int par_x = moves.get(0).getFrom().getX();
                                int par_y = moves.get(0).getFrom().getY();
                                Node node = new Node(null, makeNewBoard(board, par_x, par_y, i + 1, j + 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i + 1, j + 1, children);
                            }
                    }
                }
            }
        }
        return children;
    }
    // Find the best possible move
    private int minMax(Node root, Boolean isMaxPlayer, int alpha, int beta){
        ArrayList<Node> children = root.getChildren();
        if (children.isEmpty()){
            return root.getScore();
        }
        if(isMaxPlayer){
            int best = MIN;
            for(int i = 0; i < children.size(); i++){
                int val = minMax(children.get(i), false, alpha, beta);
                best = Math.max(best, val);
                if(best >= beta){
                    root.setScore(best);
                    return best;
                }
                alpha = Math.max(alpha, best);
            }
            root.setScore(best);
            return best;
        } else {
            int best = MAX;
            for(int i = 0; i < children.size(); i++){
                int val = minMax(children.get(i), true, alpha, beta);
                best = Math.min(best, val);
                if(best <= alpha){
                    root.setScore(best);
                    return best;
                }
                beta = Math.min(beta, best);
            }
            root.setScore(best);
            return best;
        }
    }
    // Get the next best move from the current board
    public Node getNextBestMove(InputData dataObj, int max_depth) throws IOException{
        Node root = new Node(null, dataObj.getBoard(), new ArrayList<>(), 0, 1, null, 'X');
        char myColor = dataObj.getColor();
        ArrayList<Node> leafNode = new ArrayList();
        int choose_score;
        Queue<Node> queue = new LinkedList();
        queue.add(root);
        while(queue.size() > 0){
            Node cur = queue.remove();
            if(cur.getLevel() >= max_depth){
                break;
            }
            ArrayList<Node> children = getAllValidChildren(cur.getMe(), myColor, dataObj.getMoveType());
            for(int i = 0; i < children.size(); i++){
                children.get(i).setLevel(cur.getLevel() + 1);
                children.get(i).setParent(cur.getMe());
                cur.addChildren(children.get(i));
                queue.add(children.get(i));
                if (cur.getLevel() == 1 && isGameWon(children.get(i).getMe(), myColor)){
                    putData(children.get(i));
                    System.exit(0);
                }
            }
       }
        if(root.getChildren().size() > 0){
            // For each leaf node call the utility function
            queue = new LinkedList<>();
            queue.add(root);
            while(queue.size() > 0){
                Node cur = queue.remove();
                ArrayList<Node> children = cur.getChildren();
                if(children.size() > 0){
                    for(int i = 0; i< children.size(); i++){
                        queue.add(children.get(i));
                    }
                } else{
                    leafNode.add(cur);
                }
            }
            for(int i = 0; i < leafNode.size(); i++){
                leafNode.get(i).setScore(utilityFunction(leafNode.get(i), myColor));
            }
            // Run min max to get the next best move
            choose_score = minMax(root, false, MIN, MAX);
            // Return this move (one or set of moves)
            leafNode = new ArrayList(root.getChildren());
            for(int i = 0; i < leafNode.size(); i++){
                if (leafNode.get(i).getScore() == choose_score){
                    return leafNode.get(i);
                }
            }
        }
        return null; // If unable to find anymove
    }
    // Write the output to a file
    private void putData(Node result) throws IOException{
        try (Writer bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("output.txt"), "utf-8"))) {
            if(result == null){
                bw.write("No Possible Move");
            } else {
                char typeOfMove = result.getMoveTypeToReachHere();
                ArrayList<Move> mv = result.getMoves();
                for(int i = 0; i< mv.size(); i++){
                    bw.write(typeOfMove + " " + mv.get(i).getFrom().getY() + "," + mv.get(i).getFrom().getX() +
                            " " + mv.get(i).getTo().getY() + "," + mv.get(i).getTo().getX());
                    if(i < mv.size() - 1){
                        bw.write("\n");
                    }
                }
            }
        }
    }
    
    public static void main(String args[]) throws IOException {
        homework hw = new homework();
        InputData dataObj = hw.getData();
        int max_depth = hw.getMaxDepth(dataObj.getTimeLeft(), dataObj.getMoveType());
        Node result = hw.getNextBestMove(dataObj, max_depth);
        hw.putData(result);
    }
}