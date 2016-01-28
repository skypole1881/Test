package foop;
import java.util.*; 
import java.io.*;


public class BlackJack{

	PrintStream ps = System.out ;
	int startMoney = 1000; 
	int totalPlayer = 4;
	int nRound = 200;
	Card dealerOpen;
	Card dealerClosed;
	ArrayList<Card> dealer_open ;
	Deck deck;
	Player[] player ;
	int[] bet;
	boolean[] split 	;
	boolean[] insurance ;
	boolean[] doubleBet ;
	boolean[] surrender ;
	ArrayList<Integer> splitOwner ;
	ArrayList<Hand> last_table ;
	ArrayList<Hand> current_table ;
	ArrayList<Card> current_closed ;
	public BlackJack(  int _nRound ,int _startMoney, String player0, String player1, String player2, String player3) {
		startMoney = _startMoney ;
		nRound = _nRound;
		deck = new Deck();
		String[] index = new String[4];
		index[0] = player0; 
		index[1] = player1; 
		index[2] = player2; 
		index[3] = player3; 
		player = new Player[totalPlayer];
		current_table = new ArrayList<Hand>();
		for (int i = 0 ;i < totalPlayer ; i++ ) {
			if (index[i].equals("PlayerB00201011")){
				player[i] = new PlayerB00201011(startMoney);
			} else if (index[i].equals("PlayerB03204030")) {
				player[i] = new PlayerB03204030(startMoney);
			}
		}
	}
	public void run() {
		boolean broke = false;
		for (int i = 0 ; i<nRound; i ++) {
			broke = false;
			ps.println("**** Round"+(i+1)+" ****");
			round();
			printResult();
			for (int j = 0 ; j < totalPlayer ; j++) {
				double tmp = Double.parseDouble(player[j].toString() );
				if (tmp < 0){
					broke=true;
				}
			}
			if (broke){
				break;
			}
		}
		if (broke) {
			ps.println("**** Someone is broken ,GameOver! ****");
		} else {
			ps.println("**** All Rounds are finished, Have a nice day ****");	
		}

	}

	private void round() {

		bet = new int[totalPlayer];
		split = new boolean[totalPlayer];
		insurance = new boolean[totalPlayer];
		doubleBet = new boolean[totalPlayer];
		surrender = new boolean[totalPlayer];
		splitOwner = new ArrayList<Integer>();
		last_table = current_table;
		current_closed = new ArrayList<Card>();
		current_table = new ArrayList<Hand>();
		
		stepBetAndDaelCard();
		
		if (dealerOpen.getValue() == Card.VALUE_LOWER)
			stepBuyInsurance();

		if ( !dealerIsBJ() ) {
			stepSurrender();
		}
		stepOpenCard();
		stepPlayerChoose();
		stepDealerExecute();
		stepCompare();
	}

	private boolean dealerIsBJ () {
		if (dealerOpen.getValue() == Card.VALUE_LOWER  && dealerClosed.getValue() >=10 )
			return true;	
		if (dealerClosed.getValue() == Card.VALUE_LOWER  && dealerOpen.getValue() >=10 )
			return true;
		return false;	
	}

	private boolean isBJ( Hand hand) {
		if (hand.getCards().get(0).getValue() == Card.VALUE_LOWER  && hand.getCards().get(1).getValue() >=10 )
			return true;	
		if (hand.getCards().get(1).getValue() == Card.VALUE_LOWER  && hand.getCards().get(0).getValue() >=10 )
			return true;
		return false;
	}

	private void stepBetAndDaelCard() {
		for (int i = 0 ; i< totalPlayer; i++) {
			bet[i] = player[i].make_bet(last_table,totalPlayer,i);
			try{
				player[i].decrease_chips(bet[i]);
			}
			catch(Exception e) {

			}
		}
		for (int i = 0 ; i< totalPlayer; i++) {
			ArrayList<Card> tmp = new ArrayList<Card>();
			if (deck.cards.size() == 0){
				deck = new Deck();
			}
			tmp.add( deck.deal() );
			current_table.add( new Hand(tmp) ) ; 
		}

		if (deck.cards.size() == 0){
			 deck = new Deck();
		}
		dealerOpen = deck.deal();

		for (int i = 0 ; i< totalPlayer; i++) { 
			if (deck.cards.size() == 0){
				deck = new Deck();
			}
			current_closed.add( deck.deal() );
		}

		if (deck.cards.size() == 0){
			deck = new Deck();
		}
		dealerClosed = deck.deal();
	}	

