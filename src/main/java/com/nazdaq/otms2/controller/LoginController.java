package com.nazdaq.otms2.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.otms2.model.OvertimeSummary;
import com.nazdaq.otms2.model.Company;
import com.nazdaq.otms2.model.User;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.service.UserService;
import com.nazdaq.otms2.util.Constants;

@Controller
public class LoginController extends SavedRequestAwareAuthenticationSuccessHandler implements Constants{
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonService commonService;
	
	
	@RequestMapping(value="/success", method = RequestMethod.GET)
	public String success(ModelMap model) {
	return "success"; 
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/","/index"}, method = RequestMethod.GET)
	public ModelAndView printWelcome1(ModelMap model, Principal principal, HttpSession session, HttpServletRequest request) throws ParseException {
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		String roleName = 	commonService.getAuthRoleName();
		
		String pageLocation=null;
		User user=null;
		String name = principal.getName();
		user=userService.getUser(name);			
		
		session.setAttribute("userr", name);
		session.setAttribute("uid", 1);
		session.setAttribute("userrId", session.getAttribute("userr"));
		session.setAttribute("roleName", roleName);
		
		model.addAttribute("userName", session.getAttribute("userr"));
		model.addAttribute("userId", session.getAttribute("userrId"));
		model.addAttribute("roleName", session.getAttribute("roleName"));		
			
		
		Map<String, Object> modelStr = new HashMap<String, Object>();
				
		if(request.isUserInRole("ROLE_ADMIN")){					
				if(request.getParameter("companyId") == null) {
					List<Company> companyList = (List<Company>)(Object)commonService.getObjectListByAnyColumn("Company", "status", "1");
					pageLocation="selectCompany";	
					modelStr.put("companyList", companyList);
					model.put("companyList", companyList);
					session.setAttribute("company", request.getParameter("companyId"));
					model.addAttribute("company", session.getAttribute("company"));
				} else {
					Company company = (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "id", request.getParameter("companyId"));
					pageLocation="index";
					session.setAttribute("company", company);
					
					OvertimeSummary overtimeSummary = new OvertimeSummary();
					Integer maxId = (Integer) commonService.getMaxValueByObjectAndTwoColumn("OvertimeSummary", "id", "company.id", company.getId().toString());
					
					if(maxId > 0 ) {
						overtimeSummary = (OvertimeSummary) commonService.getAnObjectByAnyUniqueColumn("OvertimeSummary", "id", maxId.toString());
						
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
						Date selectedDate = new Date();
						String otYear =  overtimeSummary.getOtYear();
						String otMonth = overtimeSummary.getOtMonth();
						
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
						
						model.put("year1", year1);
						model.put("year2", year2);
						model.put("year3", year3);
						
						model.put("prevOneMonth", prevOneMonth);
						model.put("prevTwoMonth", prevTwoMonth);
						model.put("prevThreeMonth", prevThreeMonth);
						
						model.put("otYear", otYear);
						model.put("otMonth", otMonth);
					}
				}
				
		} else if(request.isUserInRole("ROLE_USER")) {
			Company company = (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "id", user.getEmployee().getCompany().getId().toString());
			session.setAttribute("company", company);			
			pageLocation="index";	
			
			OvertimeSummary overtimeSummary = new OvertimeSummary();
			Integer maxId = (Integer) commonService.getMaxValueByObjectAndTwoColumn("OvertimeSummary", "id", "company.id", company.getId().toString());
			
			if(maxId > 0 ) {
				overtimeSummary = (OvertimeSummary) commonService.getAnObjectByAnyUniqueColumn("OvertimeSummary", "id", maxId.toString());
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");		
				Date selectedDate = new Date();
				String otYear =  overtimeSummary.getOtYear();
				String otMonth = overtimeSummary.getOtMonth();
				
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
				
				model.put("year1", year1);
				model.put("year2", year2);
				model.put("year3", year3);
				
				model.put("prevOneMonth", prevOneMonth);
				model.put("prevTwoMonth", prevTwoMonth);
				model.put("prevThreeMonth", prevThreeMonth);
				
				model.put("otYear", otYear);
				model.put("otMonth", otMonth);
			}
			
		} else {
			pageLocation="home";					
		}
		/*else if(request.isUserInRole("ROLE_USER")) {
			Company company = (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "id", user.getEmployee().getCompany().getId().toString());
			session.setAttribute("company", company);			
			pageLocation="index";							
		} */
		
		//model.put("otMonth", otMonth);
		
		return new ModelAndView(pageLocation, modelStr);
	}

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Principal principal) {
		if(principal != null) {
			return "redirect:/index";
		}
 		return "login";
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(Model model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		session.invalidate();
 		return "login";
 	}
	
	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	protected String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
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
	
	
}
