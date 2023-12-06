package domein;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Dag3 {

	private int sum, sum2;
	// lijn, index,getal

	List<String> lijnen = new ArrayList<>();

	public Dag3() {
		leesFile();
		// berekenSom();

		berekenSom2();
		System.out.println("totSum: " + sum);
		System.out.println("totSum2: " + sum2);
	}

	private void berekenSom() {
		String regex = "\\b(\\d+)\\b";
		Pattern pattern = Pattern.compile(regex);

		for (int i = 0; i < lijnen.size(); i++) {
			if (i == 0) {
				// eigen en volgende
				Matcher matcher = pattern.matcher(lijnen.get(0));
				int fromIndex = 0;
				while (matcher.find()) {
					String getal = matcher.group();
					int lengte = getal.length();

					int index = lijnen.get(0).indexOf(getal, fromIndex);
					fromIndex = index + lengte + 1;
					if (lijnen.get(1)
							.substring(index - 1 < 0 ? 0 : index - 1,
									index + lengte + 1 >= lijnen.get(1).length() - 1 ? lijnen.get(1).length() - 1
											: index + lengte + 1)
							.matches(".*[^.\\d].*")) {
						sum += Integer.parseInt(getal);
						System.out.println(getal);
					} else if ((index != 0 && lijnen.get(0).substring(index - 1, index).matches("[^.\\d]"))
							|| lijnen.get(0)
									.substring(index + lengte,
											index + lengte + 1 >= lijnen.get(0).length() - 1
													? lijnen.get(0).length() - 1
													: index + lengte + 1)
									.matches("[^.\\d]")) {
						sum += Integer.parseInt(getal);
						System.out.println(getal);
					}
				}
			} else if (i != lijnen.size() - 1) {
				// eigen + vorige en volgende
				Matcher matcher = pattern.matcher(lijnen.get(i));
				int fromIndex = 0;
				while (matcher.find()) {
					String getal = matcher.group();
					int lengte = getal.length();
					int index = lijnen.get(i).indexOf(getal, fromIndex);
					fromIndex = index + lengte + 1;
					if (lijnen.get(i - 1).substring(index - 1 < 0 ? 0 : index - 1,
							index + lengte + 1 >= lijnen.get(i - 1).length() - 1 ? lijnen.get(i - 1).length() - 1
									: index + lengte + 1)
							.matches(".*[^.\\d].*")
							|| lijnen.get(i + 1)
									.substring(index - 1 < 0 ? 0 : index - 1,
											index + lengte + 1 >= lijnen.get(i + 1).length() - 1
													? lijnen.get(i + 1).length() - 1
													: index + lengte + 1)
									.matches(".*[^.\\d].*")) {
						sum += Integer.parseInt(getal);
						System.out.println(getal);
					} else if ((index != 0 && lijnen.get(i).substring(index - 1, index).matches("[^.\\d]"))
							|| lijnen.get(i)
									.substring(index + lengte >= lijnen.get(i).length() - 1 ? index : index + lengte,
											index + lengte + 1 >= lijnen.get(i).length() - 1
													? lijnen.get(i).length() - 1
													: index + lengte + 1)
									.matches("[^.\\d]")) {
						sum += Integer.parseInt(getal);
						System.out.println(getal);
					}
				}
			} else if (i == lijnen.size() - 1) {

				// eigen en vorige
				Matcher matcher = pattern.matcher(lijnen.get(i));
				int fromIndex = 0;
				while (matcher.find()) {
					String getal = matcher.group();
					int lengte = getal.length();
					int index = lijnen.get(i).indexOf(getal, fromIndex);
					fromIndex = index + lengte + 1;
					if (lijnen.get(i - 1)
							.substring(index - 1 < 0 ? 0 : index - 1,
									index + lengte + 1 >= lijnen.get(i - 1).length() - 1
											? lijnen.get(i - 1).length() - 1
											: index + lengte + 1)
							.matches(".*[^.\\d].*")) {

						sum += Integer.parseInt(getal);
						System.out.println(getal);
					} else if ((index != 0 && lijnen.get(i).substring(index - 1, index).matches("[^.\\d]"))
							|| lijnen.get(i)
									.substring(index + lengte,
											index + lengte + 1 >= lijnen.get(i).length() - 1
													? lijnen.get(i).length() - 1
													: index + lengte + 1)
									.matches("[^.\\d]")) {
						sum += Integer.parseInt(getal);
						System.out.println(getal);
					}
				}

			}

		}

	}

	private void berekenSom2() {
		String regex = "\\b(\\d+)\\b";
		Pattern pattern = Pattern.compile(regex);
		for (int i = 0; i < lijnen.size(); i++) {
			if (i < lijnen.size() - 1) {
				// eigen en volgende
				Matcher matcher = pattern.matcher(lijnen.get(i));
				int fromIndex = 0;
				while (matcher.find()) {
					String getal = matcher.group();
					int lengte = getal.length();
					int index = lijnen.get(i).indexOf(getal, fromIndex);

					// ster volgende lijn
					if (lijnen.get(i + 1)
							.substring(index - 1 < 0 ? 0 : index - 1,
									index + lengte + 1 >= lijnen.get(1).length() - 1 ? lijnen.get(1).length() - 1
											: index + lengte + 1)
							.matches(".*[\\*].*")) {
						int indexStar = lijnen.get(i + 1).indexOf("*", index - 1 < 0 ? 0 : index - 1);
						int rakendGetal = geefRakendGetalTerug(i, indexStar);
						if (rakendGetal != 0) {
							System.out.println(getal + "*");
							System.out.println(rakendGetal);
							sum2 += (Integer.parseInt(getal) * rakendGetal);
						}
					}
					// ster zelfde lijn links

					if (index >= 1 && lijnen.get(i).charAt(index - 1) == '*') {
						int linksOnderGetal = getalOnder(index - 1, lijnen.get(i + 1));
						if (linksOnderGetal > 0) {
							System.out.println(getal + "*");
							System.out.println(linksOnderGetal);
							sum2 += (Integer.parseInt(getal) * linksOnderGetal);
						}
					}
					// rechts
					else if (index + lengte < 140 && lijnen.get(i).charAt(index + lengte) == '*') {
						int rechtsgetal = getalRechts(index + lengte, lijnen.get(i));
						if (rechtsgetal > 0) {
							sum2 += (Integer.parseInt(getal) * rechtsgetal);
							System.out.println(getal + "*");
							System.out.println(rechtsgetal);
						}
						int rechtsOnderGetal = getalOnder(index + lengte, lijnen.get(i + 1));
						if (rechtsOnderGetal > 0) {
							sum2 += (Integer.parseInt(getal) * rechtsOnderGetal);
							System.out.println(getal + "*");
							System.out.println(rechtsOnderGetal);
						}

					}

					fromIndex = index + lengte + 1;
				}
			}

		}
	}

	private int getalOnder(int indexStar, String input) {
		String regex = "\\b(\\d+)\\b";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(input);
		int fromIndex = 0;
		int getal;
		while (matcher.find()) {
			String g = matcher.group();
			int lengte = g.length();
			int index = input.indexOf(g, fromIndex);
			List<Integer> lijst = new ArrayList<>();
			lijst.add(indexStar);
			lijst.add(indexStar - 1);
			lijst.add(indexStar + 1);
			if (lijst.contains(index)) {
				getal = Integer.parseInt(g.toString());
				return getal;
			} else if (lengte == 2) {
				if (lijst.contains(index + 1)) {
					getal = Integer.parseInt(g.toString());
					return getal;
				}
			} else if (lengte == 3) {
				if (lijst.contains(index + 2)) {

					getal = Integer.parseInt(g.toString());
					return getal;
				}
			}
		}
		return 0;
	}

	private int getalRechts(int index, String input) {
		StringBuilder rightNumber = new StringBuilder();
		for (int i = index + 1; i < input.length() && Character.isDigit(input.charAt(i)); i++) {
			rightNumber.append(input.charAt(i));
		}
		if (!rightNumber.isEmpty()) {

			int getal = Integer.parseInt(rightNumber.toString());
			
			return getal;

		}
		return 0;
	}

	private int getalLinks(int index, String input) {
		StringBuilder leftNumber = new StringBuilder();
		for (int j = index - 1; j >= 0 && Character.isDigit(input.charAt(j)); j--) {
			leftNumber.insert(0, input.charAt(j));
		}
		if (!leftNumber.isEmpty()) {
			int g = Integer.parseInt(leftNumber.toString());

			return g;
		} else
			return 0;
	}

	private int geefRakendGetalTerug(int lijn, int indexStar) {
		String input = lijnen.get(lijn + 1);
		int getal;
		// Zoek naar het getal aan de linkerkant van de asterisk
		StringBuilder leftNumber = new StringBuilder();
		for (int i = indexStar - 1; i >= 0 && Character.isDigit(input.charAt(i)); i--) {
			leftNumber.insert(0, input.charAt(i));
		}
		if (!leftNumber.isEmpty()) {
			getal = Integer.parseInt(leftNumber.toString());
			
			return getal;
		} else {
			// Zoek naar het getal aan de rechterkant van de asterisk
			StringBuilder rightNumber = new StringBuilder();
			for (int i = indexStar + 1; i < input.length() && Character.isDigit(input.charAt(i)); i++) {
				rightNumber.append(input.charAt(i));
			}
			if (!rightNumber.isEmpty()) {
	
				getal = Integer.parseInt(rightNumber.toString());
				return getal;
			}
		}
		// ************************************
		input = lijnen.get(lijn + 2);

		String regex = "\\b(\\d+)\\b";
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(input);
		int fromIndex = 0;
		while (matcher.find()) {
			String g = matcher.group();
			int lengte = g.length();
			int index = input.indexOf(g, fromIndex);

			List<Integer> lijst = new ArrayList<>();
			lijst.add(indexStar);
			lijst.add(indexStar - 1);
			lijst.add(indexStar + 1);
			if (lijst.contains(index)) {
				getal = Integer.parseInt(g.toString());
				return getal;
			} else if (lengte == 2) {
				if (lijst.contains(index + 1)) {
					getal = Integer.parseInt(g.toString());
					return getal;
				}
			} else if (lengte == 3) {
				if (lijst.contains(index + 2)) {
					getal = Integer.parseInt(g.toString());
					return getal;
				}
			}
		}

		return 0;

	}

	private void leesFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(regel -> {
				Scanner scanner = new Scanner(regel);
				String lijn = scanner.next();
				lijnen.add(lijn);

			});
		} catch (IOException ex) {
			System.out.println("leesfout");
		}
	}

}