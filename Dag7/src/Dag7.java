import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag7 {

	public static void main(String[] args) {
		Dag7 d = new Dag7();

	}

	private List<Hand> listOfHands1 = new ArrayList<>();
	private int totalWinnings1;

	private List<Hand> listOfHands2 = new ArrayList<>();
	private int totalWinnings2;

	public Dag7() {
		leesFile();
		Collections.sort(listOfHands1);
		for (int i = 0; i < listOfHands1.size(); i++) {
			totalWinnings1 += listOfHands1.get(i).getBid() * (i + 1);
		}

		/*
		 * for (Hand1 h : listOfHands1) { System.out.println(h); }
		 */
		System.out.println("total winnings part 1: " + totalWinnings1);

		Collections.sort(listOfHands2);

		for (int i = 0; i < listOfHands2.size(); i++) {
			totalWinnings2 += listOfHands2.get(i).getBid() * (i + 1);
		}
		/*
		 * for (Hand2 h : listOfHands2) { System.out.println(h); }
		 */
		System.out.println("total winnings part 2: " + totalWinnings2);
	}

	private void leesFile() {
		try (Stream<String> stream = Files.lines(Paths.get("input.txt"))) {
			stream.forEach(regel -> {
				Scanner sc = new Scanner(regel);
				String cards = sc.next();
				int bid = Integer.parseInt(sc.next());
				listOfHands1.add(new Hand(cards, bid, false));
				listOfHands2.add(new Hand(cards, bid, true));
			});
		} catch (IOException e) {
			System.out.println("leesfout " + e);
		}
	}

	public class Hand implements Comparable<Hand> {
		private String cards;
		private int bid;
		private static final int FIVE_OF_A_KIND = 50;
		private static final int FOUR_OF_A_KIND = 40;
		private static final int FULL_HOUSE = 35;
		private static final int THREE_OF_A_KIND = 30;
		private static final int TWO_PAIR = 20;
		private static final int ONE_PAIR = 10;
		private static final int HIGH_CARD = 1;
		private int type;
		boolean joker;
		String order;

		public Hand(String cards, int bid, boolean joker) {
			this.cards = cards;
			this.bid = bid;
			if (joker) {
				order = "AKQT98765432J";
				this.joker = joker;
			} else {
				order = "AKQJT98765432";
			}
			calculateType();
		}

		private void calculateType() {
			Map<Character, Integer> map = new HashMap<>();
			for (int i = 0; i < 5; i++) {
				Character label = cards.charAt(i);
				map.put(label, map.get(label) == null ? 1 : map.get(label) + 1);
			}
			int max = map.values().stream().max(Integer::compareTo).get();

			if (joker && map.containsKey('J') && map.get('J') != 5) {
				int maxWithoutJ = map.entrySet().stream().filter(entry -> entry.getKey() != 'J')
						.map(entry -> entry.getValue()).max(Integer::compareTo).get();
				if (maxWithoutJ != 1) {
					max += map.get('J');
				} else {
					max += 1;
				}
				if (max > 5) {
					max = 5;
				}
			}

			/*
			 * System.out.println(cards); for(Entry<Character,Integer> entry:
			 * map.entrySet()) { System.out.printf("[%s]:%d ",entry.getKey(),
			 * entry.getValue()); } System.out.println("max: " + max );
			 */
			type = switch (max) {
			case 5 -> FIVE_OF_A_KIND;
			case 4 -> FOUR_OF_A_KIND;
			case 3 -> fullHouseOrThreeOfKind(map);
			case 2 -> twoPairOrOnePair(map);
			case 1 -> HIGH_CARD;
			default -> throw new IllegalArgumentException("Unexpected value: " + max);
			};

		}

		private int fullHouseOrThreeOfKind(Map<Character, Integer> map) {
			if (joker && map.containsKey('J')) {
				int numberOfTwos = (int) (map.values().stream().filter(x -> x == 2).count());
				return switch (numberOfTwos) {
				case 1 -> THREE_OF_A_KIND;
				case 2 -> FULL_HOUSE;
				default -> throw new IllegalArgumentException("Unexpected value: " + numberOfTwos + map);
				};
			}

			if (map.values().contains(2)) {
				return FULL_HOUSE;
			}
			return THREE_OF_A_KIND;
		}

		private int twoPairOrOnePair(Map<Character, Integer> map) {
			if (joker && map.containsKey('J')) {
				if ((map.values().stream().filter(x -> x == 2).count() + 1) == 1) {
					return ONE_PAIR;
				}
				return TWO_PAIR;
			}

			if ((int) (map.values().stream().filter(x -> x == 2).count()) == 1) {
				return ONE_PAIR;
			}
			return TWO_PAIR;
		}

		@Override
		public int compareTo(Hand h) {
			int typeCmp = Integer.compare(type, h.getType());
			return typeCmp != 0 ? typeCmp : compareHandsWithSameType(h);
		}

		private int compareHandsWithSameType(Hand h) {
			for (int i = 0; i < 5; i++) {
				char label1 = cards.charAt(i);
				char label2 = h.getCards().charAt(i);
				if (label1 == label2) {
					continue;
				}
				int index1 = order.indexOf(label1);
				int index2 = order.indexOf(label2);
				if (index1 > index2) {
					return -1;
				}
				return 1;
			}
			return 0;
		}

		public String getCards() {
			return cards;
		}

		public int getBid() {
			return bid;
		}

		public int getType() {
			return type;
		}

		@Override
		public String toString() {
			return "Hand [cards=" + cards + ", bid=" + bid + ", type=" + type + "]";
		}
	}
}
