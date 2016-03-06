package blimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import po.BenchMarkPO;
import po.StockPO;
import util.MyTime;
import vo.BenchMarkVO;
import vo.StockVO;
import blservice.APIBlservice;
import dataservice.APIInterface;
import dataservice.APIInterfaceImpl;
import enumeration.MyDate;
import enumeration.Stock_Attribute;

/**
 * API逻辑层实现
 * @author czq
 * @date 2016年3月4日
 */
public class APIBlImpl implements APIBlservice {
	/**
	 * 单例模式
	 */
	private static APIBlservice APIBlservice;
	private APIInterface APIDataSer; 
	/**
	 * 两份股票的数据，Collection版便于排序，Map版便于查找
	 */
	private List<StockVO> stocks;
	private Map<String , StockVO> stockMap;
	/**
	 * 大盘数据
	 */
	private List<BenchMarkVO> benchMarkVOs;
	
	/*
	 * 为了加快测试速度，在开发阶段只引入100只股票
	 */
	private APIBlImpl() {
		APIDataSer = new APIInterfaceImpl();
		List<String> stocksCode = APIDataSer.getAllStocks();
		stockMap = new TreeMap<String, StockVO>();
		List<String> benchCodes = APIDataSer.getAllBenchMatks();
		
		
		System.out.println("Reading Data-----------");
		
		/*
		 * 使用TreeMap支持自动排序
		 */
		
		System.out.println("股票数量："+stocksCode.size());
		int count = 0;
		stocks = new ArrayList<StockVO>(stocksCode.size());
		for (String string : stocksCode) {
			stockMap.put(string, (StockVO) VOPOchange.POtoVO(APIDataSer.getStockMes(string)));

			if(count > 100){
				break;
			}
			count ++;
		}
		stocks = new ArrayList<StockVO>(stockMap.values());
		
		for (String string : benchCodes) {
			benchMarkVOs.add( (BenchMarkVO) VOPOchange.POtoVO(APIDataSer.getBenchMes(string)));
		}
		
		
		
		
		
		System.out.println("Reading Finish-----------");
		
		
	}
	
	public static APIBlservice getAPIBLService(){
		if(APIBlservice == null){
			APIBlservice = new APIBlImpl();
		}
		return APIBlservice;
	}
	
	
	@Override
	public Iterator<StockVO> getAllStocks() {
		
		return stocks.iterator();
	}

	@Override
	public Iterator<StockVO> getSortStocks(boolean isUp, Stock_Attribute attr) {
		return StockSortHelper.sortStocks(stocks, attr , isUp);
	}

	@Override
	public Iterator<StockVO> getSortStocksInScope(boolean isUp,
			Stock_Attribute attr, List<String> stocksCode) {
		List<StockVO> stocks = new ArrayList<StockVO>(stocksCode.size());
		for (String string : stocksCode) {
			stocks.add(stockMap.get(string));
		}
		return StockSortHelper.sortStocks(stocks, attr, isUp);
	}

	

	@Override
	public Iterator<StockVO> getRecentStocks(String stockCode) {
		return getStocksByTime(stockCode, MyTime.getAnotherDay(-30) , MyTime.getToDay());
	}

	@Override
	public Iterator<StockVO> getStocksByTime(String stockCode , MyDate start, MyDate end) {
		List<StockPO> pos = APIDataSer.getStockMes(stockCode, start, end);
		if(pos != null){
			List<StockVO> result = new ArrayList<StockVO>(pos.size());
			for (StockPO stockPO : pos) {
				result.add((StockVO) VOPOchange.POtoVO(stockPO));
			}
			return result.iterator();
		}else{
			return null;
		}
	}

	@Override
	public Iterator<StockVO> getStocksByStockCode(String code) {
		List<StockVO> result = new ArrayList<StockVO>();
		
		for (Iterator<String> iterator = stockMap.keySet().iterator(); iterator.hasNext();) {
			String temp = iterator.next();
			if(temp.contains(code)){
				result.add(stockMap.get(temp));
			}
			if(result.size() > 10){
				break;
			}
		}
		
		return result.iterator();
	}
	
	@Override
	public Iterator<BenchMarkVO> getAllBenchMarks() {
		
		return benchMarkVOs.iterator();
	}
	
	
	@Override
	public Iterator<BenchMarkVO> getRecentBenchMarks(String BenchMarkCode) {
		return getBenchMarkByTime(BenchMarkCode, MyTime.getAnotherDay(-30), MyTime.getToDay());
	}

	@Override
	public Iterator<BenchMarkVO> getBenchMarkByTime(String BenchMarkCode,
			MyDate start, MyDate end) {
		List<BenchMarkPO> pos = APIDataSer.getBenchMes(BenchMarkCode, start, end);
		if(pos != null){
			List<BenchMarkVO> result = new ArrayList<BenchMarkVO>(pos.size());
			for (BenchMarkVO benchMarkVO : result) {
				result.add((BenchMarkVO) VOPOchange.POtoVO(benchMarkVO));
			}
			return result.iterator();
		}else{
			return null;
		}
	}

	
	
	
	
	
	

}
