## Viikko 6

Room toimii sovelluksen paikallisena tietokantakerroksena ja mahdollistaa datan pysyvän tallennuksen.

Arkkitehtuuri etenee seuraavasti:

1️ Entity

Määrittelee tietokantataulun rakenteen.
TaskEntity vastaa yhtä riviä tasks-taulussa.

2 DAO (Data Access Object)

Sisältää tietokantakyselyt:

Hae tehtävät

Lisää tehtävä

Päivitä tehtävä

Poista tehtävä

3 Database

AppDatabase yhdistää:

Entityt

DAO:t
ja luo varsinaisen Room-tietokannan.

4 Repository

Toimii välikerroksena DAO:n ja ViewModelin välillä.
Tarjoaa:

Flow<List<TaskEntity>>

suspend-funktiot CRUD-toimintoihin

5 ViewModel

Kerää Flow-datan

Kapseloi sovelluslogiikan

Käsittelee lisäys-, muokkaus- ja poistotoiminnot

6 UI (Jetpack Compose)

Näyttää datan

Käyttää collectAsState() Flow-datan kuunteluun

Kutsuu ViewModelin funktioita

 Projektin rakenne
data/
 ├── model/
 │    └── TaskEntity.kt
 │
 ├── local/
 │    ├── TaskDao.kt
 │    ├── AppDatabase.kt
 │    └── Converters.kt
 │
 └── repository/
      └── TaskRepository.kt

viewmodel/
 └── TaskViewModel.kt

view/
 ├── HomeScreen.kt
 └── Calendar.kt
 
## Datan kulku sovelluksessa
 Datan haku
Room Database
    ↓
DAO
    ↓
Repository
    ↓
ViewModel (Flow)
    ↓
UI (collectAsState)

Kun tietokanta muuttuu, Flow lähettää uuden datan automaattisesti UI:lle.

 Datan lisäys / päivitys / poisto
UI
    ↓
ViewModel
    ↓
Repository
    ↓
DAO
    ↓
Room Database

Tietokannan muutos käynnistää uuden Flow-päivityksen, jolloin UI päivittyy automaattisesti.
