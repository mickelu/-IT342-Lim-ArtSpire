# ArtSpire Web

Static web frontend for the ArtSpire authentication flow.

## Run

1. Start the backend in `backend/` on `http://localhost:8081`.
2. Serve this folder with any static server.
3. Open `index.html` through that server and use the login or register form.

## Main Feature

- Register a user through `POST /api/auth/register`
- Log in through `POST /api/auth/login`
- Show success and error messages from backend validation
- Display the authenticated user's basic profile after success
