import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dag20 {

	public static void main(String[] args) {
		Dag20 d = new Dag20();
	}

	// private String fileName = "test.txt";
	private String fileName = "input.txt";
	// private String fileName = "test2.txt";
	public List<Module> moduleList = new ArrayList<>();
	private long lowBeam = 0;
	private long highBeam = 0;
	private Queue<BeamTo> queue = new ArrayDeque<>();
	private Module rx;
	private int rxHit;
	private Beam beamRx;
	private long presses;

	public Dag20() {
		readFile();
		initializeConjunctionsMap();
		pressButton();
		System.out.printf("After one press on the button:%nLow beams: %d%nHigh beams: %d%n", lowBeam, highBeam);
		for (int i = 0; i < 999; i++) {
			pressButton();
		}
		System.out.printf("After 1000 presses on the button:%nLow beams: %d%nHigh beams: %d%n", lowBeam, highBeam);
		System.out.printf("Result after multiplying: %d%n", lowBeam * highBeam);

		// PART 2 takes a few centuries...
		rx = moduleList.stream().filter(module -> module.getName().equals("rx")).findFirst().orElse(null);
		do {
			presses++;
			rxHit = 0;
			beamRx = null;
			pressButton();
		} while (!(rxHit == 1 && beamRx == Beam.LOW));
		System.out.printf("Presses required to deliver a single low pulse to the module named rx: %d", presses);
	}

	private void pressButton() {
		// System.out.println("button -LOW-> broadcaster");
		lowBeam++;
		((Broadcaster) moduleList.stream().filter(module -> module instanceof Broadcaster).findFirst().orElse(null))
				.sendBeam(Beam.LOW);
		while (!queue.isEmpty()) {
			BeamTo beamTo = queue.poll();
			Module origin = moduleList.stream().filter(module -> module == beamTo.origin).findFirst().orElse(null);
			Module dest = moduleList.stream().filter(module -> module == beamTo.destination).findFirst().orElse(null);
			if (dest != null) {
				dest.receiveBeam(origin, beamTo.beam);
				if (rx == dest) {
					rxHit++;
					beamRx = beamTo.beam;
				}
			}
			/*
			 * System.out.printf("%s -%s-> %s%n", origin.getName(), beamTo.beam, dest !=
			 * null ? dest.getName() : beamTo.destination);
			 */
			switch (beamTo.beam) {
			case LOW -> lowBeam++;
			case HIGH -> highBeam++;
			}

		}

	}

	private void initializeConjunctionsMap() {
		for (Module module : moduleList) {
			if (module instanceof Conjunction) {
				for (Module m : moduleList) {
					if (m.getSendTo().contains(module.getName())) {
						((Conjunction) module).addModuleToMap(m);
					}
				}
			}
		}

	}

	private void readFile() {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(line -> {
				Scanner sc = new Scanner(line);
				String module = sc.next();
				sc.next();
				String receivers = sc.nextLine().substring(1);
				List<String> receiversList = new ArrayList<>(Arrays.asList(receivers.split(",\\s+")));

				switch (module.charAt(0)) {
				case '%' -> moduleList.add(new FlipFlop(module.substring(1, module.length()), receiversList));
				case '&' -> moduleList.add(new Conjunction(module.substring(1, module.length()), receiversList));
				case 'b' -> moduleList.add(new Broadcaster(module, receiversList));
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract class Module {
		private String name;
		public List<String> sendToList;

		public Module(String name, List<String> sendToList) {
			this.name = name;
			this.sendToList = sendToList;
		}

		public String getName() {
			return this.name;
		}

		public List<String> getSendTo() {
			return this.sendToList;
		}

		public void sendBeam(Beam beam) {
			sendToList.forEach(moduleName -> {
				Module dest = moduleList.stream().filter(module -> module.getName().equals(moduleName)).findFirst()
						.orElse(null);
				queue.offer(new BeamTo(beam, this, dest));
			});
		}

		abstract void receiveBeam(Module module, Beam beam);
	}

	public class FlipFlop extends Module {
		private boolean state;

		public FlipFlop(String name, List<String> sendTo) {
			super(name, sendTo);

		}

		public void receiveBeam(Module module, Beam beam) {
			if (beam == Beam.LOW) {
				state = !state;
				if (state) {
					sendBeam(Beam.HIGH);
				} else {
					sendBeam(Beam.LOW);
				}
			}
		}
	}

	public class Conjunction extends Module {
		private Map<Module, Beam> recentPulse = new HashMap<>();

		public Conjunction(String name, List<String> sendTo) {
			super(name, sendTo);

		}

		public void receiveBeam(Module module, Beam beam) {
			recentPulse.replace(module, beam);
			if (recentPulse.values().stream().allMatch(b -> b == Beam.HIGH)) {
				sendBeam(Beam.LOW);
			} else {
				sendBeam(Beam.HIGH);
			}

		}

		public void addModuleToMap(Module module) {
			recentPulse.put(module, Beam.LOW);
		}
	}

	public class Broadcaster extends Module {

		public Broadcaster(String name, List<String> sendTo) {
			super(name, sendTo);

		}

		public void receiveBeam(Module module, Beam beam) {
			sendBeam(beam);
		}
	}

	public record BeamTo(Beam beam, Module origin, Module destination) {
	}

	public enum Beam {
		LOW, HIGH;
	}
}
