import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Dag16 {

	public static void main(String[] args) {
		Dag16 d = new Dag16();
	}

	private char[][] square = new char[110][110];
	private char[][] energizedSquare = new char[110][110];
	private Set<String> set = new HashSet<>();

	public Dag16() {
		readFile();
		// initializeEnergizedSquare();
		startBeam();
		// printSquare(square);
		// printSquare(energizedSquare);
		System.out.printf("Sum part 1: %d", countEnergizedTiles());
	}

	private int countEnergizedTiles() {
		int sum = 0;
		for (int row = 0; row < energizedSquare.length; row++) {
			for (int col = 0; col < energizedSquare[row].length; col++) {
				if (energizedSquare[row][col] == '#') {
					sum++;
				}
			}
		}
		return sum;

	}

	private void initializeEnergizedSquare() {
		for (int row = 0; row < energizedSquare.length; row++) {
			for (int col = 0; col < energizedSquare[row].length; col++) {
				energizedSquare[row][col] = '.';
			}
		}
	}

	private void startBeam() {
		goRight(0, 0);
	}

	private void goRight(int x, int y) {
		if (x == square[0].length) {
			return;
		}
		if (energizeTile(x, y, 'R')) {
			switch (square[y][x]) {
			case '.' -> goRight(x + 1, y);
			case '-' -> goRight(x + 1, y);
			case '\\' -> goDown(x, y + 1);
			case '/' -> goUp(x, y - 1);
			case '|' -> {
				goUp(x, y - 1);
				goDown(x, y + 1);
			}
			}
		}
	}

	private void goLeft(int x, int y) {
		if (x == -1) {
			return;
		}
		if (energizeTile(x, y, 'L')) {
			switch (square[y][x]) {
			case '.' -> goLeft(x - 1, y);
			case '-' -> goLeft(x - 1, y);
			case '\\' -> goUp(x, y - 1);
			case '/' -> goDown(x, y + 1);
			case '|' -> {
				goUp(x, y - 1);
				goDown(x, y + 1);
			}
			}
		}
	}

	private void goUp(int x, int y) {
		if (y == -1) {
			return;
		}
		if (energizeTile(x, y, 'U')) {
			switch (square[y][x]) {
			case '.' -> goUp(x, y - 1);
			case '|' -> goUp(x, y - 1);
			case '\\' -> goLeft(x - 1, y);
			case '/' -> goRight(x + 1, y);
			case '-' -> {
				goLeft(x - 1, y);
				goRight(x + 1, y);
			}
			}
		}

	}

	private void goDown(int x, int y) {
		if (y == square.length) {
			return;
		}
		if (energizeTile(x, y, 'D')) {
			switch (square[y][x]) {
			case '.' -> goDown(x, y + 1);
			case '|' -> goDown(x, y + 1);
			case '\\' -> goRight(x + 1, y);
			case '/' -> goLeft(x - 1, y);
			case '-' -> {
				goLeft(x - 1, y);
				goRight(x + 1, y);
			}
			}
		}
	}

	// tegel omzetten naar # + cyclus detecteren = false
	private boolean energizeTile(int x, int y, char direction) {
		energizedSquare[y][x] = '#';
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < energizedSquare.length; row++) {
			for (int col = 0; col < energizedSquare[row].length; col++) {
				if (row == y && col == x) {
					sb.append(direction);
				} else {
					sb.append(energizedSquare[row][col]);
				}
			}
		}
		String stringSquare = sb.toString();
		if (!set.contains(stringSquare)) {
			set.add(stringSquare);
			return true;
		}
		return false;

	}

	private void printSquare(char[][] sq) {
		for (int row = 0; row < sq.length; row++) {
			for (int col = 0; col < sq[row].length; col++) {
				System.out.print(sq[row][col]);
			}
			System.out.println();
		}
		System.out.println();

	}

	private void readFile() {
		try (Scanner scanner = new Scanner(new File("input.txt"))) {
			int i = 0;
			while (scanner.hasNextLine()) {
				square[i++] = scanner.nextLine().toCharArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
