package culculateSales_exercise3;

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

		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
		}
		// ●●ここから下は、処理内容１
		File branchFile = new File(args[0] + "\\branch.lst"); // 支店定義ファイル
		if (!branchFile.exists()) {
			System.out.println(branchFile + "が存在しません");
			return;
		}
		FileReader frBranchFile = new FileReader(branchFile);
		BufferedReader brBranchFile = new BufferedReader(frBranchFile);
		try {

			String s;
			while ((s = brBranchFile.readLine()) != null) {

				String[] branches = s.split(",");
				if (!((branches.length == 2) && (branches[0].matches("^\\d{3}$")))) {
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
				branchMap.put(branches[0], branches[1]);
			}

		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try{
				brBranchFile.close();
			}
			catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		File commodityFile = new File(args[0] + "\\commodity.lst");
		if (!commodityFile.exists()) {
			System.out.println(commodityFile + "が存在しません");
		}
		FileReader frCommodityFile = new FileReader(commodityFile);
		BufferedReader brCommodityFile = new BufferedReader(frCommodityFile);
		try {

			String s;
			while ((s = brCommodityFile.readLine()) != null) {

				String[] commodities = s.split(",");
				if (!((commodities.length == 2) && (commodities[0].matches("^[a-zA-Z0-9]{8}$")))) {
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				commodityMap.put(commodities[0], commodities[1]);
			}

		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try{
				brCommodityFile.close();
			}
			catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}


		// ***********************************************************	
		
		ArrayList<String> salesArray = new ArrayList<String>();
		File salesFile = new File(args[0]); // 売上ファイルを探す
		String[] fileList = salesFile.list();
		for (String salesList : fileList) {
			if (salesList.matches("\\d{8}\\.rcd$")) {
				salesArray.add(salesList);
			}
		}
		Collections.sort(salesArray);
		System.out.println("登録データ数:" + salesArray.size()); // ■
		String listTest = salesArray.get(2); // ■
		System.out.println(listTest); // ■

		
		
		for (int salesArrayIndex = 0; salesArrayIndex < salesArray.size() - 1; salesArrayIndex++) {
			String salesArrayName = salesArray.get(salesArrayIndex).substring(1,8);
			System.out.println(salesArrayName);
			int salesArrayName_int = Integer.parseInt(salesArrayName);
			
			String salesArrayName2 = salesArray.get(salesArrayIndex + 1).substring(1,8);
			int salesArrayName2_int = Integer.parseInt(salesArrayName2);
			System.out.println(salesArrayName2_int);
			if (salesArrayName2_int - salesArrayName_int != 1) {
				System.out.println("ファイル名が連番になっていません");
				return;
			}
		}
		
		
		// **********************************************************
	

		try {
			ArrayList<String> saleFileList = new ArrayList<String>();
			for (int salesArrayIndex = 0; salesArrayIndex < salesArray.size(); salesArrayIndex++) {
				// ここで、salesArrayをファイルを順番に読み込んでいく
				File sales = new File(args[0] + "\\" + salesArray.get(salesArrayIndex));
				FileReader fr = new FileReader(sales);
				BufferedReader br = new BufferedReader(fr);
				String salesLine;

				while ((salesLine = br.readLine()) != null) {
					saleFileList.add(salesLine);
				}
				br.close();
				if (saleFileList.size() != 3) {
					System.out.println(salesArray.get(salesArrayIndex) + "のフォーマットが不正です");
					return;
				}
				if (!branchMap.containsKey(saleFileList.get(0))) {
					System.out.println(salesArray.get(salesArrayIndex) + "の支店コードが不正です");
					return;
				}
				if (!commodityMap.containsKey(saleFileList.get(1))) {
					System.out.println(salesArray.get(salesArrayIndex) + "の商品コードが不正です");
					return;
				}
				if (!(saleFileList.get(2).matches("^\\d{1,10}$"))) {
					System.out.println(salesArray.get(salesArrayIndex) + "の売上額が10桁を超えています");
					return;
				}
				long Sales_Long = Long.parseLong(saleFileList.get(2));

				if (!(branchSalesMap.containsKey(saleFileList.get(0)))) {
					branchSalesMap.put(saleFileList.get(0), Sales_Long);
					return;
				}
				long sumBranchSales = branchSalesMap.get(saleFileList.get(0)) + Sales_Long;
				String sumCount = String.valueOf(sumBranchSales);
				if (!sumCount.matches("^\\d{1,10}$")) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				branchSalesMap.put(saleFileList.get(0), sumBranchSales);

				if (!(commoditySalesMap.containsKey(saleFileList.get(1)))) {
					commoditySalesMap.put(saleFileList.get(1), Sales_Long);
					return;
				}
				long sumCommoditySales = commoditySalesMap.get(saleFileList.get(1)) + Sales_Long;
				String sumCount2 = String.valueOf(sumCommoditySales);
				if (!sumCount2.matches("^\\d{1,10}$")) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				commoditySalesMap.put(saleFileList.get(1), sumCommoditySales);
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("予期せぬエラーが発生しました");
		} finally {
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

		BufferedWriter bwBranch = null;
		try {
			File branchOutFile = new File(args[0], "branch.out");
			FileWriter fwBranch = new FileWriter(branchOutFile);
			bwBranch = new BufferedWriter(fwBranch);

			for (Entry<String, Long> forBranchOut : branchSalesEntry) {
				bwBranch.write(forBranchOut.getKey() + "," + branchMap.get(forBranchOut.getKey()) + ","
						+ forBranchOut.getValue());
				bwBranch.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try{
				bwBranch.close();
			}
			catch (IOException e) {
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

		BufferedWriter bwCommodity = null;
		try {
			File commodityOutFile = new File(args[0], "commodity.out");
			FileWriter fwCommodity = new FileWriter(commodityOutFile);
			bwCommodity = new BufferedWriter(fwCommodity);

			for (Entry<String, Long> forCommodityOut : commoditySalesEntry) {
				bwCommodity.write(forCommodityOut.getKey() + "," + commodityMap.get(forCommodityOut.getKey()) + ","
						+ forCommodityOut.getValue());
				bwCommodity.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
		} finally {
			try{
				bwCommodity.close();
			}
			catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
			}
		}
	}
}
