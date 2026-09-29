# ReferralHub

ReferralHub is a referral management platform that helps candidates discover job opportunities, understand their match with a role, and request employee referrals — while helping employees review referral requests with relevant historical context.

## Problem

Getting a job referral can be difficult.

Candidates often have to:

- Find relevant job opportunities
- Search for employees at the company
- Reach out individually
- Explain their background repeatedly
- Track referral interactions manually

At the same time, employees may receive referral requests from multiple candidates and need to remember previous interactions, referral decisions, and candidate context.

ReferralHub brings this workflow into one platform.

## Solution

ReferralHub connects candidates, employees, jobs, matching, referrals, and persistent contextual memory.

### Candidate workflow

1. Create or maintain a candidate profile
2. Browse available jobs
3. View the match percentage for a job
4. See matched and missing skills
5. Request a referral from the employee associated with the job

### Employee workflow

1. View referral requests
2. See candidate/job match information
3. Review matched and missing skills
4. View relevant historical context
5. Make a referral decision

## Hindsight Integration

Hindsight is used as the persistent memory layer for ReferralHub.

The transactional application data is stored in MySQL, while Hindsight is used to retain and recall contextual information related to candidates, employees, jobs, and referral interactions.

This allows the application to move beyond only answering:

> "What is the candidate's current match?"

and also provide context such as:

> "What do we already know about this candidate and previous referral interactions?"

### Before Hindsight

The system can calculate the candidate's current job match, but historical interaction context may not be available.

### After Hindsight

Relevant historical context can be recalled when an employee reviews a referral request.

This helps prevent every referral interaction from starting from zero.

## Architecture

```text
                    ┌─────────────────────┐
                    │   Angular Frontend  │
                    │                     │
                    │ Candidate / Employee│
                    │       UI            │
                    └──────────┬──────────┘
                               │
                               │ REST API
                               ▼
                    ┌─────────────────────┐
                    │   Spring Boot       │
                    │      Backend        │
                    │                     │
                    │ Matching            │
                    │ Referrals           │
                    │ Authentication      │
                    │ Hindsight Service   │
                    └───────┬───────┬─────┘
                            │       │
                   ┌────────┘       └─────────┐
                   ▼                          ▼
          ┌─────────────────┐       ┌─────────────────┐
          │     MySQL       │       │    Hindsight    │
          │                 │       │                 │
          │ Users           │       │ Contextual      │
          │ Jobs            │       │ persistent      │
          │ Profiles        │       │ memory          │
          │ Referrals       │       │                 │
          └─────────────────┘       └─────────────────┘
Technology Stack
Frontend
Angular
TypeScript
HTML / CSS
Backend
Java
Spring Boot
Spring Data JPA
REST APIs
Database
MySQL
Memory
Hindsight Cloud
Other
Maven
Git / GitHub
Project Structure
referralhub-phase1/
│
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           └── resources/
│
├── frontend/
│   ├── package.json
│   ├── package-lock.json
│   └── src/
│
├── docker-compose.yml
├── README.md
└── .gitignore
Backend Setup

Navigate to the backend:

cd backend

Build the project:

mvn clean install

Run the Spring Boot application:

mvn spring-boot:run

The backend runs on:

http://localhost:8080
Frontend Setup

Navigate to the frontend:

cd frontend

Install dependencies:

npm install

Start Angular:

npm start

The frontend runs on the Angular development server.

Database

Configure the MySQL connection in the backend configuration.

Create the required MySQL database before starting the backend.

Do not commit database passwords, API keys, or other secrets to GitHub.

Hindsight Configuration

Hindsight configuration should be supplied through local/environment-specific configuration.

Do not commit the Hindsight API key to the repository.

The application uses Hindsight for retaining and recalling relevant contextual memory.

Core Features
Candidate registration/login
Employee login
Candidate profiles
Job discovery
Job details
Candidate-job matching
Matched and missing skill analysis
Referral requests
Employee referral dashboard
Referral decisions
Hindsight contextual memory
Historical referral context
Demo Flow

The intended demonstration flow is:

Candidate
   ↓
View Profile
   ↓
Browse Job
   ↓
Calculate Match
   ↓
Request Referral
   ↓
Employee Reviews Request
   ↓
Hindsight Recalls Context
   ↓
Employee Makes Decision
Why Hindsight?

Traditional application databases are useful for storing structured transactional information such as users, jobs, and referral requests.

ReferralHub uses Hindsight as an additional memory layer for contextual information.

This allows the application to recall relevant context when it is needed instead of treating every interaction as completely new.

Team

Team: [YOUR TEAM NAME]

Built for HackWith Hyderabad 3.0.

Repository

GitHub:

https://github.com/Sasi0612-beep/ReferralHub


### Then save and push

From:

```text
C:\Users\reddy\Downloads\referralhub-phase1-complete\referralhub-phase1

run:

git add README.md
git commit -m "Improve project documentation"
git pushuthentication: passwords are not hashed and there are no JWT/session tokens or Spring Security authorization rules yet.
