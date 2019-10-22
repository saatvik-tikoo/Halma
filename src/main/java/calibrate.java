/**
 *
 * @author Saatvik
 */
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class calibrate extends homework{
    private long[] timeForEachDepth = new long[3];
    private char myColor = 'W';
    private ArrayList<String> board = new ArrayList<>();
    private void setBoard(){
        board.add("...WBB..........");
        board.add("B...B.B.........");
        board.add("B.BB...W....W...");
        board.add("..BW........W...");
        board.add("BB....WW..W.....");
        board.add("..........BB....");
        board.add("..WB.....W......");
        board.add("....W...WBW.....");
        board.add("..W....W....W...");
        board.add("................");
        board.add("....W...B.......");
        board.add("..W.............");
        board.add("....W...........");
        board.add("..............B.");
        board.add("............B...");
        board.add(".............B..");
    }
    private void setTimeForEachDepth(InputData dataObj) throws IOException{
        homework hw = new homework();
        for(int depth = 0; depth < 3; depth++){
            long startTime = System.currentTimeMillis();
            hw.getNextBestMove(dataObj, depth + 2);
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            timeForEachDepth[depth] = totalTime;
        }
    }
    private void writeToFile() throws UnsupportedEncodingException, IOException {
        try (Writer bw = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("calibration.txt")))) {
            for(int i = 0; i< 3; i++){
                bw.write(timeForEachDepth[i] + "\n");
            }
        }
    }
    public static void main(String[] args) throws IOException {
        calibrate cc = new calibrate();
        cc.setBoard();
        InputData dataObj = new InputData();
        dataObj.setBoard(cc.board);
        dataObj.setColor(cc.myColor);
        dataObj.setMoveType(2);
        dataObj.setTimeLeft(100);
        cc.setTimeForEachDepth(dataObj);
        cc.writeToFile();
    }
}
