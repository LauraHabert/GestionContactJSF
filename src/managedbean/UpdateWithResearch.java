package managedbean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import services.ContactService;
import daos.Adresse;
import daos.Contact;
import daos.Telephone;
import daos.ContactGroupe;
@ManagedBean(name="updateResearch")
@SessionScoped
public class UpdateWithResearch{
	
	private Contact contactAmettreAjour;
	private int id;
	private String nom;
	private String prenom;
	private String email;
	private String numTelephone;
	private String phoneKind;
	private String nomRue;
	private String ville;
	private String cp;
	private String pays;
	private String numSiret;
	private String idGroupe;
	private String nomGroupe;
	private ArrayList<Telephone> listTelephone;
	private ArrayList<ContactGroupe> listGroupe;
	
	public ArrayList<Telephone> getListTelephone() {
		return listTelephone;
	}

	public void setListTelephone(ArrayList<Telephone> listTelephone) {
		this.listTelephone = listTelephone;
	}

	public ArrayList<ContactGroupe> getListGroupe() {
		return listGroupe;
	}

	public void setListGroupe(ArrayList<ContactGroupe> listGroupe) {
		this.listGroupe = listGroupe;
	}

	public String getNumTelephone() {
		return numTelephone;
	}

	public void setNumTelephone(String numTelephone) {
		this.numTelephone = numTelephone;
	}

	public String getPhoneKind() {
		return phoneKind;
	}

	public void setPhoneKind(String phoneKind) {
		this.phoneKind = phoneKind;
	}

	public String getNomRue() {
		return nomRue;
	}

	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getNumSiret() {
		return numSiret;
	}

	public void setNumSiret(String numSiret) {
		this.numSiret = numSiret;
	}

	public String getIdGroupe() {
		return idGroupe;
	}

	public void setIdGroupe(String idGroupe) {
		this.idGroupe = idGroupe;
	}

	public String getNomGroupe() {
		return nomGroupe;
	}

	public void setNomGroupe(String nomGroupe) {
		this.nomGroupe = nomGroupe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Contact getContactAmettreAjour() {
		return contactAmettreAjour;
	}

	public void setContactAmettreAjour(Contact contactAmettreAjour) {
		this.contactAmettreAjour = contactAmettreAjour;
	}
	
	public String maj()
	{
		id = contactAmettreAjour.getId();
		nom = contactAmettreAjour.getNom();
		prenom = contactAmettreAjour.getPrenom();
		email = contactAmettreAjour.getEmail();
		nomRue = contactAmettreAjour.getAdresse().getNomRue();
		ville = contactAmettreAjour.getAdresse().getVille();
		cp = contactAmettreAjour.getAdresse().getCp();
		pays = contactAmettreAjour.getAdresse().getPays();
		Telephone[] t = contactAmettreAjour.getProfiles();
		listTelephone = new ArrayList<Telephone>();
		if(t != null)
		{
			for(int i =0; i<t.length; i++)
				listTelephone.add(t[i]);
		}

		ContactGroupe[] g = contactAmettreAjour.getGroupe();
		listGroupe = new ArrayList<ContactGroupe>();
		if(g != null)
			for(int i =0; i<g.length; i++)
				listGroupe.add(g[i]);
		numSiret = contactAmettreAjour.getNumSiretFormater();
		return("UpdateContactResearch?faces-redirect=true");
	}
	public String majContact()
	{
		Iterator<Telephone> it = listTelephone.iterator();
		Telephone[] t = new Telephone[listTelephone.size()];
		for(int i=0; i<t.length; i++)
		{
			t[i] = it.next();
		}
		Iterator<ContactGroupe> it2 = listGroupe.iterator();
		ContactGroupe[] g = new ContactGroupe[listGroupe.size()];
		for(int i=0; i<g.length; i++)
		{
			g[i] = it2.next();
		}
		Adresse adresse = null;
		
		if(!nomRue.equals("")|| !ville.equals("") || !cp.equals("") || !pays.equals(""))
		{
			if(nomRue.equals(""))
				nomRue = " ";
			if(ville.equals(""))
				ville = " ";
			if(cp.equals(""))
				cp = " ";
			if(pays.equals(" "))
				pays = " ";
			adresse = new Adresse(0, nomRue, ville, cp, pays);
		}
		if(email.equals(""))
			email = " ";
		Contact contact = new Contact(id,nom,prenom,email,adresse,t,g);
		ContactService c = new ContactService();
		if(numSiret != null && !numSiret.equals(""))
		{
			c.updateEntreprise(contact.getId(), Integer.parseInt(numSiret));
		}
		c.updateContact(contact);
		ExternalContext context =FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request =(HttpServletRequest)context.getRequest();
		Research r = (Research) request.getSession().getAttribute("research");
		request.getSession().setAttribute("research",r);
		return r.rechercher();
	}
	public void reset()
	{
		id = contactAmettreAjour.getId();
		nom = contactAmettreAjour.getNom();
		prenom = contactAmettreAjour.getPrenom();
		email= contactAmettreAjour.getEmail();
		numTelephone= "";
		phoneKind= "";
		nomRue = contactAmettreAjour.getAdresse().getNomRue();
		ville = contactAmettreAjour.getAdresse().getVille();
		cp = contactAmettreAjour.getAdresse().getCp();
		pays= contactAmettreAjour.getAdresse().getPays();
		numSiret = ""+contactAmettreAjour.getNumSiret();
		idGroupe= null;
		nomGroupe = "";
		Telephone[] t = contactAmettreAjour.getProfiles();
		listTelephone = new ArrayList<Telephone>();
		if(t != null)
		{
			for(int i =0; i<t.length; i++)
				listTelephone.add(t[i]);
		}

		ContactGroupe[] g = contactAmettreAjour.getGroupe();
		listGroupe = new ArrayList<ContactGroupe>();
		if(g != null)
			for(int i =0; i<g.length; i++)
				listGroupe.add(g[i]);
	}
	public void ajouterTelephone()
	{
		listTelephone.add(new Telephone(phoneKind,numTelephone));
	}
	public void ajouterGroupe()
	{
		listGroupe.add(new ContactGroupe(Integer.parseInt(idGroupe),nomGroupe,null));
	}
	public void supprimerGroupe(ContactGroupe g)
	{
		listGroupe.remove(g);
	}
	public void supprimerTelephone(Telephone t)
	{
		listTelephone.remove(t);
	}
}
