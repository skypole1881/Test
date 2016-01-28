package foop;
import java.util.*; 
public class PlayerB00201011 extends Player{
	private boolean dealBJ = true;
	private int nowBet;
	public PlayerB00201011(int chip){
		super(chip);

	}
	public boolean	buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		return false ;
	}
	//Ask whether the player wants to buy insurance.
	public boolean	do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if (get_chips() <=5) {
			return false;
		}
		if(sum(my_open)==9){
			if (dealer_open.getValue()>=3 && dealer_open.getValue() <=6) {
				return true;
			}
		}
		if(sum(my_open)==10){
			if (dealer_open.getValue()>=2 && dealer_open.getValue() <=9) {
				return true;
			}
		} 
		if(sum(my_open)==11){
			if (dealer_open.getValue()>=2 && dealer_open.getValue() <=10) {
				return true;
			}
		}
		if(my_open.getCards().get(0).getValue() == 1 || my_open.getCards().get(1).getValue() == 1) {
			if (sum(my_open) == 13 || sum(my_open) ==14){
				if (dealer_open.getValue()>=5 && dealer_open.getValue() <=6) {
					return true;
				}
			}
			if (sum(my_open) == 15 || sum(my_open) ==16){
				if (dealer_open.getValue()>=4 && dealer_open.getValue() <=6) {
					return true;
				}	
			}
			if (sum(my_open) == 17 ||sum(my_open) ==18){
				if (dealer_open.getValue()>=3 && dealer_open.getValue() <=6) {
					return true;
				}	
			}
		}
		return false ;
	}
	//Ask whether the player wants to double down.
	public boolean	do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if (get_chips() <=5) {
			return false;
		}
		if(my_open.get(0).getValue() == 1) {
			return true;
		}
		if(my_open.get(0).getValue() == 2) {
			if (dealer_open.getValue() <= 7 && dealer_open.getValue() >=2)
				return true;
			return false;
		}
		if(my_open.get(0).getValue() == 3) {
			if (dealer_open.getValue() <= 7 && dealer_open.getValue() >=2)
				return true;
			return false;
		}
		if(my_open.get(0).getValue() == 4) {
			if(dealer_open.getValue() ==5 || dealer_open.getValue()==6)
				return true;
			return false;
		}
		if(my_open.get(0).getValue() == 5) {
			return false;
		}
		if(my_open.get(0).getValue() == 6) {
			if (dealer_open.getValue() >=2 && dealer_open.getValue() <=6)
				return true;
			return false;
		}
		if(my_open.get(0).getValue() == 7) {
			if (dealer_open.getValue() <= 7 && dealer_open.getValue() >=2)
				return true;
			return false;
		}
		if(my_open.get(0).getValue() == 8) {
			return true;
		}
		if(my_open.get(0).getValue() == 9) {
			if (dealer_open.getValue()==7)
				return false;
			return true;
		}
		return false ;
	}
	//Ask whether the player wants to do split.
	public boolean	do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		boolean dealBJ = false;
		return false ;
	}
	//Ask whether the player wants to do surrender.
	public boolean	hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){
		if( sum(my_open) >=17) {
			return false ;	
		} 
		if (sum(my_open)>= 5 &&  sum(my_open) <=11){
			return true;
		} 
		if (sum(my_open) == 12) {
			if (dealer_open.getValue() >=4 && dealer_open.getValue() <=6 ){
				return false;
			} else{
				return true;
			}
		}
		if( sum(my_open) <=16 || sum(my_open) >=11){
			if (dealer_open.getValue() >=2 && dealer_open.getValue() <=6 ){
				return false;
			} else{
				return true;
			}	
		}

		return false ;
	}
	//Ask whether the player wants to hit.
	public int	make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position){
		boolean dealBJ = true;
		int nowBet = 5;
		return 5;
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
/*	
	abstract boolean	buy_insurance(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){}
	//Ask whether the player wants to buy insurance.
	void	decrease_chips(double diff){}
	//Decrease the number of chips by diff.
	abstract boolean	do_double(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){}
	//Ask whether the player wants to double down.
	abstract boolean	do_split(java.util.ArrayList<Card> my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){}
	//Ask whether the player wants to do split.
	abstract boolean	do_surrender(Card my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){}
	//Ask whether the player wants to do surrender.
	protected double	get_chips(){}
	//Get the chips on hand.
	abstract boolean	hit_me(Hand my_open, Card dealer_open, java.util.ArrayList<Hand> current_table){}
	//Ask whether the player wants to hit.
	void	increase_chips(double diff){}
	//Increase the number of chips by diff.
	abstract int	make_bet(java.util.ArrayList<Hand> last_table, int total_player, int my_position){}
	//Get the number of chips that the player wants to bet.
	abstract java.lang.String	toString(){}
	//Show the player's status.
*/