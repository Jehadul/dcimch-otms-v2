package com.nazdaq.otms2.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.otms2.beans.OvertimeReports;
import com.nazdaq.otms2.model.Company;
import com.nazdaq.otms2.model.Department;
import com.nazdaq.otms2.model.OvertimeDtl;
import com.nazdaq.otms2.model.OvertimeSummary;
import com.nazdaq.otms2.service.CommonService;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class ReportController {

	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/otDtlReportForm", method = RequestMethod.GET)
	public ModelAndView getOvertimeDtlReportForm (Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("otDtlReportForm", model);
	}
	
	@RequestMapping(value = "/otSummaryReportForm", method = RequestMethod.GET)
	public ModelAndView getOvertimeSummaryReportForm (Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("otSummaryReportForm", model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/generateOtDtlReport" }, method = RequestMethod.GET)
	@ResponseBody
	public void generateOtDtlReport(HttpServletResponse response, HttpServletRequest request, HttpSession session)
			throws JRException, IOException, ParseException {
		JRDataSource jRdataSource = null;
		
		Company company = (Company) session.getAttribute("company");
		
		String otYear = request.getParameter("otYear");
		String otMonth = request.getParameter("otMonth");
		String reportFormat = request.getParameter("reportFormat");
		
		List<OvertimeReports> otDetailsList = new ArrayList<OvertimeReports>();
		List<OvertimeReports> otDetailsList1 = new ArrayList<OvertimeReports>();
		
		
		List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "employee.company.id", company.getId().toString());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		OvertimeReports otReport = null;
		for (OvertimeDtl ot : otDtlList) {
			otReport = new OvertimeReports(ot.getEmployee().getName(), ot.getEmployee().getEmpId(), ot.getEmployee().getDepartment().getName(), 
					ot.getEmployee().getDesignation().getName(), 
					df.format(ot.getEmployee().getJoinDate()), ot.getEmployee().getBankAccNo(), ot.getRemarks(), ot.getBasicSalary(), 
					ot.getHandCash(), this.round2(ot.getTotalOtDays(), 3), ot.getTotalPayment(), ot.getOtUnit());
			
			//new add
			//Double totalPayment = this.round(otReport.getTotalPayment(), 3);
			Double totalPayment = this.round2(otReport.getTotalPayment(), 0);
			otReport.setTotalPayment(totalPayment);
			otReport.setDeptId(ot.getEmployee().getDepartment().getId());
			
			otDetailsList.add(otReport);
		}		
		
		for (OvertimeReports overtimeReports : otDetailsList) {
			if(overtimeReports.getTotalPayment().intValue() > 0) {
				otDetailsList1.add(overtimeReports);
			}
		}
			
		otDetailsList1
		  .stream()
		  .sorted((object1, object2) -> object1.getDeptId().compareTo(object2.getDeptId()));
		
		/*
		List<OvertimeReports> otDetailsList4 = new ArrayList<OvertimeReports>();
		for (OvertimeReports overtimeReports : otDetailsList1) {
			if(overtimeReports.getDeptId().toString().equals("17")) {
				otDetailsList4.add(overtimeReports);				
			} else {
				otDetailsList3.add(overtimeReports);
			}
		}		
		otDetailsList3.addAll(otDetailsList4);*/
		List<OvertimeReports> otDetailsList4 = new ArrayList<OvertimeReports>();
		List<OvertimeReports> otDetailsList3 = null;
		List<Department> deptList = (List<Department>)(Object)
				commonService.getAllObjectList("Department");
		
		Map<Integer, Object> orMap = new HashMap<Integer, Object>();
		
		for (OvertimeReports overtimeReports : otDetailsList1) {
			for (Department d : deptList) {
				if(overtimeReports.getDeptId().toString().equals(d.getId().toString())) {
					if(orMap.containsKey(d.getId())) {
						otDetailsList3 = (List<OvertimeReports>) orMap.get(d.getId());
						otDetailsList3.add(overtimeReports);
						orMap.put(d.getId(), otDetailsList3);
						break;
					} else {
						otDetailsList3 = new ArrayList<OvertimeReports>();		
						otDetailsList3.add(overtimeReports);						
						orMap.put(d.getId(), otDetailsList3);
						break;
					}
				}
				
			}
		}
		
		for (Map.Entry<Integer, Object> entry : orMap.entrySet())
		{
			otDetailsList4.addAll((Collection<? extends OvertimeReports>) entry.getValue());
			// System.out.println(entry.getKey() + "/" + entry.getValue());
		}
		
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/overtime_details_report.jasper");
		Map<String, Object> params = new HashMap<>();
		//jRdataSource = new JRBeanCollectionDataSource(otDetailsList, false);
		jRdataSource = new JRBeanCollectionDataSource(otDetailsList4, false);
		params.put("datasource", jRdataSource);
		params.put("OT_YEAR", otYear);
		params.put("OT_MONTH", otMonth);
		params.put("UNIT_NAME", company.getName());
			
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRdataSource);
		
		if(reportFormat.equals("xlx")){	
			response.addHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		    response.addHeader("Content-disposition", "attachment; filename=overtime_details_report.xlsx");
		    ServletOutputStream outputStream = response.getOutputStream();
		    net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
		    exporter.exportReport();
		    outputStream.flush();
		    outputStream.close();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		} else {
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=overtime_details_report.pdf");
			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/generateOtSummaryReport" }, method = RequestMethod.GET)
	@ResponseBody
	public void generateOtSummaryReport(HttpServletResponse response, HttpServletRequest request, HttpSession session)
			throws JRException, IOException, ParseException {
		JRDataSource jRdataSource = null;
		Company company = (Company) session.getAttribute("company");
		String otYear = request.getParameter("otYear");
		String otMonth = request.getParameter("otMonth");
		String reportFormat = request.getParameter("reportFormat");
		
		List<OvertimeSummary> otSummaryList1 = new ArrayList<OvertimeSummary>();
		
		List<OvertimeSummary> otSummaryList2 = new ArrayList<OvertimeSummary>();
		
		
		Integer selectedMonthKey = this.getMonthKey(otMonth);
		String month = ""; 
		if(selectedMonthKey.toString().length() < 2) {
			 month = "0" + selectedMonthKey;
		} else {
			month = selectedMonthKey.toString();
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date selectedDate = df.parse(otYear + "-" + month + "-01");
		
		Calendar cal1 =  Calendar.getInstance();
		cal1.setTime(selectedDate);
		cal1.add(Calendar.MONTH, -1);
		Date datePrevOne = cal1.getTime();
		Integer m1 = datePrevOne.getMonth()+1;
		Integer year1 = cal1.get(Calendar.YEAR);
		
		Calendar cal2 =  Calendar.getInstance();
		cal2.setTime(datePrevOne);
		cal2.add(Calendar.MONTH, -1);
		Date datePrevTwo = cal2.getTime();
		Integer m2 = datePrevTwo.getMonth()+1;
		Integer year2 = cal2.get(Calendar.YEAR);
		
		Calendar cal3 =  Calendar.getInstance();
		cal3.setTime(datePrevTwo);
		cal3.add(Calendar.MONTH, -1);
		Date datePrevThree = cal3.getTime();
		Integer m3 = datePrevThree.getMonth()+1;
		Integer year3 = cal3.get(Calendar.YEAR);
		
		String prevOneMonth = this.getMonthName(m1);
		String prevTwoMonth =  this.getMonthName(m2);
		String prevThreeMonth =  this.getMonthName(m3);
		
		
		List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeSummary", "otYear", otYear, "otMonth", otMonth, "company.id", company.getId().toString());
		
		
		List<OvertimeDtl> otDtlailsList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "employee.company.id", company.getId().toString());
				
		List<OvertimeDtl> otDtlailsList1 = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", year1.toString(), "otMonth", prevOneMonth, "employee.company.id", company.getId().toString());
		
		List<OvertimeDtl> otDtlailsList2 = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", year2.toString(), "otMonth", prevTwoMonth, "employee.company.id", company.getId().toString());
		
		List<OvertimeDtl> otDtlailsList3 = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", year3.toString(), "otMonth", prevThreeMonth, "employee.company.id", company.getId().toString());
		
		int i = 1;
		for (OvertimeSummary overtimeSummary : otSummaryList) {
			List<OvertimeDtl> otDtlList = new ArrayList<OvertimeDtl>();			
			for (OvertimeDtl overtimeDtl : otDtlailsList) {
				if(overtimeSummary.getDepartment().getId().toString().equals(
						overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList.add(overtimeDtl);
				}
			}
			
			List<OvertimeDtl> otDtlList1 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList1) {
				if(overtimeSummary.getDepartment().getId().toString().equals(
						overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList1.add(overtimeDtl);
				}
			}
			
			List<OvertimeDtl> otDtlList2 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList2) {
				if(overtimeSummary.getDepartment().getId().toString().equals(
						overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList2.add(overtimeDtl);
				}
			}
			
			List<OvertimeDtl> otDtlList3 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList3) {
				if(overtimeSummary.getDepartment().getId().toString().equals(
						overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList3.add(overtimeDtl);
				}
			}
			
			Double total = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList) {
				total+=this.round2(overtimeDtl.getTotalPayment(), 0);
			}
			
			Double total1 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList1) {
				total1+=this.round2(overtimeDtl.getTotalPayment(), 0);
			}
			
			Double total2 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList2) {
				total2+=this.round2(overtimeDtl.getTotalPayment(), 0);
			}
			
			Double total3 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList3) {
				total3+=this.round2(overtimeDtl.getTotalPayment(), 0);
			}
			
			
			
			
			overtimeSummary.setRecid(overtimeSummary.getId());
			overtimeSummary.setSlNo(i);
			/*overtimeSummary.setTotalOfThisMonth(total);
			
			overtimeSummary.setTotalOfPervOneMonth(total1);
			overtimeSummary.setTotalOfPervTwoMonth(total2);
			overtimeSummary.setTotalOfPervThreeMonth(total3);
			
			overtimeSummary.setDifferLastTwoMonth(total - total1);*/
			
			overtimeSummary.setTotalOfThisMonth(this.round2(total, 0));

			overtimeSummary.setTotalOfPervOneMonth(this.round2(total1, 0));
			overtimeSummary.setTotalOfPervTwoMonth(this.round2(total2, 0));
			overtimeSummary.setTotalOfPervThreeMonth(this.round2(total3, 0));

			Double differ = (total - total1);
			overtimeSummary.setDifferLastTwoMonth(this.round2(differ, 0));
			
			overtimeSummary.setDepartmentName(overtimeSummary.getDepartment().getName());
			
			otSummaryList1.add(overtimeSummary);
			i++;
		}
		
		for (OvertimeSummary os : otSummaryList1) {
			if(os.getTotalOfPervOneMonth() > 0 || os.getTotalOfThisMonth() > 0 || os.getTotalOfPervTwoMonth() > 0 || os.getTotalOfPervThreeMonth() > 0) {
				otSummaryList2.add(os);
			}
		}
			
		InputStream jasperStream = null;
		jasperStream = this.getClass().getResourceAsStream("/overtime_summary_report.jasper");
		Map<String, Object> params = new HashMap<>();
		jRdataSource = new JRBeanCollectionDataSource(otSummaryList2, false);
		params.put("datasource", jRdataSource);
		params.put("OT_YEAR", otYear.toString());
		params.put("OT_MONTH", otMonth);
		
		params.put("OT_YEAR_1", year1.toString());
		params.put("OT_YEAR_2", year2.toString());
		params.put("OT_YEAR_3", year3.toString());
		
		params.put("OT_MONTH_1", prevOneMonth);
		params.put("OT_MONTH_2", prevTwoMonth);
		params.put("OT_MONTH_3", prevThreeMonth);
		params.put("UNIT_NAME", company.getName());
		
			
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRdataSource);
		
		if(reportFormat.equals("xlx")){	
			response.addHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		    response.addHeader("Content-disposition", "attachment; filename=overtime_summary_report.xlsx");
		    ServletOutputStream outputStream = response.getOutputStream();
		    net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
		    exporter.exportReport();
		    outputStream.flush();
		    outputStream.close();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		} else {
			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=overtime_summary_report.pdf");
			final OutputStream outStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		}
		
	}
	
	private String getMonthName(Integer key) {
		Map<Integer, String> months = new HashMap<Integer, String>();
		months.put(1, "January");
		months.put(2, "February");
		months.put(3, "March");
		months.put(4, "April");
		months.put(5, "May");
		months.put(6, "June");
		months.put(7, "July");
		months.put(8, "August");
		months.put(9, "September");
		months.put(10, "October");
		months.put(11, "November");
		months.put(12, "December");		
		return months.get(key);
	}
	
	private Integer getMonthKey(String month) {
		Map<String, Integer> months = new HashMap<String, Integer>();
		months.put("January", 1);
		months.put("February", 2);
		months.put("March", 3);
		months.put("April", 4);
		months.put("May", 5);
		months.put("June", 6);
		months.put("July", 7);
		months.put("August", 8);
		months.put("September", 9);
		months.put("October", 10);
		months.put("November", 11);
		months.put("December", 12);
		
		return months.get(month);
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    
	    //long tmp = Math.round(value); 
	    //return (double) tmp / factor;
	    double tmp = Math.floor(value);
	    return (double) tmp / factor;
	} 
	
	private double round2(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    
	    long tmp = Math.round(value); 
	    return (double) tmp / factor;
	    //double tmp = Math.floor(value);
	    //return (double) tmp / factor;
	} 
	
}
