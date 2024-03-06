# Blogging Platform

Developed by Yusuf Kabir Adekunle

Welcome to the Blogging Platform, a comprehensive blogging solution featuring a Vue.js frontend and a Java Spring Boot with FastAPI backend. This platform utilizes a microservices architecture, with distinct services for geolocation and payment processing. This is a project I built when I was training to be a full stack engineer. The project setup was done by our instructor, I collaborated with two other students of the academy to demonstrate our collaboration skills. I designed over 90% of the project myself.

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Setup](#environment-setup)
  - [Running the Application](#running-the-application)
- [Features](#features)
- [Testing](#testing)
- [Contact](#contact)

## Getting Started

Follow these instructions to set up and run the LevelUp Blogging Platform.

### Prerequisites

- Docker
- Java Development Kit (JDK) and Maven for the Spring Boot application.
- IntelliJ IDEA or equivalent IDE for Java development.
- Vs code
- Docker Desktop (Windows & Mac)

### Environment Setup

1. Create a `.env` file in the project's root directory.
2. In the `/frontend` directory, create an additional `.env` file.
3. Populate both `.env` files with the contents from `.env.example`.

### Running the Application

Start the application using Docker Compose with the command:

docker-compose up

To run the Spring Boot application, navigate to the `backend` directory and execute:

mvn spring-boot:run

Alternatively, the application can be launched via IntelliJ IDEA.

## Features

- User Authentication: Register and log into the platform securely.
- Article Management: Post articles and manage content.
- Profile Management: Customize user profiles.
- Geolocation Service: FastAPI service to identify user location from their IP address.
- Payment Gateway: A FastAPI service dedicated to processing payments.
- Security: JWT implementation for secure access.
- Mail Service: Sends verification tokens upon registration for email validation.

## Testing

Comprehensive unit and integration tests can be run with:

mvn test

## Contact

For inquiries or support, contact kabiryusuf2307@gmail.com
