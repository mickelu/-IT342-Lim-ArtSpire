import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { postJson } from "../supabaseClient";
import { setStoredUser } from "../lib/storage";

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: "", password: "" });
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
      const data = await postJson("/api/auth/login", form);
      setStoredUser({
        name: data.name || "User",
        email: data.email || form.email
      });
      navigate("/dashboard", {
        replace: true,
        state: { message: data.message }
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
      title="Access your creative workspace."
      subtitle="Sign in using your registered account to verify the web app is connected to the backend and database."
    >
      <h2>Welcome back</h2>
      <p className="panel-copy">Log in using the account stored in your database.</p>

      <form onSubmit={handleSubmit} noValidate>
        <label className="field-label" htmlFor="loginEmail">
          Email
        </label>
        <input
          id="loginEmail"
          name="email"
          type="email"
          placeholder="you@example.com"
          value={form.email}
          onChange={(event) => setForm({ ...form, email: event.target.value })}
        />

        <label className="field-label" htmlFor="loginPassword">
          Password
        </label>
        <input
          id="loginPassword"
          name="password"
          type="password"
          placeholder="Minimum 8 characters"
          value={form.password}
          onChange={(event) => setForm({ ...form, password: event.target.value })}
        />

        <button className="primary-button" type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Logging in..." : "Log In"}
        </button>
      </form>

      <p className="inline-link">
        No account yet? <Link to="/register">Create one here.</Link>
      </p>
    </AuthLayout>
  );
}

function validate(form) {
  if (!form.email.trim() || !form.password) {
    return "Email and password are required.";
  }

  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    return "Enter a valid email address.";
  }

  return "";
}
