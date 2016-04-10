package ui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import blimpl.StockBLImpl;
import blservice.StockBLService;
import enumeration.MyDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import ui.controller.candleStick.CandleStickChart;
import util.MyTime;
import vo.OHLC_VO;

/**
 *
 * @author ss
 * @date 2016年3月24日 =。=
 */
public class CandleStickController implements Initializable {




	public CandleStickChart dayChart, weekChart, monthChart;
	public ObservableList<OHLC_VO> dayList;
	public ObservableList<OHLC_VO> weekList;
	public ObservableList<OHLC_VO> monthList;
	private String stockCode;
	private MyDate startDate, endDate;




	private static StockBLService stockBl;
	private static CandleStickController instance;

	public CandleStickController() {
		if (instance == null) {
			stockCode = "sh600000";
			startDate = MyTime.getToDay();
			endDate = startDate;
			stockBl = StockBLImpl.getAPIBLService();
			dayList = FXCollections.observableArrayList();
			weekList = FXCollections.observableArrayList();
			monthList = FXCollections.observableArrayList();
			dayList.clear();
			weekList.clear();
			monthList.clear();
			instance = this;
		}
	}

	public static CandleStickController getCandleStickController() {
		if (instance != null) {
			return instance;
		} else {
			return new CandleStickController();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}


	public Node getInitialDayChart(String code) {

		this.stockCode = code;
		getDayData();
        dayChart = CandleStickChart.createChart(dayList);
		return dayChart;
	}

	public Node getInitialWeekChart(String code){
	    this.stockCode=code;
	   getWeekData();
	    weekChart= CandleStickChart.createChart(weekList);
	    return weekChart;
	}

	public Node getInitialMonthChart(String code){
       this.stockCode=code;
      getMonthData();
       monthChart=CandleStickChart.createChart(monthList);
       return monthChart;
	}

	// set date to update charts
	public Node getUpdatedDayChart(String code, MyDate start, MyDate end) {
		this.stockCode = code;
		this.startDate = start;
		this.endDate = end;
        getDayDataByDate();
      dayChart = CandleStickChart.createChart(dayList);

		return dayChart;

	}


	public Node getUpdatedWeekChart(String code, MyDate start, MyDate end) {
		this.stockCode = code;
		this.startDate = start;
		this.endDate = end;
		getWeekDataByDate();
        weekChart=CandleStickChart.createChart(weekList);
		return weekChart;

	}


	public Node getUpdatedMonthChart(String code, MyDate start, MyDate end) {
		this.stockCode = code;
		this.startDate = start;
		this.endDate = end;
	//	Task updateTask = createUpdateMonthChartWorker();
		// showProgressIndicator(
		// updateTask.progressProperty(),updateTask.runningProperty());
	//	new Thread(updateTask).start();
		getMonthDataByDate();
		monthChart=CandleStickChart.createChart(monthList);
		return dayChart;

	}


	public void getDayData() {
		// 默认显示近一个月的数据
		MyDate end = MyTime.getToDay();
		MyDate start = MyTime.getAnotherDay(-30);
		List<OHLC_VO> list = stockBl.getDayOHLC_Data(stockCode, start, end);
		dayList.clear();
		for (OHLC_VO temp : list) {
			dayList.add(temp);
		}
	}
    public  void  getDayDataByDate(){
		if (startDate != null && endDate != null) {
			List<OHLC_VO> list = stockBl.getDayOHLC_Data(stockCode, startDate, endDate);
			dayList.clear();
			for (OHLC_VO temp : list) {
				dayList.add(temp);
			}
		}
	}

	public void getWeekData() {
		// 默认显示最近一年的数据
		MyDate end = MyTime.getToDay();
		MyDate start = MyTime.getAnotherDay(-180);
		List<OHLC_VO> list = stockBl.getWeekOHLC_Data(stockCode, start, end);
		weekList.clear();
		for (OHLC_VO temp : list) {
			weekList.add(temp);
		}
	}

	public void getWeekDataByDate() {
		if (startDate != null && endDate != null) {
			List<OHLC_VO> list = stockBl.getWeekOHLC_Data(stockCode, startDate, endDate);
			weekList.clear();
			for (OHLC_VO temp : list) {
				weekList.add(temp);
			}
		}
	}

	public void getMonthData() {
		// 默认显示最近三年的数据
		MyDate end = MyTime.getToDay();
		MyDate start = MyTime.getAnotherDay(-365 * 2);
		List<OHLC_VO> list = stockBl.getMonthOHLC_Data(stockCode, start, end);
		monthList.clear();
		for (OHLC_VO temp : list) {
			monthList.add(temp);
		}
	}

	public void getMonthDataByDate() {
		if (startDate != null && endDate != null) {
			List<OHLC_VO> list = stockBl.getMonthOHLC_Data(stockCode, startDate, endDate);
			monthList.clear();
			for (OHLC_VO temp : list) {
				monthList.add(temp);
			}
		}
	}

	public void SetStockCode(String code){
		this.stockCode=code;
	}

	public void SetStartDate(MyDate start){
		startDate=start;
	}

	public void SetEndDate(MyDate end){
		endDate=end;
	}


}