	private void stepBuyInsurance() {
		for (int i = 0 ; i< totalPlayer; i++) {
			insurance[i] =  player[i].buy_insurance(current_table.get(i).getCards().get(0), dealerOpen, current_table);
			if (insurance[i] ) {
				try{
					player[i].decrease_chips(bet[i]/2);
				}
				catch(Exception e) {

				}
			}
		}
	} 

	private void stepSurrender() {
		for (int i = 0 ; i< totalPlayer; i ++) {
			surrender[i] =  player[i].do_surrender(current_table.get(i).getCards().get(0), dealerOpen, current_table);
		}	
	}

	private void stepOpenCard() {
		for (int i = 0 ; i< totalPlayer; i ++) {
			if (!surrender[i]){
				ArrayList<Card> tmpCards; 
				Hand tmpHand ;
				tmpCards = current_table.get(i).getCards();
				tmpCards.add( current_closed.get(i) );
				tmpHand = new Hand(tmpCards);
				current_table.set(i,tmpHand);
			}
		}	
	}

	private void stepPlayerChoose() {
		for (int i = 0 ; i < totalPlayer ;i ++) {
			if ( !surrender[i] 	) {
				if (current_table.get(i).getCards().get(0).getValue() == current_table.get(i).getCards().get(1).getValue() ) {
					split[i] = player[i].do_split(current_table.get(i).getCards(), dealerOpen, current_table) ;
					if (split[i]) {
						ArrayList<Card> tmpSplit = new ArrayList<Card>();
						ArrayList<Card> tmpCards ;
						tmpSplit.add(current_table.get(i).getCards().get(1) );
						if (deck.cards.size() == 0) {
			 				deck = new Deck();
			 			}

						tmpCards = current_table.get(i).getCards();
						tmpCards.set(1,deck.deal());

						if (deck.cards.size() == 0) {
			 				deck = new Deck();
			 			}
			 			tmpSplit.add(deck.deal());
			 			current_table.set(i, new Hand(tmpCards));
			 			current_table.add(new Hand(tmpSplit));
			 			splitOwner.add(i);
					}
				}
				int sum = current_table.get(i).getCards().get(0).getValue() + current_table.get(i).getCards().get(1).getValue() ;
				
				doubleBet[i] = player[i].do_double( current_table.get(i) , dealerOpen, current_table);
		

				if( doubleBet[i] ) {
					try{
						player[i].decrease_chips(bet[i]);
					}
					catch(Exception e){

					}
					if (deck.cards.size() == 0) {
						deck = new Deck();
					}
					ArrayList<Card> tmpCards;
					tmpCards = current_table.get(i).getCards();
					tmpCards.add(deck.deal());
					current_table.set(i,new Hand(tmpCards));
				} else { 
					int tmpSum ;
					tmpSum = current_table.get(i).getCards().get(0).getValue() + current_table.get(i).getCards().get(1).getValue() ;
					while( tmpSum <= 21 && player[i].hit_me(current_table.get(i), dealerOpen, current_table ) ){
						Card tmpCard ;
						ArrayList<Card> tmpCards ;
						if(deck.cards.size() == 0) {
							deck = new Deck();
						}
						tmpCard = deck.deal(); 
						tmpCards = current_table.get(i).getCards();
						tmpCards.add(tmpCard);
						current_table.set(i,new Hand(tmpCards));
						tmpSum  = tmpSum + tmpCard.getValue();
					}
				}

				if( split[i]) {
					int tmpSum ;
					try{
						player[i].decrease_chips(bet[i]);
					}
					catch(Exception e) {

					}
					tmpSum = current_table.get(current_table.size()-1).getCards().get(0).getValue() + current_table.get(current_table.size()-1).getCards().get(1).getValue() ;
					while( tmpSum <= 21 && player[i].hit_me(current_table.get(current_table.size()-1), dealerOpen, current_table ) ){
						Card tmpCard ;
						ArrayList<Card> tmpCards ;
						if(deck.cards.size() == 0) {
							deck = new Deck();
						}
						tmpCard =deck.deal(); 
						tmpCards = current_table.get(current_table.size()-1).getCards();
						tmpCards.add(tmpCard);
						current_table.set(current_table.size()-1, new Hand(tmpCards) );
						tmpSum  = tmpSum + tmpCard.getValue();
					}
				}
			}
		}
	}

