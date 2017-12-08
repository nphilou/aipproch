package jeux;

import java.util.ArrayList;
import java.util.List;


public class NourryNguyen implements Joueur {

	int role;
	boolean libre[][];
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
					+ " vis=" + visited + " nbrofwin=" + nbrofwin;
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

		public Node nodeToVisit(int it) {
			if (children.isEmpty()) {
				return null;
			}
			Node nodetovisit = children.get(0);
			for (Node node : children) {
				if ((/*(node.nbrofwin / node.visited)+*/Math.sqrt(Math.log(2*it/*node.father.visited*/)/node.visited)) >
						(/*(nodetovisit.nbrofwin / nodetovisit.visited)+*/Math.sqrt(Math.log(2*it/*nodetovisit.father.visited*/)/nodetovisit.visited))) {
					nodetovisit = node;
				}
			}
			return nodetovisit;
		}

		public Node nodeToreturn() {
			if (children.isEmpty()) {
				return null;
			}
			Node nodetovisit = children.get(0);
			for (Node node : children) {
				if (((node.nbrofwin / node.visited) > (nodetovisit.nbrofwin / nodetovisit.visited))&&
						((node.nbrofwin / node.visited)<1)){
					nodetovisit = node;
				}
			}
			return nodetovisit;
		}
	}

	public Domino joue() {

		Node root = new Node(new Domino(0, 0, 0, 0));
		Node courant = root;
		int numiterations = 33333;
		int dominocourant=0;


		long start_time = System.currentTimeMillis();


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

		for (Domino domino : possible(role)) {
			Node child = new Node(domino);
			child.setFather(root);
			root.addchild(child);
		}

		for (int i = 0; i < numiterations; i++) {
			while (courant.nodeToVisit(i) != null) {
				courant = courant.nodeToVisit(i);
				dominosplayed.add(courant.getDomino());
				update(courant.getDomino());
				dominocourant++;
			}
			for (int j = 0; j < dominocourant; j++) {
				if(!possible(opponent.role).isEmpty()){
					Domino domino = opponent.joue();
					update(domino);
					dominosplayed.add(domino);
				}
				else{
					dominotoplay = null;
					while (courant.father != null) {
						courant.increaseRatio(player.role);
						courant = courant.father;
					}
					dominocourant=0;
				}
			}
			dominocourant=0;

			if (!possible(player.role).isEmpty() && (courant.nodeToVisit(i)==null)) {
				for (Domino domi:possible(player.role)){
					Node child = new Node(domi);
					child.setFather(courant);
					courant.addchild(child);

				}

				courant = courant.nodeToVisit(i);
				update(courant.getDomino());
				dominosplayed.add(courant.getDomino());
            /*Node child = new Node(player.joue());
            child.setFather(courant);
            courant.addchild(child);
            update(courant.nodeToVisit().getDomino());
            dominosplayed.add(courant.nodeToVisit().getDomino());*/
			} else {

				dominotoplay = null;
				while (courant.father != null) {
					courant.increaseRatio(opponent.role);
					courant = courant.father;
				}
			}

			Joueur joueur = player;

			while (!possible(player.role).isEmpty() && !possible(opponent.role).isEmpty()) {
				dominotoplay = joueur.joue();
				dominosplayed.add(dominotoplay);
				update(dominotoplay);

				joueur = joueur == player ? opponent : player;
			}

			winner = !possible(player.role).isEmpty() ? role : opponent.role;

			if (dominotoplay != null) {

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
		long end_time = System.currentTimeMillis();
		System.out.println(end_time-start_time);
		return courant.nodeToreturn().getDomino();
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