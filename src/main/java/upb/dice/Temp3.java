package upb.dice;

import java.util.Arrays;

import org.dice_research.topicmodeling.commons.sort.AssociativeSort;

public class Temp3 {

	public static int[] arr1 = { 23, 2323, 1, 343, 2, 2342, 4468, 77, 665, 4543, 799975, 2323, 356, 567 };
	
	public static void main(String[] args) {
		int[] tArr = new int[arr1.length];
		for(int i=0;i<tArr.length;i++) {
			tArr[i] = i;
		}
		//sortArr(arr1);
		AssociativeSort.quickSort(arr1, tArr);
		int index = Arrays.binarySearch(arr1, 799995);
		System.out.println(index);
	}
	
	public static void  sortArr(int[] arr) {
		Arrays.sort(arr);
	}

}
