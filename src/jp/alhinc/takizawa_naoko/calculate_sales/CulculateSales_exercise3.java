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

public class CulculateSales_exercise3 {
	public static void main(String args[]) throws FileNotFoundException {
		HashMap<String, String> branchMap = new HashMap<String, String>();
		HashMap<String, String> commodityMap = new HashMap<String, String>();
		HashMap<String, Long> branchSalesMap = new HashMap<String, Long>();
		HashMap<String, Long> commoditySalesMap = new HashMap<String, Long>();

		if (args.length != 1) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		File branchFile = new File(args[0], "branch.lst");
		if (!branchFile.exists()) {
			System.out.println("支店定義ファイルが存在しません");
			return;
		}

		BufferedReader brBranchFile = null;
		try {
			FileReader frBranchFile = new FileReader(branchFile);
			brBranchFile = new BufferedReader(frBranchFile);
			String s;
				while ((s = brBranchFile.readLine()) != null) {

					String[] branches = s.split(",");
					if (!((branches.length == 2) && (branches[0].matches("^\\d{3}$")))) {
						System.out.println("支店定義ファイルのフォーマットが不正です");
						return;
					}
					branchMap.put(branches[0], branches[1]);
					branchSalesMap.put(branches[0], 0L);
				}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				brBranchFile.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}




		File commodityFile = new File(args[0], "commodity.lst");
		if (!commodityFile.exists()) {
			System.out.println("商品定義ファイルが存在しません");
			return;
		}

		BufferedReader brcommodityFile = null;
		try {
			FileReader frcommodityFile = new FileReader(commodityFile);
			brcommodityFile = new BufferedReader(frcommodityFile);
			String s;
			while ((s = brcommodityFile.readLine()) != null) {
				String[] commodities = s.split(",");
				if (!((commodities.length == 2) && (commodities[0].matches("^[a-zA-Z0-9]{8}$")))) {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				commodityMap.put(commodities[0], commodities[1]);
				commoditySalesMap.put(commodities[0], 0L);
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				brcommodityFile.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		ArrayList<String> salesArray = new ArrayList<String>();
		File salesFilesDirs = new File(args[0]);
		File[] filelist = salesFilesDirs.listFiles();
		for(int i = 0; i < filelist.length; i++){
			if(filelist[i].isFile() && filelist[i].getName().matches("\\d{8}\\.rcd$")){
				salesArray.add(filelist[i].getName());
			}
		}
		Collections.sort(salesArray);

		for (int salesArrayIndex = 0; salesArrayIndex < salesArray.size() - 1; salesArrayIndex++) {
			String salesArrayName = salesArray.get(salesArrayIndex).substring(1, 8);
			int salesArrayName_int = Integer.parseInt(salesArrayName);

			String salesArrayName2 = salesArray.get(salesArrayIndex + 1).substring(1, 8);
			int salesArrayName2_int = Integer.parseInt(salesArrayName2);
			if (salesArrayName2_int - salesArrayName_int != 1) {
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}

		BufferedReader brsales;
		try {
			for (int arrayIndex = 0; arrayIndex < salesArray.size(); arrayIndex++) {
				File sales = new File(args[0], salesArray.get(arrayIndex));
				FileReader frsales = new FileReader(sales);
				brsales = new BufferedReader(frsales);
				String salesLine;
				ArrayList<String> saleFileList = new ArrayList<String>();
				while ((salesLine = brsales.readLine()) != null) {
					saleFileList.add(salesLine);
				}
				brsales.close();

				if (saleFileList.size() != 3) {
					System.out.println(salesArray.get(arrayIndex) + "のフォーマットが不正です");
					return;
				}
				if (!(saleFileList.get(2).matches("^[0-9]+$"))) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				long Sales_Long = Long.parseLong(saleFileList.get(2));
				

				if (!branchMap.containsKey(saleFileList.get(0))) {
					System.out.println(salesArray.get(arrayIndex) + "の支店コードが不正です");
					return;
				} else {
					long sumBranchSales = branchSalesMap.get(saleFileList.get(0)) + Sales_Long;
					String sumCount = String.valueOf(sumBranchSales);
					if (!sumCount.matches("^[0-9]{1,10}$")) {
						System.out.println("合計金額が10桁を超えました");
						return;
					}
					branchSalesMap.put(saleFileList.get(0), sumBranchSales);
				}

				if (!commodityMap.containsKey(saleFileList.get(1))) {
					System.out.println(salesArray.get(arrayIndex) + "の商品コードが不正です");
					return;
				} else {
					long sumCommoditySales = commoditySalesMap.get(saleFileList.get(1)) + Sales_Long;
					String sumCount2 = String.valueOf(sumCommoditySales);
					if (!sumCount2.matches("^\\d{1,10}$")) {
						System.out.println("合計金額が10桁を超えました");
						return;
					}
					commoditySalesMap.put(saleFileList.get(1), sumCommoditySales);
				}
			}
		}catch (IOException e) {
				// TODO 自動生成された catch ブロック
			System.out.println("予期せぬエラーが発生しました");
		}



		List<Map.Entry<String, Long>> branchSalesEntry = new ArrayList<Map.Entry<String, Long>>(
				branchSalesMap.entrySet());
		Collections.sort(branchSalesEntry, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(java.util.Map.Entry<String, Long> entry1, java.util.Map.Entry<String, Long> entry2) {
				// TODO 自動生成されたメソッド・スタブ
				return entry2.getValue().compareTo(entry1.getValue()); // 降順
			}
		});

		BufferedWriter bwBranchOutFile = null;
		try {
			File branchOutFile = new File(args[0], "branch.out");
			FileWriter fwBranchOutFile = new FileWriter(branchOutFile);
			bwBranchOutFile = new BufferedWriter(fwBranchOutFile);

			for (Entry<String, Long> forBranchOut : branchSalesEntry) {

				bwBranchOutFile.write(forBranchOut.getKey() + "," + branchMap.get(forBranchOut.getKey()) + ","
						+ forBranchOut.getValue());
				bwBranchOutFile.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				bwBranchOutFile.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		List<Map.Entry<String, Long>> commoditySalesEntry = new ArrayList<Map.Entry<String, Long>>(
				commoditySalesMap.entrySet());
		Collections.sort(commoditySalesEntry, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(java.util.Map.Entry<String, Long> entry1, java.util.Map.Entry<String, Long> entry2) {
				// TODO 自動生成されたメソッド・スタブ
				return entry2.getValue().compareTo(entry1.getValue()); // 降順
			}
		});

		BufferedWriter bwcommodityOutFile;
		try {
			File commodityOutFile = new File(args[0], "commodity.out");
			FileWriter fwcommodityOutFile = new FileWriter(commodityOutFile);
			bwcommodityOutFile = new BufferedWriter(fwcommodityOutFile);

			for (Entry<String, Long> forCommodityOut : commoditySalesEntry) {

				bwcommodityOutFile.write(forCommodityOut.getKey() + "," + commodityMap.get(forCommodityOut.getKey())
						+ "," + forCommodityOut.getValue());
				bwcommodityOutFile.newLine();
			}
			bwcommodityOutFile.close();

		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try {
				bwBranchOutFile.close();
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}
	}
}
