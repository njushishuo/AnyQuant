package service;

import util.MyDate;
import util.enumration.FactorJudge;
import vo.FactorJudgmentVO;
import vo.FactorWeightVO;
import vo.ReportVO;

import java.util.List;
import java.util.Map;

/**
 * 策略界面Service
 * @author Qiang
 * @date 6/5/16
 */
public interface StrategyService {

    /**
     * Get the factorJudgement of the giving stocks
     *
     * @param codes the stock universal
     * @return some of the most useful factors , which gives customer suggestions on strategy to analyse
     */
    List<FactorJudgmentVO> getStocksFactorJudgment(List<String> codes);

    /**
     * FactorAnalysis
     * 因子分析
     * @param codes 股票池
     * @param start 起始日期
     * @param end   结束日期
     * @param factorWeight 因子及其对应的权重
     * @return analysis report
     */
    ReportVO analyseWithFactor(List<String> codes, MyDate start , MyDate end , Map<String , Double> factorWeight);



}