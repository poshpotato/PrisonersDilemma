
/**
 * Write a description of class GameController here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;

public class GameController {
    static Scanner input = new Scanner(System.in);
    static boolean exit = false;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Player.init();
        startGame(Player.genPlayer(), Player.genPlayer(), pNum);
        //while(!exit){
            //takeCommand();
        //}
    }

    static void startGame(Player p1, Player p2, int rounds){
        System.out.println("Game is " + p1.getType() + " vs " + p2.getType() + ".");
        for(int i = 0; i<rounds; i++){
            playRound(p1, p2);
        }
    }

    static void playRound(Player p1, Player p2){
        Boolean p1betray = p1.checkBetray(p2);
        Boolean p2betray = p2.checkBetray(p1);
        
        //Make sure lastAction is set AFTER they decide whether to betray
        p1.lastAction = p1betray;
        p2.lastAction = p2betray;
        
        
        if(p1betray == true && p2betray == true){
            //if both betray
            p1.score -= 3;
            p2.score -= 3;
            System.out.println("Both players betrayed.");
        } else if(p1betray == true && p2betray == false){
            //if p1 betrays
            p1.score += 2;
            p2.score -= 5;
            System.out.println(p1.getType() + " Betrayed, but " + p2.getType() + " did not.");
        } else if(p1betray == false && p2betray == true){
            //if p2 betrays
            p1.score -= 5;
            p2.score += 2;
            System.out.println(p2.getType() + " Betrayed, but " + p1.getType() + " did not.");
        } else if(p1betray == false && p2betray == false){
            //if neither betray
            p1.score += 1;
            p2.score += 1;
            System.out.println("Both players co-operated.");
        }
        System.out.println(p1.getType() + " Score = " + p1.score + ".");
        System.out.println(p2.getType() + " Score = " + p2.score + ".");
    }
    
    static void takeCommand(){
        printCommandList();
        String command = input.nextLine();
        if(command.charAt(0) == '!'){
            String[] commandChunks = command.substring(1).split(" ");
            switch(commandChunks[0]){
                case "AIgame" :
                    int pNum = Integer.parseInt(commandChunks[1]);
                    
                    try{
                        if(commandChunks[2] != null){
                            if(commandChunks[3] != null){
                                startGame(Player.genPlayerFromString(commandChunks[2]), Player.genPlayerFromString(commandChunks[3]), pNum);
                            } else{
                                startGame(Player.genPlayerFromString(commandChunks[2]), Player.genPlayer(), pNum);
                            }
                        } else {
                        startGame(Player.genPlayer(), Player.genPlayer(), pNum);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid argument. Error = " + e);
                    }
                    break;
                case "singlePlayerGame" :
                    try{
                        startGame(new HumanPlayer(commandChunks[2]), Player.genPlayer(), Integer.parseInt(commandChunks[1]));
                    } catch(Exception e) {
                    
                    }
                    break;
                case "exit" :
                    exit = true;
                    break;
                default :
                    System.out.println("Invalid command");
            }
        } else {
            System.out.println("Please prefix your commands with \"!\"");
        }
        //exit = true;
    }
    
    static void printCommandList(){
        System.out.println("Commands (stuff within [brackets] is a parameter for the command. Stuff between (parentheses) is optional Prefix is \'!\'):");
        System.out.println("1. AIgame [int rounds] (player one type) (player two type)");
        System.out.println("Starts a game between two non player characters that lasts for the inputted number of rounds.");
        System.out.println("2. singlePlayerGame [int rounds] [String playerName]");
        System.out.println("Starts a game between a player and a non player that lasts for the inputted number of rounds.");
    }
}