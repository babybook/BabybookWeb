package com.oak.babybook.web.servlets;


import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;
import com.oak.babybook.services.PictureService;
import com.oak.babybook.web.services.BabyBookException;

public class PictureServiceServlet extends BabyBookServlet {

	private static final long serialVersionUID = -949102764857720954L;

	@Override
	protected void processRequest() throws BabyBookException {
		try{
			Picture picture = new Picture();
			PictureService pictureService = this.getPictureService();

			User user = this.getUserFromSession();

			if (command.equalsIgnoreCase(ADD)){
				picture.setId(null);
				picture.setCaption(req.getParameter("caption"));
				picture.setName(req.getParameter("name"));
				picture.setLocation(req.getParameter("location"));

				pictureService.insertPicture(picture);
				user.addPicture(picture);

			}else if (command.equalsIgnoreCase(UPDATE)){

				Long pictureId = Long.parseLong(req.getParameter("pictureId"));
				picture = pictureService.getPicture(pictureId);

				picture.setCaption(req.getParameter("caption"));
				picture.setName(req.getParameter("name"));
				picture.setLocation(req.getParameter("location"));

				pictureService.update(picture);

			}else if (command.equalsIgnoreCase(DELETE)){
				Long pictureId = Long.parseLong(req.getParameter("pictureId"));

				this.getUserService().removePictureFromUser(user, pictureId);
				picture = pictureService.getPicture(pictureId);
				pictureService.delete(picture);

			}

		}catch (Exception e){
			throw new BabyBookException(e);
		}
	}

	@Override
	protected void processXML() throws BabyBookException {


	}
}
