import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag9 {

	public static void main(String[] args) {
		Dag9 d = new Dag9();

	}

	private List<long[]> historyList = new ArrayList<>();

	public Dag9() {
		readFile();
		
		long sum1 = 0, sum2 = 0;
		for (long[] history : historyList) {
			sum1 += calculateNextValue(history, true);
			sum2 += calculateNextValue(history, false);
		}
		
		System.out.printf("Sum part 1: %d%nSum part 2: %d", sum1, sum2);
	}

	private long calculateNextValue(long[] history, boolean forward) {
		long nextValue = 0;
		List<long[]> list = new ArrayList<>();
		list.add(history);

		boolean allZeros = false;
		while (!allZeros) {
			long[] laatsteHistory = list.get(list.size() - 1);
			long[] newHistory = new long[laatsteHistory.length - 1];
			for (int i = 0; i < newHistory.length; i++) {
				newHistory[i] = laatsteHistory[i + 1] - laatsteHistory[i];
			}
			list.add(newHistory);
			allZeros = isAllZeros(newHistory);
		}
		if (forward) {
			for (long[] h : list) {
				nextValue += h[h.length - 1];
			}
		} else {
			for (int i = list.size() - 2; i >= 0; i--) {
				nextValue=list.get(i)[0]-nextValue;
			}
		}
		return nextValue;

	}

	private boolean isAllZeros(long[] history) {
		return Arrays.stream(history).allMatch(e -> e == 0l);
	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				long[] arr = new long[21];
				int i = 0;
				while (sc.hasNext()) {
					arr[i++] = sc.nextLong();
				}
				historyList.add(arr);
			});
		} catch (IOException e) {
			System.err.print(e);
		}
	}
}
