package com.nazdaq.otms2.beans;

public class OvertimeReports {

	private Integer id;
	private String empName;
	private String empId;
	private String departmentName;
	private String designationName;
	private String dateOfJoin;
	private String bankAccNo;
	private String remarks;

	private Double basicSalary = 0.0;
	private Double handCash = 0.0;

	private Double totalOtDays = 0.0;
	private Double totalPayment = 0.0;
	private Double otUnit = 0.0;

	private Integer deptId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(String dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getHandCash() {
		return handCash;
	}

	public void setHandCash(Double handCash) {
		this.handCash = handCash;
	}

	public Double getTotalOtDays() {
		return totalOtDays;
	}

	public void setTotalOtDays(Double totalOtDays) {
		this.totalOtDays = totalOtDays;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Double getOtUnit() {
		return otUnit;
	}

	public void setOtUnit(Double otUnit) {
		this.otUnit = otUnit;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public OvertimeReports() {

	}

	public OvertimeReports(String empName, String empId, String departmentName, String designationName,
			String dateOfJoin, String bankAccNo, String remarks, Double basicSalary, Double handCash,
			Double totalOtDays, Double totalPayment, Double otUnit) {
		super();
		this.empName = empName;
		this.empId = empId;
		this.departmentName = departmentName;
		this.designationName = designationName;
		this.dateOfJoin = dateOfJoin;
		this.bankAccNo = bankAccNo;
		this.remarks = remarks;
		this.basicSalary = basicSalary;
		this.handCash = handCash;
		this.totalOtDays = totalOtDays;
		this.totalPayment = totalPayment;
		this.otUnit = otUnit;
	}

}
