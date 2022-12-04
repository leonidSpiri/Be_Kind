## Entities   
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


## docker-compose.yaml
```yaml
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
```

## SQL
```sql
create table work_items
(
    id                       varchar(100) primary key,
    status                   varchar(75)  not null,
    description              varchar(300) not null,
    isDone                   bool         not null,
    whoNeedHelpId            varchar(75)  not null,
    whenNeedHelp             varchar(75)  not null,
    timeStampNow             varchar(75)  not null,
    kindOfHelp               varchar(75)  not null,
    startCoordinates         varchar(75)  null,
    address                  varchar(75)  not null,
    invalidPhone             varchar(75)  not null,
    volunteerPhone           varchar(75)  null,
    volunteerGender          varchar(75)  not null,
    volunteerAge             varchar(75)  not null,
    realCoordinatesInvalid   varchar(75)  null,
    degreeOfVolunteer        varchar(75)  null,
    needMedicineCertificate  bool         null,
    whoHelpId                varchar(75)  null,
    doneCode                 varchar(7)   null,
    realCoordinatesVolunteer varchar(75)  null
);
```
