import java.util.ArrayList;
import java.util.Random;

//the default player clas
public class Player {
    int score = 0;
    //used to determine whether actual name should be used e.g. in non-human rounds.
    Boolean anonymous = false;
    
    //returns the name of the player as a string to be used in output
    String getType(){if(!anonymous){return "Player"; }else {return "Opponent";}};
    ArrayList<Boolean> actions;//so opponent can respond.
    
    //constructor, only used for placeholder players.
    Player(){};
    
    //this function is overridden for all strategies.
    Boolean checkBetray(Player opponent, int round){
        return true;
    }
    
    //this function should return a random-strategy player for use in the game. anonymous paremeter is for use in human games.
    static Player genPlayer(Boolean anonymous){
        
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
    
    //this method uses a string to return the corresponding player type, and if invalid, generates a random one. Used for selecting opponents as human.
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

//the two basic strategies, cheater and cooperator.

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

//the first "intelligent" strategy, the copycat starts with cooperation then always copies the opponent.
class Copycat extends Player {
    Boolean checkBetray(Player opponent, int round) {
        if(round != 0){
            return opponent.actions.get(round-1); //copy opponent
        } else {
            return false;
            //always start with good faith.
        }
    }
    String getType(){if(!anonymous){return "Copycat"; }else {return "Opponent";}};
}
//the checker probes you with a cheat to see whether you'll reciprocate, to determine whether it should cheat you.
class Checker extends Player {
    Boolean checkBetray(Player opponent, int round) {
        if(round == 0 || round == 2 || round == 3){
            return false; //testing
        } else if(round == 1) {
            return true;//more testing
        } else if(opponent.actions.contains(true)) {
            return false; //lets try to cooperate
        } else {
            return true; //cheat them, they won't cheat back
        }
    }
    String getType(){if(!anonymous){return "Chceker"; }else {return "Opponent";}};
}
