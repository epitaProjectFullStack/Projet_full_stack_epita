# Projet_full_stack_epita

## pour lancer le projet l'une des 2 commandes

- docker-compose up
- mvn spring-boot:run

## visualiser l'api grace a swagger

- http://localhost:8080/swagger-ui/index.html    
- http://localhost:8080/v3/api-docs

## exemple body pou tester l'api
{
  "authorId": "UUID_ICI",
  "subjectGameName": "Zelda",
  "articleName": "Test",
  "articleContent": "Test content"
}

## projet configurer avec java 21
- sudo apt install openjdk-21-jdk
- sudo update-alternatives --config java