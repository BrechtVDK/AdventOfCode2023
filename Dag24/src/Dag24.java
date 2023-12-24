import java.awt.font.NumericShaper.Range;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag24 {

	public static void main(String[] args) {
		Dag24 d = new Dag24();
	}

	private final String fileName = "input.txt";
	// private final String fileName = "test.txt";
	private List<HailStone> hailStoneList = new ArrayList<>();
	private int count;

	public Dag24() {
		readFile();
		for (int i = 0; i < hailStoneList.size() - 1; i++) {
			HailStone h1 = hailStoneList.get(i);
			for (int j = i + 1; j < hailStoneList.size(); j++) {
				HailStone h2 = hailStoneList.get(j);
				Intersect intersect = h1.getIntersect(h2);
				if (intersect.inArea && intersect.inFuture) {
					count++;
				}
			}
		}
		System.out.printf("Intersections in test area: %d%n", count);

	}

	public class HailStone {
		private long px, py, pz;
		private final int vx, vy, vz;
		private BigDecimal b, rico;
		// private final BigDecimal MIN = new BigDecimal("7");
		// private final BigDecimal MAX = new BigDecimal("27");
		private final BigDecimal MIN = new BigDecimal("200000000000000");
		private final BigDecimal MAX = new BigDecimal("400000000000000");
		private int scale = 10;
		private RoundingMode roundingMode = RoundingMode.HALF_UP;

		public HailStone(long px, long py, long pz, int vx, int vy, int vz) {
			this.px = px;
			this.py = py;
			this.pz = pz;
			this.vx = vx;
			this.vy = vy;
			this.vz = vz;
			setRicoAndB();
		}

		private void setRicoAndB() {
			rico = new BigDecimal((double) vy / (double) vx);
			b = rico.multiply(new BigDecimal(px)).subtract(new BigDecimal(py));
			b = b.multiply(new BigDecimal(-1));
		}

		public Intersect getIntersect(HailStone h) {
			BigDecimal x;
			try {
				x = (h.getB().subtract(b)).divide(rico.subtract(h.getRico()), scale, roundingMode);
			} catch (ArithmeticException e) {
				//divide by ZERO 
				return new Intersect(BigDecimal.ZERO, BigDecimal.ZERO, false, false);
			}
			BigDecimal y = (rico.multiply(x)).add(b);

			boolean inArea = false;
			if (x.compareTo(MIN) >= 0 && x.compareTo(MAX) <= 0 && y.compareTo(MIN) >= 0 && y.compareTo(MAX) <= 0) {
				inArea = true;
			}
			boolean inFuture = false;
			if ((vx < 0 && x.compareTo(new BigDecimal(px)) < 0 || vx > 0 && x.compareTo(new BigDecimal(px)) > 0)
					&& (h.getVx() < 0 && x.compareTo(new BigDecimal(h.getPx())) < 0
							|| h.getVx() > 0 && x.compareTo(new BigDecimal(h.getPx())) > 0)) {
				inFuture = true;
			}

			return new Intersect(x, y, inArea, inFuture);

		}

		public long getPx() {
			return px;
		}

		public long getPy() {
			return py;
		}

		public long getPz() {
			return pz;
		}

		public int getVx() {
			return vx;
		}

		public int getVy() {
			return vy;
		}

		public int getVz() {
			return vz;
		}

		public BigDecimal getRico() {
			return rico;
		}

		public BigDecimal getB() {
			return b;
		}

		@Override
		public String toString() {
			return String.format("px=%16d, py=%16d, pz=%16d, @ vx=%4d vy=%4d vz=%4d || rico=%5.2f b=%7.2f", px, py, pz,
					vx, vy, vz, rico, b);
		}

	}

	public record Intersect(BigDecimal x, BigDecimal y, boolean inArea, boolean inFuture) {
	};

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				String[] tokens = sc.nextLine().split("\\s@\\s");
				String[] tokensP = tokens[0].split(",\\s");
				String[] tokensV = tokens[1].split(",\\s");
				hailStoneList.add(new HailStone(Long.parseLong(tokensP[0]), Long.parseLong(tokensP[1]),
						Long.parseLong(tokensP[2]), Integer.parseInt(tokensV[0]), Integer.parseInt(tokensV[1]),
						Integer.parseInt(tokensV[2])));
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
