package upb.rcc.fin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import upb.rcc.AdvSparqlTsv;
import upb.rcc.Methodology;

public class AdvSparqlTsv_New extends AdvSparqlTsv {


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
			List<String> categoryList = new ArrayList<>();
			for (int i = 0; i < UNION_SIZE; i++) {
				categoryList.add(linkedList.poll());
				if (linkedList.size() == 0) {
					break;
				}
			}
			System.out.println("Current Category List Size: "+categoryList.size());
			// generate query to fetch methods
			String methodQuery = genMethodsQuery(categoryList);
			fetchAllMethods(executeSparql(methodQuery), resMap);
			System.out.println("Current Map Category Size: "+resMap.size());
		}
		
		System.out.println("Final ResMap Category Size: "+resMap.size());
		System.out.println("Final ResMap Values Size: "+getValuesSize(resMap.values()));
		// get json node of map
		JsonNode jsonNode = generateJsonNode(resMap);
		writeJsonToFile(jsonNode, outputFile);
	}

}
