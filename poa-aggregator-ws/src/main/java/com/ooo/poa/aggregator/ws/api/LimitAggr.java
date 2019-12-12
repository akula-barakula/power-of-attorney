package com.ooo.poa.aggregator.ws.api;

public class LimitAggr {

	private Integer limit;
	private PeriodUnitAggr periodUnit;


	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setPeriodUnit(PeriodUnitAggr periodUnit) {
		this.periodUnit = periodUnit;
	}

	public PeriodUnitAggr getPeriodUnit() {
		return periodUnit;
	}
}
