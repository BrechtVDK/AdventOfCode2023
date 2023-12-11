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
	private Map<Integer, int[]> galaxyMap = new TreeMap<>();
	private List<int[]> galaxyPairs = new ArrayList<>();
	private List<Integer> emptyRows = new ArrayList<>();
	private List<Integer> emptyColumns = new ArrayList<>();

	public Dag11() {
		readFile();
		calculateEmptyRows();
		calculateEmptyColumns();
		mapGalaxies();
		linkTheGalaxies();
		System.out.print("Empty rows: ");
		emptyRows.forEach(x -> System.out.print(x+" ") );
		System.out.printf("%nEmpty cols: ");
		emptyColumns.forEach(x -> System.out.print(x+" ") );
		System.out.println();
		long sum1 = calculateSumShortestPaths(2);
		System.out.printf("Sum part 1: %d%n", sum1);
		
		long sum2 = calculateSumShortestPaths(1000000);
		System.out.printf("Sum part 2: %d%n", sum2);

	}

	private void calculateEmptyColumns() {
		int columnSize = imageList.size();
		int rowSize = imageList.get(0).length();
		for (int i = 0; i < rowSize; i++) {
			boolean flag = true;
			for (int j = 0; j < columnSize; j++) {
				if (imageList.get(j).charAt(i) == '#') {
					flag = false;
					break;
				}
			}
			if (flag) {
				emptyColumns.add(i);
			}
		}

	}

	private void calculateEmptyRows() {
		for (int i = 0; i < imageList.size(); i++) {
			if (!imageList.get(i).contains("#")) {
				emptyRows.add(i);
			}
		}

	}

	private long calculateSumShortestPaths(int expandFactor) {
		long sum = 0;
		for (int[] galaxyPair : galaxyPairs) {
			sum += shortestPath(galaxyPair, expandFactor);
		}
		return sum;
	}

	private int shortestPath(int[] galaxyPair, int expandFactor) {
		int[] coord1 = galaxyMap.get(galaxyPair[0]);
		int[] coord2 = galaxyMap.get(galaxyPair[1]);

		int nrEmptycolumns = 0, nrEmptyRows = 0;

		for (Integer row : emptyRows) {
			if (row > Math.min(coord1[0], coord2[0]) && row < Math.max(coord1[0], coord2[0])) {
				nrEmptyRows++;
			}
		}
		for (Integer col : emptyColumns) {
			if (col > Math.min(coord1[1], coord2[1]) && col < Math.max(coord1[1], coord2[1])) {
				nrEmptycolumns++;
			}
		}
		
		int path = Math.abs(coord2[0] - coord1[0]) + Math.abs(coord2[1] - coord1[1])+(nrEmptycolumns*(expandFactor-1))+(nrEmptyRows*(expandFactor-1));
		//System.out.printf("%d-%d %d-%d = %d #row %d #col %d%n",coord1[0],coord1[1], coord2[0],coord2[1],path,nrEmptyRows,nrEmptycolumns);
		return path;

	}

	private void linkTheGalaxies() {
		for (int i = 0; i < galaxyMap.size() - 1; i++) {
			for (int j = i + 1; j < galaxyMap.size(); j++) {
				galaxyPairs.add(new int[] { i, j });
			}
		}
	}

	private void mapGalaxies() {
		int galaxyNr = 0;
		for (int row = 0; row < imageList.size(); row++) {
			for (int col = 0; col < imageList.get(row).length(); col++) {
				if (imageList.get(row).charAt(col) == '#') {
					galaxyMap.put(galaxyNr++, new int[] { row, col });
				}
			}
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
