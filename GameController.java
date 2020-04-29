
/**
 * Write a description of class GameController here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;

public class GameController {
    static Scanner input = new Scanner(System.in);
    static boolean humanPlayer = false;
    static boolean randomRounds = true;
    static boolean tournamentMode = false;
    static int rounds = 5;
    static Player opponent;
    static HashMap<String, String> settings = new HashMap<String,String>();
    

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Player.init();
       
        readOptions();
        System.out.println(rounds);
        if(!tournamentMode){
            if(humanPlayer == true){
                System.out.println("Please enter name:");
                String name = input.nextLine();
                startGame(new HumanPlayer(name), Player.genPlayer(), rounds);
            }else{
                for(int i = 0; i<1; i++){
                    startGame(Player.genPlayer(), Player.genPlayer(), rounds);
                }
            }
        } else{
            
        }
    }

    static void startGame(Player p1, Player p2, int rounds){
        System.out.println("Game is " + p1.getType() + " vs " + p2.getType() + ".");
        int round = 0;
        for(int i = 0; i<rounds; i++){
            playRound(p1, p2, round);
            round++;
        }
    }

    static void playRound(Player p1, Player p2, int round){
        Boolean p1betray = p1.checkBetray(p2, round);
        Boolean p2betray = p2.checkBetray(p1, round);
        
        //Make sure lastAction is set AFTER they decide whether to betray
        p1.actions.add(p1betray);
        p2.actions.add(p2betray);
        
        
        if(p1betray){
            if(p2betray){
                //if both betray
                p1.score -= 3;
                p2.score -= 3;
                System.out.println("Both players betrayed.");
            } else{
                //if p1 betrays
                p1.score += 2;
                p2.score -= 5;
                System.out.println(p1.getType() + " Betrayed, but " + p2.getType() + " did not.");
            }
        }else{
            if(p2betray){
                //if p2 betrays
                p1.score -= 5;
                p2.score += 2;
                System.out.println(p2.getType() + " Betrayed, but " + p1.getType() + " did not.");
            } else{
                //if neither betray
                p1.score += 1;
                p2.score += 1;
                System.out.println("Both players co-operated.");
            }
        }
        System.out.println(p1.getType() + " Score = " + p1.score + ".");
        System.out.println(p2.getType() + " Score = " + p2.score + ".");
    }
    
    static void playTournamentRound(){
        ArrayList<Player> players  = new ArrayList<Player>();
        
    }
    
    static void readOptions(){
        File file = new File("config.txt");
        try{
            Scanner fileRead = new Scanner(file);
            while(fileRead.hasNextLine()){
                String[] chunkedLine = fileRead.nextLine().split(":");
                if(chunkedLine[0].charAt(0) != '#'){
                    settings.put(chunkedLine[0], chunkedLine[1]);
                }
            }
            if(settings.get("rounds") != null)rounds = Integer.parseInt(settings.get("rounds"));
            System.out.println(rounds);
            if(settings.get("humanPlayer") != null)humanPlayer = Boolean.parseBoolean(settings.get("humanPlayer"));
            System.out.println(humanPlayer);
            if(settings.get("randomRounds") != null) randomRounds = Boolean.parseBoolean(settings.get("randomRounds"));
            System.out.println(randomRounds);
        }catch (IOException e){
            System.out.println("Error reading config: " + e.toString());
        }
        
    }
}