# 🎟️ Prueba-Double_Partners_Farmatodo

Proyecto de gestión de **tickets** desarrollado con **Spring Boot 3 + Swagger/OpenAPI +  Docker**.

---

## 📑 Tabla de Contenidos
1. [Requisitos](#-requisitos)
2. [Ejecución del Backend](#-ejecución-del-backend)
3. [Ejecución con Docker](#-ejecución-con-docker)
4. [Swagger – Documentación de la API](#-swagger--documentación-de-la-api)
5. [Colección de Postman](#-colección-de-postman)
6. [Autor](#-autor)

---

## ✅ Requisitos

Asegúrate de tener instalados:

- ☕ **Java 17+**
- 🐘 **Maven 3.9+**
- 🐳 **Docker + Docker Compose**

---

## 🚀 Ejecución del Backend

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/edyson10/Prueba-Double_Partners_Farmatodo.git
   cd Prueba-Double_Partners_Farmatodo

2. Compila el proyecto:
   ```bash
   mvn clean package -DskipTests

3. Ejecutar el JAR:
   ```bash
   java -jar target/Sistema_Ticket-0.0.1-SNAPSHOT.jar

4. El backend estará disponible en:
   ```bash
   http://localhost:8080

## 🚀 Ejecución con Docker

1. Construir la imagen del backend:
   ```bash
   docker build -t sistema-ticket .

2. Levantar el contenedor:
   ```bash
   docker run -d -p 8080:8080 --name sistema-ticket-container sistema-ticket

## 🚀 Swagger – Documentación de la API

Una vez levantado el backend, accede a la documentación interactiva en:

[Swagger](http://localhost:8080/swagger-ui.html)


## 🚀 Colección de Postman

1. Abre Postman.

2. Clic en Import → Upload Files.

3. Selecciona el archivo:
    ```bash
   docs/postman/Prueba - Double Partners.postman_collection.json

4. Ejecuta las requests preconfiguradas.

## 🚀 Autor

# Edyson Fabian Leal Marin
Ingeniero de Sistemas – Desarrollador Fullstack 🚀
