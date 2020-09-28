package com.nazdaq.otms2.beans;

import java.util.List;

public class OvertimeDtlBean {

	private List<OvertimeSheet> overtimeSheetList;

	private String overallRemarks;

	private String otYear;

	private String otMonth;

	public List<OvertimeSheet> getOvertimeSheetList() {
		return overtimeSheetList;
	}

	public void setOvertimeSheetList(List<OvertimeSheet> overtimeSheetList) {
		this.overtimeSheetList = overtimeSheetList;
	}

	public String getOverallRemarks() {
		return overallRemarks;
	}

	public void setOverallRemarks(String overallRemarks) {
		this.overallRemarks = overallRemarks;
	}

	public String getOtYear() {
		return otYear;
	}

	public void setOtYear(String otYear) {
		this.otYear = otYear;
	}

	public String getOtMonth() {
		return otMonth;
	}

	public void setOtMonth(String otMonth) {
		this.otMonth = otMonth;
	}

}
