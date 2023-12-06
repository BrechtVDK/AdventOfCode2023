package domein;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Dag1 {
	private List<String> codes = new ArrayList<>();
	private int sumOfCalibrationValues1, sumOfCalibrationValues2;

	public Dag1() {
		leesFile();
		codes.forEach(code -> {
			sumOfCalibrationValues1 += decodeer1(code);
			sumOfCalibrationValues2 += decodeer2(code);
		});
		System.out.println("deel 1 " + sumOfCalibrationValues1);
		System.out.println("deel 2 " + sumOfCalibrationValues2);
	}

	private void leesFile() {
		try (Stream<String> stream = Files.lines(Paths.get("CalibrationDoc.txt"))) {
			stream.forEach(regel -> {
				Scanner scanner = new Scanner(regel);
				codes.add(scanner.next());
			});
		} catch (IOException ex) {
			System.out.println("leesfout");
		}
	}

	private int decodeer1(String code) {
		int firstDigit = -1, lastDigit = -1, calibrationValue;

		for (int i = 0; i < code.length(); i++) {
			int charToInt = Character.getNumericValue(code.charAt(i));
			if (charToInt < 10) {
				if (firstDigit == -1) {
					firstDigit = charToInt;
				} else {
					lastDigit = charToInt;
				}
			}
		}
		if (lastDigit == -1) {
			lastDigit = firstDigit;
		}

		calibrationValue = firstDigit * 10 + lastDigit;
		return calibrationValue;

	}

	private int decodeer2(String code) {
		int firstDigit = -1, lastDigit = -1, indexFirstDigit = -1, indexLastDigit = -1, calibrationValue;

		for (int i = 0; i < code.length(); i++) {
			int charToInt = Character.getNumericValue(code.charAt(i));
			if (charToInt < 10) {
				if (firstDigit == -1) {
					firstDigit = charToInt;
					indexFirstDigit = i;
				} else {
					lastDigit = charToInt;
					indexLastDigit = i;
				}
			}
		}
		if (lastDigit == -1) {
			lastDigit = firstDigit;
			indexLastDigit = indexFirstDigit;
		}

		TreeMap<Integer, Integer> map = containsSpelledDigits(code);

		Map.Entry<Integer, Integer> firstEntry = map.firstEntry();
		Map.Entry<Integer, Integer> lastEntry = map.lastEntry();

		if (firstEntry != null && firstEntry.getKey() < indexFirstDigit) {
			firstDigit = firstEntry.getValue();
		}
		if (lastEntry != null && lastEntry.getKey() > indexLastDigit) {
			lastDigit = lastEntry.getValue();
		}

		calibrationValue = firstDigit * 10 + lastDigit;
		return calibrationValue;
	}

	// startIndex, getal
	private TreeMap<Integer, Integer> containsSpelledDigits(String code) {
		TreeMap<Integer, Integer> map = new TreeMap<>();
		String[] arrayStrings = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
		List<String> spelledDigits = new ArrayList<>(Arrays.asList(arrayStrings));

		spelledDigits.stream().forEach(spelledDigit -> {
			int[] fromIndex = { 0 };
			if (code.indexOf(spelledDigit) != -1) {
				int startIndex = code.indexOf(spelledDigit, fromIndex[0]);
				map.put(startIndex, spelledDigits.indexOf(spelledDigit) + 1);
				fromIndex[0] = startIndex + 1;
				boolean flag = true;
				while (flag) {
					if (code.indexOf(spelledDigit, fromIndex[0]) != -1) {
						startIndex = code.indexOf(spelledDigit, fromIndex[0]);
						map.put(startIndex, spelledDigits.indexOf(spelledDigit) + 1);
						fromIndex[0] = startIndex + 1;
					} else {
						flag = false;
					}
				}
			}
		});
		return map;
	}

}
