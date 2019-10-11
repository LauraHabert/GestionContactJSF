package managedbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name="login")
public class Login {
	private String name;
	private String password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String connexion(){
		FacesContext context = FacesContext.getCurrentInstance(); 
		if(!name.equals(password))
		{
			context.addMessage("password", new FacesMessage("Identifiant ou mot de passe incorrect"));
			return(null);
		}
		return("Main.jsf?faces-redirect=true");
	}
}
