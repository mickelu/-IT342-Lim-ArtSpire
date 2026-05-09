import { postJson } from "../../../shared/lib/apiClient";

export function login(credentials) {
  return postJson("/api/auth/login", credentials);
}

export function register(details) {
  return postJson("/api/auth/register", details);
}
