import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Dag21 {

	public static void main(String[] args) {
		Dag21 d = new Dag21();
	}

	// private String fileName = "test.txt";
	private String fileName = "input.txt";
	private char[][] field;

	public Dag21() {
		readFile();

		Pos start = findStart();
		explore(start, 64);
		int plots = countPlots();
		System.out.println(plots);
		// printField();
	}

	private int countPlots() {
		int plots = 0;
		for (int y = 0; y < field.length; y++) {
			for (int x = 0; x < field[y].length; x++) {
				if (field[y][x] == 'O') {
					plots++;
				}
			}
		}
		return plots;
	}

	private Pos findStart() {
		for (int y = 0; y < field.length; y++) {
			for (int x = 0; x < field[y].length; x++) {
				if (field[y][x] == 'S') {
					return new Pos(x, y, 0);
				}
			}
		}
		return null;
	}

	private void explore(Pos start, int NumberOfSteps) {
		Queue<Pos> queue = new ArrayDeque<>();
		queue.offer(start);

		while (!queue.isEmpty()) {
			Pos pos = queue.poll();
			int x = pos.x;
			int y = pos.y;
			int step = pos.step;
			step++;
			if (step > NumberOfSteps) {
				break;
			}
			// NORTH
			if (y > 0 && field[y - 1][x] != '#') {
				field[y][x] = '.';
				field[y - 1][x] = 'O';
				Pos p = new Pos(x, y - 1, step);
				if (!queue.contains(p)) {
					queue.offer(p);
				}
			}
			// SOUTH
			if (y < field.length - 1 && field[y + 1][x] != '#') {
				field[y][x] = '.';
				field[y + 1][x] = 'O';
				Pos p = new Pos(x, y + 1, step);
				if (!queue.contains(p)) {
					queue.offer(p);
				}

			}
			// EAST
			if (x < field[y].length - 1 && field[y][x + 1] != '#') {
				field[y][x] = '.';
				field[y][x + 1] = 'O';
				Pos p = new Pos(x + 1, y, step);
				if (!queue.contains(p)) {
					queue.offer(p);
				}

			}
			// WEST
			if (x > 0 && field[y][x - 1] != '#') {
				field[y][x] = '.';
				field[y][x - 1] = 'O';
				Pos p = new Pos(x - 1, y, step);
				if (!queue.contains(p)) {
					queue.offer(p);
				}
			}
		}
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

	private void readFile() {
		try (Scanner scanner = new Scanner(new File(fileName))) {
			int rowNumber = 0;
			while (scanner.hasNextLine()) {
				String row = scanner.next();
				if (rowNumber == 0) {
					field = new char[row.length()][row.length()];
				}
				field[rowNumber++] = row.toCharArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public record Pos(int x, int y, int step) {
	}
}
