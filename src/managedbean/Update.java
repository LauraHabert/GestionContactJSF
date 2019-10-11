package managedbean;

import javax.faces.bean.ManagedBean;

import daos.Adresse;
import daos.Contact;
import services.ContactService;

@ManagedBean(name="update")
public class Update {
	private String id;
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
	private boolean reussie;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	
	public boolean isReussie() {
		return reussie;
	}

	public void setReussie(boolean reussie) {
		this.reussie = reussie;
	}

	public String maj(){
		Adresse adresse = new Adresse(0,nomRue,ville,cp,pays);
		Contact contact = new Contact(Integer.parseInt(id),nom,prenom,email,adresse);
		ContactService c = new ContactService();
		reussie = c.updateContact(contact);
		System.out.println(reussie);
		return("UpdateContactFin.jsf");	
	}
	
	public void reset()
	{
		id = "";
		nom ="";
		prenom = "";
		email= "";
		numTelephone= "";
		phoneKind= "";
		nomRue = "";
		ville = "";
		cp = null;
		pays= "";
		numSiret = null;
		idGroupe= null;
		nomGroupe = "";
	}
}
