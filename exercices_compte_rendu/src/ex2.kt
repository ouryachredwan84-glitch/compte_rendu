abstract class Vehicule(
    var immatriculation: String,
    var marque: String,
    var modele: String,
    var kilometrage: Int,
    var disponible: Boolean = true){

    open fun afficherDetails() {
        println("Immatriculation : $immatriculation")
        println("Marque : $marque")
        println("Modèle : $modele")
        println("Kilométrage : $kilometrage km")

        if (estDisponible()) {
            println("Disponible")
        } else {
            println("Indisponible")
        }
    }

    fun estDisponible(): Boolean {
        return disponible
    }

    fun marquerIndisponible() {
        disponible = false
    }

    fun marquerDisponible() {
        disponible = true
    }

    fun mettreAJourKilometrage(km: Int) {
        kilometrage = km
    }
}


class Voiture(
    immatriculation: String,
    marque: String,
    modele: String,
    kilometrage: Int,
    disponible: Boolean = true,
    var nombrePortes: Int,
    var typeCarburant: String) : Vehicule(immatriculation, marque, modele, kilometrage, disponible) {

    override fun afficherDetails() {
        super.afficherDetails()
        println("Nombre de portes : $nombrePortes")
        println("Type de carburant : $typeCarburant")
    }
}

class Moto(
    immatriculation: String,
    marque: String,
    modele: String,
    kilometrage: Int,
    disponible: Boolean = true,
    var cylindree: Int) : Vehicule(immatriculation, marque, modele, kilometrage, disponible) {

    override fun afficherDetails() {
        super.afficherDetails()
        println("Cylindrée : $cylindree cm³")
    }
}


class Conducteur(
    var nom: String,
    var prenom: String,
    var numeroPermis: String) {

    fun afficherDetails() {
        println("Nom : $nom")
        println("Prénom : $prenom")
        println("Numéro de permis : $numeroPermis")
    }
}

class Reservation(
    var vehicule: Vehicule,
    var conducteur: Conducteur,
    var dateDebut: String,
    var dateFin: String,
    var kilometrageDebut: Int,
    var kilometrageFin: Int? = null
) {

    fun cloturerReservation(kilometrageRetour: Int) {
        kilometrageFin = kilometrageRetour
        vehicule.mettreAJourKilometrage(kilometrageRetour)
        vehicule.marquerDisponible()
    }

    fun afficherDetails() {
        println("Véhicule : ${vehicule.marque} ${vehicule.modele}")
        println("Conducteur : ${conducteur.nom} ${conducteur.prenom}")
        println("Date début : $dateDebut")
        println("Date fin : $dateFin")
        println("Kilométrage début : $kilometrageDebut km")
        println("Kilométrage fin : ${kilometrageFin ?: "Non connu"}")
    }
}

class ParcAutomobile(
    var vehicules: MutableList<Vehicule> = mutableListOf(),
    var reservations: MutableList<Reservation> = mutableListOf()) {

    fun ajouterVehicule(vehicule: Vehicule) {
        vehicules.add(vehicule)
    }

    fun supprimerVehicule(immatriculation: String) {
        vehicules.removeIf { it.immatriculation == immatriculation }
    }

    fun reserverVehicule(
        immatriculation: String,
        conducteur: Conducteur,
        dateDebut: String,
        dateFin: String) {

        val vehicule = vehicules.find { it.immatriculation == immatriculation }

        if (vehicule == null) {
            throw VehiculeNonTrouveException(
                "Le véhicule avec l'immatriculation $immatriculation n'a pas été trouvé."
            )
        }

        if (!vehicule.estDisponible()) {
            throw VehiculeIndisponibleException(
                "Le véhicule n'est pas disponible."
            )
        }

        val reservation = Reservation(
            vehicule,
            conducteur,
            dateDebut,
            dateFin,
            vehicule.kilometrage
        )

        reservations.add(reservation)

        vehicule.marquerIndisponible()

        println("Réservation effectuée avec succès.")

    }

    fun afficherVehiculesDisponibles() {
        for (vehicule in vehicules) {
            if (vehicule.estDisponible()) {
                vehicule.afficherDetails()
                println("--------------------")
            }
        }
    }

    fun afficherReservations() {
        for (reservation in reservations) {
            reservation.afficherDetails()
            println("--------------------")
        }
    }
}

class VehiculeIndisponibleException(message: String): Exception(message)

class VehiculeNonTrouveException(message: String): Exception(message)




fun main() {

    val parc1 = ParcAutomobile()

    val listmoto = listOf<Moto>(
        Moto(
            "12345-B-2",
            "Honda",
            "CB500",
            20000,
            cylindree = 500
        ),
        Moto(
            "12345-B-1",
            "Yamaha",
            "MT-07",
            15000,
            cylindree = 689
        )


    )
    val listVoiture = listOf<Voiture>(
        Voiture(
            "12345-A-2",
            "Renault",
            "Clio",
            30000,
            nombrePortes = 5,
            typeCarburant = "Essence"
        ),
        Voiture(
            "12345-A-1",
            "Dacia",
            "Logan",
            50000,
            nombrePortes = 4,
            typeCarburant = "Diesel"
        )
    )

    for (i in listVoiture) {
        parc1.ajouterVehicule(i)
    }

    for (i in listmoto) {
        parc1.ajouterVehicule(i)
    }

    val listconducteur = listOf<Conducteur>(
        Conducteur("Ouryach", "Redouan", "PERMIS001"),
        Conducteur("Amine", "Ali", "PERMIS002"),
        Conducteur("Yassine", "Mohamed", "PERMIS003")
    )



    try {

        parc1.reserverVehicule(
            "12345-A-2",
            listconducteur[0],
            "10/01/2027",
            "10/02/2027"
        )

        parc1.reserverVehicule(
            "12345-A-2",
            listconducteur[1],
            "01/01/2027",
            "01/02/2027"
        )

        parc1.reserverVehicule(
            "12339-A-1",
            listconducteur[2],
            "10/02/2027",
            "20/02/2027"
        )


    } catch (e: VehiculeIndisponibleException) {

        println("Erreur : ${e.message}")

    } catch (e: VehiculeNonTrouveException) {

        println("Erreur : ${e.message}")
    }

    println("===== VÉHICULES DISPONIBLES =====")
    parc1.afficherVehiculesDisponibles()

    println("===== RÉSERVATIONS =====")
    parc1.afficherReservations()


}



