import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag12 {

	public static void main(String[] args) {
		Dag12 d = new Dag12();
	}

	private List<Row> rowsList = new ArrayList<>();

	public Dag12() {
		readFile();
		int sum = rowsList.stream().map(row -> row.possibilities).reduce(Integer::sum).get();
		System.out.println(sum);

	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get("test.txt"))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				rowsList.add(new Row(sc.next(), sc.next()));
			});
		} catch (Exception e) {
			System.err.print(e);
		}

	}

	public static class Row {
		private String row;
		private List<Integer> order = new ArrayList<>();
		private int possibilities;
		private String regex;
		private int groups;

		public Row(String row, String nrsWithComma) {
			this.row = row;
			nrsWithCommaToList(nrsWithComma);
			setRegex();
			this.groups = order.size();
			calculateNrOfPossibilities();
			System.out.println(regex+"*");
		}

		private void setRegex() {
			StringBuilder sbRegex = new StringBuilder();

			for (char c : row.toCharArray()) {
				if (c == '.') {
					sbRegex.append("\\.");
				} else if (c == '#') {
					sbRegex.append("#");
				} else {
					sbRegex.append(".");
				}
			}

			/*
			 * if (row.indexOf('.') == 0) { sbRegex1.append("\\.+"); } else if
			 * (row.indexOf('?') == 0) { sbRegex1.append("\\.*"); } for (Integer nr : order)
			 * { sbRegex1.append("#{").append(nr).append("}\\.+"); } if
			 * (row.lastIndexOf('#') == row.length() - 1) {
			 * sbRegex1.delete(sbRegex1.length() - 3, sbRegex1.length());
			 * 
			 * } else if (row.lastIndexOf(".") == row.length() - 1) {
			 * sbRegex1.delete(sbRegex1.length() - 3, sbRegex1.length());
			 * sbRegex1.append("\\.+"); }
			 */
			this.regex = sbRegex.toString();

		}

		private void nrsWithCommaToList(String nrsWithComma) {
			Arrays.stream(nrsWithComma.split(",")).forEach(nr -> this.order.add(Integer.parseInt(nr)));
		}

		public void calculateNrOfPossibilities() {
			StringBuilder sbRow = new StringBuilder(row);
			bruteForce(sbRow, 0);
		}

		private void bruteForce(StringBuilder sbRow, int index) {
			if (index == sbRow.length()) {
				return;
			}
			if (sbRow.charAt(index) == '?') {
				String withPoint = sbRow.replace(index, index + 1, ".").toString();
				System.out.print(withPoint);
				if (withPoint.matches(regex)
						&& (int) Arrays.stream(withPoint.split("[\\.*?*\\.+?*\\.*]")).filter(s -> !s.isEmpty()).count() == groups) {
					System.out.print(" y");
					possibilities++;
				}
				System.out.println();
				
				bruteForce(new StringBuilder(withPoint), index + 1);

				String withHash = sbRow.replace(index, index + 1, "#").toString();
				System.out.print(withHash);
				if (withHash.matches(regex)
						&& (int) Arrays.stream(withHash.split("[\\.*?*\\.+?*\\.*]")).filter(s -> !s.isEmpty()).count() == groups) {
					System.out.print(" y");
					possibilities++;
				}
				System.out.println();
				bruteForce(new StringBuilder(withHash), index + 1);

			} else {
				bruteForce(sbRow, index + 1);
			}
		}

	}
}
