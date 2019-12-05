# Rapport d'expérimentations

La modélisation du problème comme un CSP permet d'avoir une flexibilité dans le choix de l'algorithme de résolution.

Il existe actuellement beaucoup de recherches en ligne concernant la résolution des CSP.

Au début de la résolution, on réduit les domaines des variables avec un algorithme de type AC3.

L'algorithme AC3 existe en version distribuée [1] mais est difficile à mettre en place donc on a choisi d'itérer l'algorithme AC jusqu'à rendre fixe les domaines.

Concernant le choix de la variable, on prend celui qui possède le moins de valeurs possibles car il permet de réduire rapidement le domaine de ses voisins. C'est la méthode la plus utilisée et la plus efficace actuellement. [2]

On peut aussi prendre celui qui implique le plus de conflits mais cela s'est révélé moins rapide.

Lorsque le jeu comporte beaucoup de pièces de type 1 (- ), le problème revient à résoudre un problème de **Domino Tiling** [3] qui peut se résoudre en O(n^2) car il s'agit finalement d'un graphe bipartite[4]. (Problème SAT)

## Références

1. [Analysis of Distributed Arc-Consistency Algorithms](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.53.9938&rep=rep1&type=pdf)
2. [Choix de la variable](https://ktiml.mff.cuni.cz/~bartak/constraints/ordering.html)
3. [Domino Tiling Problem](http://www.math.cmu.edu/~bwsulliv/domino-tilings.pdf)
4. [Graphe Bipartite](http://math.uchicago.edu/~may/REU2015/REUPapers/Borys.pdf)
