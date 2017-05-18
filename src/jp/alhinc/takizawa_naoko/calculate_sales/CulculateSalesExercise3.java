package jp.alhinc.takizawa_naoko.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CulculateSalesExercise3 {


	public static boolean codeFileReader(String dirPath, String codeFileName, String letters, String kindOfCode, HashMap<String, String>codeMap, HashMap<String,Long>salesMap){
		BufferedReader br = null;
		try {
			File codeFile = new File(dirPath, codeFileName);
			if (!codeFile.exists()) {
				System.out.println(kindOfCode + "定義ファイルが存在しません");
				return false;
			}
			FileReader fr = new FileReader(codeFile);
			br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				String[] codeLines = s.split(",");
				if (codeLines.length != 2 || !codeLines[0].matches(letters)) {
					System.out.println(kindOfCode + "定義ファイルのフォーマットが不正です");
					return false;
				}
				codeMap.put(codeLines[0], codeLines[1]);
				salesMap.put(codeLines[0], 0L);
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return false;
			}
		}
		return true;
	}



	public static boolean salesSortWriter(String dirPath, String outFileName, HashMap<String,String>codeMap, HashMap<String,Long>salesMap){
		List<Map.Entry<String, Long>> salesEntry = new ArrayList<Map.Entry<String, Long>>(
				salesMap.entrySet());
		Collections.sort(salesEntry, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(java.util.Map.Entry<String, Long> entry1, java.util.Map.Entry<String, Long> entry2) {
				// TODO 自動生成されたメソッド・スタブ
				return entry2.getValue().compareTo(entry1.getValue()); // 降順
			}
		});

		BufferedWriter bw = null;
		try {
			File outFile = new File(dirPath, outFileName);
			FileWriter fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);

			for (Entry<String, Long> mapSetArrayList : salesEntry) {

				bw.write(mapSetArrayList.getKey() + "," + codeMap.get(mapSetArrayList.getKey())
						+ "," + mapSetArrayList.getValue());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return false;
		} finally {
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return false;
			}
		}
		return true;
	}

	public static void main(String args[]) throws FileNotFoundException {
		HashMap<String, String> branchMap = new HashMap<String, String>();
		HashMap<String, String> commodityMap = new HashMap<String, String>();
		HashMap<String, Long> branchSalesMap = new HashMap<String, Long>();
		HashMap<String, Long> commoditySalesMap = new HashMap<String, Long>();

		if (args.length != 1) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		if (!codeFileReader(args[0], "branch.lst", "^\\d{3}$", "支店", branchMap, branchSalesMap)) {
			return;
		}
		if (!codeFileReader(args[0], "commodity.lst", "^[a-zA-Z0-9]{8}$", "商品", commodityMap, commoditySalesMap)) {
			return;
		}

		ArrayList<String> salesArray = new ArrayList<String>();
		File salesFilesDirs = new File(args[0]);
		File[] filelist = salesFilesDirs.listFiles();
		for (int i = 0; i < filelist.length; i++){
			if (filelist[i].isFile() && filelist[i].getName().matches("\\d{8}\\.rcd$")) {
				salesArray.add(filelist[i].getName());
			}
		}
		Collections.sort(salesArray);

		for (int i = 0; i < salesArray.size() - 1; i++) {
			String salesArrayName = salesArray.get(i).substring(1, 8);
			int salesArrayNameInt = Integer.parseInt(salesArrayName);

			String salesArrayName2 = salesArray.get(i + 1).substring(1, 8);
			int salesArrayName2Int = Integer.parseInt(salesArrayName2);
			if (salesArrayName2Int - salesArrayNameInt != 1) {
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}

		BufferedReader br = null;
		try {
			for (int i = 0; i < salesArray.size(); i++) {
				File sales = new File(args[0], salesArray.get(i));
				FileReader fr = new FileReader(sales);
				br = new BufferedReader(fr);
				String salesLine;
				ArrayList<String> saleFileList = new ArrayList<String>();
				while ((salesLine = br.readLine()) != null) {
					saleFileList.add(salesLine);
				}


				if (saleFileList.size() != 3) {
					System.out.println(salesArray.get(i) + "のフォーマットが不正です");
					return;
				}
				if (!(saleFileList.get(2).matches("^[0-9]+$"))) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				long SalesLong = Long.parseLong(saleFileList.get(2));

				if (!branchMap.containsKey(saleFileList.get(0))) {
					System.out.println(salesArray.get(i) + "の支店コードが不正です");
					return;
				}
				if (!commodityMap.containsKey(saleFileList.get(1))) {
					System.out.println(salesArray.get(i) + "の商品コードが不正です");
					return;
				}

				long sumBranchSales = branchSalesMap.get(saleFileList.get(0)) + SalesLong;
				String sumCount = String.valueOf(sumBranchSales);
				if (!sumCount.matches("^[0-9]{1,10}$")) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}

				long sumCommoditySales = commoditySalesMap.get(saleFileList.get(1)) + SalesLong;
				String sumCount2 = String.valueOf(sumCommoditySales);
				if (!sumCount2.matches("^\\d{1,10}$")) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}

				branchSalesMap.put(saleFileList.get(0), sumBranchSales);
				commoditySalesMap.put(saleFileList.get(1), sumCommoditySales);
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}


		if(!salesSortWriter(args[0], "branch.out", branchMap, branchSalesMap)) {
			return;
		}
		if(!salesSortWriter(args[0], "commodity.out", commodityMap, commoditySalesMap)) {
			return;
		}
	}
}

