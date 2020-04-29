
/**
 * Write a description of class HumanPlayer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.Scanner;
public class HumanPlayer extends Player
{
    String name;
    Scanner input;
    /**
     * Constructor for objects of class HumanPlayer
     */
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
