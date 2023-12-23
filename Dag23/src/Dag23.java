
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Dag23 {
	public static void main(String[] args) {
		Dag23 d = new Dag23();
	}

	// private String fileName = "test.txt";
	private String fileName = "input.txt";
	private char[][] field;
	private final int BORDER_NORTH, BORDER_SOUTH, BORDER_WEST, BORDER_EAST;
	private String walkOptions = ".v^<>";
	private Queue<Walk> queue = new ArrayDeque<>();
	private List<Integer> stepsToFinish = new ArrayList<>();
	private boolean part2;

	public Dag23() {
		readFile();
		BORDER_NORTH = 0;
		BORDER_SOUTH = field.length - 1;
		BORDER_WEST = 0;
		BORDER_EAST = field.length - 1;

		queue.offer(new Walk(field, new Pos(0, 1, ObligatedDirection.NONE), 0));

		walkTrueTheOptions();

		System.out.printf("Part 1: steps longest hike: %d%n",
				stepsToFinish.stream().max(Comparator.naturalOrder()).get());

		readFile();
		stepsToFinish.clear();
		part2 = true;
		queue.offer(new Walk(field, new Pos(0, 1, ObligatedDirection.NONE), 0));
		walkTrueTheOptions();
		System.out.printf("Part 2: steps longest hike: %d",
				stepsToFinish.stream().max(Comparator.naturalOrder()).get());

	}

	private void walkTrueTheOptions() {
		while (!queue.isEmpty()) {
			Walk w = queue.poll();
			walk(w.field, w.pos, w.steps);
		}

	}

	private void walk(char[][] field, Pos pos, int steps) {
		field[pos.row][pos.col] = 'O';
		// FINISH
		if (pos.row == field.length - 1 && pos.col == field.length - 2) {
			stepsToFinish.add(steps);
			return;
		}

		if (pos.row > BORDER_NORTH && walkOptions.indexOf(field[pos.row - 1][pos.col]) >= 0
				&& (pos.dir == ObligatedDirection.NONE || pos.dir == ObligatedDirection.NORTH)) {
			char tile = field[pos.row - 1][pos.col];
			queue.offer(new Walk(getCopyOfField(field), new Pos(pos.row - 1, pos.col, getObligatedDirection(tile)),
					steps + 1));
		}
		if (pos.row < BORDER_SOUTH && walkOptions.indexOf(field[pos.row + 1][pos.col]) >= 0
				&& (pos.dir == ObligatedDirection.NONE || pos.dir == ObligatedDirection.SOUTH)) {
			char tile = field[pos.row + 1][pos.col];
			queue.offer(new Walk(getCopyOfField(field), new Pos(pos.row + 1, pos.col, getObligatedDirection(tile)),
					steps + 1));
		}
		if (pos.col > BORDER_WEST && walkOptions.indexOf(field[pos.row][pos.col - 1]) >= 0
				&& (pos.dir == ObligatedDirection.NONE || pos.dir == ObligatedDirection.WEST)) {
			char tile = field[pos.row][pos.col - 1];
			queue.offer(new Walk(getCopyOfField(field), new Pos(pos.row, pos.col - 1, getObligatedDirection(tile)),
					steps + 1));

		}
		if (pos.col < BORDER_EAST && walkOptions.indexOf(field[pos.row][pos.col + 1]) >= 0
				&& (pos.dir == ObligatedDirection.NONE || pos.dir == ObligatedDirection.EAST)) {
			char tile = field[pos.row][pos.col + 1];
			queue.offer(new Walk(getCopyOfField(field), new Pos(pos.row, pos.col + 1, getObligatedDirection(tile)),
					steps + 1));
		}

	}

	private char[][] getCopyOfField(char[][] field) {
		char[][] copyField = new char[field.length][field[0].length];
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				copyField[i][j] = field[i][j];
			}
		}
		return copyField;
	}

	private ObligatedDirection getObligatedDirection(char tile) {
		if (part2) {
			return ObligatedDirection.NONE;
		}
		return switch (tile) {
		case '.' -> ObligatedDirection.NONE;
		case '<' -> ObligatedDirection.WEST;
		case '>' -> ObligatedDirection.EAST;
		case '^' -> ObligatedDirection.NORTH;
		case 'v' -> ObligatedDirection.SOUTH;
		default -> throw new IllegalArgumentException("Unexpected value: " + tile);
		};
	}

	private void printField(char[][] field) {
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
			switch (fileName) {
			case "input.txt" -> field = new char[141][];
			case "test.txt" -> field = new char[23][];
			}
			int rowNumber = 0;
			while (scanner.hasNextLine()) {
				String row = scanner.next();
				field[rowNumber++] = row.toCharArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public record Pos(int row, int col, ObligatedDirection dir) {
	}

	public record Walk(char[][] field, Pos pos, int steps) {
	}

	public enum ObligatedDirection {
		EAST, WEST, NORTH, SOUTH, NONE;
	}

}
