package jeux;

import java.util.ArrayList;
import java.util.List;


public class NourryNguyen implements Joueur {

	int role;
	boolean libre[][];
	//boolean libreutc[][];
	int n;

	public NourryNguyen(int taille) {
		n = taille;
		libre = new boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;
	}

	public class Node {
		public void setFather(Node father) {
			this.father = father;
		}

		Domino d;
		double visited;
		double nbrofwin;
		Node father;
		List<Node> children;

		public Node(Domino d) {
			this.d = d;
			visited = 1;
			nbrofwin = 1;
			father = null;
			children = new ArrayList<Node>();
		}

		public String toString() {
			return d.a.i + " " + d.a.j + " " + d.b.i + " " + d.b.j + "\n"
							+ " vis=" + visited + " nbrofwin=" + nbrofwin + "father=" + father;
		}

		public void addchild(Node node) {
			children.add(node);
		}

		public Domino getDomino() {
			return d;
		}

		public void increaseRatio(int win) {
			visited++;
			if (win == role) {
				nbrofwin++;
			}
		}

		public Node nodeToVisit() {
			if (children.isEmpty()) {
				return null;
			}
			Node nodetovisit = children.get(0);
			for (Node node : children) {
				if ((node.nbrofwin / node.visited) > (nodetovisit.nbrofwin / nodetovisit.visited)) {
					nodetovisit = node;
				}
			}
			return nodetovisit;
		}
	}

	public Domino joue() {

		Node root = new Node(new Domino(0, 0, 0, 0));
		Node courant = root;
		int numiterations = 1000000;


		JoueurAleatoire player = new JoueurAleatoire(n);
		player.libre = libre;
		JoueurAleatoire opponent = new JoueurAleatoire(n);
		opponent.libre = libre;
		player.role = role;
		opponent.role = (role + 1) % 2;
		List<Domino> dominosplayed = new ArrayList<Domino>();
		int winner = 0;
		Domino dominotoplay = new Domino(0, 0, 0, 0);

		List<Domino> possibles = possible(role);
		if (possibles.size() == 0) {
			System.err.println("NourryNguyen a perdu!");
		}

		for (Domino domino : possibles) {
			Node child = new Node(domino);
			child.setFather(root);
			root.addchild(child);
		}

		for (int i = 0; i < numiterations; i++) {
			while (courant.nodeToVisit() != null) {
				courant = courant.nodeToVisit();
				dominosplayed.add(courant.getDomino());
				update(courant.getDomino());
			}

			if (!possible(player.role).isEmpty()) {
				Node child = new Node(player.joue());
				child.setFather(courant);
				courant.addchild(child);
				update(courant.nodeToVisit().getDomino());
				dominosplayed.add(courant.nodeToVisit().getDomino());
			} else {
				dominotoplay = null;
			}

			Joueur joueur = player;

			while (!possible(player.role).isEmpty() && !possible(opponent.role).isEmpty()) {
				dominotoplay = joueur.joue();
				dominosplayed.add(dominotoplay);
				update(dominotoplay);

				joueur = joueur == player ? opponent : player;
			}

			winner = !possible(player.role).isEmpty() ? role : opponent.role;

			/*while (dominotoplay != null) {
				if (!possible(player.role).isEmpty()) {
					dominotoplay = player.joue();
					winner = player.role;
					dominosplayed.add(dominotoplay);
					update(dominotoplay);
				} else {
					dominotoplay = null;
				}

				if (dominotoplay != null) {
					if (!possible(opponent.role).isEmpty()) {
						dominotoplay = opponent.joue();
						winner = opponent.role;
						dominosplayed.add(dominotoplay);
						update(dominotoplay);
					} else {
						dominotoplay = null;
					}

				}
			}*/
			if (dominotoplay != null) {
				courant = courant.nodeToVisit();
				while (courant.father != null) {
					courant.increaseRatio(winner);
					courant = courant.father;
				}
			} else {
				while (courant.father != null) {
					courant = courant.father;
				}
			}

			for (Domino dom : dominosplayed) {
				clean(dom);
			}
			dominosplayed.clear();
		}

		return courant.nodeToVisit().getDomino();
	}

	public void update(Domino l) {
		libre[l.a.i][l.a.j] = false;
		libre[l.b.i][l.b.j] = false;
	}

	public void clean(Domino l) {
		libre[l.a.i][l.a.j] = true;
		libre[l.b.i][l.b.j] = true;
	}

	public List<Domino> possible(int role) {
		List<Domino> possible = new ArrayList<Domino>();
		if (role == 0) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n - 1; j++) {
					if (libre[i][j] && libre[i][j + 1])
						possible.add(new Domino(i, j, i, j + 1));
				}
			}
		} else {
			for (int i = 0; i < n - 1; i++) {
				for (int j = 0; j < n; j++) {
					if (libre[i][j] && libre[i + 1][j])
						possible.add(new Domino(i, j, i + 1, j));
				}
			}
		}
		return possible;
	}


	public void setRole(int direction) {
		role = direction;
	}

	public String getName() {
		return "NourryNguyen";
	}

	public void reset() {
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				libre[i][j] = true;
	}
}
