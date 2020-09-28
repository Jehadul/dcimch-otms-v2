package com.nazdaq.otms2.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "overtime_dtl")
public class OvertimeDtl implements Serializable {
	private static final long serialVersionUID = -784499677138434592L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Transient
	private Integer employeeId;

	@Column(name = "ot_year")
	private String otYear;

	@Column(name = "ot_month")
	private String otMonth;

	@Column(name = "gross_salary")
	private Double grossSalary = 0.0;

	@Column(name = "basic_salary")
	private Double basicSalary = 0.0;

	@Column(name = "hand_cash")
	private Double handCash = 0.0;

	@Column(name = "overtime_unit")
	private Double otUnit = 0.0;

	@Column(name = "total_ot_hours")
	private Double totalOtHours = 0.0;

	@Column(name = "total_ot_days")
	private Double totalOtDays = 0.0;

	@Transient
	private String otTotalDays = "0.0";

	@Column(name = "total_payment")
	private Double totalPayment = 0.0;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	private Date createdDate;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	private Date modifiedDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "is_final_submit")
	private Integer finalSubmit = 0;

	@Transient
	private Integer recid;

	@Transient
	private Integer slNo;

	@Transient
	private String joinDate;

	@Column(name = "company_id")
	private Integer companyId;

	public OvertimeDtl() {

	}

	public OvertimeDtl(Integer id, Employee employee, String otYear, String otMonth, Double grossSalary,
			Double basicSalary, Double otUnit, String createdBy, Date createdDate, String remarks) {
		super();
		this.id = id;
		this.employee = employee;
		this.otYear = otYear;
		this.otMonth = otMonth;
		this.grossSalary = grossSalary;
		this.basicSalary = basicSalary;
		this.otUnit = otUnit;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.remarks = remarks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
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

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
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

	public Double getOtUnit() {
		return otUnit;
	}

	public void setOtUnit(Double otUnit) {
		this.otUnit = otUnit;
	}

	public Double getTotalOtHours() {
		return totalOtHours;
	}

	public void setTotalOtHours(Double totalOtHours) {
		this.totalOtHours = totalOtHours;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRecid() {
		return recid;
	}

	public void setRecid(Integer recid) {
		this.recid = recid;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public Double getTotalOtDays() {
		return totalOtDays;
	}

	public void setTotalOtDays(Double totalOtDays) {
		this.totalOtDays = totalOtDays;
	}

	public Integer getFinalSubmit() {
		return finalSubmit;
	}

	public void setFinalSubmit(Integer finalSubmit) {
		this.finalSubmit = finalSubmit;
	}

	public String getOtTotalDays() {
		return otTotalDays;
	}

	public void setOtTotalDays(String otTotalDays) {
		this.otTotalDays = otTotalDays;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
