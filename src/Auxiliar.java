import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class Auxiliar {
	private static final String NETWORK_ANALYSIS_FILE = "../visualAnalysis/index.html";
	private ArrayList<InstantAnalysis> analysis;
	private String type;
	private boolean withIperf;
	
	public Auxiliar(String type, boolean withIperf) {
		this.analysis = new ArrayList<>();
		this.type = type;
		this.withIperf = withIperf;
	}
	
	public void log(int windowSize, int losses, long timer,int sequence) {
		System.out.println(String.format("%s %d - Window Size: %d - Losses: %d - Timer: %d", 
				  this.type,sequence,windowSize,losses,timer));
		saveInstant(timer, windowSize, losses,sequence);
	}
	
	private void saveInstant(long timeout, int windowSize, int losses, int sequence) {
		this.analysis.add(new InstantAnalysis(timeout, windowSize, losses,sequence));
	}
	
	public void saveStructure(long time) throws IOException {
		File fileToBeModified = new File(NETWORK_ANALYSIS_FILE);
		BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
		String content = "";
		String line = reader.readLine();

		while (line != null)
			{
			content = content + line;
		    line = reader.readLine();
		}
		reader.close();
		
		int index = getIndex();
		String prefix = getPrefix();
		
		String[] splittedContent = content.split(";");
		
		String newData = prefix;
		
		for (int i = 0; i < analysis.size(); i++) {
			InstantAnalysis instantAnalysis = analysis.get(i);
			newData+=instantAnalysis.toString();
				newData+=",";
		}
		
		if (!newData.equals("")) {
			newData+=time;
			splittedContent[index] = newData;
		}
		
		FileWriter writer = new FileWriter(NETWORK_ANALYSIS_FILE);
		
		for (int i = 0; i < splittedContent.length; i++) {
			if(i+1==splittedContent.length){
				writer.write(splittedContent[i]+";");
			}else{
				writer.write(splittedContent[i]+";");
			}
		}
		writer.close();
	}
	
	private String getPrefix() {
		if(type.equals("udp")) {
			if(withIperf) {
				return "udpWithIperf_";
			}
			return "udpWithoutIperf_";
		}else {
			if(withIperf) {
				return "tcpWithIperf_";
			}
			return "tcpWithoutIperf_";
		}
	}

	public int getIndex() {
		if(type.equals("udp")) {
			if(withIperf) {
				return 3;
			}
			return 1;
		}else {
			if(withIperf) {
				return 4;
			}
			return 2;
		}
	}

	public int getNumberOfLines(String fileName) {
		LineNumberReader arquivo;
		int count=0;
		try {
			arquivo = new LineNumberReader(new FileReader(fileName));
			String line;
			
			while(( line = arquivo.readLine())!=null) {
				count++;
			}
			arquivo.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		return count;
	}

	public LineNumberReader readerInLine(int lineThatINeedToGo, String fileName) {
		
	
		try {
			LineNumberReader arquivo;
			arquivo = new LineNumberReader(new FileReader(fileName));
			
			
			for (int i = 0; i < lineThatINeedToGo; i++) {
				arquivo.readLine();
			}
			
			return arquivo;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
