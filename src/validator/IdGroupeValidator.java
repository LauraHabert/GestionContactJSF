package validator;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "idGroupeValidator")
public class IdGroupeValidator implements Validator{

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		//On récupère la valeur du champs
		String num = String.valueOf(arg2);
		
		//On récupère le message d'erreur
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msgs");
		String message = bundle.getString("error.formulaire.idGroupe");
		FacesMessage fmessage =  new FacesMessage(message);
		
		//On vérifie que la valeur est bien un entier
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
