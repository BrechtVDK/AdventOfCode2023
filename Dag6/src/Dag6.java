import java.util.Arrays;
import java.util.List;

public class Dag6 {

	public static void main(String[] args) {
		Dag6 d = new Dag6();
	}

	private List<long[]> listRecords = List.of(new long[] { 44, 283 }, new long[] { 70, 1134 }, new long[] { 70, 1134 },
			new long[] { 80, 1491 });
	
	private long[] possibilitiesToBeat = new long[listRecords.size()];

	public Dag6() {
		for (int i = 0; i < listRecords.size(); i++) {
			possibilitiesToBeat[i] = calculateNumberOfPossibilities(listRecords.get(i));
		}

		int result1 = (int) Arrays.stream(possibilitiesToBeat).reduce(1, (subtotal, element) -> subtotal * element);
		System.out.println("Part 1: " + result1);

		int result2 = calculateNumberOfPossibilities(new long[] { 44707080l, 283113411341491l });
		System.out.println("Part 2: " + result2);
	}

	private int calculateNumberOfPossibilities(long[] race) {
		int numberOfPossibilities = 0;
		long time = race[0];
		long distanceToBeat = race[1];
		// 0 and time exclusive -> distance = 0
		for (int i = 1; i < time; i++) {
			if (calculateDistance(i, time - i) > distanceToBeat) {
				numberOfPossibilities++;
			}
		}
		return numberOfPossibilities;
	}

	private long calculateDistance(long timePushed, long timeReleased) {
		return timeReleased * timePushed;
	}
}
