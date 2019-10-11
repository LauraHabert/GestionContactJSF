package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;


public class ContactDAO {
	
	private final String adresseBD = "jdbc:mysql://localhost";
	private final String nomBD = "test";
	private final String userBD = "root";
	private final String mdpBD = "root";
	
	private Connection co;
	
	public ContactDAO()
	{
		co = null;
	}
	
	public int createContact(Contact c, Entreprise entreprise)
	{
		if(c.getNom().equals("") || c.getPrenom().equals(""))
		{
			return -1;
		}
		else
		{
			Statement st = getConnection();
			String sql="";
			try {
				sql = "Select Max(id) from contact";
				ResultSet r = st.executeQuery(sql);
				try
				{
					r.next();
					int idContact = r.getInt(1)+1;
					c.setId(idContact);
					if(c.getGroupe() != null)
						if(c.getGroupe()[0].getContact() != null)
							c.getGroupe()[0].getContact()[0].setId(idContact);
				}catch(NullPointerException n){}
				/*Recupération de l'id pour l'adresse.
				 * On verifie si l'adresse existe dans la base de donnée
				 * Si elle n'exite pas on l'a crée sinon on récupére son id pour l'affecter au contact
				 * 
				 */
				int idAdresse = -1;
				if(!c.getAdresse().champsVide())
				{
					idAdresse = rechercheAdresse(st,c.getAdresse());
					if(idAdresse == -1)
					{
						idAdresse = creationAdresse(st,c.getAdresse());
					}
				}
				/* Recupération de l'id pour le groupe
				 * On verifie les informations que l'utilisateur a entré
				 * On verifie si le groupe existe 
				 * S'il n'existe pas on le crée sinon on récupère son ID
				 * 
				 * */
				int idGroupe = -1;	
				if(c.getGroupe() != null)
				{
					ContactGroupe[] listeGroupe = c.getGroupe();
					for(int i=0; i<listeGroupe.length; i++)
					{
						if(listeGroupe[i].getGroupeId() != -1)
						{
							idGroupe = rechercheGroupe(st,listeGroupe[i]);
							if(idGroupe == -1)
								idGroupe = creationGroupe(st,listeGroupe[i]);
							listeGroupe[i].setGroupeId(idGroupe);
						}
					}
				}
				
				/* Recupération de l'id pour le groupe
				 * On verifie les informations que l'utilisateur a entré
				 * On verifie si le numéro de téléphone existe
				 * S'il n'existe pas on le crée sinon on récupère son ID
				 * 
				 * */
				Telephone[] listeTelephone = c.getProfiles();
				int idNumTelephone = -1;
				if(listeTelephone != null)
				{
					for(int i=0; i<listeTelephone.length; i++)
					{
						idNumTelephone = rechercheTelephone(st,listeTelephone[i]);
						if(idNumTelephone == -1)
						{
							idNumTelephone = creationTelephone(st,listeTelephone[i],c.getId());
						}
					}
				}
				
				/* Recupération du numéro de Siret
				 * On verifie les informations que l'utilisateur a entré
				 * On verifie si le numéro de Siret existe
				 * S'il n'existe pas on le crée sinon on récupère sa valeur
				 * 
				 * */
				entreprise.addContact(c);
				int numSiret = entreprise.getNumSiret();

				if(!numSiretExiste(st,numSiret))
				{
					if(numSiret != -1)
						creationEntreprise(st,entreprise);
					else
						entreprise.setNumSiret(-1);
				}
				
				sql = "Insert into contact(id,nom,prenom,email,idAdresse) "
						+ "values("+c.getId()+",'"+c.getNom()+"','"+c.getPrenom()+"','"+c.getEmail()
						+"',"+idAdresse+")";
				int cpt = st.executeUpdate(sql);
				fermerConnection(st);
				if(cpt == 1)
					return 1;
				else
					return 0;
			} catch (SQLException e) {
				System.out.println("Erreur creation contact "+sql);
				fermerConnection(st);
				return 0;
			}
		}
	}

