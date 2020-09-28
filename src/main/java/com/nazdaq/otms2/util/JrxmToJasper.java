package com.nazdaq.otms2.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;


public class JrxmToJasper {
	public static void main(String[] args) throws JRException {
		// TODO Auto-generated method stub
		
		JasperCompileManager.compileReportToFile(
        		"E:\\git-workspace\\sts_390\\dcimch-otms-v2\\src\\main\\resources\\overtime_summary_report.jrxml", 
        		"E:\\overtime_summary_report.jasper");
     }
	
}
