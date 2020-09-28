package com.nazdaq.otms2.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nazdaq.otms2.model.Company;
import com.nazdaq.otms2.service.CommonService;
import com.nazdaq.otms2.util.Constants;
import com.nazdaq.otms2.util.ProjectConfigpath;

@Controller
@PropertySource("classpath:common.properties")
public class CompanyController implements Constants {
	private boolean isMultipart;
	private String filePath;
	private int maxFileSize = 50 * 1024;
	private int maxMemSize = 4 * 1024;
	private File file;

	@Autowired
	private CommonService commonService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${cc.email.addresss}")
	String ccEmailAddresss;

	@Value("${common.email.address}")
	String commonEmailAddress;

	@RequestMapping(value = "/newCompanyForm", method = RequestMethod.GET)
	public ModelAndView addCompany(@ModelAttribute("command") Company company, BindingResult result) {
		return new ModelAndView("addCompany");
	}

	@RequestMapping(value = "/saveCompany.htm", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView addCompany(@ModelAttribute("command") Company company, @RequestParam CommonsMultipartFile file,
			HttpSession session, @RequestParam("companyLogo") String imageValue, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Principal principal) {
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		String path = session.getServletContext().getRealPath("/");
		path = "D:\\PayRoleImage";
		String filename = file.getOriginalFilename();

		try {
			if (!file.isEmpty()) {

				this.processarAvatar(company, file);

				company.setCompanyLogo(company.getCompanyKeyword());
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		
		if (request.getParameter("submit_btn").trim().equals("save")) {
			company.setCreatedBy(principal.getName());
			String sDate1 = "31/12/1998";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String dateTime = dateFormat.format(date);
			try {
				Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dateTime);
				company.setCreatedDate(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			redirectAttributes.addFlashAttribute("success", "One new company has been  Added by you.");
		}
		if (company.getId()!=null) {
			company.setModifiedBy(principal.getName());
			//String sDate1 = "31/12/1998";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String dateTime = dateFormat.format(date);
			try {
				Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dateTime);
				company.setModifiedDate(date1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Company company2=(Company) commonService.getAnObjectByAnyUniqueColumn("Company", "id", company.getId().toString());
			company.setCompanyKeyword(company2.getCompanyKeyword());
			if (file.isEmpty()) {
				company.setCompanyLogo(company2.getCompanyLogo());
			}
			redirectAttributes.addFlashAttribute("success", "One  company has been  updated by you.");
		}

		commonService.saveOrUpdateModelObjectToDB(company);
		return new ModelAndView("redirect://companyList");
	}

	private void processarAvatar(Company company, MultipartFile photo) {
		File diretorio = new File("/payrole/upload/companyLogo");
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
		try {
			FileOutputStream arquivo = new FileOutputStream(
					diretorio.getAbsolutePath() + "/" + company.getCompanyKeyword() + ".jpg");
			arquivo.write(photo.getBytes());
			arquivo.close();
		} catch (IOException ex) {

		}

	}

	// methos to company delete
	@RequestMapping(value = "/deleteCompany/{id}", method = RequestMethod.GET)
	public ModelAndView deleteCompany(@PathVariable("id") String id, RedirectAttributes redirectAttributes,Principal principal) {
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		commonService.deleteAnObjectById("Company", Integer.parseInt(id));
		redirectAttributes.addFlashAttribute("success", "One company has been  deleted by you.");
		return new ModelAndView("redirect://companyList");
	}

	@RequestMapping(value = "/editCompany/{id}", method = RequestMethod.GET)
	public ModelAndView editCompany(@ModelAttribute("command") Company company, BindingResult result,@PathVariable("id") String id, ModelMap model,Principal principal) {
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		model.put("company", (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "id", id));
		model.put("edit", true);
		return new ModelAndView("addCompany", model);

	}

	@RequestMapping("/companyLogo/{keyword}")
	@ResponseBody
	public byte[] photo(@PathVariable("keyword") String keyword) throws IOException {
		File arquivo = new File("/payrole/upload/companyLogo/" + keyword + ".jpg");

		if (!arquivo.exists()) {
			arquivo = new File("/payrole/upload/companyLogo/photo.jpg");
		}

		byte[] resultado = new byte[(int) arquivo.length()];
		FileInputStream input = new FileInputStream(arquivo);
		input.read(resultado);
		input.close();
		return resultado;
	}

	@RequestMapping(value = "/checkUnicKEmail", method = RequestMethod.POST)
	private @ResponseBody void checkUnicKEmail(HttpServletRequest request, Principal principal,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, Exception {
		
		String toJson = "";
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Company company = (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "email",
				request.getParameter("email").toString());
		JsonElement reagentobj = gson.toJsonTree(company);
		myObj.add("companyInfo", reagentobj);
		out.println(myObj.toString());

		out.close();

	}

	@RequestMapping(value = "/checkUnicKcompanyKeyword", method = RequestMethod.POST)
	private @ResponseBody void checkUnicKcompanyKeyword(HttpServletRequest request, Principal principal,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, Exception {
		String toJson = "";
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Company company = (Company) commonService.getAnObjectByAnyUniqueColumn("Company", "companyKeyword",
				request.getParameter("companyKeyword").toString());
		JsonElement reagentobj = gson.toJsonTree(company);
		myObj.add("companyInfo", reagentobj);
		out.println(myObj.toString());

		out.close();

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/companyList", method = RequestMethod.GET)
	public ModelAndView companyList(ModelMap model,Principal principal) {
		if(principal == null) {
			return new ModelAndView("redirect:/login");
		}
		List<Company> companies = (List<Company>) (Object) commonService.getAllObjectList("Company");
		model.put("companies", companies);
		return new ModelAndView("companyListAll", model);
	}

}
