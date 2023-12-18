import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag18 {

	public static void main(String[] args) {
		Dag18 d = new Dag18();

	}

	private String fileName = "input.txt";
	// private String fileName = "test.txt";

	private List<Instruction> instructionList = new ArrayList<>();
	private List<Instruction> instructionList2 = new ArrayList<>();
	private List<List<Character>> terrain = new ArrayList<>(60000);
	private int row = 0, col = 0;

	public Dag18() {
		readFile();/*
		initializeTerrain();
		startDigging(instructionList);
		digUpInterior();
		// printTerrain();
		System.out.printf("Cubic meters part 1: %d%n", countCubicMeters());
*/
		terrain.clear();
		initializeTerrain();
		convertInstructionList();
		startDigging(instructionList2);
		digUpInterior();
		System.out.printf("Cubic meters part 2: %d%n", countCubicMeters());

	}

	private void convertInstructionList() {
		for (Instruction instruction : instructionList) {
			String hex = instruction.hex;
			int amount = Integer.parseInt(hex.substring(2, 7), 16);
			char dir = switch (instruction.hex.substring(7, 8)) {
			case "0" -> 'R';
			case "1" -> 'D';
			case "2" -> 'L';
			case "3" -> 'U';
			default -> throw new IllegalArgumentException("Unexpected value: " + instruction.hex.substring(7, 8));
			};
			instructionList2.add(new Instruction(dir, amount, hex));
		}

	}

	private int countCubicMeters() {
		return (int) terrain.stream().flatMap(List::stream).filter(x -> x == '#').count();

	}

	private void digUpInterior() {
		Coord startCoord = null;
		boolean found = false;
		for (int row = 1; row < terrain.size(); row++) {
			if (found) {
				break;
			}
			for (int col = 0; col < terrain.get(row).size() - 1; col++) {
				if (terrain.get(row).get(col) == '#' && terrain.get(row).get(col + 1) == '.') {
					found = true;
					startCoord = new Coord(row, col + 1);
					break;
				}
			}
		}

		fillUpAndArround(startCoord);
	}

	private void fillUpAndArround(Coord coord) {
		Deque<Coord> stack = new ArrayDeque<>();
		stack.push(coord);

		while (!stack.isEmpty()) {
			Coord currentCoord = stack.pop();
			int row = currentCoord.row;
			int col = currentCoord.col;

			terrain.get(row).set(col, '#');

			if (row - 1 >= 0 && terrain.get(row - 1).get(col) == '.') {
				stack.push(new Coord(row - 1, col));
			}

			if (row + 1 < terrain.size() && terrain.get(row + 1).get(col) == '.') {
				stack.push(new Coord(row + 1, col));
			}

			if (col + 1 < terrain.get(row).size() && terrain.get(row).get(col + 1) == '.') {
				stack.push(new Coord(row, col + 1));
			}

			if (col - 1 >= 0 && terrain.get(row).get(col - 1) == '.') {
				stack.push(new Coord(row, col - 1));
			}
		}
	}

	private void initializeTerrain() {
		List<Character> list = new ArrayList<>();
		for (int j = 0; j < 10000; j++) {
			list.add('.');
		}
		for (int i = 0; i < 10000; i++) {
			terrain.add(new ArrayList<>(list));
		}

	}

	private void printTerrain() {
		terrain.forEach(x -> System.out.println(x));

	}

	private void startDigging(List<Instruction> list) {
		for (Instruction instruction : list) {
			System.out.println(instruction);
			dig(instruction);
		}

	}

	private void dig(Instruction instruction) {
		switch (instruction.dir) {
		case 'U' -> digUp(instruction.amount);

		case 'D' -> digDown(instruction.amount);

		case 'L' -> digLeft(instruction.amount);

		case 'R' -> digRight(instruction.amount);

		}

	}

	private void digRight(int amount) {
		for (int i = 0; i < amount; i++) {
			if (col == terrain.get(0).size() - 1) {
				insertColAtEnd();
			}
			terrain.get(row).set(++col, '#');
		}
	}

	private void digLeft(int amount) {
		for (int i = 0; i < amount; i++) {
			if (col == 0) {
				insertColAtFront();
				terrain.get(row).set(col, '#');
			} else {
				terrain.get(row).set(--col, '#');
			}
		}

	}

	private void insertColAtEnd() {
		for (List<Character> list : terrain) {
			list.add('.');
		}

	}

	private void insertColAtFront() {
		for (List<Character> list : terrain) {
			list.add(0, '.');
		}

	}

	private void digDown(int amount) {
		for (int i = 0; i < amount; i++) {
			if (row == terrain.size() - 1) {
				insertRowUnder();
			}
			terrain.get(++row).set(col, '#');
		}

	}

	private void digUp(int amount) {
		for (int i = 0; i < amount; i++) {
			if (row == 0) {
				insertRowAbove();
				terrain.get(row).set(col, '#');
			} else {
				terrain.get(--row).set(col, '#');
			}

		}

	}

	private void insertRowAbove() {
		int lengthRow = terrain.get(0).size();
		List<Character> list = new ArrayList<>();
		for (int i = 0; i < lengthRow; i++) {
			list.add('.');
		}
		terrain.add(0, list);
	}

	private void insertRowUnder() {
		int lengthRow = terrain.get(0).size();
		List<Character> list = new ArrayList<>();
		for (int i = 0; i < lengthRow; i++) {
			list.add('.');
		}
		terrain.add(list);

	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				instructionList.add(new Instruction(sc.next().charAt(0), Integer.parseInt(sc.next()), sc.next()));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public record Instruction(char dir, int amount, String hex) {
	};

	public record Coord(int row, int col) {
	};
}