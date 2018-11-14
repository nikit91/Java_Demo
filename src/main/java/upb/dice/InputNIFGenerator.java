package upb.dice;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import com.opencsv.CSVReader;


public class InputNIFGenerator {

	public static StringBuilder nifForm;
	static {
		nifForm = new StringBuilder();
		nifForm.append("@prefix rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
		nifForm.append("@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .\n");
		nifForm.append("@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n");
		nifForm.append("@prefix nif:  <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n");
		nifForm.append("@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n");
		nifForm.append("@prefix dbr: <http://dbpedia.org/resource/> .\n");
		nifForm.append("@prefix dbo: <http://dbpedia.org/ontology/> .\n");
		nifForm.append("@prefix aksw: <http://aksw.org/notInWiki/> .\n");
		nifForm.append("@prefix oa: <http://www.w3.org/ns/oa#> .\n");
		nifForm.append("<%s>\n");
		nifForm.append("\ta  nif:RFC5147String , nif:String , nif:Context ;\n");
		nifForm.append("\tnif:beginIndex  \"0\"^^xsd:nonNegativeInteger ;\n");
		nifForm.append("\tnif:endIndex  \"%d\"^^xsd:nonNegativeInteger ;\n");
		nifForm.append("\tnif:isString \"%s\"^^xsd:string .\n");

	}

	public static void main(String[] args) {
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader("C:\\Users\\Nikit\\Google Drive\\MS (Study folder)\\UPB\\DICE-Group\\Gerbil\\okeds\\input.csv"));
			String[] line;
			int i = 1;
			String tempUri, tempText;
			File yourFile;

			while ((line = reader.readNext()) != null) {
				yourFile = new File("C:\\Users\\Nikit\\Google Drive\\MS (Study folder)\\UPB\\DICE-Group\\Gerbil\\okeds\\output\\output_" + i++ + ".ttl");
				yourFile.createNewFile();
				// write to file
				tempUri = line[0];
				tempText = line[1];
				Files.write(yourFile.toPath(),
						String.format(nifForm.toString(), tempUri, tempText.length(), tempText).getBytes());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
