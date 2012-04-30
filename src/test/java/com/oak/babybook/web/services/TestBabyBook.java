package com.oak.babybook.web.services;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.Assert;


import org.apache.log4j.Logger;

import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.EventType;
import com.oak.babybook.objects.Gender;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;


public abstract class TestBabyBook extends TestCase {

	protected Logger log = Logger.getLogger(this.getClass());

	abstract protected BabyBookService getServiceImpl() throws MalformedURLException;

	/*
	 * Test that the Web service is up and can be connected to.
	 */
	public void testBabyBookWebService() throws MalformedURLException,
	Exception {

		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		String response = "Not OK";
		try {
			response = client.testCreationCompleteOk("isOK");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			log.error("Response : " + response);
			e.printStackTrace();
		}

		Assert.assertEquals("OK", response);
	}

	/**
	 * Test that we can retrieve a known user
	 */
	public void testGetUser(){
		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		User user = null;
		try {
			user = client.getUser("user", "pass");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			e.printStackTrace();
		}

		Assert.assertEquals("Patrick", user.getFirst());
		Assert.assertEquals(2, user.getChildren().size());
		Assert.assertEquals(3, user.getEvents().size());
	}

	/**
	 * Test that we can retrieve a known user
	 */
	public void testAddChild(){
		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		User user = null;
		try {
			user = client.getUser("user", "pass");
			Person child = new Person(-1l, "Ava", "May", "Callaghan", new Date(), "ava@gmail.com", Gender.FEMALE );

			log.info("*********** Adding child to user **************");
			client.addChildToUser(child, user);
			log.info("*********** Child added **************");

			client.updateUser(user);
			user = null;
			user = client.getUser("user", "pass");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			e.printStackTrace();
		}

		Assert.assertEquals("Patrick", user.getFirst());
		Assert.assertEquals(3, user.getChildren().size());
		Assert.assertEquals(3, user.getEvents().size());
	}


	/**
	 * Test that we can retrieve a known user
	 */
	public void testAddEvent(){
		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		User user = null;
		try {
			user = client.getUser("user", "pass");
			Event event = new Event(-1l, "Birth", "54a Stephendale road", "Started at 4am and gave birth at 9.45am", new Date(), EventType.BIRTH, null);

			log.info("*********** Adding event to user **************");
			client.addEventToUser(event, user);
			log.info("*********** Event added **************");

			client.updateUser(user);
			user = null;
			user = client.getUser("user", "pass");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			e.printStackTrace();
		}

		Assert.assertEquals("Patrick", user.getFirst());
		Assert.assertEquals(2, user.getChildren().size());
		Assert.assertEquals(4, user.getEvents().size());
	}

	public void testAddPictureToEvent(){
		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		User user = null;
		try {
			user = client.getUser("user", "pass");
			Picture picture = new Picture(null, "AnotherPhoto.jpg", "picts/afafd/", "another lovely photo");

			Set<Event> events = user.getEvents();

			for (Event event : events){

				if (event.getName().equals("Another Event")){
					log.info("*********** Adding picture to event **************");
					client.addPictureToEvent(picture, event);
					log.info("*********** Picture added **************");
				}
			}
			client.updateUser(user);
			user = null;
			user = client.getUser("user", "pass");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			e.printStackTrace();
		}

		Assert.assertEquals("Patrick", user.getFirst());
		Assert.assertEquals(2, user.getChildren().size());
		Assert.assertEquals(3, user.getEvents().size());

		Set<Event> events = user.getEvents();

		for (Event event : events){

			if (event.getName().equals("Another Event")){
				Assert.assertEquals(1, event.getPictures().size());
				log.info(event);
			}
		}
	}

	public void testAddPictureToPerson(){
		BabyBookService client = null;
		try {
			client = this.getServiceImpl();
		} catch (MalformedURLException e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			Assert.fail();
		}

		// Invoke the service
		User user = null;
		try {
			user = client.getUser("user", "pass");
			Picture picture = new Picture(null, "AnotherPhoto.jpg", "picts/afafd/", "another lovely photo");

			Set<Person> children = user.getChildren();

			for (Person child : children){

				if (child.getFirst().equals("Beth")){
					log.info("*********** Adding picture to event **************");
					client.addPictureToChild(picture, child);
					log.info("*********** Picture added **************");
				}
			}
			client.updateUser(user);
			user = null;
			user = client.getUser("user", "pass");
		} catch (Exception e) {
			log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
			e.printStackTrace();
		}

		Assert.assertEquals("Patrick", user.getFirst());
		Assert.assertEquals(2, user.getChildren().size());
		Assert.assertEquals(3, user.getEvents().size());

		Set<Person> children= user.getChildren();

		for (Person child : children){

			if (child.getFirst().equals("Beth")){
				Assert.assertEquals(1, child.getPictures().size());
				log.info(child);
			}
		}
	}
}
