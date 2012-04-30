package com.oak.babybook.web.services;

import java.net.MalformedURLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oak.babybook.web.services.impl.BabyBookServiceImpl;

public class TestBabyBookService extends TestBabyBook{

	static ApplicationContext context = new ClassPathXmlApplicationContext("babybook-test-context.xml");
	final private BabyBookService babyBookServivce = new BabyBookServiceImpl(context);

	@Override
	public void setUp(){
		try {
			log.info("*************** Creating Test Data ********************");
			new BabyBookApp();
			log.info("*************** Finished creating Test Data ********************");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected BabyBookService getServiceImpl() throws MalformedURLException {
		// TODO Auto-generated method stub
		return babyBookServivce;
	}
}
