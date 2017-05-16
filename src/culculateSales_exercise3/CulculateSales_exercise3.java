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

		// ●●ここから下は、処理内容１
		File branchFile = new File(args[0] + "\\branch.lst"); // 支店定義ファイル
		if (!branchFile.exists()) {
			System.out.println(branchFile + "が存在しません");
		} else {
			try {
				FileReader fr = new FileReader(branchFile);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while ((s = br.readLine()) != null) {

					String[] branches = s.split(",");
					branchMap.put(branches[0], branches[1]);
				}
				br.close();// ■■finallyでしめる。スコープで、elseとtryの間にて、frとbrを宣言する。
			} catch (IOException e) {
				System.out.println(e);
			}
			System.out.println(branchMap.entrySet()); // ■

		}
// ***************************▲(処理内容1:OK)***********************************************

		File commodityFile = new File(args[0] + "\\commodity.lst"); // 商品定義ファイル
		if (!commodityFile.exists()) {
			System.out.println(commodityFile + "が存在しません");
			return;
		} else {
			try {
				FileReader fr = new FileReader(commodityFile);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while ((s = br.readLine()) != null) {

					String[] commodities = s.split(",");
					commodityMap.put(commodities[0], commodities[1]);
				}
				br.close();
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				System.out.println(commodityMap.entrySet());

			}
// ***************************▲(処理内容3:// OK)***********************************************

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

// ***************************▲◎(処理内容:8桁.rcdをリスト化、降順OK)********************************
			// ●この下、大問題か。(salesArrayファイルをStringからIntに変える流れ。)
			for (int arrayIndex = 0; arrayIndex < salesArray.size() - 1; arrayIndex++) {
				String sANum = salesArray.get(arrayIndex).substring(0, 8);
				int sANumNew = Integer.parseInt(sANum);
				System.out.println(sANum);// ■?

				String sA2Num = salesArray.get(arrayIndex + 1).substring(0, 8);
				int sA2NumNew = Integer.parseInt(sA2Num);
				System.out.println(sA2Num);// ■?

				if (sA2NumNew - sANumNew != 1) {
					System.out.println(salesFile + "が連番になっていません");
					return;
				}
			}
// ***************************▲◎(処理内容:連番かどうかOK)***********************************************
			try {
				for (int arrayIndex = 0; arrayIndex < salesArray.size(); arrayIndex++) {
					// ここで、salesArrayをファイルを順番に読み込んでいく
					File sales = new File(args[0] + "\\" + salesArray.get(arrayIndex)); // 売上ファイル
					FileReader fr = new FileReader(sales);
					BufferedReader br = new BufferedReader(fr);
					String salesLine;
					ArrayList<String> saleFileList = new ArrayList<String>();
					while ((salesLine = br.readLine()) != null) {
						saleFileList.add(salesLine);
					}
					br.close();
					if (saleFileList.size() != 3) { // ●3行かどうか。
						System.out.println(salesArray.get(arrayIndex) + "のフォーマットが不正です");
					}
					if (!branchMap.containsKey(saleFileList.get(0))) { // ●1行目が支店コードかどうか。
						System.out.println(salesArray.get(arrayIndex) + "の支店コードが不正です");
					}

					if (!commodityMap.containsKey(saleFileList.get(1))) { // ●2行目が商品コードかどうか。
						// ●●以下、読み込んだものが、商品コードかどうかのくだり
						System.out.println(salesArray.get(arrayIndex) + "の商品コードが不正です");
					}

					if (!(saleFileList.get(2).matches("^\\d{1,10}$"))) { // ●3行目が商品コードかどうか。
						System.out.println(salesArray.get(arrayIndex) + "の売上額が10桁を超えています");
						return;
					}
					long Sales_Long = Long.parseLong(saleFileList.get(2)); // ●足し算するためにstrをlong（数値）に変換




					if(!(branchSalesMap.containsKey(saleFileList.get(0)))){ //売上集計Mapに読み込んだ１行目が含む or not
						branchSalesMap.put(saleFileList.get(0), Sales_Long);
					} else {
						long sumBranchSales = branchSalesMap.get(saleFileList.get(0)) + Sales_Long; //●支店別売上合計値をだして、マップに戻す。
						String sumCount = String.valueOf(sumBranchSales);
						if(!sumCount.matches("^\\d{1,10}$")){
						System.out.println(sumCount);
						System.out.println("合計金額が10桁を超えました");
						return;
						}
						branchSalesMap.put(saleFileList.get(0), sumBranchSales);//■
					}
					if(!(commoditySalesMap.containsKey(saleFileList.get(1)))){ //売上集計Mapに読み込んだ１行目が含む or not
						commoditySalesMap.put(saleFileList.get(1), Sales_Long);
					} else {
						long sumCommoditySales = commoditySalesMap.get(saleFileList.get(1)) + Sales_Long; //●支店別売上合計値をだして、マップに戻す。
						String sumCount2 = String.valueOf(sumCommoditySales);
						if(!sumCount2.matches("^\\d{1,10}$")){
						System.out.println(sumCount2);
						System.out.println("合計金額が10桁を超えました");
						return;
						}
						commoditySalesMap.put(saleFileList.get(1), sumCommoditySales);
					}
				}
				System.out.println(branchSalesMap.entrySet()); // ■
				System.out.println(commoditySalesMap.entrySet()); // ■
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} finally {
			}
// ***************************▲◎(処理内容:書き込む前までOK)*****************************************

			 List<Map.Entry<String,Long>> branchSalesEntry =
		              new ArrayList<Map.Entry<String,Long>>( branchSalesMap.entrySet());
		        Collections.sort(branchSalesEntry, new Comparator<Map.Entry<String,Long>>() {

					@Override
					public int compare(java.util.Map.Entry<String, Long> entry1, java.util.Map.Entry<String, Long> entry2) {
						// TODO 自動生成されたメソッド・スタブ
						return entry2.getValue().compareTo(entry1.getValue()); //降順
				}
			});

			try{
				 File branchOutFile = new File(args[0],"branch.out");
				FileWriter fwBranch = new FileWriter(branchOutFile);
				BufferedWriter bwBranch = new BufferedWriter(fwBranch);

				for (Entry<String, Long> forBranchOut : branchSalesEntry) {



					bwBranch.write(forBranchOut.getKey() + "," + branchMap.get(forBranchOut.getKey()) + ","
						 + forBranchOut.getValue());
					bwBranch.newLine();

				}
				bwBranch.close();

			}catch (IOException e) {
			System.out.println(e);
			}


			 List<Map.Entry<String,Long>> commoditySalesEntry =
		              new ArrayList<Map.Entry<String,Long>>( commoditySalesMap.entrySet());
		        Collections.sort(commoditySalesEntry, new Comparator<Map.Entry<String,Long>>() {

					@Override
					public int compare(java.util.Map.Entry<String, Long> entry1, java.util.Map.Entry<String, Long> entry2) {
						// TODO 自動生成されたメソッド・スタブ
						return entry2.getValue().compareTo(entry1.getValue()); //降順
				}
			});

			try{
				File commodityOutFile = new File(args[0],"commodity.out");
				FileWriter fwCommodity = new FileWriter(commodityOutFile);
				BufferedWriter bwCommodity = new BufferedWriter(fwCommodity);

				for (Entry<String, Long> forCommodityOut : commoditySalesEntry) {


					bwCommodity.write(forCommodityOut.getKey() + "," + commodityMap.get(forCommodityOut.getKey()) + ","
						 + forCommodityOut.getValue());
					bwCommodity.newLine();

				}
				bwCommodity.close();

			}catch (IOException e) {
			System.out.println(e);
			}


		}
	}
}
