package validator;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "numTelephoneValidator")
public class NumeroTelephoneValidator implements Validator{

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		String num = String.valueOf(arg2);
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msgs");
		String message = bundle.getString("error.formulaire.telephone");
		FacesMessage fmessage =  new FacesMessage(message);
		if(num.length() < 10)
		{

			throw new ValidatorException(fmessage);
		}
		try
		{
			Integer.parseInt(num);
		}
		catch(NumberFormatException e)
		{
			throw new ValidatorException(fmessage);
		}		
	}

}
