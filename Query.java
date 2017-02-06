import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Query {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scan = null;

		try {
			BufferedReader br;
			HashMap<String, HashMap<Integer, List<NodeCPU>>> map = new HashMap<String, HashMap<Integer, List<NodeCPU>>>();
			scan = new Scanner(new File(args[0]));
			int count = 0;
			System.out.println("Loading...");
			while (scan.hasNextLine()) {

				String line = scan.nextLine();
				String[] lineArray = line.split(",");
				lineArray[0] = lineArray[0].trim();
				lineArray[1] = lineArray[1].trim();
				lineArray[2] = lineArray[2].trim();
				lineArray[3] = lineArray[3].trim();

				// IP Address
				if (map.containsKey(lineArray[1])) {
					HashMap<Integer, List<NodeCPU>> mP = map.get(lineArray[1]);

					if (mP.containsKey(Integer.parseInt(lineArray[2]))) {

						List<NodeCPU> l1 = mP.get(Integer
								.parseInt(lineArray[2]));
						NodeCPU n = new NodeCPU();
						n.setCpuUsage(Integer.parseInt(lineArray[3].trim()));
						n.setTimestamp(Long.parseLong(lineArray[0]));
						l1.add(n);
					} else {

						List<NodeCPU> l1 = new ArrayList<NodeCPU>();
						NodeCPU n = new NodeCPU();
						n.setCpuUsage(Integer.parseInt(lineArray[3].trim()));
						n.setTimestamp(Long.parseLong(lineArray[0]));
						l1.add(n);
						mP.put(Integer.parseInt(lineArray[2]), l1);

					}

				}

				else {
					HashMap<Integer, List<NodeCPU>> m = new HashMap<Integer, List<NodeCPU>>();
					List<NodeCPU> l1 = new ArrayList<NodeCPU>();
					NodeCPU n = new NodeCPU();
					n.setCpuUsage(Integer.parseInt(lineArray[3].trim()));
					n.setTimestamp(Long.parseLong(lineArray[0]));
					l1.add(n);
					m.put(Integer.parseInt(lineArray[2].trim()), l1);
					map.put(lineArray[1], m);

				}

			}
			System.out.println("Please enter required query");
			System.out.println("Format: QUERY IP CPU_ID Start(yyyy-MM-dd HH:mm) End(yyyy-MM-dd HH:mm) -> to query cpu usage");
			System.out.println("Format: EXIT -> to exit application");
			br = new BufferedReader(new InputStreamReader(System.in));

			while (true) {

				System.out.print(">");
				String input = br.readLine();

				String ipAddr = "";
				String cpuId = "";
				String start = "";
				String end = "";
				long st = 0L;
				long ed = 0L;

				if ("EXIT".equals(input)) {
					//System.out.println("Exit!");
					System.exit(0);
				} else {
					String[] arr = input.split(" ");
					ipAddr = arr[1];
					cpuId = arr[2];
					start = arr[3] + " " + arr[4];
					end = arr[5] + " " + arr[6];

					st = StringtoEpoch(start);
					ed = StringtoEpoch(end);

				}

				System.out.println("CPU"+cpuId+" usage on "+ipAddr+":");

				if (map.containsKey(ipAddr.trim())) {
					HashMap<Integer, List<NodeCPU>> mP = map.get(ipAddr.trim());

					if (mP.containsKey(Integer.parseInt(cpuId.trim()))) {

						List<NodeCPU> ndList = doSearch(st, ed,
								mP.get(Integer.parseInt(cpuId.trim())));
						for (NodeCPU n : ndList)
							System.out.print("("
									+ EpochToString(n.getTimestamp()) + " "
									+ n.getCpuUsage() + "%),");

					}
					System.out.println();

				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static long StringtoEpoch(String start) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = df.parse(start);
		long epoch = (date.getTime()) / 1000;

		return epoch;
	}

	public static String EpochToString(long epoc) {
		epoc *= 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(epoc));

	}

	public static List<NodeCPU> doSearch(long start, long end, List<NodeCPU> lst) {

		Collections.sort(lst, new CustomComparator());
		List<NodeCPU> nd = new ArrayList<NodeCPU>();
		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < lst.size(); i++) {

			if (start <= lst.get(i).getTimestamp()) {
				startIndex = i;
				for (int j = lst.size() - 1; j >= 0; j--) {

					if (end >= lst.get(j).getTimestamp()) {
						endIndex = j;
						break;
					}
				}
				break;
			}

		}

		if (startIndex < endIndex)
			nd = lst.subList(startIndex, endIndex + 1);

		return nd;

	}

}

class CustomComparator implements Comparator<NodeCPU> {
	@Override
	public int compare(NodeCPU o1, NodeCPU o2) {
		return (o1.getTimestamp() < o2.getTimestamp()) ? -1 : 1;
	}
}
