package com.oak.babybook.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.oak.babybook.objects.User;
import com.oak.babybook.services.EventService;
import com.oak.babybook.services.PersonService;
import com.oak.babybook.services.PictureService;
import com.oak.babybook.services.UserService;
import com.oak.babybook.services.impl.UserServiceImpl;
import com.oak.babybook.web.services.BabyBookException;
import com.oak.babybook.web.services.BabyBookService;
import com.oak.babybook.web.services.impl.BabyBookServiceImpl;

@SuppressWarnings("serial")
public abstract class BabyBookServlet extends HttpServlet {

	protected Logger log = Logger.getLogger(this.getClass());

	static final String LOGIN = "login";
	static final String ADD = "add";
	static final String UPDATE = "update";
	static final String LOAD = "load";
	static final String DELETE = "delete";
	static final String ADD_PICTURE_TO_USER = "addPictureToUser";
	static final String ADD_PICTURE_TO_EVENT = "addPictureToEvent";
	static final String ADD_PICTURE_TO_CHILD = "addPictureToChild";
	static final String REMOVE_PICTURE_FROM_USER = "removePictureFromUser";
	static final String REMOVE_PICTURE_FROM_EVENT = "removePictureFromEvent";
	static final String REMOVE_PICTURE_FROM_CHILD = "removePictureFromChild";

	private static final String COMMAND = "command";
	private static final String XML = "xml";
	private static ApplicationContext context;

	protected String command;
	protected String xml;
	protected ServletOutputStream out;
	protected HttpServletRequest req;
	protected StringBuffer response;
	protected BabyBookService babyBookService;;

	@Override
	public void init(){
		context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		babyBookService = new BabyBookServiceImpl(context);
		System.out.println("Context created");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		this.service(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		this.service(req, resp);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		this.req = req;
		this.response = new StringBuffer();
		this.command = req.getParameter(BabyBookServlet.COMMAND);
		this.xml = req.getParameter(BabyBookServlet.XML);
		this.out = resp.getOutputStream();

		//Check there is a user, if not direct to login screen.


		try {

			this.processRequest();

		} catch (BabyBookException e) {
			handleError(e);
		}

		this.out.println(response.toString());
	}

	private void handleError(BabyBookException e) throws IOException {
		response.append("<html><body><error>" + e.getMessage() + "</error></body></html>");
		System.out.println(e.getMessage());
		e.printStackTrace();
	}

	protected User getUserFromSession(){

		Object object = req.getSession().getAttribute("USER");

		if (object != null){
			return (User) object;
		}else{
			return null;
		}
	}

	protected void updateUser(User user) {
		UserService userService = this.getUserService();
		userService.updateUser(user);

		req.getSession().setAttribute("USER", user);
	}

	protected UserService getUserService(){

		UserService userService = (UserServiceImpl)context.getBean("userService");
		return userService;
	}

	protected PictureService getPictureService(){
		PictureService pictureService = (PictureService)context.getBean("pictureService");
		return pictureService;
	}

	protected EventService getEventService(){
		EventService eventService = (EventService)context.getBean("eventService");
		return eventService;
	}

	protected PersonService getPersonService(){
		PersonService personService = (PersonService)context.getBean("personService");
		return personService;
	}

	protected void putUserInSession(User user){
		req.getSession().setAttribute("USER", user);
	}

	protected abstract void processRequest() throws BabyBookException;

	protected abstract void processXML() throws BabyBookException;

	protected void removeChildFromUser(Long personId) {

		User user = this.getUserFromSession();

		getUserService().removeChildFromUser(user, personId);
	}

	protected void removeEventFromUser(Long eventId) {

		User user = this.getUserFromSession();

		getUserService().removeEventFromUser(user, eventId);
	}

	protected void removePictureFromUser(Long pictureId) {
		User user = this.getUserFromSession();

		getUserService().removePictureFromUser(user, pictureId);
	}

	protected Long convertStringToLong(String value) throws Exception{

		try{
			Long newLong = Long.parseLong(value);

			return newLong;
		}catch (Exception e){
			log.error("Could not parse " + value + " int Long.");
			throw e;
		}
	}
}
