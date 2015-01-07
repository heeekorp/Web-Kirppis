package com.kirppis.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator("FileUploadValidator")
public class FileUploadValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {

		Part part = (Part) value;

		// 1. validate file name length
		String fileName = getFileName(part);
		System.out.println("----- validator fileName: " + fileName);
		if(fileName.length() == 0 ) {
			FacesMessage message = new FacesMessage("Virhe! Ei valittua tedostoa !!");
			throw new ValidatorException(message);
		}
                
		// 2. validate file type (image/jpeg, image/gif, image/png files allowed)
		if (!"image/jpeg".equals(part.getContentType()) && !"image/gif".equals(part.getContentType()) && !"image/png".equals(part.getContentType())) {
			FacesMessage message = new FacesMessage("Virhe! Tiedostotyyppi ei kelpaa !!" + part.getContentType());
			throw new ValidatorException(message);
		  }
	}

	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		System.out.println("----- validator partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return "";
	}
}