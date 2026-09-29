# ReferralHub — Complete Phase 1 MVP

ReferralHub is a referral platform where employees publish job opportunities and candidates discover jobs, evaluate their match, request referrals, and receive referral decisions. Hindsight provides persistent contextual memory across candidate, employee, job, and referral interactions.

## Phase 1 scope

- Candidate and employee registration/login
- Role-based navigation and route protection
- Candidate profile with skills, graduation year, phone, location, experience, projects and preferences
- Employee company creation/selection
- Employee job creation
- Latest-jobs feed on the candidate homepage
- Candidate/job skill matching
- Referral request creation
- Employee referral dashboard
- Refer / Don't Refer decisions
- Hindsight retain/recall integration
- Candidate, employee and job memory scopes
- Seeded demo data for a reliable demo

## Architecture

Angular 20 -> Spring Boot 3 / Java 17 -> MySQL
                                      \
                                       -> Hindsight REST API

MySQL is the transactional source of truth. Hindsight is contextual agent memory and is never used as the application database.

## Run

### 1. MySQL

From the repository root:

```bash
docker compose up -d mysql
```

Or use a local MySQL 8 instance with database `referralhub`, username `root`, password `root`.

### 2. Backend

Requirements: Java 17+ and Maven 3.9+.

```bash
cd backend
mvn spring-boot:run
```

Backend: http://localhost:8080

The JDBC URL already includes `allowPublicKeyRetrieval=true` for MySQL 8 setups that require it.

### 3. Frontend

Requirements: Node.js 20+.

```bash
cd frontend
npm install
npm start
```

Frontend: http://localhost:4200

### 4. Demo accounts

Employee:
- Email: `ananya@referralhub.demo`
- Password: `employee123`

Candidate:
- Email: `rahul@referralhub.demo`
- Password: `candidate123`

New accounts can be created from the Register page. Candidate registration sends the user directly to the candidate profile so the matching data is entered before applying.

## Hindsight

Hindsight's local API is available on port 8888 and requires an LLM provider for memory extraction. The official quickstart documents the local Docker setup and the retain/recall APIs.

Example local setup:

```bash
docker run -it --pull always --name hindsight --restart unless-stopped \
  --shm-size=1g \
  -p 8888:8888 -p 9999:9999 \
  -e HINDSIGHT_API_LLM_API_KEY=$OPENAI_API_KEY \
  -v hindsight-data:/home/hindsight/.pg0 \
  ghcr.io/vectorize-io/hindsight:latest
```

Then start Spring Boot with:

```text
HINDSIGHT_ENABLED=true
HINDSIGHT_BASE_URL=http://localhost:8888
HINDSIGHT_API_KEY=
```

For Hindsight Cloud, set the Cloud base URL and API key instead.

ReferralHub uses separate memory banks/scopes such as:

- `referralhub-candidate-2`
- `referralhub-employee-1`
- `referralhub-job-MS-SDE-1024`

The backend retains profile updates, job postings, referral requests and referral decisions. During candidate analysis it recalls relevant context from candidate, employee and job memory.

## Demo sequence

1. Login as Rahul or create a candidate account.
2. Complete the candidate profile.
3. Open a new job from the homepage.
4. Analyze the candidate/job match.
5. Request a referral.
6. Logout and login as Ananya or an employee account.
7. Open Employee Dashboard.
8. Review the candidate match and click `Show AI + Hindsight Context`.
9. Refer or Don't Refer.
10. Repeat the candidate/referral scenario after Hindsight has stored prior interactions to demonstrate recalled context.

## Security note

This Phase 1 implementation intentionally uses simple server-side email/password validation so the end-to-end product can be demonstrated quickly. It is not production authentication: passwords are not hashed and there are no JWT/session tokens or Spring Security authorization rules yet.
