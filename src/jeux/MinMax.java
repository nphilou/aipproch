package jeux;

import java.util.Arrays;

public class MinMax implements Joueur {
	private int role;
	private boolean libre[][];
	private int n;


	public MinMax(int taille) {
		n = taille;
		libre = new boolean[n][n];
		reset();
	}

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
		role = direction;
	}

	@Override
	public String getName() {
		return "MinMax";
	}

	@Override
	public void reset() {
		for (int i = 0; i < n; i++)
			Arrays.fill(libre[i], true);
	}
}
