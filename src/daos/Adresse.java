package daos;

public class Adresse {
	private int id;
	private String nomRue;
	private String ville;
	private String cp;
	private String pays;
	
	public Adresse()
	{
		this.id = -1;
		this.nomRue = "";
		this.ville = "";
		this.cp = "";
		this.pays = "";
	}
	public Adresse(int id)
	{
		this.id = id;
		this.nomRue = "";
		this.ville = "";
		this.cp = "";
		this.pays = "";		
	}
	public Adresse(int id, String nomRue, String ville, String cp, String pays) {
		this.id = id;
		this.nomRue = nomRue;
		this.ville = ville;
		this.cp = cp;
		this.pays = pays;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public boolean champsVide()
	{
		if(nomRue.equals("") && ville.equals("") && cp.equals("") && pays.equals(""))
			return true;
		else
			return false;
	}
	
	@Override
	public String toString() {
		String retour ="";
		
		if(nomRue != null && !nomRue.equals(""))
			retour += nomRue+"<br/>";
		
		if(ville != null && !ville.equals(""))
			retour += ville+"<br/>";
		
		if(cp != null && !cp.equals(""))
			retour += cp+"<br/>";
		
		if(pays != null && !pays.equals(""))
			retour += pays+"<br/>";
		
		return retour;
	}
}
