package com.oak.babybook.web.servlets;

import com.oak.babybook.objects.Gender;
import com.oak.babybook.objects.Person;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.PersonService;
import com.oak.babybook.utils.Utilities;
import com.oak.babybook.web.services.BabyBookException;

public class ChildServiceServlet extends BabyBookServlet {

	private static final long serialVersionUID = 5595776612345668457L;

	@Override
	protected void processRequest() throws BabyBookException {

		try {
			PersonService personService = this.getPersonService();
			Person child = new Person();
			User user = this.getUserFromSession();

			if (command.equalsIgnoreCase(ADD)){
				String first = req.getParameter("first");
				String middle = req.getParameter("middle");
				String last = req.getParameter("last");
				String dob = req.getParameter("dob");
				String email = req.getParameter("email");
				String gender = req.getParameter("gender");

				child = new Person(null, first, middle, last, Utilities.parseDate(dob), email, Gender.valueOf(gender));
				personService.insertPerson(child);

				user.addChild(child);
				this.updateUser(user);

				out.println("Child with ID : " + child.getId() + " created and added to user with Id " + user.getId());

			}else if (command.equalsIgnoreCase(UPDATE)){

				Long childId = Long.parseLong(req.getParameter("childId"));
				child = personService.getPerson(childId);

				String first = req.getParameter("first");
				String middle = req.getParameter("middle");
				String last = req.getParameter("last");
				String dob = req.getParameter("dob");
				String email = req.getParameter("email");
				String gender = req.getParameter("gender");

				child.setFirst(first);
				child.setMiddle(middle);
				child.setLast(last);
				child.setDob(Utilities.parseDate(dob));
				child.setEmail(email);
				child.setGender(Gender.valueOf(gender));

				personService.update(child);

			}else if (command.equalsIgnoreCase(DELETE)){
				Long personId = this.convertStringToLong("personId");
				this.removeChildFromUser(personId);
				child = personService.getPerson(personId);
				personService.delete(child);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected void processXML() throws BabyBookException {
		// TODO Auto-generated method stub

	}

}
