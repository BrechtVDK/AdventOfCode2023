import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag13 {

	public static void main(String[] args) {
		Dag13 d = new Dag13();
	}

	List<char[][]> patternList = new ArrayList<>();
	private int sum;

	public Dag13() {
		readFile();
		/*
		 * for (char[][] pattern : patternList) { for (char[] c : pattern) {
		 * System.out.println(c); } System.out.println(); }
		 */

		for (char[][] pattern : patternList) {
			System.out.println("************");
			sum += nrsRowAboveHorizontalReflection(pattern).stream().reduce(Integer::sum).get()*100;
			//sum += nrsColsLeftVerticalReflection(pattern).stream().reduce(Integer::sum).get();
		}
		System.out.println(sum);
	}

	private List<Integer> nrsColsLeftVerticalReflection(char[][] pattern) {
		int rows = pattern.length;
		int cols = pattern[0].length;

		char[][] patternTransposed = new char[cols][rows];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				patternTransposed[j][i] = pattern[i][j];
			}
		}
		return nrsRowAboveHorizontalReflection(patternTransposed);

	}

	private List<Integer> nrsRowAboveHorizontalReflection(char[][] pattern) {
		int r = 0;
		/*for (char[] c : pattern) {
			System.out.printf("%-3d", r++);
			System.out.println(c);
		}*/

		List<Integer> list = new ArrayList<>();
		list.add(0);
		for (int i = 0; i < pattern.length - 1; i++) {
			if (Arrays.equals(pattern[i], pattern[i + 1])) {
				int nrRowsToGo = pattern.length - i - 2;
				boolean flag = true;
				for (int check = 1; check <= nrRowsToGo; check++) {
					if (i - check < 0) {
						break;
					}
					flag = Arrays.equals(pattern[i - check], pattern[i + 1 + check]);
					if (!flag) {
						break;
					}
				}
				if (flag) {
					list.add(i + 1);
					System.out.println(i + 1);
				}
			}
		}

		for (int i = 1; i < pattern.length - 1; i++) {
			if (Arrays.equals(pattern[i - 1], pattern[i + 1])) {
				int nrRowsToGo = pattern.length - i - 1-2;
				boolean flag = true;
				for (int check = 1; check <= nrRowsToGo; check++) {
					if (i -1- check < 0) {
						break;
					}
					flag = Arrays.equals(pattern[i -1 -check], pattern[i + 1 + check]);
					if (!flag) {
						break;
					}
				}
				if (flag) {
					list.add(i);
					System.out.println(i);
				}
			}
		}
		
	
		System.out.print("\n\n");
		return list;

	}

	private void readFile() {
		try (Stream<String> streams = Files.lines(Paths.get("input.txt"))) {
			List<String> list = new ArrayList<String>();
			streams.forEach(line -> {
				if (!line.isEmpty() || !line.isBlank()) {
					Scanner sc = new Scanner(line);
					String lijn = sc.next();
					list.add(lijn);
				} else {
					char[][] pattern = new char[list.size()][list.get(0).length()];
					int i = 0;
					for (String l : list) {
						pattern[i++] = l.toCharArray();
					}
					patternList.add(pattern);
					list.clear();
				}
			});
			char[][] pattern = new char[list.size()][list.get(0).length()];
			int i = 0;
			for (String l : list) {
				pattern[i++] = l.toCharArray();
			}
			patternList.add(pattern);
			list.clear();
		} catch (

		Exception e) {
			System.err.print(e);
		}

	}

}
