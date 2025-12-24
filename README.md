# EmployeeDetails API (Demo)

This demo Spring Boot application exposes a secure REST API to fetch employee details with role-based access control.

Important notes:
- Authentication: demo JWT tokens issued by POST /auth/login using in-memory users (manager/analyst, both password: password).
- Authorization: ROLE_MANAGER can see Salary and SSN; ROLE_ANALYST cannot.
- API docs: OpenAPI UI available at /swagger-ui.html or /swagger-ui/index.html

Quick manual test (after starting app on localhost:8080):

1. Obtain token for manager:

```bash
curl -s -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"manager","password":"password"}'
```

2. Search employees (manager):

```bash
curl -s "http://localhost:8080/api/employees?firstName=John" -H "Authorization: Bearer <TOKEN>"
```

Replace <TOKEN> with the token returned by the login call.

Security disclaimer: This is a demo. Do not use the in-memory secret or in-memory users in production. Use HTTPS, a proper identity provider, and secure secret storage.

