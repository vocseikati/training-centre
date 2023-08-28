# Óhaj Fodrász Oktatóközpont

## Áttekintés

Az alkalmazás egy fodrász oktatóközpont számára nyújt segítséget, annak érdekében, hogy kezelni tudják a tanulókat, a kurzusokat, oktatókat és oktatási helyszíneket. Továbbá lehetőség van a kurzusok menedzselésére. Az alkalmazás email-en értesíti a tanulókat a kurzusra való jelentkezésről, arról való törléséről, valamint a kurzus elvégzéséről és a tanúsítvány részleteiről. Az alkalmazás az oktatásokra való jelentkezések átláthatóbbá tételét, a tanulók nyilvántartását és további tanulók célirányosabb megszólítását segíti.

## Az alkalmazás struktúrája

Egy tanulónak több kurzusa, egy kurzusnak több tanulója is lehet. A kurzusokhoz kötelező tantermet rendelni, egy kurzusnak egy tanterme lehet. Egy kurzust egy oktató tart, egy oktatóhoz több kurzus is tartozhat. Egy tanuló több tanúsítványt is megszerezhet, több kurzus elvégzésével, egy tanúsítvány viszont csak egy tanuló számára kiállítható.
___
![entitiesrelationships.jpg](entitiesrelationships.jpg)

## Az alkalmazás működése

### Alap adatkezelési funkciók

Az alkalmazás entitásain mind végrehajthatók a CRUD műveletek. Új résztvevő (tanuló és oktató) rögzítésekor a név, levelezési cím, telefonszám és az email cím kötelezően megadandó. Az email címnek egyedinek kell lennie. Kurzus szerkesztésekor kötelező oktatási helyszínt megjelölni, ellenőrzésre kerül a kötelezően megadandó kezdő és végdátum alapján a tanterem elérhetősége. Továbbá a kurzus címét, hosszát, típusát és árát kötelező feltüntetni. Oktatót később is hozzá lehet rendelni a kurzusokhoz. 

### Kurzusok menedzselése

A kurzusmenedzsment funkción belül a tanulók képzésekre való jelentkezését lehet koordinálni. Amikor új tanuló szeretne képzésre jelentkezni, először regisztrálni kell az adatait a rendszerben, és utána indítható a kurzusokra való jelentkezés. Jelentkezés csak még nem befejezett kurzusra lehetséges.

Lehetséges egyéni vagy csoportos, gyakorlati és elméleti oktatáson való részvétel. Az alkalmazás tárolja az adott helyszín maximum kapacitását, és ha a jelentkezők száma elérte a maximumot, több tanuló nem tud jelentkezni az oktatásra. 

Kurzus elvégzői tanúsítványt kapnak. Amennyiben egy tanuló nem tudja elvégezni a kurzust, törölhető a kurzus tanulói listájából.

A kurzusmenedzsment funkción belüli műveletekről email-es tájékoztatást kapnak a tanulók.

### Lekérdezések

Az oktatások szempontjából hasznos szűrési lehetőségek érhetők el. Lekérdezhető a résztvevői lista. Itt nem jelenít meg valamennyi résztvevőről tárolt adatot az alkalmazás. Azok az adatok kerültek megjelenítésre, amelyek a képzés szervezője és az oktató számára a képzés folyamatában fontosak lehetnek. Listázhatóak a tanulók és oktatók kurzusai. A képzések listáját lehetséges azok státusza (befejezett, folyamatban, tervezett) alapján szűrni. Elérhetőek a megszerzett tanúsítványok, melyeket tanulói azonosítószám alapján is le lehet kérdezni.

## Alkalmazott technológiák

| funkció | megvalósítás |
| ------ | ------ |
| projekt | Spring Boot, Gradle, Java 8 |
| adatbázis-kezelés | H2 |
| adatbázis séma verziókezelése  | Flyway |
| konténerizáció | Docker |
| tesztelés | Junit5, MockMVC, Mockito |
|validáció| Javax|
| dokumentáció | Swagger

___

## Alkalmazás elindítása

Docker környezetben az alábbi paranccsal letöltődik a program image fájlja Docker Hub-ról, majd elindul az alkalmazás:
`docker run -p 8080:8080 geli1519/vocseikati_masterwork .`
Ezután az adatbázist H2 konzolon keresztül érjük el a http://localhost:8080/h2-console úton. Csatlakozni az alábbi
beállításokkal tudunk:

| Driver Class | org.h2.Driver |
| :---: | :---: |
| __JDBC URL__ | __jdbc:h2:mem:coursecenterdb__ |
| __User Name__ | __sa__ |
| __Password__ | __[none]__

