import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo {
    public static void main(String[] args) {
        System.out.println(new Demo().solution(new int[]{1, 3, 6, 4, 1, 2}));
    }

    public int solution(int[] A) {
        AtomicInteger sequence = new AtomicInteger(0);
        AtomicInteger min = new AtomicInteger(0);
        Arrays.stream(A).filter(i -> i > 0).sorted().distinct().takeWhile(i -> min.get() == 0).forEach(i -> {
            if(i != sequence.incrementAndGet()){
                min.set(sequence.get());
            }
        });

        return min.get();
    }
}
