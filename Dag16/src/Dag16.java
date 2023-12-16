import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Dag16 {

	public static void main(String[] args) {
		try {
			Dag16 d = new Dag16();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private char[][] square = new char[110][110];
	private char[][] energizedSquare = new char[110][110];
	private Set<String> set = new HashSet<>();
	private List<Integer> energiedTilesList = new ArrayList<>();

	public Dag16() throws InterruptedException {
		readFile();

		startBeam();
		// printSquare(square);
		// printSquare(energizedSquare);
		System.out.printf("Sum part 1: %d", countEnergizedTiles());
		initializeEnergizedSquare();
		beamAllPossibilites();
		int max = energiedTilesList.stream().max(Integer::max).get();
		System.out.printf("Max part 1: %d", max);

	}

	private void beamAllPossibilites() {
		for (int i = 0; i < square.length; i++) {
			try {
				goRight(0, i);
				energiedTilesList.add(countEnergizedTiles());
				initializeEnergizedSquare();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				goLeft(square.length - 1, i);
				energiedTilesList.add(countEnergizedTiles());
				initializeEnergizedSquare();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				goUp(i, square.length - 1);
				energiedTilesList.add(countEnergizedTiles());
				initializeEnergizedSquare();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				goDown(i, 0);
				energiedTilesList.add(countEnergizedTiles());
				initializeEnergizedSquare();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

	private void startBeam() throws InterruptedException {
		goRight(0, 0);
	}

	private void goRight(int x, int y) throws InterruptedException {
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
				Thread thread1 = new Thread(() -> {
					try {
						goUp(x, y - 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				Thread thread2 = new Thread(() -> {
					try {
						goDown(x, y + 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				thread1.start();
				thread2.start();
				thread1.join();
				thread2.join();

			}
			}
		}
	}

	private void goLeft(int x, int y) throws InterruptedException {
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
				Thread thread1 = new Thread(() -> {
					try {
						goUp(x, y - 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				Thread thread2 = new Thread(() -> {
					try {
						goDown(x, y + 1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				thread1.start();
				thread2.start();
				thread1.join();
				thread2.join();
			}
			}
		}
	}

	private void goUp(int x, int y) throws InterruptedException {
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
				Thread thread1 = new Thread(() -> {
					try {
						goLeft(x - 1, y);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				Thread thread2 = new Thread(() -> {
					try {
						goRight(x + 1, y);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				thread1.start();
				thread2.start();
				thread1.join();
				thread2.join();

			}
			}
		}

	}

	private void goDown(int x, int y) throws InterruptedException {
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
				Thread thread1 = new Thread(() -> {
					try {
						goLeft(x - 1, y);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				Thread thread2 = new Thread(() -> {
					try {
						goRight(x + 1, y);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				thread1.start();
				thread2.start();
				thread1.join();
				thread2.join();
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
