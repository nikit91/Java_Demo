package upb.dice.w2v;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class Word2VecDemo2 {

	public static void main(String[] args) {
		File gModel = new File("/Users/nikitsrivastava/nikit_ws/eclipse_ws/Jword2vec/data/GoogleNews-vectors-negative300.bin");
	    Word2Vec vec = WordVectorSerializer.readWord2VecModel(gModel);
	    
	    String[] wordList = {"cat", "dog", "airplane", "road"};
	    List<String> resList = new ArrayList<String>();
	    resList.addAll(vec.wordsNearest("dummy", 1));
	    long strtTime = System.currentTimeMillis();
	    for(String word: wordList) {
	    	resList.addAll(vec.wordsNearest(word, 1));
	    }
        long totTime = System.currentTimeMillis() - strtTime;
        System.out.println(" total time taken: "+totTime+" ms \n average time per query: "+(totTime/wordList.length)+" ms");
        System.out.println(resList);
        /*double cosSim = vec.similarity("day", "night");
        System.out.println(cosSim);*/
        
	}

}
