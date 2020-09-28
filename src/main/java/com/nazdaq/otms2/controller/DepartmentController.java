package com.nazdaq.otms2.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.otms2.model.Department;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.util.Constants;

@Controller
@PropertySource("classpath:common.properties")
public class DepartmentController implements Constants{

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${cc.email.addresss}")
	String ccEmailAddresss;
	
	@Value("${common.email.address}")
	String commonEmailAddress;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newDeptForm.htm", method = RequestMethod.GET)
	public ModelAndView departmentForm (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Department> departmentList = (List<Department>)(Object)
				commonService.getAllObjectList("Department");		
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("departmentList", departmentList);		
		return new ModelAndView("departmentForm", model);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editDepartment.htm", method = RequestMethod.GET)
	public ModelAndView editDepartment (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		List<Department> departmentList = (List<Department>)(Object)
				commonService.getAllObjectList("Department");	
		Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "id", departmentBean.getId().toString());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("departmentList", departmentList);
		model.put("edit", true);
		model.put("department", department);
		return new ModelAndView("departmentForm", model);		
	}
	
	@RequestMapping(value = "/saveDepartment.htm", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateDepartment (@ModelAttribute("departmentForm") Department departmentBean, Principal principal) {	
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();	
		
		if(departmentBean.getId() != null) {
			Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "id", departmentBean.getId().toString());
			Department departmentbyName = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "name", departmentBean.getName().toString());
			if(departmentbyName != null) {
				if(!departmentbyName.getId().toString().equals(department.getId().toString())) {
					model.put("message", "newDeptForm.htm");
					return new ModelAndView("failed", model);
				}
				
			}else {
				department.setName(departmentBean.getName());
				commonService.saveOrUpdateModelObjectToDB(department);
			}
		} else {
			Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "name", departmentBean.getName().toString());
			if(department != null) {
				model.put("message", "newDeptForm.htm");
				return new ModelAndView("failed", model);
			} else {
				commonService.saveOrUpdateModelObjectToDB(departmentBean);
			}
		}
		
		
		model.put("message", "newDeptForm.htm");
		return new ModelAndView("success", model);
	}
	
	@RequestMapping(value = "/deleteDepartment.htm", method = RequestMethod.GET)
	public ModelAndView deleteDepartment (Department departmentBean, Principal principal) {		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Department", departmentBean.getId());
		Map <String, Object> model = new HashMap<String, Object>();		
		model.put("message", "newDeptForm.htm");
		return new ModelAndView("success", model);		
	}	
	
	@RequestMapping(value = "/checkDepartment.htm", method = RequestMethod.POST)
	 public @ResponseBody String checkDepartmentExists(HttpServletRequest request, HttpServletResponse response, Principal principal)throws Exception {
		String text = "true";		
		if(request.getParameter("name") != null){
			Department department = (Department)commonService.getAnObjectByAnyUniqueColumn("Department", "name", request.getParameter("name").toString());
			if(department != null){ 
				text = "false";
			}
		}	 
		return text;
	 }
	
}