	public ArrayList<Contact> researchContact(String nom, String prenom) {
		Statement st = getConnection();
		ArrayList<Contact> tableau = new ArrayList<Contact>();
		try {
			String sql = "SELECT id,nom,prenom,email,idAdresse FROM contact";
			boolean comp = false;
			if(nom != null && !nom.equals(""))
			{
				sql += " WHERE nom='"+nom+"'";
				comp = true;
			}
			if(prenom != null && !prenom.equals(""))
			{
				if(comp)
					sql += " AND prenom='"+prenom+"'";
				else
				{
					sql+= " WHERE prenom='"+prenom+"'";
					comp = true;
				}
			}
			ResultSet resultat = st.executeQuery(sql);
			while(resultat.next())
			{
				int id = resultat.getInt("id");
				String nom_r = resultat.getString("nom");
				String prenom_r = resultat.getString("prenom");
				String email_r = resultat.getString("email");
				int idAdresse = resultat.getInt("idAdresse");
				Adresse adresse = new Adresse(idAdresse);
				tableau.add(new Contact(id,nom_r,prenom_r,email_r,adresse,null,null));
			}
			resultat.close();
			if(!tableau.isEmpty())
			{
				Iterator<Contact> it = tableau.iterator();
				while(it.hasNext())
				{
					Contact c = it.next();
					c.setAdresse(rechercheAdresse(st, c.getAdresse().getId()));
					ContactGroupe[] listeGroupe = rechercheListeGroupe(st,c.getId());
					Telephone[] telephone = rechercheListeTelephone(st,c.getId());
					c.setNumSiret(rechercheEntreprise(st,c.getId()));
					c.setGroupe(listeGroupe);
					c.setProfiles(telephone);
				}
			}
			fermerConnection(st);
			return tableau;
		} catch (SQLException e) {
			System.out.println("Erreur select "+e.getMessage());
			fermerConnection(st);
			return null;
		}		
	}

	private int rechercheEntreprise(Statement st, int id) {
		
		String sql = "Select numSiret from Entreprise where idContact="+id;
		try {
			ResultSet r = st.executeQuery(sql);
			if(r.first())
				return r.getInt(1);
			return -1;
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		}
	}

	public ArrayList<Entreprise> getAllEntreprise()
	{
		Statement st = getConnection();
		ArrayList<Entreprise> tableau = new ArrayList<Entreprise>();
		String sql = "SELECT numSiret, idContact FROM entreprise Order by numSiret";
		ResultSet resultat;
		try {
			resultat = st.executeQuery(sql);
			Entreprise e = null;
		while(resultat.next())
		{
			if(e != null && e.getNumSiret() == resultat.getInt("numSiret"))
				e.addContact(new Contact(resultat.getInt("idContact")));
			else
			{
				if(e != null)
					tableau.add(e);
				e = new Entreprise(resultat.getInt("numSiret"));
				e.addContact(new Contact(resultat.getInt("idContact")));
			}
		}
		if(e != null)
			tableau.add(e);
		} catch (SQLException e1) {
			fermerConnection(st);
			e1.printStackTrace();
		}
		return tableau;
		
	}
	private Telephone[] rechercheListeTelephone(Statement st, int id) {
		String sql = "SELECT * FROM NumeroTelephone WHERE idContact="+id;
		ArrayList<Telephone> t = new ArrayList<Telephone>();
		try {
			ResultSet resultat = st.executeQuery(sql);
			while(resultat.next())
			{
				int idTel = resultat.getInt("id");
				String num = resultat.getString("numeroTelephone");
				String phoneKind = resultat.getString("phoneKind");
				t.add(new Telephone(idTel,num,phoneKind,null));
			}
			if(t.isEmpty())
				return null;
			
			Telephone[] listeTelephone = new Telephone[t.size()];
			Iterator<Telephone> it = t.iterator();
			for(int i=0; i<listeTelephone.length; i++)
			{
				listeTelephone[i] =it.next();
			}
			return listeTelephone;
		} catch (SQLException e) {
			System.out.println("ERREUR recherche groupe "+sql+" "+e.getMessage());
			return null;
		}
	}
	public boolean updateContact(Contact contact){
		Statement st = getConnection();
		boolean reussie = updateContact(st,contact.getId(),contact.getNom(),contact.getPrenom(),contact.getEmail());
		if(reussie)
		{
			reussie = updateAdresse(st,contact.getAdresse(),contact.getId());
			reussie = updateTelephone(st,contact.getProfiles(),contact.getId());
			reussie = updateGroupe(st,contact.getGroupe(),contact.getId());
		}
		return reussie;
	}
	private boolean updateContact(Statement st, int id, String nom, String prenom, String email)
	{
		if(id < -1)
		{
			return false;
		}
		else
		{	
			String sql = "Select id from contact where id="+id;
			int retour=-1;
			try {
				retour = recherche(st, sql);
			} catch (SQLException e1) {
				return false;
			}
			if(retour != id)
				return false;
			sql = "UPDATE contact SET";
			boolean comp = false;
			if(!nom.equals(""))
			{
				sql += " nom='"+nom+"'";
				comp = true;
			}
			if(!prenom.equals(""))
			{
				if(comp)
					sql += " ,";
				else
					comp = true;
				sql+= " prenom='"+prenom+"'";
			}
			if(!email.equals(""))
			{
				if(email.equals(" "))
					email ="";
				if(comp)
					sql += " ,";
				sql+= " email='"+email+"'";
			}
			sql += " WHERE id="+id;
			try {
				int cpt = st.executeUpdate(sql);
				if(cpt == 1)
					return true;
				else
					return false;
			} catch (SQLException e) {
				return false;
			}
		}
	}
	
