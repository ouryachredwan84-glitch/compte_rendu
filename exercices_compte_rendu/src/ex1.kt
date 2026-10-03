open class Personne(var nom : String,
                    var prenom : String,
                    var email : String){

    fun afficherinfo(){
        println("$nom - $prenom - $email")
    }

}

class Utilisateur(nom: String,
                  prenom: String,
                  email: String,
                  var idUtilisateur: Int,
                  var emprunts : MutableList<Emprunt>) :Personne(nom,prenom,email){

    fun emprunterLivre(livre: Livre, dateEmprunt: String){

        if (livre.disponiblePourEmprunt()){
            val emprunt = Emprunt(this,livre,dateEmprunt,null)
            emprunts.add(emprunt)
            livre.nombreExemplaires -=1
        }else{
            println("Le livre  ${livre.titre} n'est pas disponible")
        }
    }

    fun afficherEmprunts(){
        println("voici les Emprunts de utilisateur $prenom")
        for(i in emprunts){
            i.afficherDetails()
        }
    }
}

class Livre(var titre : String ,
            var auteur : String ,
            var isbn : String ,
            var nombreExemplaires : Int){

    fun  afficherDetails(){
        println("titre: $titre\n auteur: $auteur\n isbn: $isbn\n nombreExemplaires:$nombreExemplaires")
    }

    fun disponiblePourEmprunt(): Boolean{

        return nombreExemplaires >0
    }

    fun mettreAJourStock(nouveauStock: Int) {
        nombreExemplaires = nouveauStock
    }

}

class Emprunt(var utilisateur : Utilisateur,
              var livre : Livre,
              var dateEmprunt : String,
              var dateRetour : String?){

    fun afficherDetails() {
        println("Utilisateur : ${utilisateur.nom} ${utilisateur.prenom}")
        println("Livre : ${livre.titre}")
        println("Date emprunt : $dateEmprunt")
        println("Date retour : $dateRetour")
    }

    fun retournerLivre(dateRetour: String) {
        this.dateRetour = dateRetour
        livre.mettreAJourStock(livre.nombreExemplaires + 1)

        println("Livre ${livre.titre} retourné le $dateRetour")
    }
}

abstract  class GestionBibliotheque{
    val utilisateurs = mutableListOf<Utilisateur>()
    val livres = mutableListOf<Livre>()

    fun ajouterUtilisateur(utilisateur: Utilisateur){
        utilisateurs.add(utilisateur)
    }
    fun ajouterLivre(livre:Livre){
        livres.add(livre)
    }
    fun afficherTousLesLivres(){
        for(i in livres){
            i.afficherDetails()
        }
    }
}

class Bibliotheque:GestionBibliotheque(){

    fun rechercherLivreParTitre(titre: String): Livre? {
        return livres.find { it.titre == titre }
    }
}


fun main(){

    val nouveauxlivres = listOf(
        Livre("Le Petit Prince", "Antoine de Saint-Exupéry", "978-0156012195", 5),
        Livre("L'Étranger", "Albert Camus", "978-2070360024", 3),
        Livre("Les Misérables", "Victor Hugo","978-2070409228", 4),
        Livre("1984", "George Orwell", "978-0451524935", 6),
        Livre("L'Alchimiste", "Paulo Coelho", "978-0062315007", 2),
    )

    val nouveauxUtilisateurs = mutableListOf(
        Utilisateur("Redouan", "Ouryach", "redouan@gmail.com", 1, mutableListOf()),
        Utilisateur("Yassine", "Amrani", "yassine@gmail.com", 2, mutableListOf()),
        Utilisateur("Sara", "Alaoui", "sara@gmail.com", 3, mutableListOf()),
        Utilisateur("Imane", "Bennani", "imane@gmail.com", 4, mutableListOf()),
        Utilisateur("Hamza", "Idrissi", "hamza@gmail.com", 5, mutableListOf())
    )

    val Bibliothequezohour = Bibliotheque()

    for (i in nouveauxlivres){
        Bibliothequezohour.ajouterLivre(i)
    }
    for (i in nouveauxUtilisateurs){
        Bibliothequezohour.ajouterUtilisateur(i)
    }

    val user1 = nouveauxUtilisateurs[2]
    user1.emprunterLivre(Bibliothequezohour.livres[2],"02/10/2026")

    val user2 = nouveauxUtilisateurs[4]
    user2.emprunterLivre(Bibliothequezohour.livres[0],"02/10/2026")


    for(i in Bibliothequezohour.livres ){
        println("livre: ${i.titre}")
        i.afficherDetails()
    }

    for(i in Bibliothequezohour.utilisateurs){
        println("iduser: ${i.idUtilisateur}")
        i.afficherinfo()
        i.afficherEmprunts()
    }

    user1.emprunts[0].retournerLivre("01/01/2027")

}
