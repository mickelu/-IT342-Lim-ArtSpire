import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { setStoredUser } from "../lib/storage";

export default function Register() {
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [feedback, setFeedback] = useState(
    location.state?.message ? { type: "success", message: location.state.message } : null
  );
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();

    const validationMessage = validate(form);
    if (validationMessage) {
      setFeedback({ type: "error", message: validationMessage });
      return;
    }

    setIsSubmitting(true);
    setFeedback(null);

    try {
      const response = await fetch("http://localhost:8081/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(form)
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Registration failed");
      }

      setStoredUser({
        name: data.name || form.name,
        email: data.email || form.email
      });

      navigate("/dashboard", {
        replace: true,
        state: { message: data.message || "Registration successful" }
      });

    } catch (error) {
      setFeedback({ type: "error", message: error.message });
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <AuthLayout
      feedback={feedback}
      title="Create an account for ArtSpire."
      subtitle="Register a new user, store the record in the database, and continue directly to the dashboard after success."
    >
      <h2>Create account</h2>
      <p className="panel-copy">Register a new user and save the record to the backend database.</p>

      <form onSubmit={handleSubmit} noValidate>
        <label className="field-label" htmlFor="registerName">
          Full name
        </label>
        <input
          id="registerName"
          name="name"
          type="text"
          placeholder="Juan Dela Cruz"
          value={form.name}
          onChange={(event) => setForm({ ...form, name: event.target.value })}
        />

        <label className="field-label" htmlFor="registerEmail">
          Email
        </label>
        <input
          id="registerEmail"
          name="email"
          type="email"
          placeholder="you@example.com"
          value={form.email}
          onChange={(event) => setForm({ ...form, email: event.target.value })}
        />

        <label className="field-label" htmlFor="registerPassword">
          Password
        </label>
        <input
          id="registerPassword"
          name="password"
          type="password"
          placeholder="Minimum 8 characters"
          value={form.password}
          onChange={(event) => setForm({ ...form, password: event.target.value })}
        />

        <button className="primary-button" type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Creating account..." : "Create Account"}
        </button>
      </form>

      <p className="inline-link">
        Already registered? <Link to="/login">Go to login.</Link>
      </p>
    </AuthLayout>
  );
}

function validate(form) {
  if (!form.name.trim() || !form.email.trim() || !form.password) {
    return "Name, email, and password are required.";
  }

  if (form.name.trim().length < 2) {
    return "Name must be at least 2 characters.";
  }

  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    return "Enter a valid email address.";
  }

  if (form.password.length < 8) {
    return "Password must be at least 8 characters.";
  }

  return "";
}