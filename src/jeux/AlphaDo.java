package jeux;

public class AlphaDo implements Joueur {
	private int role;
	private boolean libre[][];

	@Override
	public Domino joue() {
		return null;
	}

	@Override
	public void update(Domino l) {
		libre[l.a.i][l.a.j] = false;
		libre[l.b.i][l.b.j] = false;
	}

	@Override
	public void setRole(int direction) {

	}

	@Override
	public String getName() {
		return "AlphaDo";
	}

	@Override
	public void reset() {

	}
}
