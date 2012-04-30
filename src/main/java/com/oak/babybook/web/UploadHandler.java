package com.oak.babybook.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xml.sax.helpers.AttributesImpl;

import com.oak.babybook.objects.Picture;
import com.oak.babybook.objects.User;
import com.oak.babybook.web.services.BabyBookException;
import com.oak.babybook.web.servlets.BabyBookServlet;

public class UploadHandler extends BabyBookServlet {

	private static final int THUMBNAIL_MAXSIZE = 100;
	private static final long serialVersionUID = 7021914907719940832L;
	private static final String CONTENT_TYPE = "text/xml; charset=utf-8";

	@Override
	protected void processRequest() throws BabyBookException {

		String uploadDirectory = this.getServletContext().getInitParameter("UploadDirectory");      // Get the upload directory from the web.xml file.
		String userName = this.getUserFromSession().getId().toString();

		System.out.println("Got username : " + userName);

		String contextPath = this.getServletContext().getRealPath(File.separator);

		ArrayList<String> allowedFormats = new ArrayList<String>();                                 // Allowed image format types are stored in an ArrayList.
		allowedFormats.add("jpeg");
		allowedFormats.add("png");
		allowedFormats.add("gif");
		allowedFormats.add("jpg");

		File disk = null;
		FileItem item = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();                                    // We use the FileUpload package provided by Apache to process the request.
		String statusMessage = "";

		String caption = "", location = "";
		Picture picture;

		ListIterator iterator = null;
		List items = null;
		ServletFileUpload upload = new ServletFileUpload(factory);

		// SAX 2.0 ContentHandler.
		TransformerHandler hd = null;

		try {
			StreamResult streamResult = new StreamResult(out);                                      // Used for writing debug errors to the screen.

			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance(); // SAX XML parsing factory.

			items = upload.parseRequest(this.req);
			iterator = items.listIterator();

			while( iterator.hasNext() )                                                             // Loop over the items in the request.
			{

				item = (FileItem) iterator.next();

				// If the current item is an HTML form field...
				if( item.isFormField() )
				{

					if (item.getFieldName().equalsIgnoreCase("caption")){
						caption = item.getString();                                                  // Get the value and store it.
					}
					if (item.getFieldName().equalsIgnoreCase("location")){
						location = item.getString();                                                  // Get the value and store it.
					}

				} else {  // If the item is a file...
					/*
					 * Use an ImageInputStream to validate the file's format name.
					 * This actually reads the image's internal file format, versus
					 * reading the file extension, which isn't always reliable.
					 */
					ImageInputStream imageInputStream = ImageIO.createImageInputStream(item.getInputStream());
					Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

					ImageReader imageReader = null;

					if(imageReaders.hasNext()){                                                     // Get the next (only) image.
						imageReader = imageReaders.next();
					}

					/*
					 * Non-image files will throw a NullPointerException on the next line.
					 * This code uses a high-level Try/Catch block, but you can use a
					 * more fine-grained exception handling mechanism.
					 */
					String imageFormat = imageReader.getFormatName();

					long milliTime = System.currentTimeMillis();

					String newFileName = userName + "_" + milliTime + "." + imageFormat;                            // New image's filename, concatenation of employee ID and image format.
					String thumbFileName = "thumb_" + userName + "_" + milliTime + "." + imageFormat;                              // New image's filename, concatenation of employee ID and image format.

					if (allowedFormats.contains(imageFormat.toLowerCase())){                        // If the image format is one of the allowed ones...
						/*
						 * Custom FileFilter implements java.io.FilenameFilter.
						 * See FileFilter.java.
						 */
						//FileFilter fileFilter = new FileFilter();
						File dir = new File(contextPath + "//" + uploadDirectory); // Declare and instantiate a FileFilter object.

						if(!dir.exists()){
							dir.mkdir();
						}

						File fileList[] = dir.listFiles();        // Get a filtered list of files from the upload directory.

						disk = new File(contextPath + "//" + uploadDirectory + newFileName);                             // Instantiate a File object for the file to be written.
						File thumbdisk = new File(contextPath + "//" + uploadDirectory + thumbFileName);

						System.out.println("Writing file to " + disk.getAbsolutePath());
						item.write(disk);                                                          // Write the uploaded file to disk.
						//thumbnail.createThumbnail(disk.getAbsolutePath(), thumbdisk.getAbsolutePath(), THUMBNAIL_MAXSIZE);

						picture = new Picture(null, newFileName, location, caption);
						this.getPictureService().insertPicture(picture);
						User user = this.getUserFromSession();
						user.addPicture(picture);
						this.updateUser(user);

						/*
						 * Get a Calendar object and fetch the current time from it.
						 */
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd.yy hh:mm:ss aaa");
						statusMessage = "File successfully written to " + disk.getAbsolutePath() + " at " + simpleDateFormat.format(calendar.getTime());
						out.println(statusMessage);
					}

					/*
					 * If you're processing multiple files, you'd place
					 * these lines outside of the loop.
					 */
					imageReader.dispose();
					imageInputStream.close();
				}
			}
		}
		/*
		 * Some very basic exception handling.
		 * 
		 */
		catch (Exception e){
			throw new BabyBookException(e);
		}
	}

	@Override
	protected void processXML() throws BabyBookException {
		// TODO Auto-generated method stub

	}
}
