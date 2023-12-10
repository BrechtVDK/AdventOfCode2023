import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag10 {

	public static void main(String[] args) {
		Dag10 d = new Dag10();
	}

	private List<String> diagram = new ArrayList<>();
	private int steps;
	private final int LOWERLIMIT, UPPERLIMIT = 0, RIGHTBORDER, LEFTBORDER = 0;

	public Dag10() {
		readFile();
		LOWERLIMIT = diagram.size() - 1;
		RIGHTBORDER = diagram.get(0).length() - 1;
		int[] startpoint = getStartPoint();
		System.out.println("start: S " + startpoint[0] + " " + startpoint[1]);
		
		loop("start", startpoint, 'S');
		
		int farthestDistance = (int) Math.ceil((double) steps / 2);
		
		System.out.println("steps for loop part 1: " + steps);
		System.out.println("farthest distance part 1: " + farthestDistance);
	}

	private void loop(String previousStep, int[] coord, char symbol) {
		do {
			steps++;
			Result result = findNextStep(previousStep, coord, symbol);
			previousStep = result.getStep();
			coord = result.getCoord();
			int x = getX(coord);
			int y = getY(coord);
			symbol = diagram.get(y).charAt(x);
			System.out.printf("next: %-5s %s %d %d step: %d%n", previousStep, symbol, x, y, steps);
		} while (symbol != 'S');
		steps--;
	}

	private Result findNextStep(String previousStep, int[] coord, char symbol) {
		int x = getX(coord);
		int y = getY(coord);
		if ("S|7F".indexOf(symbol) != -1 && !previousStep.equals("up")) {
			if ("S|LJ".indexOf(symbolUnder(coord)) != -1) {
				return new Result("down", new int[] { x, y + 1 });
			}
		}
		if ("S|LJ".indexOf(symbol) != -1 && !previousStep.equals("down")) {
			if ("S|7F".indexOf(symbolAbove(coord)) != -1) {
				return new Result("up", new int[] { x, y - 1 });
			}
		}
		if ("S-J7".indexOf(symbol) != -1 && !previousStep.equals("right")) {
			if ("S-LF".indexOf(symbolLeft(coord)) != -1) {
				return new Result("left", new int[] { x - 1, y });
			}
		}
		if ("S-FL".indexOf(symbol) != -1 && !previousStep.equals("left")) {
			if ("S-J7".indexOf(symbolRight(coord)) != -1) {
				return new Result("right", new int[] { x + 1, y });
			}
		}
		return null;

	}

	private char symbolRight(int[] startpoint) {
		int x = getX(startpoint);
		int y = getY(startpoint);
		if (x == RIGHTBORDER) {
			return 'X';
		}
		return diagram.get(y).charAt(x + 1);
	}

	private char symbolLeft(int[] startpoint) {
		int x = getX(startpoint);
		int y = getY(startpoint);
		if (x == LEFTBORDER) {
			return 'X';
		}
		return diagram.get(y).charAt(x - 1);
	}

	private char symbolAbove(int[] startpoint) {
		int x = getX(startpoint);
		int y = getY(startpoint);
		if (y == UPPERLIMIT) {
			return 'X';
		}
		return diagram.get(y - 1).charAt(x);
	}

	private char symbolUnder(int[] startpoint) {
		int x = getX(startpoint);
		int y = getY(startpoint);
		if (y == LOWERLIMIT) {
			return 'X';
		}
		return diagram.get(y + 1).charAt(x);
	}

	private int getX(int[] coord) {
		return coord[0];
	}

	private int getY(int[] coord) {
		return coord[1];
	}

	private int[] getStartPoint() {
		for (int i = 0; i < diagram.size(); i++) {
			if (diagram.get(i).contains("S")) {
				return new int[] { diagram.get(i).indexOf("S"), i };
			}
		}
		return null;
	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				diagram.add(sc.nextLine());
			});
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	public class Result {
		private String step;
		private int[] coord;

		public Result(String step, int[] coord) {
			this.coord = coord;
			this.step = step;
		}

		public int[] getCoord() {
			return coord;
		}

		public String getStep() {
			return step;
		}

	}
}
