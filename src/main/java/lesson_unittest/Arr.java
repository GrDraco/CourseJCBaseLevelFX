package lesson_unittest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arr {
    public static int[] cut(int[] arr) {
        if(arr == null || Arrays.stream(arr).noneMatch(item -> item == 4))
            throw new RuntimeException();
        List<Integer> res = new ArrayList<>();
        for (int i = arr.length - 1; i >= 0; i--) {
            if(arr[i] == 4) {
                for (int j = i + 1; j < arr.length; j++) {
                    res.add(arr[j]);
                }
                break;
            }
        }
        return res.stream().mapToInt(item -> item).toArray();
    }

    public static boolean exist(int[] arr, int... existNum) {
        return Arrays.stream(arr).anyMatch(item -> Arrays.stream(existNum).anyMatch(num -> num == item));
    }
}
