import lesson_unittest.Arr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ArrTest {

    public static Stream<Arguments> dataForTestCut() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] { 1, 2, 4, 4, 2, 3, 4, 1, 7 }));
        out.add(Arguments.arguments(new int[] { 4, 2, 5, 1, 2, 3, 0, 1, 7 }));
        out.add(Arguments.arguments(new int[] { 1, 2, 4, 4, 2, 3, 0, 1, 4 }));
        out.add(Arguments.arguments(new int[] { 1, 2, 5, 1, 2, 3, 0, 1, 7 }));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestCut")
    public void testCut(int[] data) {
        int[] res = Arr.cut(data);
        if (res.length == 0) {
            Assertions.assertEquals(data[data.length - 1], 4);
            return;
        }
        for (int i = data.length - 1, j = res.length - 1; i >= 0 && j >= 0; i--, j--) {
            if(data[i] == 4)
                break;
            Assertions.assertEquals(data[i], res[j]);
        }
    }

    public static Stream<Arguments> dataForTestExist() {
        List<Arguments> out = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Integer> values = new ArrayList<>();
            int len = 5 + new Random().nextInt(10);
            boolean expected = new Random().nextBoolean();
            for (int j = 0; j < len; j++) {
                int value = 0;
                do {
                    value = new Random().nextInt(10);
                } while (!expected && (value == 1 || value == 4));
                values.add(value);
            }
            if (expected && values.stream().noneMatch(v -> v == 1 || v == 4)) {
                values.add(1);
            }
            out.add(Arguments.arguments(values.stream().mapToInt(item -> item).toArray(), expected));
        }
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("dataForTestExist")
    public void testExist(int[] data, boolean expected) {
        Assertions.assertEquals(Arrays.stream(data).anyMatch(d -> d == 1 || d == 4), Arr.exist(data, 1, 4));
    }
}
