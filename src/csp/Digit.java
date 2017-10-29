package csp;

public enum Digit {
	one(1),
	two(2),
	three(3),
	four(4),
	five(5),
	six(6),
	seven(7),
	eight(8),
	nine(9);

	private int value = 0;

	Digit(int v) {
		value = v;
	}

	public String toString() {
		return "" + value;
	}

	public int getValue() {
		return value;
	}

	public static Digit get(int i) {
		return Digit.values()[i-1];
	}

	/*public static Digit get(int i) {
		switch (i) {
			case 1:
				return one;
			case 2:
				return two;
			case 3:
				return three;
			case 4:
				return four;
			case 5:
				return five;
			case 6:
				return six;
			case 7:
				return seven;
			case 8:
				return eight;
			case 9:
				return nine;
			default:
				System.err.println("This is not a digit!");
				return null;
		}
	}
*/

}
