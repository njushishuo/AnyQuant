package ui.controller;


import java.util.List;

import blimpl.OptionalStockBLImpl;
import blservice.OptionalStockBLService;
import enumeration.MyDate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ui.controller.candleStick.CandleStickController;
import ui.controller.candleStick.TimeSharingChart;
import vo.Stock;

public class StockDetailController {

	@FXML
	Label nameLB;  Label codeLB;
	@FXML
	Label openLB;  Label closeLB;  Label lowLB ;Label highLB;
	@FXML
	Label turnoverRateLB; Label turnoverVolLB;
	@FXML
	Label peLB;Label pbLB;
	@FXML
	Button addBtn;
	@FXML
	Tab k_day;  Tab k_week; 	Tab k_month;
	@FXML
	Tab timeSharing;
	@FXML
	DatePicker  dayStart,dayEnd,weekStart,weekEnd,monthStart,monthEnd;
	@FXML
	Button dayBT,weekBT,monthBT;
	@FXML
	TabPane tabPane;
	private Boolean exist;
	private String stockCode;

	private Stock currentStock;
	private OptionalStockBLService optionBl = OptionalStockBLImpl.getOptionalBLService();
	private CandleStickController candleStickController = CandleStickController.getCandleStickController();
	private static StockDetailController instance;

	public StockDetailController() {
		// System.err.println("init constructor======");
		if (instance == null) {
			// System.out.println("【null instance】");
			instance = this;
		}
	}

	public static StockDetailController getStockDetailController() {
		// System.err.println("get instance");
		if (instance == null) {
			// System.err.println("create");
			instance = new StockDetailController();
		}
		return instance;
	}

	@FXML
	private void initialize() {

	}

	public static StockDetailController getCurrent() {
		return instance;
	}

	public void setData(Stock stock) {
		// if(nameLabel==null){
		// System.out.println("name null in set method");
		// return;
		// }else{
		// System.out.println("not null in set method");
		// }
		System.out.println("[get in and current stock :" + stock.code + "]");
		currentStock = stock;
		System.out.println("changed!!");
		stockCode = stock.code.get();
		nameLB.setText(stock.name.get());
		codeLB.setText(stockCode);
		openLB.setText(String.valueOf(stock.open.get()));
		closeLB.setText(String.valueOf(stock.close.get()));
		highLB.setText(String.valueOf(stock.high.get()));
		lowLB.setText(String.valueOf(stock.low.get()));
		turnoverRateLB.setText(String.valueOf(stock.turnoverRate.get()));
		peLB.setText(String.valueOf(stock.pe.get()));
		pbLB.setText(String.valueOf(stock.pb.get()));
		turnoverVolLB.setText(String.valueOf(stock.turnoverVol.get()));
		if (optionBl.ifStockExist(stockCode)) {// 存在于自选股
			addBtn.setText("删除该自选股");
			exist = true;
		} else {
			addBtn.setText("加入自选股");
			exist = false;
		}
		// add time sharing then
		initTimeSharing();
		// add k_line
		initKLine();
	}

	private void initTimeSharing() {
		TimeSharingChart timeChart = new TimeSharingChart(currentStock);
		timeSharing.setContent(timeChart.getTimeSharingChart());
		// timeSharing.getTabPane().
	}

	private void initKLine() {
		List<Node> nodes = candleStickController.getInitialCharts(currentStock.code.get());
		if (nodes.size() == 3) {
			k_day.setContent(nodes.get(0));
			k_week.setContent(nodes.get(1));
			k_month.setContent(nodes.get(2));
		}
	}

	private  void updateDayChart(){
		MyDate start = new MyDate(dayStart.getValue().getYear(),dayStart.getValue().getMonthValue(),dayStart.getValue().getDayOfMonth());
		MyDate end = new MyDate(dayEnd.getValue().getYear(),dayEnd.getValue().getMonthValue(),dayEnd.getValue().getDayOfMonth());
        Node dayChart = candleStickController.getUpdatedDayChart(currentStock.code.get(), start, end);
        k_day.setContent(dayChart);
	}

	private  void updateWeekChart(){
		MyDate start = new MyDate(weekStart.getValue().getYear(),weekStart.getValue().getMonthValue(),weekStart.getValue().getDayOfMonth());
		MyDate end = new MyDate(weekEnd.getValue().getYear(),weekEnd.getValue().getMonthValue(),weekEnd.getValue().getDayOfMonth());
        Node weekChart = candleStickController.getUpdatedWeekChart(currentStock.code.get(), start, end);
        k_week.setContent(weekChart);
	}

	private  void updateMonthChart(){
		MyDate start = new MyDate(monthStart.getValue().getYear(),monthStart.getValue().getMonthValue(),monthStart.getValue().getDayOfMonth());
		MyDate end = new MyDate(monthEnd.getValue().getYear(),monthEnd.getValue().getMonthValue(),monthEnd.getValue().getDayOfMonth());
        Node monthChart = candleStickController.getUpdatedMonthChart(currentStock.code.get(), start, end);
        k_month.setContent(monthChart);
	}

	@FXML
 	private void addOptionalStock() {
		if (currentStock == null)
			System.err.println("current null");
		if (exist) {// 执行删除操作
			if (optionBl.deleteStockCode(stockCode)) {// 删除成功
				addBtn.setText("加入自选股");
				exist = false;
			} else {
				addBtn.setText("删除失败");// TODO 失败原因？。。
			}
		} else {// 执行增加操作
			System.out.println("add begin");
			if (optionBl.addStockCode(stockCode)) {// 添加成功
				addBtn.setText("删除该自选股");
				exist = true;
			} else {
				addBtn.setText("加入失败");
			}
		}

	}
}
