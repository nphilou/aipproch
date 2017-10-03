package search;

public enum PuzzleAction {
	left,
	right,
	up,
	down;
	
	@Override public String toString(){
		switch(this){
		case left:
			return "\u21E6";
		case right:
			return "\u21E8";
		case up:
			return "\u21E7";
		case down:
			return "\u21E9";
		default:
			return "error";
		}
	}
}
