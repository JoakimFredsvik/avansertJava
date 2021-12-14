[![Java CI with Maven](https://github.com/kristiania-pgr203-2021/pgr203-exam-JoakimFredsvik/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr203-2021/pgr203-exam-JoakimFredsvik/actions/workflows/maven.yml)
# pgr203-exam-JoakimFredsvik
## Beskrivelse
Http-server med brukergrensesnitt hvor man kan lage, og svare på spørreundersøkelser.

## Oppsett
1. Legg til en fil med navn pgr203.properties, hvor du fyller ut passende verdier for: 
  - dataSource.url = [din jdbc url]
  - dataSource.username = [din postgresbruker] 
  - dataSource.password = [ditt postgrespassord]
2. Kjør kommandoen: <code>mvn package</code>
3. For å starte programmet, kjør kommandoen: <code>java -jar target/pgr203-exam-JoakimFredsvik-1.0-SNAPSHOT.jar</code>

## Funksjonalitet
Gå til http://localhost:1968 for å bruke løsningen i browseren.
Index siden tar imot navn og lager deg en bruker etter du har sendt dette til serveren. Det blir så lagret som cookie slik at det følger med på svarene du gjør.
Etter du har laget bruker får man opp en meny:
  - "View questionnaires": Viser liste over alle spørreundersøkelser som eksisterer, man har da valget mellom å svare på dem, eller redigere dem.
  - "New questionnaire": Gir deg mulighet til å legge til en ny spørre undersøkelse.(sender deg videre til edit-siden for å videre legge til spørsmål/alternativer
  - "View your answers": Viser alle svar gitt som tilsvarer bruker id som er lagret i cookie.
  - "View all answers": Viser alle svar i databasen. 

## Diagram database
![uml_database](https://user-images.githubusercontent.com/71790015/141684484-4277c2cc-b9ed-4cba-8b20-d91dd6d07d23.png)

## Diagram klasser
![klassediagram](https://user-images.githubusercontent.com/71790015/141684817-e5ca68ae-8245-4631-9bb1-be97f0bb733a.png)


## Ekstra leveranse
#### Update
Alle entiteter bortsett fra svar og bruker kan oppdateres via apien. Gjøres i brukergrensesnittet ved å klikke på "edit" på en av undersøkelsene når man er på siden "view all questionnaires".
#### Cookie
Har implementert at man legger til navnet sitt når man kommer til index.html første gang, det blir da sendt som en post-request til server, som lagrer navnet i en
som en ny bruker i databasen. Serveren svarer så med enn Set-Cookie header, hvor id til brukeren blir satt som en cookie hos klienten. Brukes da til å lagre svar på bruker, samt hente svarene brukeren har sendt inn.
#### Sletting
Man kan slette både spørsmål(question) og spørreundersøkelser(questionnaire). Det er implementert som en "soft-delete", hvor det er en boolean i databasen som sier om det er slettet eller ikke. Det ble gjort for å unngå å måtte implementere "cascading delete", hvor jeg da måtte lagret spørsmålene i svar(answer) tabellen for at ikke svarene ville forsvunnet med spørsmålene.
#### Favicon
Har implementert favicon.
#### Filecontroller
Har implementert FileController klasse, som implementerer HttpController, for å håndtere request av filer.
#### Norske tegn
Har implementert decoding av den urlencodede strengen som blir sendt fra klienten slik at norske tegn fungerer.
#### Gjort endepunktene mer "rest-fulle"
Endepunktene er altid /api/[entitet], hvor serveren responderer forskjellig basert på metode til request-en. Alle kontrollere for entiteter arver fra en abstrakt klasse(HttpEntityController), hvor jeg samlet koden som ble gjenbrukt i de forskjellige kontrollerne, hvor kontrollerene overrider visse metoder for funksjonalitet som ikke er felles for alle kontrollerne.
#### HttpMessage
Gjorde HttpMessage om til en abstrakt klasse, hvor man lager objekter av enten Response eller Request klassen. De to klassene blir både brukt til sette opp en response/request ved å gi de forskjellige feltene verdier, sette inn headers osv. De skaper legger også selv verdier inn hvis de får en socket som parameter i konstruktør.
#### AbstractDao
Alle de forskjellige Daoene arver fra en abstrakt dao, hvor mye 



link til repository: https://github.com/kristiania-pgr203-2021/pgr203-exam-JoakimFredsvik
