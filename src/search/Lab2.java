package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Lab2 {
	public static void main(String[] args) {
		BufferedReader reader;
		BufferedWriter writer;
		List<Long> runTimesH1 = new ArrayList<Long>();
		List<Long> runTimesH2 = new ArrayList<Long>();
		List<Integer> visitedH1 = new ArrayList<Integer>();
		List<Integer> visitedH2 = new ArrayList<Integer>();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
		nf.setGroupingUsed(false);
		
		int milli = 1000000;
		
		try{
			reader = new BufferedReader(new FileReader("taquin-f.txt"));
			writer = new BufferedWriter(new FileWriter("results.csv"));
			writer.write("Size;\"avg time H1\";\"std\";\"states visited H1\";\"std \";");
			writer.write("\"avg time H2\";\"std\";\"states visited H2\";\" std\"\n");
			String line = reader.readLine();
			int problemSize=0;
			while (line!=null){
				if (line.charAt(0)=='%'){
					if (!runTimesH1.isEmpty()){
						// completed one size
						writer.write(problemSize + ";" + nf.format(mean(runTimesH1)/milli) +";"+ nf.format(std(runTimesH1)/milli)+";\t");
						writer.write(nf.format(mean(visitedH1)) +";"+ nf.format(std(visitedH1))+";");
						writer.write(nf.format(mean(runTimesH2)/milli) +";"+nf.format(std(runTimesH2)/milli)+";");
						writer.write(nf.format(mean(visitedH2)) +";"+ nf.format(std(visitedH2)));
						writer.write("\n");	
					}	
					problemSize = Integer.parseInt(line.substring(2));
				}
				else
				{
					Problem<Puzzle, PuzzleAction> eightpuzzle 
					= new Problem<Puzzle,PuzzleAction>(new Puzzle(line), new Puzzle("012345678"));
					
					// run A* with H1
					long startTime = System.nanoTime();
					int nb = eightpuzzle.aStar( (Puzzle s )-> new Double(s.depth()),
							(Puzzle s )-> new Double(h1(s)));
							
					long finishTime = System.nanoTime();
					runTimesH1.add(finishTime-startTime);
					visitedH1.add(nb);
					// run A* with H2
					startTime = System.nanoTime();
					nb = eightpuzzle.aStar((Puzzle s )-> new Double(s.depth()),
							(Puzzle s )-> new Double(h2(s)));
					finishTime = System.nanoTime();
					runTimesH2.add(finishTime-startTime);
					visitedH2.add(nb);
					
				}
				line = reader.readLine();
			}
			reader.close();
			writer.close();
		}catch(IOException e){
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	
	public static double mean(List<? extends Number> l){
		double res=0;
		for (Number val: l)
			res+=val.doubleValue();
		return (1.0*res) / l.size();
	}
	
	public static double std(List<? extends Number> l){
		double m = mean(l);
		double res=0;
		for (Number val: l)
			res+= Math.pow(val.doubleValue()-m,2);
		return Math.sqrt(res / l.size());	
	}
	
	public static int h1(Puzzle p) {
		int cost=0;
		for (int i=0;i<9;i++){//for each tile, compute Manhattan distance to goal state 
			short a = Short.parseShort(p.getDesc().substring(i,i+1));
			int ia = a/3;
			int ja = a%3;
			int ii = i/3;
			int ji = i%3;
			if (ia != ii || ja != ji)
				cost += 1;
		}
		return cost;	
	}
	
	public static int h2(Puzzle p){
		int cost=0;
		for (int i=0;i<9;i++){//for each tile, compute Manhattan distance to goal state 
			short a = Short.parseShort(p.getDesc().substring(i,i+1));
			int ia = a/3;
			int ja = a%3;
			int ii = i/3;
			int ji = i%3;
			cost += Math.abs(ia-ii)+Math.abs(ja-ji); 
		}
		return cost;	
	}

}
