package homework;

import java.io.*;
import java.util.ArrayList;
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
        // TODO: If very less time is remaining then just return the value till now.
        // Very Less time can be determined based on the games we play.
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
    
    // Change this according to the time
    private int getMaxDepth(){
        return 2;
    }
    // Generate the current state of Board
    private ArrayList<String> makeNewBoard(ArrayList<String> board, int old_x, int old_y, int new_x, int new_y){
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
    // Get the Score of leaf Nodes
    private int utilityFunction(ArrayList<String> board, char myColor){
        int result = 1000;
        Pair destinationPair;
        if(myColor == 'W'){
            destinationPair = new Pair(0, 0);
        } else {
            destinationPair = new Pair(15, 15);
        }
        for(int i = 0; i< BOARD_SIZE; i++){
            for(int j = 0; j< BOARD_SIZE ; j++){
                if (board.get(i).charAt(j) == myColor){
                    result -= Math.sqrt(Math.pow((destinationPair.getX() - i), 2) + 
                            Math.pow((destinationPair.getY() - j), 2));
                }
            }
        }
        return result;
    }
    // Get Euclidian Distance
    private int getEuclidianDistance(ArrayList<String> board, char myColor){
        int result = 0;
        Pair destinationPair;
        if(myColor == 'W'){
            destinationPair = new Pair(0, 0);
        } else {
            destinationPair = new Pair(15, 15);
        }
        for(int i = 0; i< BOARD_SIZE; i++){
            for(int j = 0; j< BOARD_SIZE ; j++){
                if (board.get(i).charAt(j) == myColor){
                    result += Math.sqrt(Math.pow((destinationPair.getX() - i), 2) + 
                            Math.pow((destinationPair.getY() - j), 2));
                }
            }
        }
        return result;
    }
    // Get how many points are in my camp
    private int getTotalCampPoints(ArrayList<String> board, char myColor, int start, int end, int sum){
        int count = 0;
        for(int i = start; i < end; i++){
            for(int j = start; j < end; j++){
                if (((sum == 6 && i + j < sum) || (sum == 24 && i + j > sum)) && (myColor == board.get(i).charAt(j))){
                    count++;
                }
            }
        }
        return count;
    }
    // Get all the valid moves for the current board
    private ArrayList<Node> getAllValidChildren(ArrayList<String> parentBoard, char myColor){
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
        int count_mycamp = getTotalCampPoints(parentBoard, myColor, start, end, sum);
        int count_oppcamp = getTotalCampPoints(parentBoard, myColor, opp_start, opp_end, opp_sum);
        int distance = getEuclidianDistance(parentBoard, myColor);
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
                        int t_count_mycamp = getTotalCampPoints(node.getMe(), myColor, start, end, sum);
                        if(t_count_mycamp < count_mycamp){
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
                            int t_count_mycamp = getTotalCampPoints(node.getMe(), myColor, start, end, sum);
                            int t_distance = getEuclidianDistance(node.getMe(), myColor);
                            if(t_count_mycamp == count_mycamp && t_distance < distance){
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
                            int t_count_mycamp = getTotalCampPoints(node.getMe(), myColor, start, end, sum);
                            int t_count_oppcamp = getTotalCampPoints(node.getMe(), myColor, opp_start, opp_end, opp_sum);
                            if(t_count_mycamp == count_mycamp && t_count_oppcamp == count_oppcamp){
                                children.add(node);
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
                                location_y, i, j), new ArrayList<>(), 0, 0, moves, 'J'));
                    }
                }
            }
        }
        return children;
    }
    // Jumps that will move a point out of base camp
    private ArrayList getJumpMovesFromCurrentPlayer(ArrayList<String> board, char color, 
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
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j - 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i - 1, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j - 1, children);
                                
                            } else if ((i == location_x - 1 && j == location_y) && (i - 1 >= 0 && board.get(i - 1).charAt(j) == '.')){
                                visitedInHops[i - 1][j] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i - 1, j), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j, children);
                            } else if ((i == location_x - 1 && j == location_y + 1) && (i - 1 >= 0 && j + 1 < BOARD_SIZE && 
                                        board.get(i - 1).charAt(j + 1) == '.')){
                                visitedInHops[i - 1][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i - 1, j + 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i - 1, j + 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i - 1, j + 1, children);
                            } else if ((i == location_x && j == location_y - 1) && (j - 1 >= 0 && board.get(i).charAt(j - 1) == '.')){
                                visitedInHops[i][j - 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i, j - 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i, j - 1, children);
                            } else if ((i == location_x && j == location_y + 1) && (j + 1 < BOARD_SIZE && board.get(i).charAt(j + 1) == '.')){
                                visitedInHops[i][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i, j + 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i, j + 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i, j + 1, children);
                            } else if ((i == location_x + 1 && j == location_y - 1) && (i + 1 < BOARD_SIZE && j - 1 >= 0 &&
                                        board.get(i + 1).charAt(j- 1) == '.')){
                                visitedInHops[i + 1][j - 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j - 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i + 1, j - 1), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i + 1, j - 1, children);
                            } else if ((i == location_x + 1 && j == location_y) && (i + 1 < BOARD_SIZE && board.get(i + 1).charAt(j) == '.')){
                                visitedInHops[i + 1][j] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i + 1, j), 
                                                    new ArrayList<>(), 0, 0, moves, 'J');
                                children.add(node);
                                getJumpMovesFromCurrentPlayer(board, color, i + 1, j, children);
                            } else if ((i == location_x + 1 && j == location_y + 1) && (i + 1 < BOARD_SIZE && j + 1 < BOARD_SIZE && 
                                        board.get(i + 1).charAt(j + 1) == '.')){
                                visitedInHops[i + 1][j + 1] = 1;
                                ArrayList<Move> moves = new ArrayList<>();
                                if(children.size() > 0){
                                    for(int a = 0; a < children.size(); a++){
                                        int last = children.get(a).getMoves().size() - 1;
                                        if((children.get(a).getMoves().get(last).getTo().getX()== location_x) &&
                                                (children.get(a).getMoves().get(last).getTo().getY()== location_y)){
                                            moves = new ArrayList<>(children.get(a).getMoves());
                                            break;
                                        }
                                    }
                                 }
                                moves.add(new Move(new Pair(location_x, location_y),new Pair(i + 1, j + 1)));
                                Node node = new Node(null, makeNewBoard(board, location_x, location_y, i + 1, j + 1), 
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
                if(best <= beta){
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
    private Node getNextBestMove(InputData dataObj, int max_depth){
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
            ArrayList<Node> children = getAllValidChildren(cur.getMe(), myColor);
            for(int i = 0; i < children.size(); i++){
                children.get(i).setLevel(cur.getLevel() + 1);
                children.get(i).setParent(cur.getMe());
                cur.addChildren(children.get(i));
                queue.add(children.get(i));
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
                leafNode.get(i).setScore(utilityFunction(leafNode.get(i).getMe(), myColor));
            }
            // Run min max to get the next best move
            choose_score = minMax(root, true, MIN, MAX);
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
        int max_depth = hw.getMaxDepth();
        Node result = hw.getNextBestMove(dataObj, max_depth);
        hw.putData(result);
    }
}
