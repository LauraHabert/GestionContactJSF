package managedbean;
import javax.faces.bean.ManagedBean;

import services.ContactService;

@ManagedBean(name="delete")
public class Delete {
	private String id;
	private boolean reussie;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isReussie() {
		return reussie;
	}

	public void setReussie(boolean reussie) {
		this.reussie = reussie;
	}

	public String maj(){
		ContactService c = new ContactService();
		reussie = c.deleteContact(Integer.parseInt(id));
		return("DeleteContactFin.jsf");
	}
}
