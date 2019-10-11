package managedbean;

import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import daos.Contact;
import daos.Entreprise;
import services.ContactService;

@ManagedBean(name="research")
@SessionScoped
public class Research {
	private String nom;
	private String prenom;
	private ArrayList<Contact> tableau;
	private ArrayList<Entreprise> tableauEntreprise;
	private Contact contactAmettreAjour;
	
	public Contact getContactAmettreAjour() {
		return contactAmettreAjour;
	}

	public void setContactAmettreAjour(Contact contactAmettreAjour) {
		this.contactAmettreAjour = contactAmettreAjour;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public ArrayList<Contact> getTableau() {
		return tableau;
	}

	public void setTableau(ArrayList<Contact> tableau) {
		this.tableau = tableau;
	}

	public String getNumSiretContact(int idContact)
	{
		Iterator<Entreprise> it = tableauEntreprise.iterator();
		while(it.hasNext())
		{
			Entreprise tmp = it.next();
			if(tmp.contactPresent(idContact))
				return ""+tmp.getNumSiret();
		}
		return "";
	}
	public String rechercher()
	{
		ContactService c = new ContactService();
		tableau = c.researchContact(nom,prenom);
		tableauEntreprise = c.getAllEntreprise();
		System.out.println(tableauEntreprise);
		return("AffichageContact?faces-redirect=true");
	}
	
}
