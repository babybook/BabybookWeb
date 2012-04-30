package com.oak.babybook.web.servlets;

import com.oak.babybook.objects.Event;
import com.oak.babybook.objects.EventType;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.EventService;
import com.oak.babybook.utils.Utilities;
import com.oak.babybook.web.services.BabyBookException;

public class EventServiceServlet extends BabyBookServlet {

	@Override
	protected void processRequest() throws BabyBookException {

		Event event = new Event();
		EventService eventService = this.getEventService();

		User user = this.getUserFromSession();

		try {
			if (command.equalsIgnoreCase(ADD)) {
				String dateTime = req.getParameter("dateTime");
				String name = req.getParameter("name");
				String location = req.getParameter("location");
				String description = req.getParameter("description");
				String eventType = req.getParameter("eventType");
				String other = req.getParameter("other");

				event = new Event(null, name, location, description, Utilities.parseDate(dateTime), EventType
						.valueOf(eventType), other);

				eventService.insertEvent(event);
				user.addEvent(event);
				this.updateUser(user);

				out.println("Event with ID : " + event.getId() + " created and added to user with Id " + user.getId());


			} else if (command.equalsIgnoreCase(UPDATE)) {

				Long eventId = Long.parseLong(req.getParameter("eventId"));

				event = eventService.getEvent(eventId);

				String dateTime = req.getParameter("dateTime");
				String name = req.getParameter("name");
				String location = req.getParameter("location");
				String description = req.getParameter("description");
				String eventType = req.getParameter("eventType");
				String other = req.getParameter("other");

				event.setDateTime(Utilities.parseDate(dateTime));
				event.setDescription(description);
				event.setLocation(location);
				event.setName(name);
				event.setOther(other);
				event.setType(EventType.valueOf(eventType));

				eventService.update(event);

			} else if (command.equalsIgnoreCase(DELETE)) {

				Long eventId = Long.parseLong(req.getParameter("eventId"));

				this.getUserService().removeEventFromUser(user, eventId);
				event = eventService.getEvent(eventId);
				eventService.delete(event);
			}

		} catch (Exception e) {
			throw new BabyBookException(e);
		}

	}

	@Override
	protected void processXML() throws BabyBookException {
		// TODO Auto-generated method stub

	}

}
