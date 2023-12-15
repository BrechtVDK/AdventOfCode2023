import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Dag15 {

	public static void main(String[] args) {
		Dag15 d = new Dag15();
	}

	List<String> stringList = new ArrayList<>();
	Map<Integer, List<String>> map = new HashMap<>();
	Map<String, Integer> focusMap = new HashMap<>();

	public Dag15() {
		readFile();
		int sum1 = stringList.stream().mapToInt(string -> hashString(string)).sum();
		System.out.printf("Sum part 1: %d%n", sum1);
		
		stringList.forEach(string -> map(string));
		System.out.printf("Sum part 2: %d", calculateSumPart2());
	}

	private int calculateSumPart2() {
		int sum = 0;
		for (Entry<Integer, List<String>> entry : map.entrySet()) {
			int box = entry.getKey();
			List<String> labels = entry.getValue();
			int slot = 1;
			for (String label : labels) {
				sum += (box + 1) * slot++ * focusMap.get(String.format("%d%s", box, label));
			}
		}
		return sum;
	}

	private void map(String string) {
		String[] tokens = string.split("[-||=]");
		String label = tokens[0];
		int box = hashString(label);
		
		if (string.contains("-")) {
			List<String> labels = map.get(box) != null ? map.get(box) : new ArrayList<>();
			if (labels.contains(label)) {
				labels.remove(label);
				focusMap.remove(String.format("%d%s", box, label));
				map.replace(box, labels);
			}
		}
		// "="
		else {
			int focus = Integer.parseInt(tokens[1]);
			List<String> labels = map.get(box) != null ? map.get(box) : new ArrayList<>();
			if (labels.contains(label)) {
				focusMap.replace(String.format("%d%s", box, label), focus);
			} else {
				labels.add(label);
				map.put(box, labels);
				focusMap.put(String.format("%d%s", box, label), focus);
			}
		}

	}

	private int hashString(String string) {
		int value = 0;
		for (char c : string.toCharArray()) {
			value += c;
			value *= 17;
			value %= 256;
		}
		return value;
	}

	private void readFile() {
		try (Scanner scanner = new Scanner(new File("input.txt"))) {
			String inputString = scanner.next();
			String[] tokens = inputString.split(",");
			Arrays.stream(tokens).forEach(token -> stringList.add(token));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
