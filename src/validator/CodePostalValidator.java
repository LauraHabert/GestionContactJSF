package validator;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "cpValidator")
public class CodePostalValidator implements Validator{

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		
		try
		{
			String cp = String.valueOf(arg2);
			Integer.parseInt(cp);
		}
		catch(NumberFormatException e)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msgs");
			String message = bundle.getString("error.formulaire.cp");
			FacesMessage fmessage =  new FacesMessage(message);
			throw new ValidatorException(fmessage);
		}

		
	}

}
