import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Dag5_2 {

	private List<long[]> seedToSoilList = new ArrayList<>();
	private List<long[]> soilToFertilizerList = new ArrayList<>();
	private List<long[]> fertilizerToWaterList = new ArrayList<>();
	private List<long[]> waterToLightList = new ArrayList<>();
	private List<long[]> lightToTemperatureList = new ArrayList<>();
	private List<long[]> temperatureToHumidityList = new ArrayList<>();
	private List<long[]> humidityToLocationList = new ArrayList<>();
	private List<List<long[]>> lijsten = new ArrayList<>();
	private long minLocation = Long.MAX_VALUE;

	public Dag5_2() {
		leesFile("seed-to-soil.txt");
		leesFile("soil-to-fertilizer.txt");
		leesFile("fertilizer-to-water.txt");
		leesFile("water-to-light.txt");
		leesFile("light-to-temperature.txt");
		leesFile("temperature-to-humidity.txt");
		leesFile("humidity-to-location.txt");

		lijsten.add(seedToSoilList);
		lijsten.add(soilToFertilizerList);
		lijsten.add(fertilizerToWaterList);
		lijsten.add(waterToLightList);
		lijsten.add(lightToTemperatureList);
		lijsten.add(temperatureToHumidityList);
		lijsten.add(humidityToLocationList);

		long[] inputSeeds = { 1310704671l, 312415190l, 1034820096l, 106131293l, 682397438l, 30365957l, 2858337556l,
				1183890307l, 665754577l, 13162298l, 2687187253l, 74991378l, 1782124901l, 3190497l, 208902075l,
				226221606l, 4116455504l, 87808390l, 2403629707l, 66592398l };
//		long[] inputSeeds= {79l,14l,55l,13l};
		
		for (int i = 0; i < inputSeeds.length; i = i + 2) {
			for (long j = 0l; j < inputSeeds[i + 1]; j++) {
				long location = getLocation(inputSeeds[i] + j);
				if (location < minLocation) {
					minLocation = location;
				}
			}
		}
		System.out.println("deel2: " + minLocation);

	}

	private long getLocation(long seednr) {
		long[] seed = { seednr, 0l, 0l, 0l, 0l, 0l, 0l, 0l };

		for (int map = 0; map < 7; map++) {
			for (int i = 0; i < lijsten.get(map).size(); i++) {
				if (seed[map] >= lijsten.get(map).get(i)[1]
						&& seed[map] < lijsten.get(map).get(i)[1] + lijsten.get(map).get(i)[2]) {
					long verschil = seed[map] - lijsten.get(map).get(i)[1];
					long result = lijsten.get(map).get(i)[0] + verschil;
					seed[map + 1] = result;
					break;
				}
				if (seed[map + 1] == 0l) {
					seed[map + 1] = seed[map];
				}
			}
		}
		return seed[7];
	}

	private void leesFile(String naamBestand) {
		try (Stream<String> stream = Files.lines(Paths.get(naamBestand))) {
			stream.forEach(regel -> {
				Scanner scanner = new Scanner(regel);
				String lijn = scanner.nextLine();
				String[] getallen = lijn.split(" ");
				long[] array = new long[3];
				for (int i = 0; i < 3; i++) {
					array[i] = Long.parseLong(getallen[i]);
				}
				switch (naamBestand) {
				case "seed-to-soil.txt" -> seedToSoilList.add(array);
				case "soil-to-fertilizer.txt" -> soilToFertilizerList.add(array);
				case "fertilizer-to-water.txt" -> fertilizerToWaterList.add(array);
				case "water-to-light.txt" -> waterToLightList.add(array);
				case "light-to-temperature.txt" -> lightToTemperatureList.add(array);
				case "temperature-to-humidity.txt" -> temperatureToHumidityList.add(array);
				case "humidity-to-location.txt" -> humidityToLocationList.add(array);
				}
			});
		} catch (IOException ex) {
			System.out.println("leesfout");
		}
	}

}
