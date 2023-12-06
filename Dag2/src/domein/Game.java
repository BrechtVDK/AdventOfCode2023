package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
	private int ID;
	// red green blue
	List<int[]> trekkingLijst = new ArrayList<>();

	public Game(int id, List<int[]> trekkingLijst) {
		this.ID = id;
		this.trekkingLijst = trekkingLijst;
	}

	private int getMaxAantalRood() {
		return trekkingLijst.stream().map(trekking -> trekking[0]).max(Integer::compare).get();
	}

	private int getMaxAantalGroen() {
		return trekkingLijst.stream().map(trekking -> trekking[1]).max(Integer::compare).get();
	}

	private int getMaxAantalBlauw() {
		return trekkingLijst.stream().map(trekking -> trekking[2]).max(Integer::compare).get();
	}

	public int getID() {
		return ID;
	}

	// red green blue
	public boolean isTrekkingMogelijk(int[] aantalBallen) {
		return !(getMaxAantalRood() > aantalBallen[0] || getMaxAantalGroen() > aantalBallen[1]
				|| getMaxAantalBlauw() > aantalBallen[2]);
	}

	private int[] minBallen() {
		int[] minBallen = { getMaxAantalRood(), getMaxAantalGroen(), getMaxAantalBlauw() };
		return minBallen;
	}

	public int powerSet() {
		return minBallen()[0] * minBallen()[1] * minBallen()[2];
	}

	@Override
	public String toString() {
		return "Game [ID=" + ID + " " + trekkingLijst.stream()
				.map(trekking -> trekking[0] + " " + trekking[1] + " " + trekking[2]).collect(Collectors.toList())
				+ "]";
	}

}
