package jeux;

import java.util.Arrays;
import java.util.Vector;

public class MinMax implements Joueur {
  private int role;
  private boolean libre[][];
  private boolean memory[][];
  private int n;
  private Domino action;

  public MinMax(int taille) {
    n = taille;
    libre = new boolean[n][n];
    memory = new boolean[n][n];
    reset();
    memoryReset();
  }

  public static void display(boolean[][] booleans) {
    System.out.println("TABLE : ");
    for (boolean[] line : booleans) {
      for (boolean column : line) {
        System.out.print((column ? "1" : "O") + "|");
      }
      System.out.println("");
    }
    System.out.println("");
  }

  public Vector<Domino> possibles(int role) {
    Vector<Domino> possibles = new Vector<Domino>();
    if (role == Jeu.LIGNE) {
      for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n; j++) {
          if (memory[i][j] && memory[i + 1][j])
            possibles.add(new Domino(i, j, i + 1, j));
        }
      }
    } else {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n - 1; j++) {
          if (memory[i][j] && memory[i][j + 1])
            possibles.add(new Domino(i, j, i, j + 1));
        }
      }
    }
    return possibles;
  }

  public int utility() {
    return possibles(role).size() - possibles((role == Jeu.LIGNE ? Jeu.COLONNE : Jeu.LIGNE)).size();
  }

  public int max() {
    Domino action = new Domino(0,0,0,0);
    Vector<Domino> possibles = possibles(role);
    if (possibles.size() == 0) return utility();
    int u = -(n * n);
    int temp;
    for (Domino domino : possibles) {
      memoryUpdate(domino);
      display(memory);
      if ((temp = min()) > u) {
        action = domino;
        u = temp;
      }
      resetDomino(domino);
    }
    this.action = action;
    return u;
  }

  public int min() {
    Vector<Domino> possibles = possibles((role == Jeu.LIGNE ? Jeu.COLONNE : Jeu.LIGNE));
    if (possibles.size() == 0) return utility();
    int u = n * n;
    int temp;
    for (Domino domino : possibles) {
      memoryUpdate(domino);
      display(memory);
      if ((temp = max()) < u) {
        u = temp;
      }
      resetDomino(domino);
    }
    return u;
  }

  public void resetDomino(Domino l) {
    memory[l.a.i][l.a.j] = true;
    memory[l.b.i][l.b.j] = true;
  }

  @Override
  public Domino joue() {
    max();
    return action;
  }

  @Override
  public void update(Domino l) {
    libre[l.a.i][l.a.j] = false;
    libre[l.b.i][l.b.j] = false;

    memoryUpdate(l);
  }

  public void memoryUpdate(Domino l) {
    memory[l.a.i][l.a.j] = false;
    memory[l.b.i][l.b.j] = false;
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

  public void memoryReset() {
    for (int i = 0; i < n; i++)
      Arrays.fill(memory[i], true);
  }
}
