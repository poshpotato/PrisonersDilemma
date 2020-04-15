
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    int score = 0;
    String getType(){return "bananapotato";};
    Boolean lastAction; //so opponent can respond.
    
    Player(){
    
    }
    //
    Player(Player toCopy){
        this.score = toCopy.score;
        this.lastAction = toCopy.lastAction;
    }
    

    static List<Player> playerList = new ArrayList<Player>();
    static void init() {
        playerList.add(new Cheater());
        playerList.add(new Cooperator());
        playerList.add(new Copycat());
    }
    Boolean checkBetray(Player opponent){ //will override.
        return true;
    }

    static Player genPlayer(){
        //this function should return a random-strategy player for use in the game
        Player copy = new Player();
        switch (new Random().nextInt(playerList.size())){
            case 0 : 
                copy = new Cheater();
                break;
            case 1 : 
                copy = new Cooperator();
                break;
            case 2 : 
                copy = new Copycat();
                break;
                
        }
        
        return copy;
    }
    
    static Player genPlayerFromString(String from){
        Player p = null;
        switch (from.toLowerCase()){
            case "cheater" : 
                p = new Cheater();
                break;
            case "cooperator" :
                p = new Cooperator();
                break;
            case "copycat" :
                p = new Copycat();
                break;
            default :
                System.out.println("Invalid playerType: \"" + from + "\"");
                break;
        }
        return p;
    }
}

class Cheater extends Player {
    Boolean checkBetray(Player opponent) {
        return true; //cheater always cheats;
    }
    String getType(){return "Cheater";};
}

class Cooperator extends Player {
    Boolean checkBetray(Player opponent) {
        return false; //cooperator always cooperates;
    }
    String getType(){return "Cooperator";};
}

class Copycat extends Player {
    Boolean checkBetray(Player opponent) {
        if(opponent.lastAction != null){
            return opponent.lastAction; //copy opponent
        } else {
            return false;
        }
    }
    String getType(){return "Copycat";};

}
