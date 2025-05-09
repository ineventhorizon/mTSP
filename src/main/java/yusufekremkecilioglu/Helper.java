package yusufekremkecilioglu;

import java.util.List;
import java.util.Random;

public class Helper {
    private static final Random _random = new Random();
    public static <T> int GetRandomIndex(List<T> list) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException("List cannot be null or empty");
        return _random.nextInt(list.size());
    }
    public static <T> int[] getTwoDistinctRandomIndexes(List<T> list) {
        if (list == null || list.size() < 2) throw new IllegalArgumentException("List must have at least 2 elements");

        int first = _random.nextInt(list.size());
        int second;
        do {
            second = _random.nextInt(list.size());
        } while (second == first);

        return new int[]{first, second};
    }
}
