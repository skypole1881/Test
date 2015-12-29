public class Player {
	String name ;
	Profession profession ;
	int professionType;
	Group group; 
	Hand hand;
	public Player(String _name){
		name = _name;
	}

	class Group {
		String name ;
		public Group (String _name){
			name = _name ; 
		}
	}
}
