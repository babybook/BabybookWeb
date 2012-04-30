package com.oak.babybook.web.services;

import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;

public interface BabyBookService {

	public User getUser(String userName, String password);
	
	public void addEventToUser(Event event, User user);
	
	public void addChildToUser(Person person, User user);
	
	public void addPictureToUser(Picture picture, User user);
	
	public void addPictureToEvent(Picture picture, Event event);
	
	public void createUser(User user);
	
	public void updateUser(User user) throws BabyBookException;
	
	public void deleteUser(User user);
	
	public String testCreationCompleteOk(String checkOk);
	
	public void createChild(Person child);
	
	public void createEvent(Event event);
	
	public void createPicture(Picture picture);

	public void addPictureToChild(Picture picture, Person child);
}
