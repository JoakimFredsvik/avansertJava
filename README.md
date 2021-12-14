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
