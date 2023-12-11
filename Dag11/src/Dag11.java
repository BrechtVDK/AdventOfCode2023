import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Dag11 {

	public static void main(String[] args) {
		Dag11 d = new Dag11();

	}

	private List<String> imageList = new ArrayList<>();
	private char[][] image;
	private Map<Integer, int[]> galaxyMap = new TreeMap<>();
	private List<int[]> galaxyPairs = new ArrayList<>();

	public Dag11() {
		readFile();
		expandEmptyRows();
		expandEmptyColumns();
		transformListToDoubleArray();
		mapGalaxies();
		linkTheGalaxies();
		long sum = calculateSumShortestPaths();
		
		imageList.forEach(x -> System.out.println(x));
		galaxyPairs.forEach(x -> System.out.println(x[0] + "-" + x[1]));
		System.out.printf("Sum part 1: %d%n",sum);
		
	}

	private long calculateSumShortestPaths() {
		long sum = 0;
		for (int[] pair : galaxyPairs) {
			sum += shortestPath(pair);
		}
		return sum;
	}

	private int shortestPath(int[] pair) {
		int[] coord1 = galaxyMap.get(pair[0]);
		int[] coord2 = galaxyMap.get(pair[1]);
		
		return Math.abs(coord2[0]-coord1[0])+ Math.abs(coord2[1]-coord1[1]);
		
	}

	private void linkTheGalaxies() {
		for (int i = 0; i < galaxyMap.size(); i++) {
			for (int j = i + 1; j < galaxyMap.size(); j++) {
				galaxyPairs.add(new int[] { i, j });
			}
		}
	}

	private void mapGalaxies() {
		int galaxyNr = 0;
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[row].length; col++) {
				if (image[row][col] == '#') {
					galaxyMap.put(galaxyNr++, new int[] { row, col });
				}
			}
		}

	}

	private void transformListToDoubleArray() {
		int rows = imageList.size();
		int columns = imageList.get(0).length();
		image = new char[rows][columns];
		for (int i = 0; i < rows; i++) {
			image[i] = imageList.get(i).toCharArray();
		}

	}

	private void expandEmptyRows() {
		int rowSize = imageList.get(0).length();
		List<Integer> rowsToInsert = new ArrayList<>();
		StringBuilder sbRow = new StringBuilder();
		for (int i = 0; i < rowSize; i++) {
			sbRow.append(".");
		}
		String stringRow = sbRow.toString();

		for (int i = 0; i < imageList.size(); i++) {
			if (!imageList.get(i).contains("#")) {
				rowsToInsert.add(i);
			}
		}

		for (Integer row : rowsToInsert) {
			imageList.add(row + 1, stringRow);
		}

	}

	private void expandEmptyColumns() {
		int columnSize = imageList.size();
		int rowSize = imageList.get(0).length();
		List<Integer> columnsToInsert = new ArrayList<>();
		for (int i = 0; i < rowSize; i++) {
			boolean flag = true;
			for (int j = 0; j < columnSize; j++) {
				if (imageList.get(j).charAt(i) == '#') {
					flag = false;
					break;
				}
			}
			if (flag) {
				columnsToInsert.add(i);
			}
		}

		int columnsInserted = 0;
		for (Integer column : columnsToInsert) {
			for (int i = 0; i < columnSize; i++) {
				String row = imageList.get(i);
				String newRow = row.substring(0, column + columnsInserted) + "."
						+ row.substring(column + columnsInserted);
				imageList.set(i, newRow);
			}
			columnsInserted++;
		}

	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				imageList.add(sc.nextLine());
			});
		} catch (Exception e) {
			System.err.print(e);
		}

	}
}
