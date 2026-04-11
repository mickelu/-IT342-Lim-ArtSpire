# ArtSpire Web

React frontend for the ArtSpire authentication flow.

## Run

1. Start the backend in `backend/` on `http://localhost:8081`.
2. In `web/`, run `npm install`.
3. Start the client with `npm run dev`.

## Routes

- `/login`
- `/register`
- `/dashboard`

## Main Feature

- Register a user through `POST /api/auth/register`
- Log in through `POST /api/auth/login`
- Show success and error messages from backend validation
- Display the authenticated user's basic profile after success
