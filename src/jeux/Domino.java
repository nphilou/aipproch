package jeux;


public class Domino {
	Case a, b;

	public Domino(Case p1, Case p2) {
		a = p1;
		b = p2;
	}

	public Case get_a() {
		return a;
	}

	public Case get_b() {
		return b;
	}

	public Domino(int xi, int xj, int yi, int yj) {
		a = new Case(xi, xj);
		b = new Case(yi, yj);
	}

}