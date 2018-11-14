package upb.dice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DemoMain {

	/*
	 * public static void main(String[] args) {
	 * 
	 * // loads model in memory final Word2VecModel model = Word2VecFactory.get();
	 * final float[] vecCat = model.word2vec.get("cat"); final float[] vecDog =
	 * model.word2vec.get("dog"); final float[] vecItaly =
	 * model.word2vec.get("Italy"); final float[] vecFrance =
	 * model.word2vec.get("France"); final float[] vecInstead =
	 * model.word2vec.get("instead"); final float[] vecWhether =
	 * model.word2vec.get("whether"); final float[] vecHello =
	 * model.word2vec.get("hello"); final float[] vecBand =
	 * model.word2vec.get("band"); final float[] vecBeer =
	 * model.word2vec.get("beer"); final float[] vecBear =
	 * model.word2vec.get("bear"); final float[] vecBang =
	 * model.word2vec.get("bang");
	 * 
	 * vecCat[34] = 234; vecDog[12] = 342; vecItaly[80] = 12; vecItaly[23] = 212;
	 * vecFrance[76] = 12; vecFrance[26] = 34; vecFrance[35] = 1231; vecInstead[37]
	 * = 35; vecWhether[29] = 67; vecHello[78] = 34; vecBand[76] = -12; vecBeer[23]
	 * = 32; vecBear[56] = -8; vecBang[13] = 23;
	 * 
	 * System.out.println(model.getClosestEntry(vecCat));
	 * System.out.println(model.getClosestEntry(vecDog));
	 * System.out.println(model.getClosestEntry(vecItaly));
	 * System.out.println(model.getClosestEntry(vecFrance));
	 * System.out.println(model.getClosestEntry(vecInstead));
	 * System.out.println(model.getClosestEntry(vecWhether));
	 * System.out.println(model.getClosestEntry(vecHello));
	 * System.out.println(model.getClosestEntry(vecBand));
	 * System.out.println(model.getClosestEntry(vecBeer));
	 * System.out.println(model.getClosestEntry(vecBear));
	 * System.out.println(model.getClosestEntry(vecBang));
	 * 
	 * final double sim = Word2VecMath.cosineSimilarity(vecCat, vecDog);
	 * System.out.println(sim);
	 * 
	 * }
	 */

	/*
	 * public static void main(String[] args) { int[] data =
	 * {334,11,7,3,8,96,2,265,334}; for(int i=0;i<data.length;i++) { for(int
	 * j=i+1;j<data.length;j++) { if(data[i] > data[j]) { int temp = data[i];
	 * data[i] = data[j]; data[j] = temp; } } }
	 * 
	 * for(int x: data) { System.out.print(x+" "); } }
	 */

	public static void main(String[] args) {
		List<String> demList = new ArrayList<String>();
		for (int i = 1; i < 100000; i++) {
			demList.add("entry" + i);
		}
		int itNum = 500;
		long feTimeTot = 0;
		long itrTimeTot = 0;
		long pmItrTimeTot= 0;
		Iterator<String> it;
		for (int i = 0; i < itNum; i++) {
			if (i > 100) {
				it = demList.iterator();
				pmItrTimeTot = runPreMadeIterator(it);
				feTimeTot += runForEach(demList);
				itrTimeTot += runIterator(demList);
			}
		}
		long avgFETime = feTimeTot / itNum;
		long avgItrTime = itrTimeTot / itNum;
		long avgPmItrTime = pmItrTimeTot / itNum;
		System.out.println("Total traversal time through foreach is: " + avgFETime + " nanosecs");
		System.out.println("Total traversal time through iterator is: " + avgItrTime + " nanosecs");
		System.out.println("Total traversal time through pre initialized iterator is: " + avgPmItrTime + " nanosecs");
	}

	public static long runForEach(List<String> demList) {
		int jCount = 0;
		long qryStrtTm = System.nanoTime();
		for (String entry : demList) {
			if (entry instanceof String) {
				jCount++;
			}
		}
		return (System.nanoTime() - qryStrtTm);
	}

	public static long runIterator(List<String> demList) {
		int jCount = 0;
		long qryStrtTm = System.nanoTime();
		Iterator<String> it = demList.iterator();
		String entry;
		while (it.hasNext()) {
			entry = it.next();
			if (entry instanceof String) {
				jCount++;
			}
		}
		return (System.nanoTime() - qryStrtTm);
	}
	
	public static long runPreMadeIterator(Iterator<String> it) {
		int jCount = 0;
		long qryStrtTm = System.nanoTime();
		String entry;
		while (it.hasNext()) {
			entry = it.next();
			if (entry instanceof String) {
				jCount++;
			}
		}
		return (System.nanoTime() - qryStrtTm);
	}
}
