package jeux;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Jeu {

	/**
	 * n est la taille du jeu
	 */
	final public int n;
	/**
	 * it represente la nombre de coups joues. Quand it est pair, c'est a ligne de jouer
	 * Quand it est impair, c'est a colonne de jouer
	 */
	private int it;
	/**
	 * tableau indiquant quelles cases sont libres
	 */
	private boolean[][] libre;
	/**
	 * ensemble des dominos places par le joueur ligne
	 */
	private Set<Domino> coupsLigne;
	/**
	 * ensemble des dominos places par le joueur colonne
	 */
	private Set<Domino> coupsColonne;
	/**
	 * attributs pour l'interface graphique pour suivre le jeu
	 */
	private JFrame affichage;
	/**
	 * affiche l'interface graphique
	 */
	private boolean affichageON = true;
	public final static int COLONNE = 0;
	public final static int LIGNE = 1;
	/**
	 * ligne et colonne sont les deux joueurs
	 */
	private Joueur ligne;
	private Joueur colonne;


	public Jeu(int taille, boolean affON) {
		affichageON = affON;
		n = taille;
		libre = new boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;

		coupsLigne = new HashSet<Domino>();
		coupsColonne = new HashSet<Domino>();
		if (affichageON) {
			affichage = new JFrame();
			affichage.setTitle("Jeu");
			affichage.setSize(400, 400);
			affichage.add(new Grillage());
			affichage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			affichage.setVisible(true);
		}
	}


	public boolean legal(Domino l) {
		// le domino est-il placé dans la grille?
		boolean testa = (l.a.i >= 0 && l.a.i < n && l.a.j >= 0 && l.a.j < n);
		boolean testb = (l.b.i >= 0 && l.b.i < n && l.b.j >= 0 && l.b.j < n);
		if (testa && testb) {
			boolean estLibre = (libre[l.a.i][l.a.j] && libre[l.b.i][l.b.j]);
			if (!estLibre) {
				System.out.println("Domino pas sur une case libre selon le joueur ");
			}
			// le domino a-t-il la bonne taille
			boolean horizontal = ((l.a.i == l.b.i) && ((l.a.j - l.b.j == 1) || l.a.j - l.b.j == -1));
			boolean vertical = ((l.a.j == l.b.j) && ((l.a.i - l.b.i == 1) || l.a.i - l.b.i == -1));
			if (!(horizontal || vertical)) {
				System.out.println("domino pas de la bonne taille");
			}
			return (estLibre && (horizontal || vertical));
		} else {
			System.out.println("Domino en dehors de la grille ");
			return false;
		}
	}

	/**
	 * Retourne l'ensemble des dominos qui peuvent être posés sur le plateau selon si le joueur joue
	 * en ligne ou en colonne
	 *
	 * @param role du joueur
	 * @return l'ensemble des dominos que le joueur avec ce role peut placer
	 */
	public Set<Domino> getActionsPossibles(int role) {
		Set<Domino> actions = new HashSet<Domino>();
		if (role == Jeu.LIGNE) {
			for (int i = 0; i < n - 1; i++)
				for (int j = 0; j < n; j++)
					if (libre[i][j] && libre[i + 1][j])
						actions.add(new Domino(i, j, i + 1, j));
		} else { // role est colonne
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n - 1; j++)
					// if (courant.libre[i][j] && courant.libre[i][j + 1])
					if (this.libre[i][j] && this.libre[i][j + 1])
						actions.add(new Domino(i, j, i, j + 1));
		}
		return actions;
	}

	public void joue() throws CoupIllegal {
		Domino l = null;
		int tour;
		if (it % 2 == 0)
			tour = LIGNE;
		else
			tour = COLONNE;
		if (tour == LIGNE) {
			l = ligne.joue();
			System.out.println("LIGNE joue (" + l.a.i + "," + l.a.j + ") -> (" + l.b.i + "," + l.b.j + ")");
		} else {
			l = colonne.joue();
			System.out.println("COLONNE joue (" + l.a.i + "," + l.a.j + ") -> (" + l.b.i + "," + l.b.j + ")");
		}

		if (legal(l)) {
			if (tour == LIGNE) {
				coupsLigne.add(l);
				// on indique à colonne quel coup ligne a joue
				colonne.update(l);
				ligne.update(l);
			} else {
				coupsColonne.add(l);
				ligne.update(l);
				colonne.update(l);
			}
			libre[l.a.i][l.a.j] = false;
			libre[l.b.i][l.b.j] = false;
			if (affichageON) {
				affiche();
			}
		} else {
			String coupable;
			if (tour == LIGNE)
				coupable = "LIGNE";
			else
				coupable = "COLONNE";
			throw new CoupIllegal("le coup n'est pas legal joueur " + coupable + "!", tour);
		}
	}


	public void affiche() {
		affichage.repaint();
	}


	/**
	 * classe interne pour l'affichage graphique du jeu
	 */
	public class Grillage extends JPanel {

		public Grillage() {
			setBackground(Color.yellow);
		}

		public void paintComponent(Graphics g) {
			int step = 40;
			int border = 5;
			int pointSize = 8;

			// dessine les liens
			g.setColor(Color.red);
			for (Domino l : coupsLigne) {
				int x = (1 + l.a.i) * step;
				int y = (n - l.a.j) * step;
				if (l.a.i < l.b.i)
					g.fillRect(x, y, step, pointSize);
				else
					g.fillRect(x - step, y, step, pointSize);
			}
			g.setColor(Color.cyan);
			for (Domino l : coupsColonne) {
				int x = (1 + l.a.i) * step;
				int y = (n - l.a.j) * step;
				if (l.a.j < l.b.j)
					g.fillRect(x, y - step, pointSize, step);
				else
					g.fillRect(x, y, pointSize, step);
			}
			g.setColor(Color.black);
			// dessine les points
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					g.fillOval(i * step - pointSize / 2, j * step, pointSize, pointSize);
				}
			}
			// place les labels
			for (int i = 1; i <= n; i++)
				g.drawString((i - 1) + "", i * step - border, n * step + step);
			for (int j = 1; j <= n; j++)
				g.drawString((n - j) + "", 5, j * step + border);
		}
	}

	public boolean estTerminee() {
		if (it % 2 == 0) { // c'est à ligne de jouer
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n; j++) {
					if (libre[i][j] && libre[i + 1][j])
						return false;
				}
			}
		} else { // c'est à colonne de jouer
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n - 1; j++) {
					if (libre[i][j] && libre[i][j + 1])
						return false;
				}
			}
		}
		return true;
	}

	public static boolean estTermine(boolean[][] libre, int role) {
		int n = libre.length;
		if (role == LIGNE) { // c'est à ligne de jouer
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n; j++) {
					if (libre[i][j] && libre[i + 1][j])
						return false;
				}
			}
		} else { // c'est à colonne de jouer
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n - 1; j++) {
					if (libre[i][j] && libre[i][j + 1])
						return false;
				}
			}
		}
		return true;
	}

	public Joueur jouePartie() {
		System.out.println("Debut de la partie");
		int gagnant = -1;

		while (!estTerminee()) {
			try {
				joue();
			} catch (CoupIllegal e) {
				System.out.println(e);
				gagnant = it % 2;
				break;
			}
			it++;
		}
		if (affichageON) {
			affiche();
		}
		gagnant = it % 2;
		if (gagnant == LIGNE) {
			System.out.println("\n @@ Le gagnant est " + ligne.getName() + " @@");
			return ligne;
		} else {
			System.out.println("\n @@ Le gagnant est " + colonne.getName() + " @@");
			return colonne;
		}
	}

	public void reset() {
		it = 0;
		ligne.reset();
		colonne.reset();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;
		coupsLigne.clear();
		coupsColonne.clear();
	}

	public static void main(String[] args) {
		int taille = 8;
		int numGames = 5;

		//Joueur clavier = new JoueurClavier();
		//Joueur random = new JoueurAleatoire(taille);
		//Joueur random = new JoueurClavier();
		//Joueur random    = new MinMax(taille);
		Joueur clavier = new NourryNguyen(taille);
		Joueur random = new AlphaBeta(taille);
		Jeu g = new Jeu(taille, true);

		g.ligne = clavier;
		g.colonne = random;
		g.ligne.setRole(LIGNE);
		g.colonne.setRole(COLONNE);


		Vector<String> resultat = new Vector<String>();
		for (int i = 0; i < numGames; i++) {
			resultat.add(g.jouePartie().getName());
			g.reset();
		}

		// changement de role
		Joueur j = g.colonne;
		g.colonne = g.ligne;
		g.ligne = j;
		g.ligne.setRole(LIGNE);
		g.colonne.setRole(COLONNE);

		for (int i = 0; i < numGames; i++) {
			resultat.add(g.jouePartie().getName());
			g.reset();
		}
		for (String name : resultat)
			System.out.println(name);
	}
	
	
	/*
	public void competition(){	
		int size = 8;
		int nbRepeats = 1;
		Jeu g = new Jeu(size,false);
		List<Joueur> players = new LinkedList<Joueur>();
		// list of players
		players = new LinkedList<Joueur>();
		
		
		
		
		int num_players = players.size();
		int[][] scoresLigne = new int[num_players][num_players];
		int[][] scoresColonne = new int[num_players][num_players];
		for (int i=0;i<num_players;i++)
			for (int j=0;j<num_players;j++){
				scoresLigne[i][j]=0;
				scoresColonne[i][j]=0;
			}
		
		//play on!
		int ii=0,jj=0;
		for (Joueur ligne: players){
			jj=0;
			for (Joueur colonne: players){
				if (!ligne.equals(colonne)){
				// play i row, j column
				g.ligne = ligne;
				g.colonne = colonne;
				for (int k=0;k<50;k++)
					System.out.print("~");
				System.out.println();
				System.out.println("Game " + ligne.getName() +" vs " + colonne.getName());
				for (int k=0;k<50;k++)
					System.out.print("~");
				System.out.println();
				g.ligne.setRole(LIGNE);
				g.colonne.setRole(COLONNE);
				
				
				for (int it=0;it<nbRepeats;it++){
					String winner = g.jouePartie().getName();
					if (winner.equals(ligne.getName()))
						scoresLigne[ii][jj]++;
					else if (winner.equals(colonne.getName()))
						scoresColonne[jj][ii]++;
					else{
						System.err.println("Houston, we have a problem!");
						System.exit(9);
					}
					g.reset();
				}					
			}
				jj++;
			}
			ii++;
		}
		for (int i=0;i<50;i++)
			System.out.print("~");
		System.out.println();
		for (int i=0;i<num_players;i++){
			System.out.print(players.get(i).getName()+"\t");
			for (int j=0;j<num_players;j++)
				System.out.print(scoresLigne[i][j]+" ");
			System.out.println();
		}
		for (int i=0;i<50;i++)
			System.out.print("~");
		System.out.println();
		for (int i=0;i<num_players;i++){
			System.out.print(players.get(i).getName()+"\t");
			for (int j=0;j<num_players;j++)
				System.out.print(scoresColonne[i][j]+" ");
			System.out.println();
		}
		System.out.print("~");
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		// competition();
		// int row = Integer.parseInt(args[0]);
		// int col = Integer.parseInt(args[1]);
		// String filename = args[2];

		List<Joueur> players = loadPlayers();
		int num_players = players.size();

		for (int row = 0; row < num_players; row++) {
			for (int col = 0; col < num_players; col++) {
				if (row != col) {

					for (int i = 0; i < 60; i++)
						System.out.print("~");
					System.out.println();
					System.out.print("Game: " + players.get(row).getName()
							+ " vs " + players.get(col).getName());
					System.out.println();

					int nbRepeats = 2;
					int scoreLigne = 0;

					try {
						scoreLigne = battle(players.get(row), players.get(col),
								nbRepeats);
						System.out.println(" " + scoreLigne + " / " + (nbRepeats-scoreLigne));

					} catch (Exception e) {
						System.out.println("Problem Bataille "
								+ players.get(row).getName() + " vs "
								+ players.get(col).getName());
					}
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								"results.txt", true));
						bw.write(row + " " + col + " " + scoreLigne + " " + (nbRepeats - scoreLigne) + " " + players.get(row).getName() + " " + players.get(col).getName());
						bw.newLine();
						bw.close();
					} catch (IOException e) {
						System.err.println("Error Writing the file");
					}

				}
			}
		}
	}

	
	
	
	
	
	
	public static List<Joueur> loadPlayers(){	
		int size = 8;
		int nbRepeats = 1;
		Jeu g = new Jeu(size,false);
		// list of players
		
		List<Joueur> players = new LinkedList<Joueur>();
		
		return players;
	}
	
	public static int battle(Joueur ligne, Joueur colonne, int nbRepeats) throws Exception{
		Jeu g = new Jeu(8,false);
		int scoreLigne=0;
		int scoreColonne =0;
				g.ligne = ligne;
				g.colonne = colonne;
				for (int k=0;k<50;k++)
					System.out.print("~");
				System.out.println();
				System.out.println("Game " + ligne.getName() +" vs " + colonne.getName());
				for (int k=0;k<50;k++)
					System.out.print("~");
				System.out.println();
				g.ligne.setRole(LIGNE);
				g.colonne.setRole(COLONNE);
				
				
				for (int it=0;it<nbRepeats;it++){
					String winner = g.jouePartie().getName();
					if (winner.equals(ligne.getName()))
						scoreLigne++;
					else if (winner.equals(colonne.getName()))
						scoreColonne++;
					else{
						System.err.println("Houston, we have a problem!");
						System.exit(9);
					}
					g.reset();
				}
				
				return scoreLigne;
		
	}
	
	
	
	*/


}
