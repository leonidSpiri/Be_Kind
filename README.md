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


docker-compose.yaml
'
version: '3.5'

services:

db_be_kind:
container_name: db_be_kind
ports:
- "32876:5432"
environment:
- POSTGRES_PASSWORD=72bc02774a938cdd81c33e5e3d56bab6ad52e79f64b878d9284387c4e04ce930
- POSTGRES_USER=ygaxqwmkigdfoe
image: postgres:14.3-alpine
restart: unless-stopped
command:
- "postgres"
- "-c"
- "port=5432"
volumes:
- db_be_kind_vol:/var/lib/postgresql/data/

volumes:
db_be_kind_vol: { }
'