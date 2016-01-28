package foop;
import java.util.*; 
public class PlayerB03204030 extends Player{

	
	
	public PlayerB03204030(int chip){
		super(chip);
	}
	public boolean	buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if(my_open.getValue() > 11)
			return true ;
		else
			return false;
	}	
	//Ask whether the player wants to buy insurance.
	public boolean	do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		return false;

	}
	//Ask whether the player wants to double down.
	public boolean	do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if(my_open.get(0).getValue() > 1 && my_open.get(0).getValue() < 6 ) {
			return true;
		}
		else
			return false ;
	}
	//Ask whether the player wants to do split.
	public boolean	do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		
		return false ;
	}
	//Ask whether the player wants to do surrender.
	public boolean	hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if( sum(my_open) >= 14) {
			return false ;	
		} 
		if (sum(my_open) == 13){
			if (dealer_open.getValue() >=5 && dealer_open.getValue() <= 7 ){
				return false;
			} else{
				return true;
			}
		} 
		if (sum(my_open) == 12) {
			if (dealer_open.getValue() >=4 && dealer_open.getValue() <=6 ){
				return false;
			} else{
				return true;
			}
		}
		if( sum(my_open) <= 11) {
			return true ;	
		} 
		return false;
	}
	//Ask whether the player wants to hit.
	public int	make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position){
		
		return 3;
	}
	//Get the number of chips that the player wants to bet.
	public java.lang.String toString(){
		return String.valueOf(get_chips());
	}
	//Show the player's status.

	private int sum(Hand hand){ 
		int sum = 0 ;
		int index = 0 ; 
		int[] num = new int[hand.getCards().size()] ;
		for (int i = 0 ; i < hand.getCards().size() ; i++) {
			int tmp;
			tmp = hand.getCards().get(i).getValue();
			if (tmp > 10 ) {
				num[i] = 10 ;
			} else if (tmp <=10 && tmp >= 2) {
				num[i] = tmp ;
			} else {
				index++; 
				num[i] = 11;
			}
		}
		for (int i = 0 ; i <hand.getCards().size() ; i++ ) {
			sum = sum + num[i];
		}
		while (sum >21 && index >0 ) {
			sum = sum -10;
		}
		return sum;
	}	

}




