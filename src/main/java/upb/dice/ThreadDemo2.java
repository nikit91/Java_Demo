package upb.dice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadDemo2 {

	static class Thrd1 implements Runnable {
		List<String> strList;
		int size;

		public Thrd1(List<String> strList, int size) {
			this.strList = strList;
			this.size = size;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < size; i++) {
					int indx = ThreadLocalRandom.current().nextInt(0, size);
					// System.out.println(strMap.get(indx));
					strList.add("thrd1_" + indx);
					// Thread.sleep(10);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static class Thrd2 implements Runnable {
		List<String> strList;
		int size;

		public Thrd2(List<String> strList, int size) {
			this.strList = strList;
			this.size = size;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < size; i++) {
					int indx = ThreadLocalRandom.current().nextInt(0, size);
					// System.out.println(strMap.get(indx));
					strList.add("thrd2_" + indx);
					// Thread.sleep(10);

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
		List<String> demList = new CopyOnWriteArrayList<>();
		int size = 100000;
		Thrd1 thrd1 = new Thrd1(demList, size);
		Thrd2 thrd2 = new Thrd2(demList, size);

		Thread thread1 = new Thread(thrd1);
		Thread thread2 = new Thread(thrd2);

		thread1.start();
		thread2.start();

	}

}
