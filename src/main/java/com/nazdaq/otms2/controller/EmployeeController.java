package com.nazdaq.otms2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nazdaq.otms2.model.Company;
import com.nazdaq.otms2.model.Department;
import com.nazdaq.otms2.model.Designation;
import com.nazdaq.otms2.model.Employee;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.service.UserService;

@Controller
@PropertySource("classpath:common.properties")
public class EmployeeController {
	
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/employeeForm", method = RequestMethod.GET)
	public ModelAndView getNewEmployeeForm (@ModelAttribute("employeeForm") Employee emp, 
			BindingResult result, Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		List<Department> departmentList = (List<Department>)(Object)commonService.getAllObjectList("Department");
		List<Designation> designationList = (List<Designation>)(Object)commonService.getAllObjectList("Designation");
		List<Company> companyList = (List<Company>)(Object)commonService.getAllObjectList("Company");
		
		model.put("departmentList", departmentList);
		model.put("designationList", designationList);
		model.put("companyList", companyList);
		model.put("employee", null);
		return new ModelAndView("employeeForm", model);
	}
	
	
	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateEmployee (@ModelAttribute("employeeForm") Employee emp, 
			BindingResult result, Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		Department department = (Department)
				commonService.getAnObjectByAnyUniqueColumn("Department", "id", emp.getDepartmentId().toString());
		Designation designation = (Designation)
				commonService.getAnObjectByAnyUniqueColumn("Designation", "id", emp.getDesignationId().toString());
		
		Company company = (Company)
				commonService.getAnObjectByAnyUniqueColumn("Company", "id", emp.getCompanyId().toString());
		
		if(emp.getId() != null) {
			Employee employee = (Employee) 
					commonService.getAnObjectByAnyUniqueColumn("Employee", "id", emp.getId().toString());
			//update field start
			employee.setName(emp.getName());
			employee.setJoinDate(emp.getJoinDate());
			employee.setBankAccNo(emp.getBankAccNo());
			employee.setWorkingHours(emp.getWorkingHours());
			employee.setGrossSalary(emp.getGrossSalary());
			employee.setDepartment(department);
			employee.setDesignation(designation);
			employee.setActive(emp.getActive());
			employee.setRemarks(emp.getRemarks());
			employee.setFixedUnit(emp.getFixedUnit());
			
			employee.setCompany(company);
			//update field end			
			employee.setModifiedBy(principal.getName());
			employee.setModifiedDate(new Date());
			commonService.saveOrUpdateModelObjectToDB(employee);
			return new ModelAndView("redirect:/employeeList", model);
		}else {
			
			emp.setCreatedBy(principal.getName());
			emp.setCreatedDate(new Date());
			emp.setDepartment(department);
			emp.setDesignation(designation);
			emp.setCompany(company);
			commonService.saveOrUpdateModelObjectToDB(emp);
			return new ModelAndView("redirect:/employeeList", model);
		}		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/employeeList", method = RequestMethod.GET)
	public ModelAndView getEmployeeList (Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		List<Employee> employeeList = (List<Employee>)(Object)
				commonService.getObjectListByAnyColumn("Employee", "active", "1");
		Map <String, Object> model = new HashMap<String, Object>();
		model.put("employeeList", employeeList);
		return new ModelAndView("employeeList", model);
	} 
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editEmployee", method = RequestMethod.GET)
	public ModelAndView editEmployee (@ModelAttribute("employeeForm") Employee emp, 
			BindingResult result, Principal principal){
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		
		Map <String, Object> model = new HashMap<String, Object>();
		List<Department> departmentList = (List<Department>)(Object)commonService.getAllObjectList("Department");
		List<Designation> designationList = (List<Designation>)(Object)commonService.getAllObjectList("Designation");
		List<Company> companyList = (List<Company>)(Object)commonService.getAllObjectList("Company");
		Employee employee = (Employee) 
				commonService.getAnObjectByAnyUniqueColumn("Employee", "id", emp.getId().toString());
		
		model.put("companyList", companyList);
		model.put("departmentList", departmentList);
		model.put("designationList", designationList);
		model.put("employee", employee);
		model.put("edit", true);
		return new ModelAndView("employeeForm", model);
	}
	
	@RequestMapping(value = "/employeeUpdateForm", method = RequestMethod.GET)
	public ModelAndView employeeUpdateForm (@ModelAttribute("employeeForm") Employee emp, 
			BindingResult result, Principal principal){		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}		
		Map <String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("employeeUpdateForm", model);
	}
	
	
	@RequestMapping(value = "/employeeGSUpload", method = RequestMethod.POST)
	public ModelAndView employeeGSUpload (@ModelAttribute("employeeForm") Employee emp, 
			BindingResult result, Principal principal, @RequestParam(value="excelFile", required=true) MultipartFile excelFile, @RequestParam(value="firstRowIgnore", required=true) Integer firstRowIgnore) throws IOException{
		
		
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		String loginUserName = principal.getName();
		if (!excelFile.isEmpty()) {	
			try {
				//int i = firstRowIgnore;
				// Creates a workbook object from the uploaded excelfile
				HSSFWorkbook workbook = new HSSFWorkbook(excelFile.getInputStream());
				// Creates a worksheet object representing the first sheet
				HSSFSheet worksheet = workbook.getSheetAt(0);
				Date now = new Date();
				// Reads the data in excel file until last row is encountered
				while (firstRowIgnore <= worksheet.getLastRowNum()) {
					HSSFRow row = worksheet.getRow(firstRowIgnore++);
					Double empId = row.getCell(0).getNumericCellValue();
					Employee employee = (Employee) commonService.getAnObjectByAnyUniqueColumn("Employee", "empId", (empId.intValue())+"");
										
						if(employee != null) {
							Double grossSalary = row.getCell(1).getNumericCellValue();
							employee.setGrossSalary(grossSalary.doubleValue());
							employee.setModifiedBy(loginUserName);
							employee.setModifiedDate(now);
							commonService.saveOrUpdateModelObjectToDB(employee);							
						}
					
											
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return new ModelAndView("redirect:/employeeUpdateForm");
			}
		}

		
		Map <String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("redirect:/employeeList", model);
	}
	
	@RequestMapping(value = "/downloadEmpSample", method = RequestMethod.GET)
	public @ResponseBody void downloadEmpSample(HttpServletRequest request, HttpServletResponse response)throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("employee");  

            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.createCell(0).setCellValue("employee_id");
            rowhead.createCell(1).setCellValue("gross_salary");

            HSSFRow row = sheet.createRow((short)1);
            row.createCell(0).setCellValue("2315");
            row.createCell(1).setCellValue(10200);
            
            HSSFRow row1 = sheet.createRow((short)2);
            row1.createCell(0).setCellValue("2535");
            row1.createCell(1).setCellValue(15000);
	        
	       response.reset();
	       response.setContentType("application/vnd.ms-excel");
	       response.setHeader("Content-Disposition", "attachment; filename=\"employee.xls");
	       workbook.write(response.getOutputStream());	        
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
