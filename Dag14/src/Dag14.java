import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Dag14 {

	public static void main(String[] args) {
		Dag14 d = new Dag14();
	}

	private char[][] field = new char[100][100];

	private Map<String, Integer> map = new HashMap<>();

	public Dag14() {
		readFile();
		tiltNorth();
		System.out.printf("Total load part 1: %d%n", getLoad());

		readFile();
		int cycleInt = findCycleInt();
		int cyclesToDo = 1000000000 % cycleInt +cycleInt;
		readFile();
		for (int i = 0; i < cyclesToDo; i++) {
			cycle();
		}
		System.out.printf("Total load part 2: %d%n", getLoad());
	}

	private int findCycleInt() {
		for (int i = 0; i < 1000000000; i++) {
			cycle();
			String stringField = toStringField(field);
			if (!map.containsKey(stringField)) {
				map.put(stringField, i);
			} else {
				return map.get(stringField);
			}
		}
		return 0;
	}

	private String toStringField(char[][] field2) {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col++) {
				sb.append(field[row][col]);
			}
		}
		return sb.toString();
	}

	private void cycle() {
		tiltNorth();
		tiltWest();
		tiltSouth();
		tiltEast();
	}

	private int getLoad() {
		int load = field.length;
		int totalLoad = 0;
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col++) {
				if (field[row][col] == 'O') {
					totalLoad += load;
				}
			}
			load--;
		}
		return totalLoad;

	}

	private void printField() {
		for (int row = 0; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col++) {
				System.out.print(field[row][col]);
			}
			System.out.println();
		}
		System.out.println();

	}

	private void tiltNorth() {
		for (int row = 1; row < field.length; row++) {
			for (int col = 0; col < field[row].length; col++) {
				if (field[row][col] == 'O') {
					int newRow = findMaxRowNorth(row, col);
					if (row != newRow) {
						field[row][col] = '.';
						field[newRow][col] = 'O';
					}
				}
			}
		}
	}

	private void tiltSouth() {
		for (int row = field.length - 2; row >= 0; row--) {
			for (int col = 0; col < field[row].length; col++) {
				if (field[row][col] == 'O') {
					int newRow = findMaxRowSouth(row, col);
					if (row != newRow) {
						field[row][col] = '.';
						field[newRow][col] = 'O';
					}
				}
			}
		}
	}

	private int findMaxRowSouth(int row, int col) {
		char rock = field[row][col];
		do {
			rock = field[++row][col];
		} while (row < field.length - 1 && rock == '.');

		if (row == field.length - 1 && rock == '.') {
			return row;
		}
		return --row;
	}

	private void tiltEast() {
		for (int row = 0; row < field.length; row++) {
			for (int col = field[row].length - 2; col >= 0; col--) {
				if (field[row][col] == 'O') {
					int newCol = findMaxColEast(row, col);
					if (col != newCol) {
						field[row][col] = '.';
						field[row][newCol] = 'O';
					}
				}
			}
		}
	}

	private void tiltWest() {
		for (int row = 0; row < field.length; row++) {
			for (int col = 1; col < field.length; col++) {
				if (field[row][col] == 'O') {
					int newCol = findMaxColWest(row, col);
					if (col != newCol) {
						field[row][col] = '.';
						field[row][newCol] = 'O';
					}
				}
			}
		}
	}

	private int findMaxColWest(int row, int col) {
		char rock = field[row][col];
		do {
			rock = field[row][--col];
		} while (col > 0 && rock == '.');

		if (col == 0 && rock == '.') {
			return col;
		}
		return ++col;
	}

	private int findMaxColEast(int row, int col) {
		char rock = field[row][col];
		do {
			rock = field[row][++col];
		} while (col < field.length - 1 && rock == '.');

		if (col == field.length - 1 && rock == '.') {
			return col;
		}
		return --col;
	}

	private int findMaxRowNorth(int row, int col) {
		char rock = field[row][col];
		do {
			rock = field[--row][col];
		} while (row > 0 && rock == '.');

		if (row == 0 && rock == '.') {
			return row;
		}
		return ++row;
	}

	private void readFile() {
		try (Scanner scanner = new Scanner(new File("input.txt"))) {
			int rowNumber = 0;
			while (scanner.hasNextLine()) {
				String row = scanner.next();
				field[rowNumber++] = row.toCharArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
