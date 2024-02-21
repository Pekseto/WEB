package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import beans.OrderCancellation;

public class OrderCancellationDao {
	private ArrayList<OrderCancellation> cancellations = new ArrayList<OrderCancellation>();
	private String contextPath = "C:\\Users\\Luka\\Desktop\\rent-a-car\\WebContent\\WEB-INF\\cancellations.txt";
	
	public OrderCancellationDao() {
		loadCancellations();
	}

	private void loadCancellations() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath);
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;

			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while (st.hasMoreTokens()) {
					int customerId = Integer.parseInt(st.nextToken().trim());
					LocalDate cancellationDate = LocalDate.parse(st.nextToken().trim());
					OrderCancellation orderCancellation = new OrderCancellation(customerId, cancellationDate);
					cancellations.add(orderCancellation);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
	
	private void saveCancellations() {
		try {
			FileWriter file = new FileWriter(contextPath);

			BufferedWriter output = new BufferedWriter(file);

			for (OrderCancellation cancellation : cancellations) {
				output.write(cancellation.returnSerializableFormat());
			}

			output.close();

		} catch (Exception e) {
			System.out.println(e);
			e.getStackTrace();
		}
	}
	
	public boolean addCancellation(int customerId) {
		try {
			OrderCancellation newCancellation = new OrderCancellation(customerId, LocalDate.now());
			cancellations.add(newCancellation);
			saveCancellations();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isSuspicious(int customerId) {
		int cancelCounter = 0;
		for (OrderCancellation orderCancellation : cancellations) {
			if(orderCancellation.getCustomerId() == customerId) {
				LocalDate lastMonthDate = LocalDate.now().minusMonths(1);
				LocalDate cancellationDate = orderCancellation.getCancellationDate();
				if(cancellationDate.compareTo(lastMonthDate) >= 0) {
					cancelCounter++;
				}
			}
		}
		return cancelCounter >= 5;
	}

}
