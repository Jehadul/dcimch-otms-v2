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
@Table(name = "overtime_summary")
public class OvertimeSummary implements Serializable {
	private static final long serialVersionUID = -205537767781224828L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ot_year")
	private String otYear;

	@Column(name = "ot_month")
	private String otMonth;

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = true)
	private Company company;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@Transient
	private Integer departmentId;

	@Transient
	private Integer companyId;

	@Transient
	private Double totalOfThisMonth = 0.0;

	@Transient
	private Double totalOfPervOneMonth = 0.0;

	@Transient
	private Double totalOfPervTwoMonth = 0.0;

	@Transient
	private Double totalOfPervThreeMonth = 0.0;

	@Transient
	private Double differLastTwoMonth = 0.0;

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

	@Column(name = "ovarall_remarks")
	private String overallRemarks = "";

	@Column(name = "is_final_submit")
	private Integer finalSubmit = 0;

	@Transient
	private Integer recid;

	@Transient
	private Integer slNo;

	private String departmentName;

	public OvertimeSummary() {

	}

	public OvertimeSummary(Integer id, String otYear, String otMonth, Department department, String createdBy,
			Date createdDate, String remarks, String overallRemarks) {
		super();
		this.id = id;
		this.otYear = otYear;
		this.otMonth = otMonth;
		this.department = department;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.remarks = remarks;
		this.overallRemarks = overallRemarks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Double getTotalOfThisMonth() {
		return totalOfThisMonth;
	}

	public void setTotalOfThisMonth(Double totalOfThisMonth) {
		this.totalOfThisMonth = totalOfThisMonth;
	}

	public Double getTotalOfPervOneMonth() {
		return totalOfPervOneMonth;
	}

	public void setTotalOfPervOneMonth(Double totalOfPervOneMonth) {
		this.totalOfPervOneMonth = totalOfPervOneMonth;
	}

	public Double getTotalOfPervTwoMonth() {
		return totalOfPervTwoMonth;
	}

	public void setTotalOfPervTwoMonth(Double totalOfPervTwoMonth) {
		this.totalOfPervTwoMonth = totalOfPervTwoMonth;
	}

	public Double getTotalOfPervThreeMonth() {
		return totalOfPervThreeMonth;
	}

	public void setTotalOfPervThreeMonth(Double totalOfPervThreeMonth) {
		this.totalOfPervThreeMonth = totalOfPervThreeMonth;
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

	public String getOverallRemarks() {
		return overallRemarks;
	}

	public void setOverallRemarks(String overallRemarks) {
		this.overallRemarks = overallRemarks;
	}

	public Integer getFinalSubmit() {
		return finalSubmit;
	}

	public void setFinalSubmit(Integer finalSubmit) {
		this.finalSubmit = finalSubmit;
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

	public Double getDifferLastTwoMonth() {
		return differLastTwoMonth;
	}

	public void setDifferLastTwoMonth(Double differLastTwoMonth) {
		this.differLastTwoMonth = differLastTwoMonth;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
