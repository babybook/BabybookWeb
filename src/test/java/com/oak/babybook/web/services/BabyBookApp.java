package com.oak.babybook.web.services;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.EventType;
import com.oak.babybook.objects.Gender;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.EventService;
import com.oak.babybook.services.PersonService;
import com.oak.babybook.services.PictureService;
import com.oak.babybook.services.UserService;

public class BabyBookApp {

	public BabyBookApp() throws Exception {
		System.out.println("Looking for context");
		ApplicationContext context = new ClassPathXmlApplicationContext("babybook-test-context.xml");

		PersonService personService = (PersonService)context.getBean("personService");
		UserService userService = (UserService)context.getBean("userService");
		PictureService pictureService = (PictureService)context.getBean("pictureService");
		EventService eventService = (EventService)context.getBean("eventService");

		User user = new User(null, "Patrick", "Michael", "Callaghan", new Date(), "patrick@oakitfinancial.co.uk", Gender.MALE, "user", "pass");
		userService.insertUser(user);

		Person beth = new Person(null, "Beth", "Anne", "Callaghan", new Date(), "beth@oakitfinancial.co.uk", Gender.FEMALE);
		personService.insertPerson(beth);
		user.addChild(beth);

		System.out.println(beth.getId());

		Person eva = new Person(null, "Eva", "May", "Callaghan", new Date(), "eva@oakitfinancial.co.uk", Gender.FEMALE);
		personService.insertPerson(eva);
		user.addChild(eva);

		Picture pic1 = new Picture(null, "MyPhoto.jpg", "Paris", "Me and the girls in Paris");
		Picture pic2 = new Picture(null, "Pic.gif", "Home", "Playing in front of the fire");

		pictureService.insertPicture(pic1);
		pictureService.insertPicture(pic2);

		user.addPicture(pic1);
		user.addPicture(pic2);

		Event event1 = new Event(null, "My Event", "At home", "Some short description", new Date(), EventType.SAYINGS, null);
		Event event2 = new Event(null, "Another Event", "In the park", "Another short description", new Date(), EventType.FIRST_STEPS, null);
		eventService.insertEvent(event1);
		eventService.insertEvent(event2);


		Picture pic3 = new Picture(null, "WithEvent.gif", "Event Pic", "Picture for the event");
		pictureService.insertPicture(pic3);
		user.addPicture(pic3);

		user.addEvent(event1);
		user.addEvent(event2);

		Event event3 = new Event(null, "Another1 Event", "In the park", "Another short description", new Date(), EventType.FIRST_STEPS, null);
		eventService.insertEvent(event3);
		user.addEvent(event3);
		userService.updateUser(user);

		User loadUser = userService.getUser(1l);
		System.out.println(loadUser);

		try {
			loadUser = userService.getUserByNamePass("user", "pass");
			System.out.println(loadUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			new BabyBookApp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
