#Build frontend
FROM node:lts-alpine3.22 AS front-builder

WORKDIR /app

COPY frontend/package.json frontend/package-lock.json* .

RUN --mount=type=cache,target=/root/.npm npm ci

COPY ./frontend/ .

RUN npm install -g @angular/cli
RUN npm install
RUN npm run build:prod

#Build backend
FROM maven:3.9.8-eclipse-temurin-21 AS back-builder
WORKDIR /app
COPY backend/pom.xml ./
RUN mvn -q -e dependency:go-offline

COPY backend/src ./src
COPY --from=front-builder /app/dist/*/browser ./src/main/resources/static/

RUN mvn -q -Dmaven.test.skip package


#Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=back-builder /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
