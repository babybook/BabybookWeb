package com.oak.babybook.web;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) {
		doPost(request, resp);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) {
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		try {
			List items = upload.parseRequest(request);

			// Process the uploaded items
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();

					System.out.println("Name : " + name + " Value : " + value);
				} else {
					// Process a file upload
					File uploadedFile = new File(request.getContextPath()
							+ "Photo.jpg");
					item.write(uploadedFile);
				}
			}

		} catch (FileUploadException fue) {
			fue.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
