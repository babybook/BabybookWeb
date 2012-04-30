package com.oak.babybook.web.servlets;


import com.oak.babybook.web.services.BabyBookException;

public class PersonServiceServlet extends BabyBookServlet {

	private static final long serialVersionUID = -6185272056519966866L;

	@Override
	protected void processRequest() throws BabyBookException {
		if (command.equalsIgnoreCase(ADD)){


		}else if (command.equalsIgnoreCase(UPDATE)){


		}else if (command.equalsIgnoreCase(DELETE)){

		}


	}

	@Override
	protected void processXML() throws BabyBookException {
		// TODO Auto-generated method stub

	}

}
