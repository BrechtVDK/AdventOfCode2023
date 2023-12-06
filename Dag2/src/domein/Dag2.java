package domein;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag2 {
	private List<Game> games = new ArrayList<>();

	public Dag2() {
		leesFile();
		spel1();
		spel2();
	}

	private void spel1() {
		int[] aantalBallen = { 12, 13, 14 };
		int somIDs = games.stream().filter(game -> game.isTrekkingMogelijk(aantalBallen)).map(game -> game.getID()).reduce(0, Integer::sum);
		System.out.println(somIDs);
	}
	private void spel2() {
		int somPowerSets = games.stream().map(game -> game.powerSet()).reduce(0, Integer::sum);
		System.out.println(somPowerSets);
	}

	private void leesFile() {
		try (Stream<String> stream = Files.lines(Paths.get("games.txt"))) {
			stream.forEach(regel -> {
				Scanner scanner = new Scanner(regel);
				String game = scanner.nextLine();
				String[] tokens = game.split(":");
				String gameEnId = tokens[0];
				String trekkingen = tokens[1];
				int gameId = Integer.parseInt(gameEnId.split(" ")[1]);

				String[] trekkingenOpgesplitst = trekkingen.split(";");

				List<int[]> trekkingenLijst = ontleedTrekkingen(trekkingenOpgesplitst);
				games.add(new Game(gameId, trekkingenLijst));

			});
		} catch (IOException ex) {
			System.out.println("leesfout");
		}
	}

	private List<int[]> ontleedTrekkingen(String[] trekkingenOpgesplitst) {
		int aantalTrekkingen = trekkingenOpgesplitst.length;
		List<int[]> trekkingLijst = new ArrayList<>();

		for (int i = 0; i < aantalTrekkingen; i++) {
			String[] aantalEnKleuren = trekkingenOpgesplitst[i].split(",");
			int aantalKleuren = aantalEnKleuren.length;
			// red green blue
			int[] arrayAantal = new int[3];

			for (int j = 0; j < aantalKleuren; j++) {

				String[] split = aantalEnKleuren[j].split(" ");

				switch (split[2]) {
				case "red" -> arrayAantal[0] = Integer.parseInt(split[1]);
				case "green" -> arrayAantal[1] = Integer.parseInt(split[1]);
				case "blue" -> arrayAantal[2] = Integer.parseInt(split[1]);
				}
			}

			trekkingLijst.add(arrayAantal);

		}

		return trekkingLijst;
	}
}
