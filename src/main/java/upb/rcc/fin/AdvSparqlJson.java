package upb.rcc.fin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import upb.rcc.AdvSparqlTsv;
import upb.rcc.Methodology;

public class AdvSparqlJson extends AdvSparqlTsv {
	
	
	

	public static void main(String[] args) {
		
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		try {
			process(inputFile, outputFile);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void process(File inputFile, File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		LinkedList<String> linkedList = new LinkedList<>();
		// Load all categories from file
		linkedList.addAll(Files.readAllLines(inputFile.toPath()));
		
		Map<String, List<Methodology>> resMap = new HashMap<String, List<Methodology>>();
		while (linkedList.size() > 0) {
			System.out.println("Current Queue size: "+linkedList.size());
			String category = linkedList.poll();
			System.out.println("Current Category: "+category);
			// generate query to fetch methods
			String methodQuery = genMethodsQuery(category);
			fetchAllMethods(executeSparql(methodQuery), resMap);
			List<Methodology> methodList = resMap.get(category);
			System.out.println("Current Category's resources size: "+(methodList==null?0:methodList.size()));
		}
		
		System.out.println("Final ResMap Category Size: "+resMap.size());
		System.out.println("Final ResMap Values Size: "+getValuesSize(resMap.values()));
		// get json node of map
		JsonNode jsonNode = generateJsonNode(resMap);
		writeJsonToFile(jsonNode, outputFile);
	}
	
	public static String genMethodsQuery(String category) {

		StringBuilder methodsQuery = new StringBuilder();

		methodsQuery.append(QUERY_PREFIX);
		methodsQuery.append(METHOD_QUERY_PRT1);
		methodsQuery.append(WHERE_PRT);
		methodsQuery.append(String.format(SUBJ_PARAM_STR, category));
		methodsQuery.append(METHOD_QUERY_PRT2);
		methodsQuery.append(String.format(CATEG_FILTER_STR, category));
		methodsQuery.append(END_PRT);

		return methodsQuery.toString();

	}

}
