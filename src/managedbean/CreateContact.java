package managedbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import services.ContactService;
import daos.Adresse;
import daos.Contact;
import daos.ContactGroupe;
import daos.Entreprise;
import daos.Telephone;

@ManagedBean(name="createcontact")
@ViewScoped
public class CreateContact {
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
	private ArrayList<ContactGroupe> listeGroupe;
	private ArrayList<Telephone> listeTelephone;
	private boolean contactCree;
	
	public ArrayList<Telephone> getListeTelephone() {
		return listeTelephone;
	}
	public void setListeTelephone(ArrayList<Telephone> listeTelephone) {
		this.listeTelephone = listeTelephone;
	}
	public ArrayList<ContactGroupe> getListeGroupe() {
		return listeGroupe;
	}
	public void setListeGroupe(ArrayList<ContactGroupe> listeGroupe) {
		this.listeGroupe = listeGroupe;
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
	
	@PostConstruct
	public void initialisationListe()
	{
		listeGroupe = new ArrayList<ContactGroupe>();
		listeTelephone = new ArrayList<Telephone>();
	}
	public void ajouterGroupe()
	{	
		ContactGroupe c = new ContactGroupe(Integer.parseInt(idGroupe), nomGroupe, null);
		if(groupeNotInList(c))
			listeGroupe.add(c);
	}
	
	public boolean groupeNotInList(ContactGroupe c)
	{
		Iterator<ContactGroupe> i = listeGroupe.iterator();
		while(i.hasNext())
		{
			if(c.equals(i.next()))
				return false;
		}
		return true;
	}
	public void supprimer(ContactGroupe groupe)
	{
		listeGroupe.remove(groupe);
	}
	
	public void ajouterTelephone()
	{
		if(numeroTelephoneValide(numTelephone))
		{
			Telephone t = new Telephone(0,phoneKind,numTelephone,null);
			if(telephoneNotInList(t))
				listeTelephone.add(t);
		}
	}
	
	public void supprimer(Telephone t)
	{
		listeTelephone.remove(t);
	}
	
	public boolean telephoneNotInList(Telephone t)
	{
		Iterator<Telephone> i = listeTelephone.iterator();
		while(i.hasNext())
		{
			if(t.equals(i.next()))
				return false;
		}
		return true;
	}
	
	public boolean controleListeTelephone(String listeTelephone)
	{
		if(!listeTelephone.equals(""))
		{
			JSONParser j = new JSONParser();
			try {
				JSONArray json = (JSONArray) j.parse(listeTelephone);
				for(int i=0; i<json.size(); i++)
				{
					JSONObject o = (JSONObject) json.get(i);
					if(!numeroTelephoneValide((String) o.get("num")))
						return false;
				}
			} catch (Exception e){
				return false;
			}
		}
		return true;
	}
	
	public boolean numeroTelephoneValide(String num)
	{
		if(num.length() < 10)
			return false;
		try
		{
			Integer.parseInt(num);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	public boolean controleListeGroupe(String listeGroupe)
	{
		if(!listeGroupe.equals(""))
		{
			JSONParser j = new JSONParser();
			try {
				JSONArray json = (JSONArray) j.parse(listeGroupe);
				for(int i=0; i<json.size(); i++)
				{
					JSONObject o = (JSONObject) json.get(i);
					if(!groupeValide((String) o.get("id"), (String) o.get("nom")))
						return false;
				}
			} catch (Exception e){
				return false;
			}
		}
		return true;
	}
	
	public boolean groupeValide(String num, String nom)
	{
		try
		{
			Integer.parseInt(num);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		if(nom.equals(""))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CreateContact [nom=" + nom + ", prenom=" + prenom + ", email="
				+ email + ", numTelephone=" + numTelephone + ", phoneKind="
				+ phoneKind + ", nomRue=" + nomRue + ", ville=" + ville
				+ ", cp=" + cp + ", pays=" + pays + ", numSiret=" + numSiret
				+ ", idGroupe=" + idGroupe + ", nomGroupe=" + nomGroupe
				+ ", listeGroupe=" + listeGroupe + ", listeTelephone="
				+ listeTelephone + "]";
	}
	
	public boolean isContactCree() {
		return contactCree;
	}
	public void setContactCree(boolean contactCree) {
		this.contactCree = contactCree;
	}
	public String creerContact()
	{
		Entreprise entreprise;
		if(numSiret.equals(""))
			entreprise = new Entreprise(-1);
		else
			entreprise = new Entreprise(Integer.parseInt(numSiret));
		
		Adresse adresse = new Adresse(0,nomRue,ville,cp,pays);
		
		Contact contact = new Contact(0,nom,prenom,email,adresse);
		Telephone[] t = arrayToTelephone();
		contact.setProfiles(t);
		
		ContactGroupe[] groupe = arrayToGroupe(); 
		contact.setGroupe(groupe);
		
		ContactService cs = new ContactService();
		if(cs.createContact(contact, entreprise)==1)
			contactCree=true;
		else
			contactCree=false;
		System.out.println(contactCree);
		return("CreateContactFin.jsf");
	}
	public Telephone[] arrayToTelephone()
	{
		if(listeTelephone.size() == 0)
			return null;
		Telephone[] t = new Telephone[listeTelephone.size()];
		Iterator<Telephone> i = listeTelephone.iterator();
		for(int r=0; i.hasNext(); r++)
		{
			t[r] = i.next();
		}
		return t;
	}
	
	public ContactGroupe[] arrayToGroupe()
	{
		
		if(listeGroupe.size() == 0)
			return null;
		ContactGroupe[] g = new ContactGroupe[listeGroupe.size()];
		Iterator<ContactGroupe> i = listeGroupe.iterator();
		for(int r=0; i.hasNext(); r++)
		{
			g[r] = i.next();
		}
		return g;	
	}
	public void reinitialiser()
	{
		nom="";
		prenom=null;
		email=null;
		numTelephone=null;
		phoneKind=null;
		nomRue=null;
		ville=null;
		cp=null;
		pays=null;
		numSiret=null;
		idGroupe=null;
		nomGroupe=null;
		listeGroupe = new ArrayList<ContactGroupe>();
		listeTelephone = new ArrayList<Telephone>();;
	}
	
}
