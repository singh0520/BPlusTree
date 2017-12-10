import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class treesearch {

	private static final String Insert = "Insert";
	private static final String Search = "Search";
	private static final String FILENAME="output_file.txt";
    public static int degree;
    
	public static void main(String[] args) throws FileNotFoundException {
		String filename = args[0];
		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fr = new FileReader(filename+".txt");
			br = new BufferedReader(fr);
			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			
			String currentLine = br.readLine();
			degree = Integer.parseInt(currentLine);
			BplusTree bPlusTree = new BplusTree(degree);
			while((currentLine = br.readLine()) != null) {
				if(currentLine.startsWith(Insert)) {
					String[] line = currentLine.split("Insert");
					String argument = line[1];
					String[] part = argument.split(",");
					String k = part[0];
					String part2 = part[1];
					double key = Double.parseDouble(k.replace("(", ""));
					String value = part2.replace(")", "");
					bPlusTree.insert(key, value);
				} else if(currentLine.startsWith(Search)) {
					String[] line = currentLine.split("Search");
					String argument = line[1];
					if(argument.contains(",")){
						String[] part = argument.split(",");
						String k1 = part[0];
						String k2 = part[1];
						double key1 = Double.parseDouble(k1.replace("(", ""));
						double key2 = Double.parseDouble(k2.replace(")", ""));
						bw.write(bPlusTree.search(key1,key2)+"\n");
					} else {
						String k = argument.replace("(", "");
						double key = Double.parseDouble(k.replace(")", ""));
						bw.write(bPlusTree.search(key)+"\n");
					}
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
