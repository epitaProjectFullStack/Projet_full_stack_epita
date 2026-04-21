# 📘 Documentation – Système temps réel (Kafka + WebSocket)

## 🎯 Objectif

Ce module permet de notifier automatiquement le frontend lorsqu’un événement métier se produit côté backend (ex : création d’un jeu), sans rechargement manuel de la page.

Concrètement : lorsqu’un jeu est créé, tous les utilisateurs connectés voient la mise à jour immédiatement.

---

## 🧱 Architecture globale

Le système repose sur 4 éléments :

- Backend (Spring Boot) : logique métier
- Kafka : transport d’événements asynchrone
- WebSocket (STOMP) : diffusion temps réel
- Frontend (Angular) : affichage et interaction

---

## 🔁 Flow complet (de la création à l’affichage)

1. Le frontend envoie une requête HTTP :

POST /api/games

2. Le backend traite la requête :

GameController → GameService

3. Le service envoie un événement Kafka :

kafkaProducerService.sendGameCreated(gameId)

4. Kafka reçoit l’événement dans le topic "game-events"

5. Le backend consomme cet événement :

@KafkaListener(topics = "game-events")

6. Le backend envoie l’événement au WebSocket :

messagingTemplate.convertAndSend("/topic/games", message)

7. Le frontend est connecté au WebSocket et écoute :

client.subscribe('/topic/games', ...)

8. Le frontend reçoit l’événement et recharge les données :

getAllGames()

---

## 📡 Configuration WebSocket

### Backend

Le backend expose un endpoint WebSocket :

registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:4200");

Le broker est configuré ainsi :

config.enableSimpleBroker("/topic");
config.setApplicationDestinationPrefixes("/app");

Explication :
- /ws = point d’entrée WebSocket
- /topic = canal de diffusion
- /topic/games = canal utilisé pour les jeux

---

### Frontend

Connexion WebSocket :

const client = new Stomp.Client({
  brokerURL: 'ws://localhost/ws',
  reconnectDelay: 5000,
  debug: (str) => console.log('[STOMP]', str),
});

Souscription :

client.subscribe('/topic/games', (message) => {
  const data = JSON.parse(message.body);
});


## 🔐 Sécurité

### Autorisation WebSocket

.requestMatchers("/ws/**").permitAll()

Sinon la connexion est bloquée.

---

### Configuration CORS

config.setAllowedOrigins(List.of("http://localhost:4200"));
config.setAllowedMethods(List.of("*"));
config.setAllowedHeaders(List.of("*"));
config.setAllowCredentials(true);

Cela signifie :
- seul le frontend local peut appeler le backend
- ce n’est PAS ouvert à tout internet

---

### CSRF

.csrf(csrf -> csrf.disable())

Acceptable ici car :
- API stateless
- authentification via JWT

---

## 🧠 Rôle de Kafka

Kafka permet :
- communication asynchrone
- découplage des composants
- scalabilité
- gestion des événements métier

Le backend n’envoie pas directement au WebSocket : il passe par Kafka.

---

## 🧠 Rôle du WebSocket

Le WebSocket permet :
- communication temps réel serveur → client
- suppression du polling HTTP
- meilleure expérience utilisateur

---

## 📊 Schéma simplifié

Frontend → POST /api/games  
→ Backend (Controller → Service)  
→ Kafka Producer  
→ Kafka (topic game-events)  
→ Kafka Consumer  
→ WebSocket (/topic/games)  
→ Frontend (subscribe)  
→ Refresh UI  

---

## 👤 Gestion des rôles

| Rôle | Permissions |
|------|-----------|
| GUEST | lecture |
| USER | création |
| MODERATOR | modération |
| ADMINISTRATOR | accès complet |

---

## 🚀 Avantages

- temps réel
- découplage fort
- architecture scalable
- extensible (nouveaux événements)

---

## ❌ Limites

- dépendance à Kafka
- complexité plus élevée qu’une API REST classique
- gestion de la sécurité WebSocket nécessaire


## 📌 Conclusion

Le projet met en place une architecture événementielle complète :

REST → Kafka → WebSocket → Frontend

Ce modèle permet une mise à jour temps réel efficace tout en gardant un backend découplé et scalable.