package com.nazdaq.otms2.util;

import java.util.Calendar;
import java.util.Date;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Date now = new Date();
		//Calendar calc = Calendar.getInstance();
		//calc.setTime(now);
		//Integer currentYear = calc.get(Calendar.YEAR);
		TestMain test = new TestMain();
		Integer num = test.myMathTest(3);
		System.out.println(num);
	}
	
	private Integer myMathTest(Integer x) {
		return ((x*x)-9)/(x-3);
	}

}
