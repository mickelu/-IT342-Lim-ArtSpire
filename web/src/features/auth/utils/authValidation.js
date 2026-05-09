const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function validateLoginForm(form) {
  if (!form.email.trim() || !form.password) {
    return "Email and password are required.";
  }

  if (!EMAIL_PATTERN.test(form.email)) {
    return "Enter a valid email address.";
  }

  return "";
}

export function validateRegisterForm(form) {
  if (!form.name.trim() || !form.email.trim() || !form.password) {
    return "Name, email, and password are required.";
  }

  if (form.name.trim().length < 2) {
    return "Name must be at least 2 characters.";
  }

  if (!EMAIL_PATTERN.test(form.email)) {
    return "Enter a valid email address.";
  }

  if (form.password.length < 8) {
    return "Password must be at least 8 characters.";
  }

  return "";
}
