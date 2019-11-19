import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import sun.misc.IOUtils;

public class Auxiliar {
	private static final String NETWORK_ANALYSIS_FILE = "./last_network_analysis.txt";
	private ArrayList<InstantAnalysis> analysis;
	private String type;
	private boolean withIperf;
	
	public Auxiliar(String type, boolean withIperf) {
		this.analysis = new ArrayList<>();
		this.type = type;
		this.withIperf = withIperf;
	}
	
	public void log(int windowSize, int losses, long timer) {
		System.out.println(String.format(
				  "%s - Window Size: %d - Losses: %d - Timer: &d", 
				  this.type,windowSize,losses,timer));
		saveInstant(timer, windowSize, losses);
	}
	
	private void saveInstant(long timeout, int windowSize, int losses) {
		this.analysis.add(new InstantAnalysis(timeout, windowSize, losses));
	}
	
	public void saveStructure() throws IOException {
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
			if(i+1==analysis.size()) {
				newData+=",";
			}
		}
		if (!newData.equals("")) {
			splittedContent[index] = newData;
		}
		
		FileWriter writer = new FileWriter(NETWORK_ANALYSIS_FILE);
		
		writer.write("DO NOT MODIFY THIS FILE;");
		for (int i = 0; i < splittedContent.length; i++) {
			writer.write(splittedContent[i]+";\n");
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
	
	
}