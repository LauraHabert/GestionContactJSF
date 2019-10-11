package validator;

import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "emailAddressValidator")
public class EmailAdresseValidator implements Validator{

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		String email = String.valueOf(arg2);
		StringTokenizer st = new StringTokenizer(email,"@");
		if(st.countTokens()<2)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msgs");
			String message = bundle.getString("error.formulaire.email");
			FacesMessage fmessage =  new FacesMessage(message);
			throw new ValidatorException(fmessage);
		}
		
	}

}
