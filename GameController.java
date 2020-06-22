
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
    static boolean silent = false;
    static int rounds = 5;
    static int tournamentPlayers = 10;
    static Player opponent;
    static HashMap<String, String> settings = new HashMap<String,String>();
    

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Player.init();
        readOptions();
        System.out.println(rounds);
        if(!tournamentMode){
            
            if(humanPlayer == true && opponent == null){
                System.out.println("Please enter name:");
                String name = input.nextLine();
                if(  !=null){
                    try{
                        
                    }
                }
                startGame(new HumanPlayer(name), Player.genPlayer(), rounds);
            }else{
                for(int i = 0; i<1; i++){
                    startGame(Player.genPlayer(), Player.genPlayer(), rounds);
                }
            }
        } else{
            ArrayList<Player> players  = new ArrayList<Player>();
            for(int i=0; i<tournamentPlayers; i++){
                players.add(i, Player.genPlayer());
            }
            playTournamentRound(players);
        }
    }

    static void startGame(Player p1, Player p2, int rounds){
        if(!silent)System.out.println("Game is " + p1.getType() + " vs " + p2.getType() + ".");
        //make sure to reset actions each game
        p1.actions = new ArrayList<Boolean>();
        p2.actions = new ArrayList<Boolean>();
        int round = 0;
        if(randomRounds != null) rounds = new Random().nextInt(10);
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
                if(!silent)System.out.println("Both players betrayed.");
            } else{
                //if p1 betrays
                p1.score += 2;
                p2.score -= 5;
                if(!silent)System.out.println(p1.getType() + " Betrayed, but " + p2.getType() + " did not.");
            }
        }else{
            if(p2betray){
                //if p2 betrays
                p1.score -= 5;
                p2.score += 2;
                if(!silent)System.out.println(p2.getType() + " Betrayed, but " + p1.getType() + " did not.");
            } else{
                //if neither betray
                p1.score += 1;
                p2.score += 1;
                if(!silent)System.out.println("Both players co-operated.");
            }
        }
        if(!silent)System.out.println(p1.getType() + " Score = " + p1.score + ".");
        if(!silent)System.out.println(p2.getType() + " Score = " + p2.score + ".");
    }
    
    static void playTournamentRound(ArrayList<Player> players){
        System.out.println("actual players :" + players.size());
        for(int i=0; i<players.size(); i++){
            for(int j=0; j<players.size(); j++){
                //is the player ahead of me?
                if(!(j <= i)){
                    //if so, fight them
                    System.out.println("player "+i+" against player "+j );
                    startGame(players.get(i), players.get(j), rounds);
                }
                //if not, don't.
            }
        }
        //after round is played, print results.
        //but first: sorting. selection sort seems suited.
        /*run it a number of times equal to the number of elements
        ArrayList<Player> sortedPlayers = new ArrayList<Player>();
        for (int i = 0; i < players.size() - 1; i++)  
        {  
            int index = i;  
            for (int j = i + 1; j < players.size(); j++){  
                if (players.get(j).score < players.get(index).score){  
                    index = j;//searching for lowest index  
                }  
            }  
            Player smallerScore = players.get(index);   
            players.set(index, players.get(i));  
            players.set(i, smallerScore);  
        }*/
        //Bubble sort
        for(int i = 0; i < players.size(); i++){
            for(int j = 1; j < players.size()-i; j++){
                if(players.get(j).score > players.get(j-1).score){
                    Player temp = players.get(j);
                    players.set(j, players.get(j-1));
                    players.set(j-1, temp);
                }
            }
        }
        for(Player p:players){
            System.out.println(p.getType() + ", " + p.score);
        }
    }
    
    static void readOptions(){
        File file = new File("config.txt");
        try{
            Scanner fileRead = new Scanner(file);
            while(fileRead.hasNextLine()){
                String[] chunkedLine = fileRead.nextLine().split(":");
                System.out.println(Arrays.toString(chunkedLine));
                if(chunkedLine[0].charAt(0) != '#'){
                    settings.put(chunkedLine[0], chunkedLine[1]);
                }
            }
            if(settings.get("rounds") != null)rounds = Integer.parseInt(settings.get("rounds"));
            System.out.println("Rounds: " +rounds);
            if(settings.get("humanPlayer") != null)humanPlayer = Boolean.parseBoolean(settings.get("humanPlayer"));
            System.out.println("humanPlayer: " +humanPlayer);
            if(settings.get("randomRounds") != null) randomRounds = Boolean.parseBoolean(settings.get("randomRounds"));
            System.out.println("randomRounds :" + randomRounds);
            
            if(settings.get("tournamentMode") != null) tournamentMode = Boolean.parseBoolean(settings.get("tournamentMode"));
            System.out.println("tournamentMode :" +tournamentMode);
            if(settings.get("tournamentPlayers") != null)  tournamentPlayers = Integer.parseInt(settings.get("tournamentPlayers"));
            System.out.println("tournamentPlayers :" +tournamentPlayers);
            if(settings.get("verbose") != null) silent = Boolean.parseBoolean(settings.get("verbose"));
            System.out.println("verbose :" + silent);
            if(settings.get("opponentType" != null)) opponent = Player.genPlayerFromString(settings.get("opponentType"));
        }catch (IOException e){
            System.out.println("Error reading config: " + e.toString());
        }
        
    }
}