package upb.dice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


public class TripleCreator {
	
	public static void main(String[] args) {
		String path = "D:\\Nikit\\ProjectGroup_WS\\Datasets\\";
		String inputFileName = "citydistance-raw.csv";
		String outputFileName = "citydistance-final.csv";
		createTripleFile(path, inputFileName, outputFileName);
	}

	public static void createTripleFile(String path, String inputFileName, String outputFileName) {
		CSVReader reader;
		try {
			String[] line;
			List<String> cities = new ArrayList<>();
			reader = new CSVReader(new FileReader(path + inputFileName));
			File yourFile = new File(path + outputFileName);
			yourFile.createNewFile();
			yourFile.createNewFile();
			BufferedWriter writer = Files.newBufferedWriter(yourFile.toPath());
			CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
			csvWriter.writeNext(new String[]{"city1","city2","distance"});
			line = reader.readNext();
			for (int i = 1; i < line.length; i++) {
				cities.add(line[i]);
			}
			while ((line = reader.readNext()) != null) {
				String city1 = line[0];
				for (int i = 1; i < line.length; i++) {
					String distance = line[i];
					String city2 = cities.get(i-1);
					if (city1.equalsIgnoreCase(city2)) {
						continue;
					}
					csvWriter.writeNext(new String[]{city1,city2,distance});
				}

			}
			csvWriter.close();
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
