package upb.rcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class DummySparql {

	public static final ObjectMapper OBJ_MAPPER = new ObjectMapper();
	public static final ObjectReader OBJ_READER = OBJ_MAPPER.reader();
	public static final ObjectWriter OBJ_WRITER = OBJ_MAPPER.writer(new DefaultPrettyPrinter());
	public static final JsonNodeFactory JSON_NODE_FACTORY = OBJ_MAPPER.getNodeFactory();
	public static int tempId=1;
	public static final StringBuilder QUERY_PREFIX = new StringBuilder();
	public static final StringBuilder BROADER_QUERY_STR = new StringBuilder();
	public static final StringBuilder SUBJ_QUERY_STR = new StringBuilder();
	static {
		QUERY_PREFIX.append("PREFIX dbo: <http://dbpedia.org/ontology/> ");
		QUERY_PREFIX.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns/> ");
		QUERY_PREFIX.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		QUERY_PREFIX.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ");
		QUERY_PREFIX.append("PREFIX dct: <http://purl.org/dc/terms/> ");

		BROADER_QUERY_STR.append(QUERY_PREFIX);
		BROADER_QUERY_STR.append("SELECT distinct ?m ");
		BROADER_QUERY_STR.append(" WHERE { ");
		BROADER_QUERY_STR.append(" ?m skos:broader <%s> . ");
		BROADER_QUERY_STR.append(" } ");

		SUBJ_QUERY_STR.append(QUERY_PREFIX);
		SUBJ_QUERY_STR.append("SELECT DISTINCT ?s (str(?lbl) as ?label) (str(?about) as ?abs) ");
		SUBJ_QUERY_STR.append(" WHERE { ");
		SUBJ_QUERY_STR.append(" ?s dct:subject <%s> . ");
		SUBJ_QUERY_STR.append(" ?s rdfs:label ?lbl .");
		SUBJ_QUERY_STR.append(" ?s dbo:abstract ?about . ");
		SUBJ_QUERY_STR.append(" FILTER langMatches(lang(?about) , 'en') . ");
		SUBJ_QUERY_STR.append(" FILTER langMatches(lang(?lbl) , 'en') . ");
		SUBJ_QUERY_STR.append(" } ");
	}

	public static void main(String[] args) {

		File outputFile = new File("data/methods_abstract.json");
		try {
			process(outputFile);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void process(File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		LinkedList<String> linkedList = new LinkedList<>();
		linkedList.add("http://dbpedia.org/resource/Category:Methodology");
		Map<String, List<Methodology>> resMap = new HashMap<String, List<Methodology>>();
		while (linkedList.size() > 0) {
			String methodCategoryUri = linkedList.poll();
			System.out.println("Processing for Category: "+methodCategoryUri);
			/*
			 * Fetch all the method categories that have the current method category as
			 * their "skos:broader"
			 */
			String broadQuery = String.format(BROADER_QUERY_STR.toString(), methodCategoryUri);
			List<QuerySolution> broadRes = executeSparql(broadQuery);
			List<String> methodCategories = fetchAllUri("m", broadRes);
			// Add all the new method categories to the queue
			linkedList.addAll(methodCategories);
			/*
			 * Fetch all the method resources and their abstracts that have the current
			 * method category as their "dct:subject"
			 */
			String subjQuery = String.format(SUBJ_QUERY_STR.toString(), methodCategoryUri);
			List<QuerySolution> subjRes = executeSparql(subjQuery);
			List<Methodology> methodsList = fetchAllMethods(subjRes);
			System.out.println("Methods found: "+methodsList.size());
			// Add entry to map
			resMap.put(methodCategoryUri, methodsList);

		}
		// get json node of map
		JsonNode jsonNode = generateJsonNode(resMap);
		writeJsonToFile(jsonNode, outputFile);
	}

	public static List<String> fetchAllUri(String varName, List<QuerySolution> res) {
		List<String> resList = new ArrayList<String>();
		for (QuerySolution entry : res) {
			String uri = entry.get(varName).toString();
			resList.add(uri);
		}

		return resList;
	}

	public static List<Methodology> fetchAllMethods(List<QuerySolution> res) {
		List<Methodology> resList = new ArrayList<>();
		for (QuerySolution entry : res) {
			String uri = entry.get("s").toString();
			String label = entry.get("label").toString();
			String abs = entry.get("abs").toString();
			Methodology method = new Methodology(tempId++, uri, label, abs);
			resList.add(method);
		}
		return resList;
	}

	public static List<QuerySolution> executeSparql(String queryStr) {
		ResultSet res = null;
		List<QuerySolution> querySolutionList = new ArrayList<>();
		Query query = QueryFactory.create(queryStr.toString());
		// Remote execution.
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
			// Set the DBpedia specific timeout.
			((QueryEngineHTTP) qexec).addParam("timeout", "10000");
			// Execute.
			res = qexec.execSelect();
			while(res.hasNext()) {
				querySolutionList.add(res.next());
			}
		} catch (Exception e) {
			System.out.println("Query Failed: "+queryStr);
			e.printStackTrace();
		}
		// sleep
		sleep(1000);
		return querySolutionList;
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static JsonNode generateJsonNode(Map<String, List<Methodology>> resMap) {
		JsonNode jsonNode = OBJ_MAPPER.valueToTree(resMap);
		return jsonNode;
	}

	/**
	 * Method to write a json object to a file
	 * 
	 * @param node       - json object to be written
	 * @param outputFile - file to write json in
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void writeJsonToFile(JsonNode node, File outputFile)
			throws JsonGenerationException, JsonMappingException, IOException {
		// ensure directory creation
		outputFile.getParentFile().mkdirs();
		OBJ_WRITER.writeValue(outputFile, node);
	}

}
