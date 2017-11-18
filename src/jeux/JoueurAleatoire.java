package jeux;

import java.util.Random;
import java.util.Vector;


public class JoueurAleatoire implements Joueur {

	int role;
	boolean libre[][];
	int n;
	Random gen;

	public String getName() {
		return "Aleatoire";
	}

	public JoueurAleatoire(int taille) {
		n = taille;
		libre = new boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;
		gen = new Random();
	}

	public void reset() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;
	}


	@Override
	public Domino joue() {
		Vector<Domino> possibles = new Vector<Domino>();
		if (role == Jeu.LIGNE) {
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n; j++) {
					if (libre[i][j] && libre[i + 1][j])
						possibles.add(new Domino(i, j, i + 1, j));
				}
			}
			if (possibles.size() == 0) {
				System.err.println("J'ai perdu!");
			}
			return possibles.get(gen.nextInt(possibles.size()));
		} else {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n - 1; j++) {
					if (libre[i][j] && libre[i][j + 1])
						possibles.add(new Domino(i, j, i, j + 1));
				}
			}
			return possibles.get(gen.nextInt(possibles.size()));
		}

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

}
