package upb.rcc;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SparqlDemo {
	
	public static void main(String[] args) {
		StringBuilder queryStr = new StringBuilder();
		queryStr.append(DummySparql.QUERY_PREFIX);
		queryStr.append("SELECT DISTINCT ?s (str(?lbl) as ?label) (str(?about) as ?abs) ");
		queryStr.append(" WHERE { ");
		queryStr.append(" ?s dct:subject <http://dbpedia.org/resource/Category:Symptoms_and_signs:_Cognition,_perception,_emotional_state_and_behaviour> . ");
		queryStr.append(" ?s rdfs:label ?lbl .");
		queryStr.append(" ?s dbo:abstract ?about . ");
		queryStr.append(" FILTER langMatches(lang(?about) , 'en') . ");
		queryStr.append(" FILTER langMatches(lang(?lbl) , 'en') . ");
		queryStr.append(" } ");
		ResultSet res = null;
		Query query = QueryFactory.create(queryStr.toString(), Syntax.syntaxARQ);
		// Remote execution.
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
			// Set the DBpedia specific timeout.
			((QueryEngineHTTP) qexec).addParam("timeout", "10000");
			// Execute.
			res = qexec.execSelect();
			while(res.hasNext()) {
				System.out.println(res.next());
			}
		} catch (Exception e) {
			System.out.println("Query Failed: "+queryStr);
			e.printStackTrace();
		}
	}

}
