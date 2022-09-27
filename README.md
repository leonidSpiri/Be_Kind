#Entities   
All:   
        //val uuid: String,
        val surName: String,
        val name: String,
        val lastname: String,
        var personalPhone: String,
        var email: String,
        val birthday: String,
        var city: String,
        //var photoUrl: String? = null,
        //var passportGeneralUrl: String? = null,
        //var passportRegistrationUrl: String? = null,
        //var isAccountConfirmed: Boolean = false

Invalid:
        var relativePhone: String? = null,
        var address: String? = null,
        var helpReason: List<String>,
        //var certOfDisabilityUrl: String? = null,

Volunteer:
        var helpReason: List<String>,
        //var certOfMedicalEduUrl: String? = null,