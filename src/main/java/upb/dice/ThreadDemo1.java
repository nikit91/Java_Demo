package upb.dice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadDemo1 {

	static class Thrd1 implements Runnable {
		Map<Integer, String> strMap;

		public Thrd1(Map<Integer, String> strMap) {
			this.strMap = strMap;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < strMap.size(); i++) {
					int indx = ThreadLocalRandom.current().nextInt(0, strMap.size());
					// System.out.println(strMap.get(indx));
					strMap.put(indx, "thrd1_" + indx);
					//Thread.sleep(10);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class Thrd2 implements Runnable {
		Map<Integer, String> strMap;

		public Thrd2(Map<Integer, String> strMap) {
			this.strMap = strMap;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < strMap.size(); i++) {
					int indx = ThreadLocalRandom.current().nextInt(0, strMap.size());
					// System.out.println(strMap.get(indx));
					strMap.put(indx, "thrd2_" + indx);
					//Thread.sleep(10);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// method to populate a list
	public static Map<Integer, String> fetchStrList(int size) {
		Map<Integer, String> strMap = new HashMap<Integer, String>();
		for (int i = 0; i < size; i++) {
			strMap.put(i, "item" + i);
		}
		return strMap;
	}

	public static void main(String[] args) {
		Map<Integer, String> strMap = fetchStrList(100000);
		Thrd1 thrd1 = new Thrd1(strMap);
		Thrd2 thrd2 = new Thrd2(strMap);

		Thread thread1 = new Thread(thrd1);
		Thread thread2 = new Thread(thrd2);

		thread1.start();
		thread2.start();

	}

}
