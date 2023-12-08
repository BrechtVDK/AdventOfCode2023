import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Dag8 {

	public static void main(String[] args) {
		Dag8 d = new Dag8();
	}

	private String instructions;
	private Map<String, String[]> map = new HashMap<>();

	public Dag8() {
		readFile();
		int steps = navigate("AAA","ZZZ");
		System.out.println("steps part 1: " + steps);

		List<Long> firstNumberOfStepsForMatchOfNodesEndingWithA = new ArrayList<>();
		for (String key : map.keySet()) {
			if (key.matches("..A")) {
				firstNumberOfStepsForMatchOfNodesEndingWithA.add((long) navigate(key,"..Z"));
			}
		}

		long steps2 = firstNumberOfStepsForMatchOfNodesEndingWithA.stream().reduce(1l, (subelement,element) -> lcm(subelement,element));

		System.out.println("steps part 2 :" + steps2);
		 
	}

	private long lcm(long x, long y) {
		long max = Math.max(x, y);
		long min = Math.min(x, y);
		long lcm = max;
		while (lcm % min != 0) {
			lcm += max;
		}
		return lcm;
	}


	private int navigate(String startNode, String endNode) {
		String node = startNode;
		int steps = 0;
		while (!node.matches(endNode)) {
			for (int i = 0; i < instructions.length(); i++) {
				steps++;
				char instruction = instructions.charAt(i);
				switch (instruction) {
				case 'L' -> node = map.get(node)[0];
				case 'R' -> node = map.get(node)[1];
				}
				if (node.matches(endNode)) {
					break;
				}
			}
		}
		return steps;
	}

	private void readFile() {
		try (Scanner sc = new Scanner(new File("input.txt"))) {
			int line = 0;
			while (sc.hasNextLine()) {
				switch (line) {
				case 0 -> instructions = sc.nextLine();
				case 1 -> {
				}
				default -> {
					String node = sc.next();
					String rest = sc.nextLine();
					String left = rest.substring(4, 7);
					String right = rest.substring(9, 12);
					map.put(node, new String[] { left, right });
				}
				}
				line++;
			}
		} catch (Exception e) {
			System.err.print("read error " + e);
		}
	}

}
