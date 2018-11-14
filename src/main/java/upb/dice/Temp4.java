package upb.dice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Temp4 {

	public static void main(String[] args) {
		String[] arr = { "a", "b", "c", "d", "e" };
		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList(arr));
		System.out.println(powerSet(set));
	}

	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<Set<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<T>(originalSet);
		T head = list.get(0);
		Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
		for (Set<T> set : powerSet(rest)) {
			Set<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	Map<TreeSet<Integer>, Integer> resMap;
	List<BitSet> setList;
	Integer maxSize;

	public int findVennRelations(Integer head, BitSet resSet) {
		int count = 0;
		if (head < maxSize) {
			

		} else {
			BitSet lastSet = setList.get(head);
			BitSet curSet = (BitSet) resSet.clone();
			curSet.and(resSet);
			count = curSet.cardinality();
		}
		return count;
	}

}
