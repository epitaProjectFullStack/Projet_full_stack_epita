# 🧪 CI/CD Pipeline Documentation

## 🎯 Objectif

Ce projet utilise GitHub Actions pour garantir que :

* le frontend Angular fonctionne correctement
* le backend Spring Boot fonctionne correctement
* la qualité du code est analysée (SonarCloud)
* aucun code cassé ne part en production

---

## 🧱 Architecture des workflows

Le pipeline est composé de 4 workflows :

| Workflow   | Rôle                                    |
| ---------- | --------------------------------------- |
| Angular CI | Tests frontend                          |
| Maven CI   | Tests backend                           |
| Quality CI | Validation globale + analyse Sonar      |
| Release CI | Build Docker + déploiement              |

---

## 🔄 Fonctionnement global

```text
Push / PR
    ↓
Quality CI
    ↓
Frontend OK + Backend OK ?
    ↓
Oui → Release CI
Non → STOP
````

---

## 🧪 Quality CI

Le workflow `Quality CI` est le cœur du pipeline.

Il exécute :

### 🔹 Frontend

* installation des dépendances (`npm ci`)
* build Angular (`npm run build:prod`)
* tests + couverture (`npm run coverage`)
* génération du rapport LCOV
* upload du coverage en artifact

---

### 🔹 Backend

* build Maven (`mvn clean verify`)
* exécution des tests
* génération du rapport JaCoCo (`jacoco.xml`)
* upload du coverage en artifact

---

### 🔹 Analyse SonarCloud

* récupération du coverage frontend (LCOV)
* build backend (classes + coverage)
* analyse qualité complète :

  * coverage frontend + backend
  * bugs
  * vulnérabilités
  * code smells

---

👉 Si un seul job échoue → tout le workflow échoue

👉 Logique stricte :

* frontend OK
* ET backend OK

---

## 📊 Analyse qualité (SonarCloud)

👉 Accessible ici :
[https://sonarcloud.io/project/overview?id=epitaProjectFullStack_Projet_full_stack_epita](https://sonarcloud.io/project/overview?id=epitaProjectFullStack_Projet_full_stack_epita)

### Permet de visualiser :

* couverture de code (frontend + backend)
* bugs
* vulnérabilités
* code smells
* duplications

---

### ⚠️ IMPORTANT

👉 Le **Quality Gate Sonar peut être en échec**

MAIS :

❗ **cela n’empêche PAS la pipeline de réussir**

👉 Sonar sert uniquement à informer et améliorer la qualité du code
👉 il ne bloque pas la release

---

## 🚀 Release CI

Le workflow `Release CI` est déclenché uniquement si :

* `Quality CI` est terminé
* ET `Quality CI` est SUCCESS

Configuration :

```yaml
on:
  workflow_run:
    workflows: ["Quality CI"]
    types:
      - completed
```

```yaml
if: github.event.workflow_run.conclusion == 'success'
```

---

### Actions réalisées

* génération d’un tag Git (version)
* build de l’image Docker
* push vers GitHub Container Registry

---

## 🎮 workflow_dispatch

Permet de lancer un workflow manuellement depuis GitHub :

👉 bouton "Run workflow"

Utilisations :

* tester le pipeline
* rejouer un build
* debug

---

## 📊 Couverture de code

### Frontend

* générée via `npm run coverage`
* format : LCOV

### Backend

* générée via JaCoCo (`mvn verify`)
* fichier utilisé par Sonar :

```
backend/target/site/jacoco/jacoco.xml
```

👉 Rapport HTML local :

```
backend/target/site/jacoco/index.html
```

---

## 🔐 Sécurité du pipeline

Le pipeline garantit :

❌ aucun déploiement si les tests échouent
✅ uniquement du code fonctionnel déployé

---

## 🧠 Bonnes pratiques

* utiliser des Pull Requests
* vérifier `Quality CI` avant merge
* ne pas ignorer les résultats Sonar
* améliorer progressivement le coverage
* tester sur branches `feature/*` avant `main`

---

## ⚠️ Limitation GitHub Actions

GitHub ne permet pas de faire dépendre un workflow de plusieurs workflows avec un "ET".

👉 Solution utilisée :

regrouper frontend + backend dans un seul workflow (`Quality CI`)

---

## ✅ Résultat

* pipeline robuste
* tests automatisés frontend + backend
* analyse qualité continue (SonarCloud)
* déploiement sécurisé
* conforme aux standards industriels