	public boolean updateAdresse(Statement st, Adresse adresse, int idContact)
	{
		if(idContact < 0)
			return false;
		String sql = "Select idAdresse from contact where id="+idContact;
		int id = -2;
		try {
			id = recherche(st,sql);
		if(id == -2)
			return false;
		if(id == -1)
		{
			if(adresse == null)
				return true;
			int idAdresse = creationAdresse(st,adresse);
			sql = "Update contact set idAdresse="+idAdresse+" where id="+idContact;
			int cpt = st.executeUpdate(sql);
			if(cpt == 1)
				return true;
			else
				return false;
		}
		}catch (SQLException e1) {
				return false;
		}
		if(adresse == null)
		{
			sql = "DELETE FROM adresse WHERE id="+id;
			int cpt;
			try {
				cpt = st.executeUpdate(sql);
				sql = "Update contact set idAdresse=-1 where id="+idContact;
				cpt = st.executeUpdate(sql);
				if(cpt == 1)
					return true;
				else
					return false;
			} catch (SQLException e) {
				return false;
			}
		}
		sql = "UPDATE adresse SET";
		boolean comp = false;

		if(!adresse.getNomRue().equals(""))
		{
			if(adresse.getNomRue().equals(" "))
				adresse.setNomRue("");
			sql += " rue='"+adresse.getNomRue()+"'";
			comp = true;
		}
		if(!adresse.getVille().equals(""))
		{
			if(adresse.getVille().equals(" "))
				adresse.setVille("");
			if(comp)
				sql += " ,";
			else
				comp = true;
			sql+= " ville='"+adresse.getVille()+"'";
		}
		if(!adresse.getCp().equals(""))
		{
			if(adresse.getCp().equals(" "))
				adresse.setCp("");
			if(comp)
				sql += " ,";
			sql+= " cp='"+adresse.getCp()+"'";
		}
		if(!adresse.getPays().equals(""))
		{
			if(adresse.getPays().equals(" "))
				adresse.setPays("");
			if(comp)
				sql += " ,";
			sql+= " pays='"+adresse.getPays()+"'";
		}
		sql += " WHERE id="+id;
		try {
			int cpt = st.executeUpdate(sql);
			if(cpt == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	private boolean updateTelephone(Statement st, Telephone[] t, int idContact)
	{
		if(idContact < 0)
			return false;
		String sql = "DELETE FROM numerotelephone WHERE idContact="+idContact;
		supprimer(st,sql);
		if(t != null)
		{
			for(int i=0; i<t.length; i++)
				creationTelephone(st, t[i],idContact);
		}
		return true;
	}
	
	private boolean updateGroupe(Statement st, ContactGroupe[] g, int idContact)
	{
		if(idContact < 0)
			return false;
		String sql = "DELETE FROM contactgroupe WHERE idContact="+idContact;
		supprimer(st,sql);
		if(g != null)
		{
			for(int i=0; i<g.length; i++)
			{
				if(g[i].getGroupeId() != -1)
				{
					sql = "INSERT INTO ContactGroupe(groupId, nomGroupe, idContact) "
							+ "VALUES ("+g[i].getGroupeId()+",'"+g[i].getNomGroupe()+"',"+idContact+")";
					try {
						st.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return true;		
	}
	
	public boolean updateEntreprise(int idContact, int numSiret)
	{
		System.out.println(idContact+" - "+numSiret);
		if(idContact<0)
			return false;
		if(numSiret <0)
			return false;
		Statement st = getConnection();
		
		String sql = "Select Count(*) from Entreprise where idContact="+idContact;
		try {
		ResultSet r = st.executeQuery(sql);
		r.first();
		int nb = r.getInt(1);
		System.out.println("nb Entreprise pour le client n�"+idContact+" : "+nb);
		if(nb != 1)
		{
			sql = "INSERT INTO entreprise(numSiret, idContact) VALUES ("+numSiret+","+idContact+")";
		}
		else
		{
			sql = "Update Entreprise set numSiret="+numSiret+" where idContact="+idContact;
		}
		st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e);
			fermerConnection(st);
			return false;
		}
		fermerConnection(st);
		return true;
	}
	public boolean deleteContact(int id) {
		Statement st = getConnection();
		String sql = "SELECT id FROM contact WHERE id="+id;
		
		try {
			int idContact = recherche(st,sql);
			if(idContact == -1)
			{
				fermerConnection(st);
				return false;
			}
		} catch (SQLException e) {
			fermerConnection(st);
			return false;
		}
		
		sql = "SELECT idAdresse FROM contact WHERE id="+id;
		short reussi = 1;
		try {
			int idAdresse = recherche(st,sql);
			if(idAdresse != -1)
			{
				sql = "DELETE FROM adresse WHERE id="+idAdresse;
				if(!supprimer(st,sql))
					reussi=0;
			}
		} catch (SQLException e) {
			fermerConnection(st);
			return false;
		}
		sql = "DELETE FROM contact WHERE ID="+id;
		supprimer(st,sql);
		sql = "DELETE FROM contactgroupe WHERE idContact="+id;
		supprimer(st,sql);
		sql = "DELETE FROM entreprise WHERE idContact="+id;
		supprimer(st,sql);
		sql = "DELETE FROM numerotelephone WHERE idContact="+id;
		supprimer(st,sql);
		fermerConnection(st);
		return true;
		
	}
	
	public boolean supprimer(Statement st, String sql)
	{
		try {
			int cpt = st.executeUpdate(sql);
			if(cpt > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			return false;
		}
	}
	private int recherche(Statement st, String requete) throws SQLException
	{
		ResultSet r = st.executeQuery(requete);
		if(r.next()){
			return r.getInt(1);
		}
		return -1;
	}
	
	private int rechercheAdresse(Statement st, Adresse adresse) throws SQLException
	{
		String requete = "Select id from adresse where "
				+" rue='"+adresse.getNomRue()+"' and "
				+" ville='"+adresse.getVille()+"' and "
				+" cp='"+adresse.getCp()+"' and "
				+" pays='"+adresse.getPays()+"'";
		return recherche(st,requete);
	}
	
	private Adresse rechercheAdresse(Statement st, int idAdresse) throws SQLException
	{
		Adresse adresse = null;
		if(idAdresse != -1) 
		{
			String requete = "Select * from adresse where "
					+"id="+idAdresse;
			ResultSet resultat = st.executeQuery(requete);
			resultat.first();
			String rue = resultat.getString("rue");
			String ville = resultat.getString("ville");
			String cp = resultat.getString("cp");
			String pays = resultat.getString("pays");
			adresse = new Adresse(idAdresse,rue,ville,cp,pays);
		}
		else
			adresse = new Adresse(idAdresse);
		return adresse;
		
	}
	private ContactGroupe[] rechercheListeGroupe(Statement st, int id) {
		String sql = "SELECT * FROM ContactGroupe WHERE idContact="+id;
		ArrayList<ContactGroupe> t = new ArrayList<ContactGroupe>();
		try {
			ResultSet resultat = st.executeQuery(sql);
			while(resultat.next())
			{
				int idGroupe = resultat.getInt("groupId");
				String nomGroupe = resultat.getString("nomGroupe");
				t.add(new ContactGroupe(idGroupe,nomGroupe, null));
			}
			if(t.isEmpty())
				return null;
			
			ContactGroupe[] liste = new ContactGroupe[t.size()];
			Iterator it = t.iterator();
			for(int i=0; i<liste.length; i++)
			{
				liste[i] = (ContactGroupe) it.next();
			}
			return liste;
		} catch (SQLException e) {
			System.out.println("ERREUR recherche groupe "+sql+" "+e.getMessage());
			return null;
		}
	}

	private Adresse rechercheAdresseParId(Statement st, int id) {
		String requete = "Select * from adresse where "
				+"id="+id;
		try {
			ResultSet resultat = st.executeQuery(requete);
			if(resultat.next())
			{
			int i = resultat.getInt("id");
			String rue = resultat.getString("rue");
			String ville = resultat.getString("ville");
			String cp = resultat.getString("cp");
			String pays = resultat.getString("pays");
			return new Adresse(i,rue,ville,cp,pays);
			}
			else
				return null;
		} catch (SQLException e) {
			System.out.println("Erreur recherche par id "+requete+" "+e.getMessage());
			return null;
		}
	}
	private int rechercheGroupe(Statement st, ContactGroupe groupe)
	{
		String requete = "Select groupId from ContactGroupe where "
				+"groupId="+groupe.getGroupeId();
		try {
			return recherche(st,requete);
		} catch (SQLException e) {
			System.out.println("Erreur recherche groupe "+requete);
			return -1;
		}
	}
	
	private int rechercheTelephone(Statement st, Telephone telephone)
	{
		String requete = "Select id from NumeroTelephone where "
				+"numeroTelephone='"+telephone.getNumTelephone()+"' and "
				+"phoneKind='"+telephone.getPhoneKind()+"'";
		try {
			return recherche(st,requete);
		} catch (SQLException e) {
			System.out.println("Erreur recherche telephone "+requete+" "+e.getMessage());
			return -1;
		}
	}
	
	private boolean numSiretExiste(Statement st, int numSiret)
	{
		String requete = "SELECT numSiret FROM Entreprise WHERE numSiret="+numSiret;
		try {
			
			int retour = recherche(st,requete);
			if(retour >0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Erreur recherche groupe "+requete);
			return false;
		}
	}
	
	private int creationAdresse(Statement st, Adresse adresse) throws SQLException
	{
		String sql = "INSERT INTO Adresse(id, rue, ville, cp, pays) "
				+ "VALUES (0,'"+adresse.getNomRue()+"','"+adresse.getVille()+"','"+adresse.getCp()+"','"+adresse.getPays()+"')";
		int nbLigne = st.executeUpdate(sql);
		if(nbLigne == 1)
		{
			int id = rechercheAdresse(st,adresse);
			return id;
		}
		else
			return -1;		
	}
	
	private int creationGroupe(Statement st, ContactGroupe groupe)
	{
		String sql = "";
		Contact[] listeContact = groupe.getContact();
		sql = "Select Max(groupId) from ContactGroupe";
		ResultSet resultat;
		try {
			resultat = st.executeQuery(sql);		
			int idGroupe = 0;
			if(resultat.next())
			{
				try{
				idGroupe = resultat.getInt(1)+1;
				}
				catch(NullPointerException n){}
			}
			int nbLigne = 0;
			for(int i=0; i<listeContact.length; i++)
			{
				sql = "INSERT INTO ContactGroupe(groupId, nomGroupe, idContact) "
						+ "VALUES ("+idGroupe+",'"+groupe.getNomGroupe()+"',"+listeContact[i].getId()+")";
				nbLigne = st.executeUpdate(sql);
				if(nbLigne != 1)
					return -1;
			}
			return idGroupe;
		} catch (SQLException e) {
			System.out.println("Création groupe impossible "+sql+"\n"+e.getMessage());
			return -1;
		}
	}

	private int creationTelephone(Statement st, Telephone telephone, int idContact)
	{
		String sql = "INSERT INTO NumeroTelephone(id, numeroTelephone, phoneKind, idContact)"
				+ " VALUES (0,'"+telephone.getNumTelephone()+"','"+telephone.getPhoneKind()+"',"+idContact+")";
		int nbLigne=0;
		try {
			nbLigne = st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Creation telephone impossible "+sql+" "+e.getMessage());
		}
		if(nbLigne == 1)
		{
			int id = rechercheTelephone(st,telephone);
			return id;
		}
		else
			return -1;		
	}
	private boolean creationEntreprise(Statement st, Entreprise entreprise) {
		Contact c = entreprise.getContact(0);
		String sql = "INSERT INTO Entreprise(numSiret, idContact) "
				+ "VALUES ("+entreprise.getNumSiret()+","+c.getId()+")";
		try {
			int nbLigne = st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("Erreur creation entreprise "+sql+" "+e.getMessage());
			return false;
		}
	}	
	private Statement getConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			co = DriverManager.getConnection(adresseBD+"/"+nomBD,userBD,mdpBD);
			Statement st = co.createStatement();
			return st;
		} catch (ClassNotFoundException e) {
			System.out.println("Classe non trouv\u00e9e "+e.getMessage());
		} catch (SQLException e) {
			System.out.println("Erreur SQL : "+e.getMessage());
		}
		return null;
	}
	private void fermerConnection(Statement st)
	{
		try {
			st.close();
		} catch (SQLException e1) {
		} catch(NullPointerException n){}
		try {
			co.close();
		} catch (SQLException e) {
		} catch(NullPointerException n){}
	}
}
