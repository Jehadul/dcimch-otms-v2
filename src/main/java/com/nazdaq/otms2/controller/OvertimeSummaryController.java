package com.nazdaq.otms2.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.nazdaq.otms2.beans.OvertimeDtlBean;
import com.nazdaq.otms2.beans.OvertimeSheet;
import com.nazdaq.otms2.model.Department;
import com.nazdaq.otms2.model.OvertimeDtl;
import com.nazdaq.otms2.model.OvertimeSummary;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.service.UserService;
import com.nazdaq.otms2.model.Company;

@Controller
@PropertySource("classpath:common.properties")
public class OvertimeSummaryController {

	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@RequestMapping(value = "/otSummaryEntryForm", method = RequestMethod.GET)
	public ModelAndView getOvertimeSummaryForm(@ModelAttribute("overtimeSummaryForm") OvertimeSummary overtimeSummary,
			BindingResult result, Principal principal) {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}

		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelAndView("otSummaryEntryForm", model);
	}

	// generateOtSummary
	@RequestMapping(value = "/generateOtSummary", method = RequestMethod.POST)
	public ModelAndView generateOtSummary(@ModelAttribute("overtimeSummaryForm") OvertimeSummary overtimeSummaryBean,
			BindingResult result, Principal principal, RedirectAttributes attributes, HttpSession session, HttpServletRequest request) throws Exception {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		Map<String, Object> model = new HashMap<String, Object>();

		String otYear = overtimeSummaryBean.getOtYear();
		String otMonth = overtimeSummaryBean.getOtMonth();
		Integer selectedMonthKey = this.getMonthKey(otMonth);
		String month = "";
		if (selectedMonthKey.toString().length() < 2) {
			month = "0" + selectedMonthKey;
		} else {
			month = selectedMonthKey.toString();
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date selectedDate = df.parse(otYear + "-" + month + "-01");

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(selectedDate);
		cal1.add(Calendar.MONTH, -1);
		Date datePrevOne = cal1.getTime();
		Integer m1 = datePrevOne.getMonth() + 1;
		Integer year1 = cal1.get(Calendar.YEAR);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(datePrevOne);
		cal2.add(Calendar.MONTH, -1);
		Date datePrevTwo = cal2.getTime();
		Integer m2 = datePrevTwo.getMonth() + 1;
		Integer year2 = cal2.get(Calendar.YEAR);

		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(datePrevTwo);
		cal3.add(Calendar.MONTH, -1);
		Date datePrevThree = cal3.getTime();
		Integer m3 = datePrevThree.getMonth() + 1;
		Integer year3 = cal3.get(Calendar.YEAR);

		String prevOneMonth = this.getMonthName(m1);
		String prevTwoMonth = this.getMonthName(m2);
		String prevThreeMonth = this.getMonthName(m3);

		String loginUser = principal.getName();

		//List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) commonService.getObjectListByTwoColumn("OvertimeSummary", "otYear", otYear, "otMonth", otMonth);
		List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) 
				commonService.getObjectListByThreeColumn("OvertimeSummary", "otYear", otYear, "otMonth", otMonth, "company.id", company.getId().toString());

		if (otSummaryList != null && otSummaryList.size() > 0) {
			OvertimeSummary otd = otSummaryList.get(0);
			if (otd.getFinalSubmit() == 1) {
				return this.otSummaryFinalList(overtimeSummaryBean, principal, session, request);
			} else {
				model.put("year1", year1);
				model.put("year2", year2);
				model.put("year3", year3);

				model.put("prevOneMonth", prevOneMonth);
				model.put("prevTwoMonth", prevTwoMonth);
				model.put("prevThreeMonth", prevThreeMonth);

				model.put("otYear", otYear);
				model.put("otMonth", otMonth);
				return new ModelAndView("otSummaryListForm", model);
			}
		} else {
			//Integer maxId = (Integer) commonService.getMaxValueByObjectAndColumn("OvertimeSummary", "id");
			Integer maxId = (Integer) commonService.getMaxValueByObjectAndTwoColumn("OvertimeSummary", "id", "company.id", company.getId().toString());
			if (maxId > 0) {
				OvertimeSummary otSummaryMax = (OvertimeSummary) commonService
						.getAnObjectByAnyUniqueColumn("OvertimeSummary", "id", maxId.toString());
				String otYearDb = otSummaryMax.getOtYear();
				String otMonthDb = otSummaryMax.getOtMonth();
				boolean res = this.isCorrectRequest(otYear, otMonth, otYearDb, otMonthDb);
				if (res) {
					this.saveNewOtSummary(otYear, otMonth, loginUser, company);

					model.put("year1", year1);
					model.put("year2", year2);
					model.put("year3", year3);

					model.put("prevOneMonth", prevOneMonth);
					model.put("prevTwoMonth", prevTwoMonth);
					model.put("prevThreeMonth", prevThreeMonth);

					model.put("otYear", otYear);
					model.put("otMonth", otMonth);
					return new ModelAndView("otSummaryListForm", model);
				} else {
					attributes.addFlashAttribute("successMsg",
							"Please Select Correct Year or Month or Contact with Developer.");
					return new ModelAndView("redirect:/otSummaryEntryForm", model);
				}
			} else {
				this.saveNewOtSummary(otYear, otMonth, loginUser, company);

				model.put("year1", year1);
				model.put("year2", year2);
				model.put("year3", year3);

				model.put("prevOneMonth", prevOneMonth);
				model.put("prevTwoMonth", prevTwoMonth);
				model.put("prevThreeMonth", prevThreeMonth);

				model.put("otYear", otYear);
				model.put("otMonth", otMonth);
				return new ModelAndView("otSummaryListForm", model);
			}
		}
	}

	public ModelAndView otSummaryFinalList(OvertimeSummary overtimeSummary, Principal principal, HttpSession session, HttpServletRequest request) throws ParseException {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		String otYear = overtimeSummary.getOtYear();
		String otMonth = overtimeSummary.getOtMonth();

		Integer selectedMonthKey = this.getMonthKey(otMonth);
		String month = "";
		if (selectedMonthKey.toString().length() < 2) {
			month = "0" + selectedMonthKey;
		} else {
			month = selectedMonthKey.toString();
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date selectedDate = df.parse(otYear + "-" + month + "-01");

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(selectedDate);
		cal1.add(Calendar.MONTH, -1);
		Date datePrevOne = cal1.getTime();
		Integer m1 = datePrevOne.getMonth() + 1;
		Integer year1 = cal1.get(Calendar.YEAR);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(datePrevOne);
		cal2.add(Calendar.MONTH, -1);
		Date datePrevTwo = cal2.getTime();
		Integer m2 = datePrevTwo.getMonth() + 1;
		Integer year2 = cal2.get(Calendar.YEAR);

		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(datePrevTwo);
		cal3.add(Calendar.MONTH, -1);
		Date datePrevThree = cal3.getTime();
		Integer m3 = datePrevThree.getMonth() + 1;
		Integer year3 = cal3.get(Calendar.YEAR);

		String prevOneMonth = this.getMonthName(m1);
		String prevTwoMonth = this.getMonthName(m2);
		String prevThreeMonth = this.getMonthName(m3);

		model.put("year1", year1);
		model.put("year2", year2);
		model.put("year3", year3);

		model.put("prevOneMonth", prevOneMonth);
		model.put("prevTwoMonth", prevTwoMonth);
		model.put("prevThreeMonth", prevThreeMonth);

		model.put("otYear", otYear);
		model.put("otMonth", otMonth);
		return new ModelAndView("otSummaryFinalList", model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/getMonthlyOtSummaryList" }, method = RequestMethod.POST)
	private @ResponseBody String getMonthlyOtSummaryList(@RequestBody String jesonString, Principal principal, HttpSession session, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, Exception {
		
		String toJson = "";
		Gson gson = new Gson();
		
		Company company = (Company) session.getAttribute("company");
		
		OvertimeSummary otSummaryBean = gson.fromJson(jesonString, OvertimeSummary.class);

		List<OvertimeSummary> otSummaryList1 = new ArrayList<OvertimeSummary>();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
		Date selectedDate = new Date();
		String otYear =  otSummaryBean.getOtYear();
		String otMonth = otSummaryBean.getOtMonth();
		
		Integer selectedMonthKey = this.getMonthKey(otMonth);
		String month = ""; 
		if(selectedMonthKey != null) {
			if(selectedMonthKey.toString().length() < 2) {
				 month = "0" + selectedMonthKey;
			} else {
				month = selectedMonthKey.toString();
			}
		}
		
		
		if(otYear != null && otYear.length() > 0) {
			selectedDate = df.parse(otYear + "-" + month + "-01");
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(selectedDate);
		cal1.add(Calendar.MONTH, -1);
		Date datePrevOne = cal1.getTime();
		Integer m1 = datePrevOne.getMonth() + 1;
		Integer year1 = cal1.get(Calendar.YEAR);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(datePrevOne);
		cal2.add(Calendar.MONTH, -1);
		Date datePrevTwo = cal2.getTime();
		Integer m2 = datePrevTwo.getMonth() + 1;
		Integer year2 = cal2.get(Calendar.YEAR);

		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(datePrevTwo);
		cal3.add(Calendar.MONTH, -1);
		Date datePrevThree = cal3.getTime();
		Integer m3 = datePrevThree.getMonth() + 1;
		Integer year3 = cal3.get(Calendar.YEAR);

		String prevOneMonth = this.getMonthName(m1);
		String prevTwoMonth = this.getMonthName(m2);
		String prevThreeMonth = this.getMonthName(m3);

		//List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) commonService.getObjectListByTwoColumn("OvertimeSummary", "otYear", otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth());

		List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) 
				commonService.getObjectListByThreeColumn("OvertimeSummary", "otYear", 
						otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth(),
						"company.id", company.getId().toString());
		
		List<OvertimeDtl> otDtlailsList = (List<OvertimeDtl>) (Object) commonService.getObjectListByThreeColumn(
				"OvertimeDtl", "otYear", otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth(), "employee.company.id", company.getId().toString());

		List<OvertimeDtl> otDtlailsList1 = (List<OvertimeDtl>) (Object) commonService
				.getObjectListByThreeColumn("OvertimeDtl", "otYear", year1.toString(), "otMonth", prevOneMonth, "employee.company.id", company.getId().toString());

		List<OvertimeDtl> otDtlailsList2 = (List<OvertimeDtl>) (Object) commonService
				.getObjectListByThreeColumn("OvertimeDtl", "otYear", year2.toString(), "otMonth", prevTwoMonth, "employee.company.id", company.getId().toString());

		List<OvertimeDtl> otDtlailsList3 = (List<OvertimeDtl>) (Object) commonService
				.getObjectListByThreeColumn("OvertimeDtl", "otYear", year3.toString(), "otMonth", prevThreeMonth, "employee.company.id", company.getId().toString());

		int i = 1;
		for (OvertimeSummary overtimeSummary : otSummaryList) {
			List<OvertimeDtl> otDtlList = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList) {
				if (overtimeSummary.getDepartment().getId().toString()
						.equals(overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList.add(overtimeDtl);
				}
			}

			List<OvertimeDtl> otDtlList1 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList1) {
				if (overtimeSummary.getDepartment().getId().toString()
						.equals(overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList1.add(overtimeDtl);
				}
			}

			List<OvertimeDtl> otDtlList2 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList2) {
				if (overtimeSummary.getDepartment().getId().toString()
						.equals(overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList2.add(overtimeDtl);
				}
			}

			List<OvertimeDtl> otDtlList3 = new ArrayList<OvertimeDtl>();
			for (OvertimeDtl overtimeDtl : otDtlailsList3) {
				if (overtimeSummary.getDepartment().getId().toString()
						.equals(overtimeDtl.getEmployee().getDepartment().getId().toString())) {
					otDtlList3.add(overtimeDtl);
				}
			}

			/*
			 * List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
			 * commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear",
			 * otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth(),
			 * "employee.department.id",
			 * overtimeSummary.getDepartment().getId().toString());
			 * 
			 * List<OvertimeDtl> otDtlList1 = (List<OvertimeDtl>)(Object)
			 * commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear",
			 * year1.toString(), "otMonth", prevOneMonth, "employee.department.id",
			 * overtimeSummary.getDepartment().getId().toString());
			 * 
			 * List<OvertimeDtl> otDtlList2 = (List<OvertimeDtl>)(Object)
			 * commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear",
			 * year2.toString(), "otMonth", prevTwoMonth, "employee.department.id",
			 * overtimeSummary.getDepartment().getId().toString());
			 * 
			 * List<OvertimeDtl> otDtlList3 = (List<OvertimeDtl>)(Object)
			 * commonService.getObjectListByThreeColumn("OvertimeDtl",
			 * "otYear",year3.toString(), "otMonth", prevThreeMonth,
			 * "employee.department.id",
			 * overtimeSummary.getDepartment().getId().toString());
			 */

			Double total = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList) {
				total += this.round2(overtimeDtl.getTotalPayment(), 0);
			}

			Double total1 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList1) {
				total1 += this.round2(overtimeDtl.getTotalPayment(), 0);
			}

			Double total2 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList2) {
				total2 += this.round2(overtimeDtl.getTotalPayment(), 0);
			}

			Double total3 = 0.0;
			for (OvertimeDtl overtimeDtl : otDtlList3) {
				total3 += this.round2(overtimeDtl.getTotalPayment(), 0);
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

			otSummaryList1.add(overtimeSummary);
			i++;
		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(otSummaryList1);
		return toJson;
	}

	// saveMonthlyOtSummaryList
	@RequestMapping(value = { "/saveMonthlyOtSummaryList" }, method = RequestMethod.POST)
	private @ResponseBody String saveMonthlyOtSummaryList(@RequestBody String jesonString, Principal principal, HttpSession session, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String result = "";
		Gson gson = new Gson();
		OvertimeDtlBean otSummaryBean = gson.fromJson(jesonString, OvertimeDtlBean.class);
		List<OvertimeSheet> otSummaryList = otSummaryBean.getOvertimeSheetList();
		Company company = (Company) session.getAttribute("company");
		
		//List<OvertimeSummary> otSummaryListDb = (List<OvertimeSummary>) (Object) commonService.getObjectListByTwoColumn("OvertimeSummary", "otYear", otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth());
		
		List<OvertimeSummary> otSummaryListDb = (List<OvertimeSummary>) (Object) commonService.getObjectListByThreeColumn(
				"OvertimeSummary", "otYear", otSummaryBean.getOtYear(), "otMonth", otSummaryBean.getOtMonth(),
				"company.id", company.getId().toString());
		
		Date now = new Date();
		String loginUser = principal.getName();
		if (otSummaryList.size() > 0) {
			for (OvertimeSheet otSummary : otSummaryList) {
				OvertimeSummary otSummaryDb = (OvertimeSummary) commonService
						.getAnObjectByAnyUniqueColumn("OvertimeSummary", "id", otSummary.getRecid().toString());
				
				for (OvertimeSummary overtimeSummary : otSummaryListDb) {
					if (overtimeSummary.getId().toString().equals(otSummary.getRecid().toString())) {

						if (otSummary.getRemarks() != null) {
							overtimeSummary.setRemarks(otSummary.getRemarks());
						}
						overtimeSummary.setOverallRemarks(otSummaryBean.getOverallRemarks());
						overtimeSummary.setModifiedBy(loginUser);
						overtimeSummary.setModifiedDate(now);
						commonService.saveOrUpdateModelObjectToDB(overtimeSummary);
						break;
					} 
				}
				
								
				/*if (otSummaryDb.getOverallRemarks().equals(otSummaryBean.getOverallRemarks())) {
						if (otSummary.getRemarks() != null) {
							otSummaryDb.setRemarks(otSummary.getRemarks());
						}
						otSummaryDb.setModifiedBy(loginUser);
						otSummaryDb.setModifiedDate(now);
						commonService.saveOrUpdateModelObjectToDB(otSummaryDb);
					} else {
						for (OvertimeSummary overtimeSummary : otSummaryListDb) {
							if (overtimeSummary.getId().toString().equals(otSummary.getRecid().toString())) {

								if (otSummary.getRemarks() != null) {
									overtimeSummary.setRemarks(otSummary.getRemarks());
								}
								overtimeSummary.setOverallRemarks(otSummaryBean.getOverallRemarks());
								overtimeSummary.setModifiedBy(loginUser);
								overtimeSummary.setModifiedDate(now);
								commonService.saveOrUpdateModelObjectToDB(overtimeSummary);
								break;
							}
						}
					}*/

				

			}
			
			for (OvertimeSummary overtimeSummary : otSummaryListDb) {				
					overtimeSummary.setOverallRemarks(otSummaryBean.getOverallRemarks());
					overtimeSummary.setModifiedBy(loginUser);
					overtimeSummary.setModifiedDate(now);
					commonService.saveOrUpdateModelObjectToDB(overtimeSummary);
			}
			
			result = "success";
		} else {
			for (OvertimeSummary overtimeSummary : otSummaryListDb) {
				overtimeSummary.setOverallRemarks(otSummaryBean.getOverallRemarks());
				overtimeSummary.setModifiedBy(loginUser);
				overtimeSummary.setModifiedDate(now);
				commonService.saveOrUpdateModelObjectToDB(overtimeSummary);
			}

			result = "success";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(result);
		return toJson;
	}

	@RequestMapping(value = "/otSummaryFinalSubmit", method = RequestMethod.POST)
	public ModelAndView otSummaryFinalSubmit(OvertimeSummary otSummary, Principal principal,
			RedirectAttributes attributes, HttpSession session, HttpServletRequest request) throws ParseException {

		if (principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		Date now = new Date();
		Map<String, Object> model = new HashMap<String, Object>();
		String otYear = otSummary.getOtYear();
		String otMonth = otSummary.getOtMonth();
		String loginUser = principal.getName();

		//List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) commonService.getObjectListByTwoColumn("OvertimeSummary", "otYear", otYear, "otMonth", otMonth);
		
		List<OvertimeSummary> otSummaryList = (List<OvertimeSummary>) (Object) commonService
				.getObjectListByThreeColumn("OvertimeSummary", "otYear", otYear, "otMonth", otMonth, 
						"company.id", company.getId().toString());

		if (otSummaryList.size() > 0) {
			OvertimeSummary otd = otSummaryList.get(0);
			if (otd.getFinalSubmit() == 0) {
				for (OvertimeSummary overtimeSummary : otSummaryList) {
					overtimeSummary.setFinalSubmit(1);
					overtimeSummary.setModifiedBy(loginUser);
					overtimeSummary.setModifiedDate(now);
					commonService.saveOrUpdateModelObjectToDB(overtimeSummary);
				}
			}
		}

		model.put("otYear", otYear);
		model.put("otMonth", otMonth);
		// return new
		// ModelAndView("redirect:/otDtlFinalList?otYear="+otYear+"&otMonth="+otMonth,
		// model);
		return this.otSummaryFinalList(otSummary, principal, session, request);
	}

	@SuppressWarnings("unchecked")
	private void saveNewOtSummary(String otYear, String otMonth, String loginUser, Company company) {
		List<Department> departmentList = (List<Department>) (Object) commonService.getAllObjectList("Department");

		OvertimeSummary otSummaryModel = null;
		Date now = new Date();
		for (Department department : departmentList) {
			otSummaryModel = new OvertimeSummary(null, otYear, otMonth, department, loginUser, now, null, null);
			otSummaryModel.setCompany(company);
			commonService.saveOrUpdateModelObjectToDB(otSummaryModel);
		}
	}

	private boolean isCorrectRequest(String otYear, String otMonth, String otYearDb, String otMonthDb) {
		boolean result = false;
		Integer otMKey = this.getMonthKey(otMonth);
		Integer otMDbKey = this.getMonthKey(otMonthDb);

		if (otMDbKey == (otMKey - 1) && otYear.equals(otYearDb)) {
			result = true;
		}

		if ((otMDbKey - otMKey) == 11 && Integer.parseInt(otYear) == (Integer.parseInt(otYearDb) + 1)) {
			result = true;
		}

		return result;
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

	/*private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}*/
	
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
