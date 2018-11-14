package upb.dice;

import java.io.IOException;

public class Temp {

	public static void main(String[] args) throws IOException {
		/// System.out.println(hello);
		int[] spaghetti = { 3, 13, 14, 20 };
		System.out.println(cut(spaghetti, 2));
	}

	public static int cut(int[] spaghetti, int maximumCuts) {
		int[] divRes = new int[spaghetti.length];
		int[] cutRes = new int[spaghetti.length];	
		int[] posArr = new int[spaghetti.length];
		float[] scoreRes = new float[spaghetti.length];
		int highestScore = -1;
		for (int i = 0; i < spaghetti.length; i++) {
			posArr[i] = i;
			divRes[i] = spaghetti[i] / 10;
			if (spaghetti[i] == 10) {
				cutRes[i] = 0;
				scoreRes[i] = highestScore;
			} else if (divRes[i] > 0) {
				cutRes[i] = spaghetti[i] % 10 == 0 ? divRes[i] - 1 : divRes[i];
				scoreRes[i] = divRes[i] / cutRes[i];
			} else {
				cutRes[i] = 0;
				scoreRes[i] = 0;
			}
		}
		for (int i = 0; i < spaghetti.length; i++) {
			for (int j = i + 1; j < spaghetti.length; j++) {
				float iScore = scoreRes[i];
				float jScore = scoreRes[j];
				if (iScore < jScore || jScore == -1) {
					scoreRes[i] = jScore;
					scoreRes[j] = iScore;
					posArr[i] = j;
					posArr[j] = i;
				}
			}
		}
		int spaghettiCount = 0;
		int cuts = 0;
		int cutDiff;
		for (int pos : posArr) {
			cutDiff = maximumCuts - cuts;
			if (cutDiff == 0) {
				break;
			} else if (cutRes[pos] <= cutDiff) {
				spaghettiCount += divRes[pos];
				cuts += cutRes[pos];
			} else {
				spaghettiCount += cutDiff;
				cuts += cutDiff;
			}
		}
		return spaghettiCount;
	}
}
