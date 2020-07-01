/**
 * Write a description of class GameController here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
import java.io.*;

public class GameController {
    //default settings, defaulted to if there's a problem with file reading
    static boolean humanPlayer = false;
    static boolean randomRounds = true;
    static boolean tournamentMode = false;
    static boolean silent = false;
    static int rounds = 5;
    static int tournamentPlayers = 10;
    static Player opponent = Player.genPlayer(true);
    static HashMap<String, String> settings = new HashMap<String,String>();
    

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        readOptions();
        System.out.println(rounds);
        if(!tournamentMode){
            if(humanPlayer == true){
               //If its a human player make sure to name them first
                
               System.out.println("Please enter name:");
               String name = input.nextLine();
               startGame(new HumanPlayer(name), opponent, rounds);
            }else{
                //if non human, always randomise both players.
               startGame(Player.genPlayer(false), Player.genPlayer(false), rounds);
            }
        } else{
            
            //this generates a list of players for the tournament round to use.
            ArrayList<Player> players  = new ArrayList<Player>();
            for(int i=0; i<tournamentPlayers; i++){
                players.add(i, Player.genPlayer(false));
            }
            playTournamentRound(players);
        }
    }
    
    
    //This method resets and counts down the rounds until the end.
    static void startGame(Player p1, Player p2, int rounds){
        if(!silent)System.out.println("Game is " + p1.getType() + " vs " + p2.getType() + ".");
        //make sure to reset actions each game
        p1.actions = new ArrayList<Boolean>();
        p2.actions = new ArrayList<Boolean>();
        int round = 0;
        if(randomRounds == true) rounds = new Random().nextInt(10);
        for(int i = 0; i<rounds; i++){
            playRound(p1, p2, round);
            round++;
        }
        System.out.println("Game over.");
        
        //print who won
        if(p1.score > p2.score){
            System.out.println(p1.getType() + " won!");
        } else if (p2.score > p1.score){
            System.out.println(p2.getType() + " won!");
        }
    }
    
    
    //this method checks what each player does and scores them acccordingly
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
                if(!silent){
                    System.out.print(p1.getType() + " Betrayed, but ");
                    if(humanPlayer){System.out.println("The Opponent did not");}  else {System.out.println(p2.getType() + " did not.");}
                }
            }
        }else{
            if(p2betray){
                //if p2 betrays
                p1.score -= 5;
                p2.score += 2;
                if(!silent){
                    if(humanPlayer){System.out.print("The Opponent");} else{System.out.print(p2.getType());}
                    System.out.println(" Betrayed, but " + p1.getType() + " did not.");
                }
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
    
    //This method makes sure that all the players fight each other once, and only once
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
        //rank them.
        
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
            Boolean randomOpponent = true;
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
            silent = !silent;
            System.out.println("verbose :" + silent);
            if(settings.get("randomOpponent") != null) randomOpponent = Boolean.parseBoolean(settings.get("randomOpponent"));
            System.out.println("randomOpponent :" + randomOpponent);
            if(settings.get("opponentType") != null && randomOpponent){
                opponent = Player.genPlayerFromString(settings.get("opponentType"));
                System.out.println("opponent :" + opponent);
            }
        }catch (IOException e){
            System.out.println("Error reading config: " + e.toString());
        }
        
    }
}