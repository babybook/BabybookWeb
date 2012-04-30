package com.oak.babybook.web.servlets;


import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.Gender;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.UserService;
import com.oak.babybook.utils.Utilities;
import com.oak.babybook.web.services.BabyBookException;

public class UserServiceServlet extends BabyBookServlet {

	private static final long serialVersionUID = -8734055066165488378L;

	@Override
	protected void processRequest() throws BabyBookException {

		UserService userService = this.getUserService();

		try {
			if (command.equalsIgnoreCase(LOAD)){
				String userName = req.getParameter("username");
				String password = req.getParameter("password");

				if (userName==null || password==null) {
					out.print("Error - usernames and passwords must be provided.");
				}

				if (userName.equals("")|| password.equals("")) {
					out.print("Error - usernames and passwords must be provided.");
				}

				User user = userService.getUserByNamePass(userName, password);
				this.putUserInSession(user);

				out.println("User with ID : " + user.getId() + " found and added to session");

			}else if (command.equalsIgnoreCase(ADD)){
				String userName = req.getParameter("username");
				String password = req.getParameter("password");
				String first = req.getParameter("first");
				String middle = req.getParameter("middle");
				String last = req.getParameter("last");
				String email = req.getParameter("email");
				String dob = req.getParameter("dob");
				String gender = req.getParameter("gender");

				User user = new User(null, first, middle, last, Utilities.parseDate(dob), email, Gender.valueOf(gender), userName, password);

				userService.insertUser(user);
				this.putUserInSession(user);

				out.println("User with ID : " + user.getId() + " created and added to session");

				//Forward to create child page ?

			}else if (command.equalsIgnoreCase(UPDATE)){
				String userName = req.getParameter("username");
				String password = req.getParameter("password");
				String first = req.getParameter("first");
				String middle = req.getParameter("middle");
				String last = req.getParameter("last");
				String email = req.getParameter("email");
				String dob = req.getParameter("dob");
				String gender = req.getParameter("gender");

				User user = this.getUserFromSession();
				user.setPassword(password);
				user.setUsername(userName);
				user.setDob(Utilities.parseDate(dob));
				user.setFirst(first);
				user.setMiddle(middle);
				user.setLast(last);
				user.setEmail(email);
				user.setGender(Gender.valueOf(gender));

				this.updateUser(user);

			}else if (command.equalsIgnoreCase(DELETE)){
				User user = this.getUserFromSession();
				this.getUserService().delete(user);

			}else if (command.equalsIgnoreCase(ADD_PICTURE_TO_CHILD)){
				User user = this.getUserFromSession();

				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				Picture picture = this.getPictureService().getPicture(pictureId);

				Long childId = Long.parseLong(req.getParameter("childId"));
				Person child = this.getPersonService().getPerson(childId);

				child.addPicture(picture);
				this.updateUser(user);

			}else if (command.equalsIgnoreCase(ADD_PICTURE_TO_EVENT)){
				User user = this.getUserFromSession();
				Long eventId = Long.parseLong(req.getParameter("eventId"));
				Event event = this.getEventService().getEvent(eventId);

				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				Picture picture = this.getPictureService().getPicture(pictureId);

				event.addPicture(picture);
				this.updateUser(user);

			}else if (command.equalsIgnoreCase(ADD_PICTURE_TO_USER)){
				User user = this.getUserFromSession();
				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				Picture picture = this.getPictureService().getPicture(pictureId);

				user.addPicture(picture);
				this.updateUser(user);

			}else if (command.equalsIgnoreCase(REMOVE_PICTURE_FROM_CHILD)){
				User user = this.getUserFromSession();
				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				Picture picture = this.getPictureService().getPicture(pictureId);

				Long childId = Long.parseLong(req.getParameter("childId"));
				Person child = this.getPersonService().getPerson(childId);

				child.getPictures().remove(picture);
				this.updateUser(user);

			}else if (command.equalsIgnoreCase(REMOVE_PICTURE_FROM_EVENT)){
				User user = this.getUserFromSession();
				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				Picture picture = this.getPictureService().getPicture(pictureId);

				Long eventId = Long.parseLong(req.getParameter("eventId"));
				Event event = this.getEventService().getEvent(eventId);

				event.getPictures().remove(picture);
				this.updateUser(user);

			}else if (command.equalsIgnoreCase(REMOVE_PICTURE_FROM_USER)){
				User user = this.getUserFromSession();
				Long pictureId = Long.parseLong(req.getParameter("pictureId"));

				this.getUserService().removePictureFromUser(user, pictureId);
				this.updateUser(user);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BabyBookException(e);
		}

	}

	@Override
	protected void processXML() throws BabyBookException {


	}
}
