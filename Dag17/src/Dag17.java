
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Dag17 {

	// private String fileName="test.txt";
	private String fileName = "input.txt";

	private int[][] square;
	private Node[][] shortestPathArr;

	public static void main(String args[]) {
		Dag17 d = new Dag17();
	}

	public Dag17() {
		switch (fileName) {
		case "test.txt" -> {
			square = new int[13][13];
			shortestPathArr = new Node[13][13];
		}
		case "input.txt" -> {
			square = new int[141][141];
			shortestPathArr = new Node[141][141];
		}
		}
		readFile();
		initializeShortestPathArr();
		findShortestPath();

		// printArr(shortestPathArr);
		System.out.printf("%n%d", shortestPathArr[shortestPathArr.length - 1][shortestPathArr.length - 1].getValue());
	}

	private void findShortestPath() {
		// zolang niet alle knopen bezocht zijn
		int i = 0;
		while (!Arrays.stream(shortestPathArr).flatMap(Arrays::stream).allMatch(node -> node.visited)) {
			// zoek knoop met laagste value
			Node node = Arrays.stream(shortestPathArr).flatMap(Arrays::stream)
					.filter(n -> n.value != -1 && n.visited == false)
					.min((n1, n2) -> Integer.compare(n1.value, n2.value)).orElse(null);
			int value = node.value;
			int row = node.row;
			int col = node.col;
			// System.out.printf("%nvisited row %d col %d value %d ||", row, col, value);
			// System.out.printf("row %d col %d value %d%n",row,col,value);

			// zoek buren waar visited=false;
			// North
			if (row > 0) {
				Node nodeNorth = shortestPathArr[row - 1][col];
				if (!nodeNorth.visited && node.isNorthAllowed()) {
					if (shortestPathArr[row - 1][col].getValue() == -1
							|| shortestPathArr[row - 1][col].getValue() > (square[row - 1][col] + value)) {
						shortestPathArr[row - 1][col].setValue(square[row - 1][col] + value);
						shortestPathArr[row - 1][col].addDirection(node, "N");
						// System.out.printf("buur bezocht: row %d col %d value %d ",
						// row-1,col,square[row-1][col]+value );

					}
				}
			}
			// South
			if (row < square.length - 1) {
				Node nodeSouth = shortestPathArr[row + 1][col];
				if (!nodeSouth.visited && node.isSouthAllowed()) {
					if (shortestPathArr[row + 1][col].getValue() == -1
							|| shortestPathArr[row + 1][col].getValue() > (square[row + 1][col] + value)) {
						shortestPathArr[row + 1][col].setValue(square[row + 1][col] + value);
						shortestPathArr[row + 1][col].addDirection(node, "S");
						// System.out.printf("buur bezocht: row %d col %d value %d ",
						// row+1,col,square[row+1][col]+value );
					}
				}
			}
			// East
			if (col < square[0].length - 1) {
				Node nodeEast = shortestPathArr[row][col + 1];
				if (!nodeEast.visited && node.isEastAllowed()) {
					if (shortestPathArr[row][col + 1].getValue() == -1
							|| shortestPathArr[row][col + 1].getValue() > (square[row][col + 1] + value)) {
						shortestPathArr[row][col + 1].setValue(square[row][col + 1] + value);
						shortestPathArr[row][col + 1].addDirection(node, "E");
						// System.out.printf("buur bezocht: row %d col %d value %d ",
						// row,col+1,square[row][col+1]+value );

					}

				}
			}
			// West
			if (col > 0) {
				Node nodeWest = shortestPathArr[row][col - 1];
				if (!nodeWest.visited && node.isWestAllowed()) {
					if (shortestPathArr[row][col - 1].getValue() == -1
							|| shortestPathArr[row][col - 1].getValue() > (square[row][col - 1] + value)) {
						shortestPathArr[row][col - 1].setValue(square[row][col - 1] + value);
						shortestPathArr[row][col - 1].addDirection(node, "W");
						// System.out.printf("buur bezocht: row %d col %d value %d ",
						// row,col-1,square[row][col-1]+value );
					}
				}
			}

			// alle buren bezocht = visited
			shortestPathArr[row][col].setVisited(true);

		}

	}

	private void initializeShortestPathArr() {
		for (int row = 0; row < shortestPathArr.length; row++) {
			for (int col = 0; col < shortestPathArr[row].length; col++) {
				if (row == 0 && col == 0) {
					shortestPathArr[0][0] = new Node(row, col, square[0][0], false);
				} else {
					shortestPathArr[row][col] = new Node(row, col, -1, false);
				}
			}
		}
	}

	private void printArr(Node[][] arr) {
		for (int row = 0; row < arr.length; row++) {
			for (int col = 0; col < arr[row].length; col++) {
				System.out.printf("%-11d", arr[row][col].value);
			}
			System.out.println();
		}
	}

	private void readFile() {
		try (Scanner sc = new Scanner(new File(fileName))) {
			int row = 0;
			while (sc.hasNextLine()) {
				int col = 0;
				for (char c : sc.nextLine().toCharArray()) {
					square[row][col++] = Integer.parseInt(String.format("%s", c));
				}
				row++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Node {
		public int row, col, value;
		public boolean visited;
		private String[] lastThreeDirections = new String[] { "X", "X", "X" };

		public Node(int row, int col, int value, boolean visited) {
			setRow(row);
			setCol(col);
			setValue(value);
			setVisited(visited);
		}

		public void setRow(int row) {
			this.row = row;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public int getValue() {
			return value;
		}

		public boolean isVisited() {
			return visited;
		}

		public boolean isWestAllowed() {
			return Arrays.stream(lastThreeDirections).filter(x -> x.equals("W")).count() != 3;
		}

		public boolean isEastAllowed() {
			return Arrays.stream(lastThreeDirections).filter(x -> x.equals("E")).count() != 3;
		}

		public boolean isSouthAllowed() {
			return Arrays.stream(lastThreeDirections).filter(x -> x.equals("S")).count() != 3;
		}

		public boolean isNorthAllowed() {
			return Arrays.stream(lastThreeDirections).filter(x -> x.equals("N")).count() != 3;
		}

		public String[] getLastThreeDirections() {
			return lastThreeDirections;
		}

		public void addDirection(Node node, String dir) {
			if (Arrays.stream(node.getLastThreeDirections()).allMatch(x -> x.equals("X"))) {
				lastThreeDirections = new String[] { dir, dir, "X" };
			} else {
				lastThreeDirections = new String[] { node.getLastThreeDirections()[0], node.getLastThreeDirections()[1],
						dir };
			}
		}

	}
}