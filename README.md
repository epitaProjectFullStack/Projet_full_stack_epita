# Projet_full_stack_epita

## POUR TESTER LE BUILD AVANT DE POUSSER
- ./mvnw clean verify

## BUILD FRONTEND (AVANT PUSH)
- npm run build

## visualiser l'api grace a swagger

- http://localhost/swagger-ui/index.html
- http://localhost/v3/api-docs

## projet configurer avec java 21
- sudo apt install openjdk-21-jdk
- sudo update-alternatives --config java

## mettre a jour les deps

- mvn clean test
- mvn clean install

## RESTART PROPRE DOCKER
- docker-compose down -v
- docker-compose up --build
=> backend → 80
=> ng serve --configuration production

## RESTART LOCAL
- mvn spring-boot:run
- npm start
=> backend → 8080
=> frontend → 4200

## KAFKA UI
- http://localhost:8080/ui/clusters/local

## COMMANDE dans le  CMD POUR VOIR LE POURCENTAGE DE TEST avec jacoco

- xdg-open target/site/jacoco/index.html