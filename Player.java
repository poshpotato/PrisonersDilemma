
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
    Boolean anonymous = false;
    String getType(){if(!anonymous){return "bananapotato"; }else {return "Opponent";}};
    ArrayList<Boolean> actions;//so opponent can respond.
    Player(){
        this.actions = new ArrayList<Boolean>();
    }
    //
    Player(Player toCopy){
        this.score = toCopy.score;
        this.actions = toCopy.actions;
    }
    
    Boolean checkBetray(Player opponent, int round){ //will override.
        return true;
    }

    static Player genPlayer(Boolean anonymous){
        //this function should return a random-strategy player for use in the game
        Player copy = new Player();
        switch (new Random().nextInt(4)){
            case 0 : 
                copy = new Cheater();
                break;
            case 1 : 
                copy = new Cooperator();
                break;
            case 2 : 
                copy = new Copycat();
                break;
            case 3 :
                copy = new Checker();
                break;
            default :
                copy = Player.genPlayer(anonymous);
        }
        copy.anonymous = anonymous;
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
                
                
                
                
            case "checker" :
                p = new Copycat();
                break;
            default :
                p = Player.genPlayer(false);
                System.out.println("Invalid playerType: \"" + from + "\". Generating random player.");
                break;
        }
        return p;
    }
}

class Cheater extends Player {
    Boolean checkBetray(Player opponent, int round) {
        return true; //cheater always cheats;
    }
    String getType(){if(!anonymous){return "Cheater"; }else {return "Opponent";}};
}

class Cooperator extends Player {
    Boolean checkBetray(Player opponent, int round) {
        return false; //cooperator always cooperates;
    }
    String getType(){if(!anonymous){return "Cooperator"; }else {return "Opponent";}};
}

class Copycat extends Player {
    Boolean checkBetray(Player opponent, int round) {
        if(round != 0){
            return opponent.actions.get(round-1); //copy opponent
        } else {
            return false;
            //always start with good faith.
        }
    }
    String getType(){if(!anonymous){return "bananapotato"; }else {return "Copycat";}};
}
//the checker probes you with a cheat to see whether you'll reciprocate, to decide what it should do.
class Checker extends Player {
    Boolean checkBetray(Player opponent, int round) {
        if(round == 0 || round == 2 || round == 3){
            return false; //testing
        } else if(round == 1) {
            return true;//more testing
        } else if(opponent.actions.contains(true)) {
            return false; //lets try to cooperate
        } else {
            return true; //bleed them for all they're worth
        }
    }
    String getType(){if(!anonymous){return "bananapotato"; }else {return "Opponent";}};
}
