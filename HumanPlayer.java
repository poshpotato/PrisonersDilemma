//the HumanPlayer class functions almost the same as a normal player, except that its checkBetray method uses user input to determine its result.
import java.util.Scanner;
public class HumanPlayer extends Player
{
    String name;
    Scanner input;
    
    //unlike other players, HumanPlayer gets a custom name and a direct connection to System.in.
    public HumanPlayer(String name){
        this.name = name;
        this.input = new Scanner(System.in);
    }
    
    Boolean checkBetray(Player opponent, int round) {
        System.out.println(name + ", Will you betray? Y/N");
        String choice = input.nextLine().toLowerCase();
        if(choice.equals("y")){
            return true;
        } else if(choice.equals("n")){
            return false;
        } else{
            System.out.println("Error: Please enter only y or n");
            return this.checkBetray(opponent, round);
        }
    }
    
    String getType(){return name;}
}
