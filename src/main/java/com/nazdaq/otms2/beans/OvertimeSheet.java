package com.nazdaq.otms2.beans;

public class OvertimeSheet {

	private Integer id;

	private Integer recid = null;

	private Double handCash = null;

	private Double totalOtHours = null;

	private String remarks = null;

	private String overallRemarks = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecid() {
		return recid;
	}

	public void setRecid(Integer recid) {
		this.recid = recid;
	}

	public Double getHandCash() {
		return handCash;
	}

	public void setHandCash(Double handCash) {
		this.handCash = handCash;
	}

	public Double getTotalOtHours() {
		return totalOtHours;
	}

	public void setTotalOtHours(Double totalOtHours) {
		this.totalOtHours = totalOtHours;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOverallRemarks() {
		return overallRemarks;
	}

	public void setOverallRemarks(String overallRemarks) {
		this.overallRemarks = overallRemarks;
	}

}