	private void stepDealerExecute() {
		dealer_open = new ArrayList<Card>();
		dealer_open.add(dealerOpen);
		dealer_open.add(dealerClosed);
		while (sumBJ(dealer_open) <= 16 || isSoft17(dealer_open) )  {
			if (deck.cards.size()==0){
				deck = new Deck();
			}
			dealer_open.add(deck.deal());		
		}
	}

	private void stepCompare() {
		for (int i = 0  ; i < totalPlayer ; i++ ) {
			if (doubleBet[i]) {
				bet[i] = bet[i] * 2 ;
			}
			if (surrender[i]) {
				try{
					player[i].increase_chips(bet[i]/2);
				}
				catch(Exception e) {

				}
			} else if ( sumBJ( current_table.get(i)) > 21  ) {

			} else if ( isBJ( current_table.get(i) ) ) {
				if ( dealerIsBJ() ){
				
				} else {
					try{
						player[i].increase_chips( (double)bet[i]*5/2);
					}
					catch(Exception e) {

					}
				}  
			} else if ( sumBJ(dealer_open) >21 ) {
				try{
					player[i].increase_chips(bet[i]*2);
				}
				catch(Exception e) {

				}
			} else if ( dealerIsBJ() ) {
				if ( insurance[i] ) {
					try{
						player[i].increase_chips(bet[i]);
					}
					catch(Exception e) {

					}
				} 
			} else {
				if ( sumBJ(current_table.get(i)) >  sumBJ(dealer_open) ) {
					try{
						player[i].increase_chips(bet[i]*2);	
					}
					catch(Exception e) {

					}
				}
				if ( sumBJ(current_table.get(i)) ==  sumBJ(dealer_open) ) {
					try{
						player[i].increase_chips(bet[i]);	
					}
					catch(Exception e) {

					}
				}
			}
			if (doubleBet[i]) {
				bet[i] = bet[i] / 2 ;
			}
		}
		for (int i = totalPlayer ; i < current_table.size() ; i ++) {
			int index  = splitOwner.get(i - totalPlayer );

			if ( sumBJ( current_table.get(i)) > 21  ) {

			} else if ( isBJ( current_table.get(i) ) ) {
				if ( dealerIsBJ() ){

				} else {
					try{
						player[index].increase_chips( (double)bet[index]*5/2);
					}
					catch(Exception e) {

					}
				}  
			} else if ( sumBJ(dealer_open) >21 ) {
				try{
					player[index].increase_chips(bet[index]*2);
				}
				catch(Exception e) {

				}
			} else if ( dealerIsBJ() ) {

			} else {
				if ( sumBJ(current_table.get(i)) >  sumBJ(dealer_open) ) {
					try{
						player[index].increase_chips(bet[index]*2);	
					}
					catch(Exception e) {

					}
				}
				if ( sumBJ(current_table.get(i)) ==  sumBJ(dealer_open) ) {
					try{
						player[index].increase_chips(bet[index]);	
					}
					catch(Exception e) {

					}
				}
			}	
		}
	}

