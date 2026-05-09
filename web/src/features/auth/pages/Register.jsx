import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import AuthLayout from "../components/AuthLayout";
import { register } from "../services/authService";
import { validateRegisterForm } from "../utils/authValidation";
import { setStoredUser } from "../../../shared/lib/storage";

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

    const validationMessage = validateRegisterForm(form);
    if (validationMessage) {
      setFeedback({ type: "error", message: validationMessage });
      return;
    }

    setIsSubmitting(true);
    setFeedback(null);

    try {
      const data = await register(form);
      setStoredUser({
        name: data.name || form.name,
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
