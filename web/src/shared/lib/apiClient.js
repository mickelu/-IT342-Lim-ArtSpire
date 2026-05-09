import { getApiBaseUrl } from "./storage";

export async function postJson(path, payload) {
  const response = await fetch(`${getApiBaseUrl()}${path}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(resolveErrorMessage(data));
  }

  return data;
}

function resolveErrorMessage(data) {
  if (data?.message === "Validation failed" && data.errors) {
    return Object.values(data.errors).join(" ");
  }

  return data?.message || "Request failed.";
}
