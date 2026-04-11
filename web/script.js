const storageKeys = {
    apiBaseUrl: "artspire.apiBaseUrl",
    currentUser: "artspire.currentUser"
};

const apiBaseUrlInput = document.querySelector("#apiBaseUrl");
const feedback = document.querySelector("#feedback");
const loginForm = document.querySelector("#loginForm");
const registerForm = document.querySelector("#registerForm");
const tabs = document.querySelectorAll(".tab");
const tabPanels = document.querySelectorAll(".tab-panel");
const dashboardPanel = document.querySelector("#dashboardPanel");
const logoutButton = document.querySelector("#logoutButton");
const welcomeTitle = document.querySelector("#welcomeTitle");
const profileName = document.querySelector("#profileName");
const profileEmail = document.querySelector("#profileEmail");

initialize();

function initialize() {
    const savedApiBaseUrl = localStorage.getItem(storageKeys.apiBaseUrl);
    if (savedApiBaseUrl) {
        apiBaseUrlInput.value = savedApiBaseUrl;
    }

    const savedUser = readStoredUser();
    if (savedUser) {
        showDashboard(savedUser);
    }

    apiBaseUrlInput.addEventListener("change", () => {
        localStorage.setItem(storageKeys.apiBaseUrl, normalizeBaseUrl(apiBaseUrlInput.value));
    });

    tabs.forEach((tab) => {
        tab.addEventListener("click", () => activateTab(tab.dataset.tabTarget));
    });

    loginForm.addEventListener("submit", handleLogin);
    registerForm.addEventListener("submit", handleRegister);
    logoutButton.addEventListener("click", handleLogout);
}

async function handleLogin(event) {
    event.preventDefault();
    clearFeedback();

    const payload = {
        email: loginForm.email.value.trim(),
        password: loginForm.password.value
    };

    const validationMessage = validateLogin(payload);
    if (validationMessage) {
        showFeedback(validationMessage, "error");
        return;
    }

    await submitAuth("/api/auth/login", payload, "Login successful");
}

async function handleRegister(event) {
    event.preventDefault();
    clearFeedback();

    const payload = {
        name: registerForm.name.value.trim(),
        email: registerForm.email.value.trim(),
        password: registerForm.password.value
    };

    const validationMessage = validateRegister(payload);
    if (validationMessage) {
        showFeedback(validationMessage, "error");
        return;
    }

    await submitAuth("/api/auth/register", payload, "Registration successful");
}

async function submitAuth(path, payload, successPrefix) {
    const baseUrl = normalizeBaseUrl(apiBaseUrlInput.value);
    localStorage.setItem(storageKeys.apiBaseUrl, baseUrl);

    try {
        const response = await fetch(`${baseUrl}${path}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        const data = await parseResponse(response);

        if (!response.ok) {
            showFeedback(resolveErrorMessage(data), "error");
            return;
        }

        const user = {
            name: data.name || payload.name || "User",
            email: data.email || payload.email
        };

        localStorage.setItem(storageKeys.currentUser, JSON.stringify(user));
        showFeedback(`${successPrefix}: ${data.message}`, "success");
        showDashboard(user);
        loginForm.reset();
        registerForm.reset();
    } catch (error) {
        showFeedback("Unable to connect to the backend API. Check the base URL and ensure the server is running.", "error");
    }
}

function validateLogin(payload) {
    if (!payload.email || !payload.password) {
        return "Email and password are required.";
    }

    if (!isValidEmail(payload.email)) {
        return "Enter a valid email address.";
    }

    return "";
}

function validateRegister(payload) {
    if (!payload.name || !payload.email || !payload.password) {
        return "Name, email, and password are required.";
    }

    if (payload.name.length < 2) {
        return "Name must be at least 2 characters.";
    }

    if (!isValidEmail(payload.email)) {
        return "Enter a valid email address.";
    }

    if (payload.password.length < 8) {
        return "Password must be at least 8 characters.";
    }

    return "";
}

function showDashboard(user) {
    dashboardPanel.classList.remove("hidden");
    welcomeTitle.textContent = `Welcome, ${user.name}`;
    profileName.textContent = user.name;
    profileEmail.textContent = user.email;
}

function handleLogout() {
    localStorage.removeItem(storageKeys.currentUser);
    dashboardPanel.classList.add("hidden");
    activateTab("loginPanel");
    showFeedback("You have been logged out.", "success");
}

function activateTab(targetId) {
    tabs.forEach((tab) => {
        tab.classList.toggle("is-active", tab.dataset.tabTarget === targetId);
    });

    tabPanels.forEach((panel) => {
        panel.classList.toggle("is-active", panel.id === targetId);
        panel.classList.toggle("hidden", panel.id !== targetId);
    });
}

function showFeedback(message, type) {
    feedback.textContent = message;
    feedback.className = `feedback ${type}`;
}

function clearFeedback() {
    feedback.textContent = "";
    feedback.className = "feedback hidden";
}

function parseResponse(response) {
    return response.json().catch(() => ({}));
}

function resolveErrorMessage(data) {
    if (data?.message === "Validation failed" && data.errors) {
        return Object.values(data.errors).join(" ");
    }

    return data?.message || "Request failed.";
}

function normalizeBaseUrl(value) {
    return (value || "").trim().replace(/\/+$/, "");
}

function isValidEmail(value) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

function readStoredUser() {
    try {
        const raw = localStorage.getItem(storageKeys.currentUser);
        return raw ? JSON.parse(raw) : null;
    } catch {
        return null;
    }
}
