package jeux;

import java.util.ArrayList;
import java.util.List;


public class NourryNguyen implements Joueur{

    int role;
    boolean libre[][];
    //boolean libreutc[][];
    int n;

    public NourryNguyen(int taille){
        n=taille;
        libre = new boolean[n][n];
        for (int i=0;i<n;i++)
            for (int j=0;j<n;j++)
                libre[i][j]=true;
    }

    public class Node{
        Domino d;
        double visited;
        double nbrofwin;
        Node father;
        List<Node> children;

        public Node(Node node,Domino d){
            this.d=d;
            visited=1;
            nbrofwin=1;
            father=node;
            children=new ArrayList<Node>();
        }

        public String toString() {
            return d.a.i+" "+d.a.j+" "+d.b.i+" "+d.b.j+"\n"
                    +" vis="+visited+" nbrofwin="+nbrofwin+"father="+father;
        }

        public void addchild(Node node){
            children.add(node);
        }

        public Domino getDomino(){
            return d;
        }

        public void increaseRatio(int win){
            visited++;
            if(win==role){
                nbrofwin++;
            }
        }

        public Node nodeToVisit(){
            if(children.isEmpty()){
                return null;
            }
            Node nodetovisit=children.get(0);
            for (Node node: children) {
                if((node.nbrofwin/node.visited)>(nodetovisit.nbrofwin/nodetovisit.visited)){
                    nodetovisit=node;
                }
            }
            return nodetovisit;
        }
    }

    public Domino joue() {

        Node root=new Node(null,new Domino(0,0,0,0));
        Node courant=root;
        int numiterations=100;


        JoueurAleatoire player=new JoueurAleatoire(n);
        player.libre=libre;
        JoueurAleatoire opponent=new JoueurAleatoire(n);
        opponent.libre=libre;
        player.role=role;
        opponent.role=(role+1)%2;
        List<Domino> dominosplayed=new ArrayList<Domino>();
        int winner=0;
        Domino dominotoplay=new Domino(0,0,0,0);

        List<Domino> possibles = possible(role);
        if (possibles.size()==0){
            System.err.println("NourryNguyen a perdu!");
        }

        for (Domino domino : possibles) {
            root.addchild(new Node(root,domino));
        }

        for (int i = 0; i < numiterations; i++) {
            while(courant.nodeToVisit()!=null){//plays

                courant=courant.nodeToVisit();
                dominosplayed.add(courant.getDomino());
                update(courant.getDomino());
                //player.update(courant.getDomino());
                //opponent.update(courant.getDomino());
                /*dominotoplay=opponent.joue(); opponent dont play for now, have to after
                updateutc(dominotoplay);
                player.update(dominotoplay);
                opponent.update(dominotoplay);*/

            }

            //System.out.println(possible(player.role));
            if(!possible(player.role).isEmpty()) {
                courant.addchild(new Node(courant, player.joue()));
                update(courant.nodeToVisit().getDomino());//move here
                dominosplayed.add(courant.nodeToVisit().getDomino());
            }
            else{
                dominotoplay=null;
            }


            /*for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    System.out.print(libreutc[j][k]+" ");
                }
                System.out.println();
            }
            player.libre[courant.nodeToVisit().getDomino().get_a().get_x()][courant.nodeToVisit().getDomino().get_a().get_y()]=false;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    System.out.print(libreutc[j][k]+" ");
                }
                System.out.println();
            }
            opponent.libre[courant.nodeToVisit().getDomino().get_b().get_x()][courant.nodeToVisit().getDomino().get_b().get_y()]=false;*/
            //player.update(courant.nodeToVisit().getDomino());
            //opponent.update(courant.nodeToVisit().getDomino());

            while(dominotoplay!=null){//avec possibes
                if(!possible(player.role).isEmpty()){
                    dominotoplay=player.joue();
                    winner=player.role;
                    dominosplayed.add(dominotoplay);
                    update(dominotoplay);
                    //player.update(dominotoplay);
                    //opponent.update(dominotoplay);
                    /*player.libre[dominotoplay.get_a().get_x()][dominotoplay.get_a().get_y()]=false;
                    opponent.libre[dominotoplay.get_b().get_x()][dominotoplay.get_b().get_y()]=false;*/
                }
                else{
                    dominotoplay=null;
                }

                if(dominotoplay!=null){
                    if(!possible(opponent.role).isEmpty()){
                        dominotoplay=opponent.joue();
                        winner=opponent.role;
                        dominosplayed.add(dominotoplay);
                        update(dominotoplay);
                        //player.update(dominotoplay);
                        //opponent.update(dominotoplay);
                        /*player.libre[dominotoplay.get_a().get_x()][dominotoplay.get_a().get_y()]=false;
                        opponent.libre[dominotoplay.get_b().get_x()][dominotoplay.get_b().get_y()]=false;*/
                    }
                    else{
                        dominotoplay=null;
                    }

                }
            }
            dominotoplay=new Domino(0,0,0,0);

            courant=courant.nodeToVisit();
            while(courant.father!=null){
                System.out.println(courant);
                courant.increaseRatio(winner);
                courant=courant.father;
                System.out.println("apres"+courant);
            }

            //libreutc=libre.clone();
            //player.libre=libre.clone();
            //opponent.libre=libre.clone();
            for (Domino dom : dominosplayed) {
                clean(dom);
            }
            dominosplayed.clear();
        }//end iterations

        return courant.nodeToVisit().getDomino();
    }

    public void update(Domino l) {
        libre[l.a.i][l.a.j]=false;
        libre[l.b.i][l.b.j]=false;
    }

    public void clean(Domino l){
        libre[l.a.i][l.a.j]=true;
        libre[l.b.i][l.b.j]=true;
    }

    public List<Domino> possible(int role){
        List<Domino> possible=new ArrayList<Domino>();
        if(role==0){
            for (int i=0;i<n;i++){
                for (int j=0;j<n-1;j++){
                    if (libre[i][j] && libre[i][j+1])
                        possible.add(new Domino(i,j,i,j+1));
                }
            }
        }
        else{
            for (int i=0;i<n-1;i++){
                for (int j=0;j<n;j++){
                    if (libre[i][j] && libre[i+1][j])
                        possible.add(new Domino(i,j,i+1,j));
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
        for (int i=0;i<n;i++)
            for (int j=0;j<n;j++)
                libre[i][j]=true;
    }
}