	private void printResult() {
		ps.print("Dealer Get :");
		for (int i = 0 ; i < dealer_open.size()  ; i++) {
			ps.print(" ");
			ps.print(stringToCard (dealer_open.get(i) ) );
		}
		ps.println("");
		for (int i = 0 ; i < totalPlayer ; i++ ){
			ps.print("Player"+i+" Get :");
			for (int j = 0 ; j < current_table.get(i).getCards().size() ; j ++) {
				ps.print(" ");
				ps.print(stringToCard( current_table.get(i).getCards().get(j) ) );		
			} 

			ps.print("\tChips: "+player[i].toString() );

			if (doubleBet[i] || surrender[i] ) {
				ps.print("\t//");
				if ( doubleBet[i]){
					ps.print(" Double");
				}
				if ( surrender[i]){
					ps.print(" Surrender");
				}
			}

			if(split[i]) {
				ps.println();
				int index = 0 ; 
				for (int  k= 0 ; k < splitOwner.size() ; k++) {
					if (splitOwner.get(k) == i) {
						index =k;
					}
				}
				ps.print("            :");
				for (int j = 0 ; j < current_table.get(totalPlayer+index).getCards().size() ; j ++) {
					ps.print(" ");
					ps.print(stringToCard( current_table.get(totalPlayer+index).getCards().get(j) ) );		
				} 				
			}
			ps.println();
		}

	}
	private String stringToCard(Card card) {
		String tmpSuit = new String();
		String tmpValue = new String();
		if(card.getValue()==1) {
			tmpValue = "A";
		} else if (card.getValue() == 11) {
			tmpValue = "J";
		} else if (card.getValue() == 12) {
			tmpValue = "Q";
		} else if (card.getValue() == 13) {
			tmpValue = "K";
		} else {
			tmpValue = ""+card.getValue() ;
		}
		
		if (card.getSuit() == 1) {
			tmpSuit = "S";
		} else if (card.getSuit() == 2) {
			tmpSuit = "H";
		} else if (card.getSuit() == 3) {
			tmpSuit = "D";
		} else if (card.getSuit() == 4) {
			tmpSuit = "C";
		}
		String tmpreturn =  tmpSuit+ tmpValue ;
		return tmpreturn;
	}

	private boolean isSoft17(ArrayList<Card> card) {
		int sum = 0 ;
		int index = 0 ; 
		int[] num = new int[card.size()] ;
		for (int i = 0 ; i < card.size() ; i++) {
			int tmp;
			tmp = card.get(i).getValue();
			if (tmp > 10 ) {
				num[i] = 10 ;
			} else if (tmp <=10 && tmp >= 2) {
				num[i] = tmp ;
			} else {
				index++; 
				num[i] = 11;
			}
		}
		for (int i = 0 ; i <card.size() ; i++ ) {
			sum = sum + num[i];
		}
		while (sum >21 && index >0 ) {
			sum = sum -10;
		}
		boolean soft ;
		if (sum == 17 && index >= 1) {
			soft = true;
		} else {
			soft =false;
		}
		return soft ;
	}

	private int sumBJ(ArrayList<Card> card){
		int sum = 0 ;
		int index = 0 ; 
		int[] num = new int[card.size()] ;
		for (int i = 0 ; i < card.size() ; i++) {
			int tmp;
			tmp = card.get(i).getValue();
			if (tmp > 10 ) {
				num[i] = 10 ;
			} else if (tmp <=10 && tmp >= 2) {
				num[i]= tmp ;
			} else {
				index++; 
				num[i] = 11;
			}
		}
		for (int i = 0 ; i <card.size() ; i++ ) {
			sum = sum + num[i];
		}
		while (sum >21 && index >0 ) {
			sum = sum -10;
		}
		return sum;
	}

	private int sumBJ(Hand hand){ 
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

	private class Deck {
		private LinkedList<Card> cards ;
		public Deck() {
			cards = new LinkedList<Card>();
			for (byte i=Card.VALUE_LOWER ; i<Card.VALUE_UPPER ; i++) {
				cards.push(new Card(Card.CLUB,i));
				cards.push(new Card(Card.DIAMOND,i));
				cards.push(new Card(Card.HEART,i));
				cards.push(new Card(Card.SPADE,i));
			}
			shuffle();
		}
		public Card deal(){
			return cards.pop();
		}

		protected void shuffle () {
			Collections.shuffle(cards);
		}
	}
}



















