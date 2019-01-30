package upb.rcc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

public class AdvSparqlTsv extends DummySparql {
	
	public static final int UNION_SIZE = 50;

	public static final StringBuilder CATEGORY_QUERY_PRT1 = new StringBuilder();
	public static final StringBuilder METHOD_QUERY_PRT1 = new StringBuilder();
	public static final StringBuilder METHOD_QUERY_PRT2 = new StringBuilder();

	public static final StringBuilder WHERE_PRT = new StringBuilder();
	public static final StringBuilder UNION_PRT = new StringBuilder();
	public static final StringBuilder END_PRT = new StringBuilder();

	public static final String BROADER_PARAM_STR = " { ?c skos:broader <%s> . } ";
	public static final String SUBJ_PARAM_STR = " { ?m dct:subject <%s> . } ";
	public static final String CATEG_FILTER_STR = " FILTER(?c=<%s>) . ";
	
	static {
		WHERE_PRT.append(" WHERE { ");
		UNION_PRT.append(" UNION ");
		END_PRT.append(" } ");

		CATEGORY_QUERY_PRT1.append("SELECT distinct ?c ");

		METHOD_QUERY_PRT1.append("SELECT DISTINCT ?c ?m (str(?lbl) as ?label) (str(?about) as ?abs) ");

		METHOD_QUERY_PRT2.append(" ?m dct:subject ?c . ");
		METHOD_QUERY_PRT2.append(" ?m rdfs:label ?lbl .");
		METHOD_QUERY_PRT2.append(" ?m dbo:abstract ?about . ");
		METHOD_QUERY_PRT2.append(" FILTER langMatches(lang(?about) , 'en') . ");
		METHOD_QUERY_PRT2.append(" FILTER langMatches(lang(?lbl) , 'en') . ");
	}

	public static void main(String[] args) {

		File outputFile = new File(args[0]);
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
			System.out.println("Current Queue size: "+linkedList.size());
			List<String> categoryList = new ArrayList<>();
			for (int i = 0; i < UNION_SIZE; i++) {
				categoryList.add(linkedList.poll());
				if (linkedList.size() == 0) {
					break;
				}
			}
			System.out.println("Current Category List Size: "+categoryList.size());
			// generate query to fetch sub categories
			String subCategoryQuery = genSubCategoryQuery(categoryList);
			List<String> subCategoryList = fetchAllUri("c", executeSparql(subCategoryQuery));
			// add to existing linkedlist
			linkedList.addAll(subCategoryList);
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
	
	public static <T> int getValuesSize(Collection<List<T>> col) {
		int count = 0;
		for(Collection<T> inCol : col) {
			count+=inCol.size();
		}
		return count;
	}

	public static String genSubCategoryQuery(List<String> subCategoryList) {
		int len = subCategoryList.size();

		StringBuilder broaderQuery = new StringBuilder();

		broaderQuery.append(QUERY_PREFIX);
		broaderQuery.append(CATEGORY_QUERY_PRT1);
		broaderQuery.append(WHERE_PRT);
		for (int i = 0; i < len; i++) {
			broaderQuery.append(String.format(BROADER_PARAM_STR, subCategoryList.get(i)));
			if (i == len - 1) {
				break;
			}
			broaderQuery.append(UNION_PRT);
		}
		broaderQuery.append(END_PRT);

		return broaderQuery.toString();

	}

	public static String genMethodsQuery(List<String> subCategoryList) {
		int len = subCategoryList.size();

		StringBuilder methodsQuery = new StringBuilder();

		methodsQuery.append(QUERY_PREFIX);
		methodsQuery.append(METHOD_QUERY_PRT1);
		methodsQuery.append(WHERE_PRT);
		for (int i = 0; i < len; i++) {
			methodsQuery.append(String.format(SUBJ_PARAM_STR, subCategoryList.get(i)));
			if (i == len - 1) {
				break;
			}
			methodsQuery.append(UNION_PRT);
		}
		methodsQuery.append(METHOD_QUERY_PRT2);
		methodsQuery.append(END_PRT);

		return methodsQuery.toString();

	}

	public static void fetchAllMethods(List<QuerySolution> res, Map<String, List<Methodology>> resMap) {

		for (QuerySolution entry : res) {
			String uri = entry.get("m").toString();
			String label = entry.get("label").toString();
			String abs = entry.get("abs").toString();
			String category = entry.get("c").toString();
			Methodology method = new Methodology(tempId++, uri, label, abs);
			List<Methodology> resList = resMap.get(category);
			if (resList == null) {
				resList = new ArrayList<>();
				resMap.put(category, resList);
			}
			resList.add(method);
		}
	}
	
	public static List<QuerySolution> executeSparql(String queryStr) {
		ResultSet res = null;
		List<QuerySolution> querySolutionList = new ArrayList<>();
		Query query = QueryFactory.create(queryStr.toString(), Syntax.syntaxARQ);
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
			System.out.println("Query Failed: "+query);
			e.printStackTrace();
		}
		// sleep
		sleep(1000);
		return querySolutionList;
	}

}
