# 🧪 CI/CD Pipeline Documentation

## 🎯 Objectif

Ce projet utilise GitHub Actions pour garantir que :

* le frontend Angular fonctionne correctement
* le backend Spring Boot fonctionne correctement
* aucun code cassé ne part en production

---

## 🧱 Architecture des workflows

Le pipeline est composé de 4 workflows :

| Workflow   | Rôle                                    |
| ---------- | --------------------------------------- |
| Angular CI | Tests frontend                          |
| Maven CI   | Tests backend                           |
| Quality CI | Validation globale (frontend + backend) |
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
```

---

## 🧪 Quality CI

Le workflow `Quality CI` exécute les mêmes étapes que les pipelines individuels :

### Frontend

* installation des dépendances (`npm ci`)
* build Angular (`npm run build:prod`)
* tests + couverture (`npm run coverage`)
* upload du rapport de coverage

### Backend

* build Maven (`mvn clean verify`)
* exécution des tests
* génération du rapport JaCoCo
* upload du rapport

👉 Si un seul job échoue → tout le workflow échoue

👉 Cela garantit une logique **ET stricte** :

* frontend OK
* ET backend OK

---

## 🚀 Release CI

Le workflow `Release CI` est déclenché uniquement si :

* `Quality CI` est terminé
* ET `Quality CI` est SUCCESS

Grâce à :

```yaml
on:
  workflow_run:
    workflows: ["Quality CI"]
    types:
      - completed
```

et :

```yaml
if: github.event.workflow_run.conclusion == 'success'
```

---

### Actions réalisées

* génération d’une version (tag Git)
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

* Frontend : via `npm run coverage`
* Backend : via JaCoCo (`mvn verify`)

Rapport backend :

```
backend/target/site/jacoco/index.html
```

---

## 🔐 Sécurité du pipeline

Le pipeline garantit :

❌ aucun déploiement si les tests échouent
✅ uniquement du code validé en production

---

## 🧠 Bonnes pratiques

* utiliser des Pull Requests
* vérifier Quality CI avant merge
* ne jamais bypass les checks
* tester sur branche `dev` avant `main`

---

## ⚠️ Limitation GitHub Actions

GitHub ne permet pas de faire dépendre un workflow de plusieurs workflows avec un "ET".

👉 Solution utilisée :

regrouper frontend + backend dans un seul workflow (`Quality CI`)

---

## ✅ Résultat

* pipeline robuste
* logique fiable
* déploiement sécurisé
* conforme aux standards industriels

---
