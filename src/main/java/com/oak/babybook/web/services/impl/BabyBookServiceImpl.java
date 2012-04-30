package com.oak.babybook.web.services.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.EventService;
import com.oak.babybook.services.PersonService;
import com.oak.babybook.services.PictureService;
import com.oak.babybook.services.UserService;
import com.oak.babybook.web.services.BabyBookService;

public class BabyBookServiceImpl implements BabyBookService {

	private EventService eventService;
	private PictureService pictureService;
	private UserService userService;
	private PersonService personService;

	public BabyBookServiceImpl(ApplicationContext context) {

		personService = (PersonService)context.getBean("personService");
		userService = (UserService)context.getBean("userService");
		pictureService = (PictureService)context.getBean("pictureService");
		eventService = (EventService)context.getBean("eventService");
	}

	@Override
	public void addChildToUser(Person child, User user) {
		this.personService.insertPerson(child);
		user.addChild(child);
	}

	@Override
	public void addEventToUser(Event event, User user) {
		this.eventService.insertEvent(event);
		user.addEvent(event);
	}

	@Override
	public void createUser(User user) {
		userService.insertUser(user);
	}

	@Override
	public User getUser(String userName, String password) {
		try {
			return userService.getUserByNamePass(userName, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String testCreationCompleteOk(String checkOk) {
		return "OK";
	}

	@Override
	public void addPictureToEvent(Picture picture, Event event) {
		this.pictureService.insertPicture(picture);
		event.addPicture(picture);
		eventService.update(event);
	}

	@Override
	public void addPictureToUser(Picture picture, User user) {
		this.pictureService.insertPicture(picture);
		user.addPicture(picture);
		userService.updateUser(user);
	}

	@Override
	public void addPictureToChild(Picture picture, Person child) {
		this.pictureService.insertPicture(picture);
		child.addPicture(picture);
		personService.update(child);
	}

	@Override
	public void deleteUser(User user) {
		this.userService.delete(user);
	}

	@Override
	public void updateUser(User user) {
		this.userService.updateUser(user);
	}

	@Override
	public void createChild(Person child) {
		this.personService.insertPerson(child);
	}

	@Override
	public void createEvent(Event event) {
		this.eventService.insertEvent(event);
	}

	@Override
	public void createPicture(Picture picture) {
		this.pictureService.insertPicture(picture);
	}
}
