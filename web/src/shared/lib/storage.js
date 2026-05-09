const STORAGE_KEYS = {
  apiBaseUrl: "artspire.apiBaseUrl",
  currentUser: "artspire.currentUser"
};

export function getApiBaseUrl() {
  return localStorage.getItem(STORAGE_KEYS.apiBaseUrl) || "http://localhost:8081";
}

export function setApiBaseUrl(value) {
  localStorage.setItem(STORAGE_KEYS.apiBaseUrl, normalizeBaseUrl(value));
}

export function getStoredUser() {
  try {
    const raw = localStorage.getItem(STORAGE_KEYS.currentUser);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function setStoredUser(user) {
  localStorage.setItem(STORAGE_KEYS.currentUser, JSON.stringify(user));
}

export function clearStoredUser() {
  localStorage.removeItem(STORAGE_KEYS.currentUser);
}

function normalizeBaseUrl(value) {
  return (value || "").trim().replace(/\/+$/, "");
}
