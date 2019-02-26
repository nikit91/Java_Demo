package upb.dice.w2v;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.reader.impl.TreeModelUtils;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Word2VecDemo {

	public static final float[][] TEST_CENTROIDS = { TestConst.CENT1, TestConst.CENT2, TestConst.CENT3, TestConst.CENT4,
			TestConst.CENT5, TestConst.CENT6, TestConst.CENT7, TestConst.CENT8, TestConst.CENT9, TestConst.CENT10,
			TestConst.CENT11, TestConst.CENT12, TestConst.CENT13, TestConst.CENT14, TestConst.CENT15, TestConst.CENT16,
			TestConst.CENT17, TestConst.CENT18, TestConst.CENT19, TestConst.CENT20 };
//	public static final float[][] TEST_CENTROIDS = { TestConst.CENT4};
	public static final String[] TEST_WORDS = { "cat", "dog", "airplane", "road", "kennedy", "rome", "human", "disney",
			"machine", "intelligence", "palaeontology", "surgeon", "amazon", "jesus", "gold", "atlantis", "ronaldo",
			"pele", "scissors", "lizard" };

	protected static INDArray wrapVecToNDArr(float[] vec) {
		INDArray wordVec = Nd4j.create(1, vec.length);
		for (int i = 0; i < vec.length; i++) {
			wordVec.put(0, i, vec[i]);
		}
		return wordVec;
	}

	protected static INDArray[] getTestData(float[][] testCentroids) {
		INDArray[] resArr = new INDArray[testCentroids.length];
		for (int i = 0; i < testCentroids.length; i++) {
			float[] curVec = testCentroids[i];
			resArr[i] = wrapVecToNDArr(curVec);
		}
		return resArr;
	}

	protected static Word2Vec getW2VModel(String filePath, boolean isTree) {
		File gModel = new File(filePath);
		Word2Vec vec = WordVectorSerializer.readWord2VecModel(gModel);
		if (isTree) {
			vec.setModelUtils(new TreeModelUtils<VocabWord>());
		}
		return vec;
	}

	public static void main(String[] args) {
		Word2Vec vec = getW2VModel(args[0],false);
		INDArray[] testData = getTestData(TEST_CENTROIDS);
		List<String> resList = new ArrayList<String>();
		resList.addAll(vec.wordsNearest("dummy", 1));
		long strtTime = System.currentTimeMillis();
		for (INDArray word : testData) {
			resList.addAll(vec.wordsNearest(word, 5));
		}
		long totTime = System.currentTimeMillis() - strtTime;
		System.out.println(" total time taken: " + totTime + " ms \n average time per query: "
				+ (totTime / testData.length) + " ms");
		System.out.println(resList);
	}

}
