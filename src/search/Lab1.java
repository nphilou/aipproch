package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Lab1 {


    public static void main(String[] args) {
        BufferedReader reader;
        BufferedWriter writer;
        List<Long> runTimesDFS = new ArrayList<Long>();
        List<Long> runTimesBFS = new ArrayList<Long>();
        List<Integer> visitedBFS = new ArrayList<Integer>();
        List<Integer> visitedDFS = new ArrayList<Integer>();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        nf.setGroupingUsed(false);

        int milli = 1000000;

        try {
            reader = new BufferedReader(new FileReader("games2.txt"));
            writer = new BufferedWriter(new FileWriter("results.csv"));
            writer.write("\"Problem size\";\"DFS avg time\";\"Std\";\"DFS visited states\";\"Std \";");
            writer.write("\"BFS avg time\";\"Std\";\"BFS visited states\";\"Std\"\n");
            String line = reader.readLine();
            int problemSize = 0;
            while (line != null) {
                if (line.charAt(0) == '%') {
                    if (!runTimesDFS.isEmpty()) {
                        // one completed size
                        writer.write(problemSize + ";" + nf.format(mean(runTimesDFS) / milli) + ";" + nf.format(std(runTimesDFS) / milli) + ";\t");
                        writer.write(nf.format(mean(visitedDFS)) + ";" + nf.format(std(visitedDFS)) + ";");
                        writer.write(nf.format(mean(runTimesBFS) / milli) + ";" + nf.format(std(runTimesBFS) / milli) + ";");
                        writer.write(nf.format(mean(visitedBFS)) + ";" + nf.format(std(visitedBFS)));
                        writer.write("\n");
                    }
                    problemSize = Integer.parseInt(line.substring(2));
                } else {
                    Problem<Puzzle, PuzzleAction> eightpuzzle
                            = new Problem<Puzzle, PuzzleAction>(new Puzzle(line), new Puzzle("012345678"));

                    // run BFS
                    long startTime = System.nanoTime();
                    int nb = eightpuzzle.bfs();
                    long finishTime = System.nanoTime();
                    runTimesBFS.add(finishTime - startTime);
                    visitedBFS.add(nb);

                    // run DFS
                    startTime = System.nanoTime();
                    nb = eightpuzzle.dfs();
                    finishTime = System.nanoTime();
                    runTimesDFS.add(finishTime - startTime);
                    visitedDFS.add(nb);

                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }


    public static double mean(List<? extends Number> l) {
        double res = 0;
        for (Number val : l)
            res += val.doubleValue();
        return (1.0 * res) / l.size();
    }

    // Ecart type
    public static double std(List<? extends Number> l) {
        double m = mean(l);
        double res = 0;
        for (Number val : l)
            res += Math.pow(val.doubleValue() - m, 2);
        return Math.sqrt(res / l.size());
    }
}
