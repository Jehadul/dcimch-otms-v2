package com.nazdaq.otms2.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.nazdaq.otms2.model.Company;
import com.nazdaq.otms2.model.Employee;
import com.nazdaq.otms2.model.OvertimeDtl;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.service.UserService;

@Controller
@PropertySource("classpath:common.properties")
public class OvertimeController {

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
	
	@RequestMapping(value = "/otDtlEntryForm", method = RequestMethod.GET)
	public ModelAndView getOvertimeDtlForm (@ModelAttribute("overtimeDtlForm") OvertimeDtl otDtl, 
			BindingResult result, Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("otDtlEntryForm", model);
	}
	
	@RequestMapping(value = "/generateOtDtl", method = RequestMethod.POST)
	public ModelAndView generateOtDtl (@ModelAttribute("overtimeDtlForm") OvertimeDtl otDtl, 
			BindingResult result, Principal principal, RedirectAttributes attributes, HttpSession session, HttpServletRequest request) throws Exception{
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		String otYear =  otDtl.getOtYear();
		String otMonth = otDtl.getOtMonth();
		String loginUser = principal.getName();
		
		List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "employee.company.id", company.getId().toString());
		if(otDtlList != null && otDtlList.size() > 0) {
			OvertimeDtl otd = otDtlList.get(0);
			if(otd.getFinalSubmit() == 1) {
				return this.otDtlFinalList(otDtl, principal);
			} else {
				model.put("otYear", otYear);
				model.put("otMonth", otMonth);
				return new ModelAndView("otDtlListForm", model);
			}
		} else {
			
			Integer maxId = (Integer) commonService.getMaxValueByObjectAndTwoColumn("OvertimeDtl", "id", "employee.company.id", company.getId().toString());
			
			if(maxId > 0 ) {
				OvertimeDtl otDtlMax = (OvertimeDtl) commonService.getAnObjectByAnyUniqueColumn("OvertimeDtl", "id", maxId.toString());
				String otYearDb = otDtlMax.getOtYear();
				String otMonthDb = 	otDtlMax.getOtMonth();
				boolean res = this.isCorrectRequest(otYear, otMonth, otYearDb, otMonthDb);
				if(res) {
					this.saveNewOtDtl(otYear, otMonth, loginUser, company);
					model.put("otYear", otYear);
					model.put("otMonth", otMonth);
					return new ModelAndView("otDtlListForm", model);
				} else {
					attributes.addFlashAttribute("successMsg", "Please Select Correct Year or Month or Contact with Developer.");
					return new ModelAndView("redirect:/otDtlEntryForm", model);
				}
			} else {
				this.saveNewOtDtl(otYear, otMonth, loginUser, company);
				model.put("otYear", otYear);
				model.put("otMonth", otMonth);
				return new ModelAndView("otDtlListForm", model);
			}
		}
	}
	
	@RequestMapping(value = "/otDtlFinalSubmit", method = RequestMethod.POST)
	public ModelAndView otDtlFinalSubmit (OvertimeDtl otDtl, Principal principal, RedirectAttributes attributes, HttpSession session, HttpServletRequest request) {
	
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Company company = (Company) session.getAttribute("company");
		
		Date now = new Date();
		Map <String, Object> model = new HashMap<String, Object>();		
		String otYear =  otDtl.getOtYear();
		String otMonth = otDtl.getOtMonth();
		String loginUser = principal.getName();
		List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "employee.company.id", company.getId().toString());
		if(otDtlList.size() > 0) {
			OvertimeDtl otd = otDtlList.get(0);
			if(otd.getFinalSubmit() == 0) {
				for (OvertimeDtl overtimeDtl : otDtlList) {
					overtimeDtl.setFinalSubmit(1);
					overtimeDtl.setModifiedBy(loginUser);
					overtimeDtl.setModifiedDate(now);
					overtimeDtl.setCompanyId(company.getId());
					commonService.saveOrUpdateModelObjectToDB(overtimeDtl);
				}
			}
		}
		
		model.put("otYear", otYear);
		model.put("otMonth", otMonth);
		//return new ModelAndView("redirect:/otDtlFinalList?otYear="+otYear+"&otMonth="+otMonth, model);
		return this.otDtlFinalList(otDtl, principal);
	}
	
	
	public ModelAndView otDtlFinalList (OvertimeDtl otDtl, Principal principal) {
	
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();		
		String otYear =  otDtl.getOtYear();
		String otMonth = otDtl.getOtMonth();
		
		model.put("otYear", otYear);
		model.put("otMonth", otMonth);
		return new ModelAndView("otDtlFinalList", model);
	}
	
	//@RequestMapping(value = {"/otDtlFinalList"}, method = RequestMethod.POST)
	
	//monthly Overtime Dtl List
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/getMonthlyOtDltList"}, method = RequestMethod.POST)
	private @ResponseBody String getMonthlyOtDltList(@RequestBody String jesonString, Principal principal, HttpSession session, HttpServletRequest request) 
			throws JsonGenerationException, JsonMappingException, Exception {
		
		Company company = (Company) session.getAttribute("company");
		
		String toJson = "";
		Gson gson = new Gson();
		OvertimeDtl otDtlBean = gson.fromJson(jesonString, OvertimeDtl.class);
		List<OvertimeDtl> otDtlList1 = new ArrayList<OvertimeDtl>();
		List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otDtlBean.getOtYear(), "otMonth", otDtlBean.getOtMonth(), "employee.company.id", company.getId().toString());
		int i = 1;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (OvertimeDtl overtimeDtl : otDtlList) {
			overtimeDtl.setRecid(overtimeDtl.getId());
			overtimeDtl.setSlNo(i);
			overtimeDtl.setJoinDate(
					df.format(overtimeDtl.getEmployee().getJoinDate())
					);
			overtimeDtl.setOtTotalDays(overtimeDtl.getTotalOtDays().toString());
			
			Double totalPayment = this.round(overtimeDtl.getTotalPayment(), 3);
			overtimeDtl.setTotalPayment(totalPayment);
			
			overtimeDtl.setTotalOtDays(this.round2(overtimeDtl.getTotalOtDays(), 3));
			
			otDtlList1.add(overtimeDtl);
			i++;
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(otDtlList1);
		return toJson;
	}
	
	
	@RequestMapping(value = {"/saveMonthlyOtDltList"}, method = RequestMethod.POST)
	private @ResponseBody String saveMonthlyOtDltList(@RequestBody String jesonString, Principal principal, HttpSession session, HttpServletRequest request) 
			throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		String result = "";
		Gson gson = new Gson();
		OvertimeDtlBean otDtlBean = gson.fromJson(jesonString, OvertimeDtlBean.class);		
		List<OvertimeSheet>	otDtlList = otDtlBean.getOvertimeSheetList();
		Date now = new Date();
		String loginUser = principal.getName();
		if(otDtlList.size() > 0 ) {
			for (OvertimeSheet otDtl : otDtlList) {
				OvertimeDtl otDtlDb = (OvertimeDtl) commonService.getAnObjectByAnyUniqueColumn("OvertimeDtl", "id", otDtl.getRecid().toString());
				
				
				if(otDtl.getHandCash() != null) {	
					otDtlDb.setHandCash(otDtl.getHandCash());
				}
				
				if(otDtl.getRemarks() != null) {	
					otDtlDb.setRemarks(otDtl.getRemarks());
				}
				
				if(otDtl.getTotalOtHours() != null) {					
					Double otDays = (otDtl.getTotalOtHours()).doubleValue() / (otDtlDb.getEmployee().getWorkingHours()).doubleValue();
					
					//Double otDaysRound = this.round(otDays, 3); 
					//Double totalPayment = otDaysRound * (otDtlDb.getOtUnit()).doubleValue();
					//otDtlDb.setTotalOtDays(otDaysRound);
					
					Double totalPayment = otDays * (otDtlDb.getOtUnit()).doubleValue();
					otDtlDb.setTotalOtDays(otDays);
					
					otDtlDb.setTotalOtHours(otDtl.getTotalOtHours());					
					otDtlDb.setTotalPayment(totalPayment);
				}
				
				
				otDtlDb.setModifiedBy(loginUser);
				otDtlDb.setModifiedDate(now);
				
				commonService.saveOrUpdateModelObjectToDB(otDtlDb);
			}
			result = "success";
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		toJson = ow.writeValueAsString(result);
		return toJson;
	}	
	
	
	@RequestMapping(value = "/otDtlDeleteForm", method = RequestMethod.GET)
	public ModelAndView getOvertimeDtlDeleteForm (@ModelAttribute("overtimeDtlForm") OvertimeDtl otDtl, 
			BindingResult result, Principal principal, HttpSession session, HttpServletRequest request){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("otDtlDeleteForm", model);
	}
	
	@RequestMapping(value = "/otDtlDelete", method = RequestMethod.POST)
	public ModelAndView otDtlDelete (@ModelAttribute("overtimeDtlForm") OvertimeDtl otDtl, 
			BindingResult result, Principal principal, RedirectAttributes attributes, HttpSession session, HttpServletRequest request) throws Exception{
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		String otYear =  otDtl.getOtYear();
		String otMonth = otDtl.getOtMonth();
		String loginUser = principal.getName();
		try {
			
			commonService.deleteObjectListByFourColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "finalSubmit", "0", "employee.company.id", company.getId().toString());
			
			//attributes.addAttribute("message", "Data Deleted Successfully");
			model.put("message", "Data Deleted Successfully");
			return new ModelAndView("otDtlDeleteForm", model);
		} catch (Exception e){
			e.printStackTrace();
			//attributes.addAttribute("message", "Data Deleted Failed");
			model.put("message", "Data Deleted Failed for no data exist or already finally submitted");
			return new ModelAndView("otDtlDeleteForm", model);
		}
		
	}
	
	@RequestMapping(value = "/updateOtDtlList", method = RequestMethod.GET)
	public ModelAndView getUpdateOtDtlList (OvertimeDtl otDtl, 
			BindingResult result, Principal principal, HttpSession session, HttpServletRequest request){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		Company company = (Company) session.getAttribute("company");
		
		OvertimeDtl otDtlModel = null;
		Date now = new Date();
		String loginUser = principal.getName();
		String otYear =  otDtl.getOtYear();
		String otMonth = otDtl.getOtMonth();
		List<Employee> employeeList1 = new ArrayList<Employee>();
		
		List<OvertimeDtl> otDtlList = (List<OvertimeDtl>)(Object)
				commonService.getObjectListByThreeColumn("OvertimeDtl", "otYear", otYear, "otMonth", otMonth, "employee.company.id", company.getId().toString());			
		
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "active", "1", "company.id", company.getId().toString());
		
		
		for (Employee e : employeeList) {
			boolean flag = false;
			for (OvertimeDtl d : otDtlList) {
				if(e.getEmpId().toString().equals(d.getEmployee().getEmpId().toString())) {
					if(!e.getGrossSalary().toString().equals(d.getGrossSalary().toString())) {
						// update
						
						Double basicSalary = (e.getGrossSalary()*40)/100;
						Double	otUnit = (basicSalary*8)/100;
						Double totalPayment = otUnit * d.getTotalOtDays();	
							
						d.setGrossSalary(e.getGrossSalary());
						d.setBasicSalary(basicSalary);
						d.setOtUnit(otUnit);
						d.setTotalPayment(totalPayment);
						d.setCompanyId(company.getId());
						
						commonService.saveOrUpdateModelObjectToDB(d);
						flag = true;
						//commonService.deleteAnObjectById("OvertimeDtl", d.getId());
						//flag = false;
						break;
					} else if(e.getFixedUnit() != null && e.getFixedUnit().intValue() > 0){
						if(!e.getFixedUnit().toString().equals(d.getOtUnit().toString())) {
							commonService.deleteAnObjectById("OvertimeDtl", d.getId());
							flag = false;
							break;
						} else {
							flag = true;
							break;
						}
					} else {
						flag = true;
						break;
					}
					
				} 
			}
			
			if(!flag) {
				employeeList1.add(e);
			}
		}
		
		for (Employee employee : employeeList1) {
			Double otUnit = 0.0;
			Double basicSalary = (employee.getGrossSalary()*40)/100;
			
			
			if(employee.getFixedUnit() != null && employee.getFixedUnit() > 0) {
				otUnit = employee.getFixedUnit();
			} else {
				// 8 % of basic salary
				//otUnit = (basicSalary.intValue()*8)/100;
				otUnit = (basicSalary*8)/100;
			}
			
			otDtlModel = new OvertimeDtl(null, employee, otYear, otMonth, employee.getGrossSalary(),
					basicSalary, otUnit, loginUser, now, null);
			otDtlModel.setFinalSubmit(0);
			otDtlModel.setCompanyId(company.getId());
			commonService.saveOrUpdateModelObjectToDB(otDtlModel);
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		
		model.put("otYear", otYear);
		model.put("otMonth", otMonth);
		return new ModelAndView("otDtlListForm", model);
	}
	
	@SuppressWarnings("unchecked")
	private void saveNewOtDtl(String otYear, String otMonth, String loginUser, Company company) {
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByTwoColumn("Employee", "active", "1", "company.id", company.getId().toString());
		OvertimeDtl otDtlModel = null;
		Date now = new Date();
		
		for (Employee employee : employeeList) {
			Double otUnit = 0.0;
			Double basicSalary = (employee.getGrossSalary()*40)/100;
			
			
			if(employee.getFixedUnit() != null && employee.getFixedUnit() > 0) {
				otUnit = employee.getFixedUnit();
			} else {
				// 8 % of basic salary
				//otUnit = (basicSalary.intValue()*8)/100;
				otUnit = (basicSalary*8)/100;
			}
			
			otDtlModel = new OvertimeDtl(null, employee, otYear, otMonth, employee.getGrossSalary(),
					basicSalary, otUnit, loginUser, now, null);
			otDtlModel.setFinalSubmit(0);
			otDtlModel.setCompanyId(company.getId());
			commonService.saveOrUpdateModelObjectToDB(otDtlModel);
		}
	}
	
	private boolean isCorrectRequest(String otYear, String otMonth, String otYearDb, String otMonthDb) {
		boolean result = false;
		Integer otMKey = this.getMonthKey(otMonth);
		Integer otMDbKey = this.getMonthKey(otMonthDb);
		
		if(otMDbKey == (otMKey-1) && otYear.equals(otYearDb)) {
			result = true;
		}
		
		if((otMDbKey-otMKey) == 11 && 
				Integer.parseInt(otYear) == (Integer.parseInt(otYearDb)+1)) {
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
