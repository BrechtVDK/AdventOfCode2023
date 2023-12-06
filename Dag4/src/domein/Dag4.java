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
import java.util.stream.Stream;

public class Dag4 {
	private int sum, aantalKaarten;
	private Map<Integer, Integer> map = new HashMap<>();

	public Dag4() {
		leesFile();
		System.out.println("deel 1 " + sum);
		for (Integer v : map.values()) {
			aantalKaarten+=v;
		}
		System.out.println("deel 2 "+ aantalKaarten);
	}

	private void leesFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(regel -> {
				Scanner scanner = new Scanner(regel);
				String lijn = scanner.nextLine();

				int[] numbers = new int[25];
				int[] winningNumbers = new int[10];
				String[] kaartEnNummers = lijn.split(":");
				int kaartnr = Integer.parseInt(kaartEnNummers[0].split("\\D+")[1]);

				String[] nummers = kaartEnNummers[1].split("\\|");

				String[] winningNumbersStrings = nummers[0].split("\\D+");
				String[] nummbersStrings = nummers[1].split("\\D+");

				for (int i = 1; i <= 10; i++) {
					winningNumbers[i - 1] = Integer.parseInt(winningNumbersStrings[i]);
				}
				for (int i = 1; i <= 25; i++) {
					numbers[i - 1] = Integer.parseInt(nummbersStrings[i]);
				}

				 sum += berekenPuntenKaart(winningNumbers, numbers);

				int matches = 0;
				matches = berekenAantalMatches(winningNumbers, numbers);
				int aantalKaarten = map.get(kaartnr) == null ? 0 : map.get(kaartnr);
				// eigen kaart
				map.put(kaartnr, map.get(kaartnr) == null ? 1 : map.get(kaartnr) + 1);
				// kopies
				for (int j = 1; j <= aantalKaarten+1; j++) {
					for (int i = 1; i <= matches; i++) {
						if (kaartnr + i < 197) {
							map.put(kaartnr + i, map.get(kaartnr + i) == null ? 1 : map.get(kaartnr + i) + 1);
						}
					}
				}

			});
		} catch (IOException ex) {
			System.out.println("leesfout");
		}

	}

	private int berekenPuntenKaart(int[] winningNumbers, int[] numbers) {
		int punten = 0;
		List<Integer> lijstWinningNumbers = new ArrayList<>(
				Arrays.asList(Arrays.stream(winningNumbers).boxed().toArray(Integer[]::new)));

		for (int nr : numbers) {
			if (lijstWinningNumbers.contains(nr)) {
				if (punten == 0) {
					punten = 1;
				} else {
					punten *= 2;
				}
			}
		}
		return punten;
	}

	private int berekenAantalMatches(int[] winningNumbers, int[] numbers) {
		int matches = 0;
		List<Integer> lijstWinningNumbers = new ArrayList<>(
				Arrays.asList(Arrays.stream(winningNumbers).boxed().toArray(Integer[]::new)));

		for (int nr : numbers) {
			if (lijstWinningNumbers.contains(nr)) {
				matches += 1;
			}
		}
		return matches;
	}

}
