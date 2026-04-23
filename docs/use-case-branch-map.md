# Use Case Branch Map

This repository uses `.idea/project-pulse-ai` as the source of truth for requirements, while implementation lives at the repository root.

## Branches

- `main`: stable integration branch
- `feature/foundation-bootstrap`: repository structure, backend foundation, frontend foundation
- `feature/auth-account`: student and instructor account setup and login flows
- `feature/admin-rubrics-sections`: rubric management and section management
- `feature/admin-team-management`: team creation, editing, membership, and instructor assignment
- `feature/admin-people-management`: student and instructor invitation, discovery, activation, and removal flows
- `feature/student-war-peer-eval`: WAR and peer evaluation submission
- `feature/reporting`: WAR and peer evaluation reports

## Use Case Allocation

### `feature/auth-account`

- UC-25 Student sets up a student account
- UC-26 Student edits an account
- UC-30 Instructor sets up an instructor account

### `feature/admin-rubrics-sections`

- UC-1 Admin creates a rubric
- UC-2 Admin finds senior design sections
- UC-3 Admin views a senior design section
- UC-4 Admin creates a senior design section
- UC-5 Admin edits a senior design section
- UC-6 Admin sets up active weeks for a senior design section

### `feature/admin-team-management`

- UC-7 Admin/Instructor finds senior design teams
- UC-8 Admin/Instructor views a senior design team
- UC-9 Admin creates a senior design team
- UC-10 Admin edits a senior design team
- UC-12 Admin assigns students to senior design teams
- UC-13 Admin removes a student from a senior design team
- UC-14 Admin deletes a senior design team
- UC-19 Admin assigns instructors to senior design teams
- UC-20 Admin removes an instructor from a senior design team

### `feature/admin-people-management`

- UC-11 Admin invites students to join a senior design section
- UC-15 Admin/Instructor finds students
- UC-16 Admin/Instructor views a student
- UC-17 Admin deletes a student
- UC-18 Admin invites instructors to register an account
- UC-21 Admin finds instructors
- UC-22 Admin views an instructor
- UC-23 Admin deactivates an instructor
- UC-24 Admin reactivates an instructor

### `feature/student-war-peer-eval`

- UC-27 Student manages activities in a WAR
- UC-28 Student submits a peer evaluation for the previous week
- UC-29 Student views her own peer evaluation report

### `feature/reporting`

- UC-31 Instructor generates a peer evaluation report of the entire section
- UC-32 Instructor/Student generates a WAR report of a team
- UC-33 Instructor generates a peer evaluation report of a student
- UC-34 Instructor generates a WAR report of a student
