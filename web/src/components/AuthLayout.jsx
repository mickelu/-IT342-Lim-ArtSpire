import { Link, useLocation } from "react-router-dom";
import { getApiBaseUrl, setApiBaseUrl } from "../lib/storage";

export default function AuthLayout({ title, subtitle, children, feedback }) {
  const location = useLocation();

  return (
    <main className="page-shell">
      <section className="hero-panel">
        <p className="eyebrow">ArtSpire Web Portal</p>
        <h1>{title}</h1>
        <p className="hero-copy">{subtitle}</p>

        <div className="status-card">
          <p className="status-label">Backend API</p>
          <label className="field-label" htmlFor="apiBaseUrl">
            Base URL
          </label>
          <input
            id="apiBaseUrl"
            defaultValue={getApiBaseUrl()}
            onChange={(event) => setApiBaseUrl(event.target.value)}
            type="url"
          />
          <p className="status-hint">Use the same host and port where the backend is running.</p>
        </div>
      </section>

      <section className="app-panel">
        <div className="tabs">
          <Link className={location.pathname === "/login" ? "tab is-active" : "tab"} to="/login">
            Login
          </Link>
          <Link
            className={location.pathname === "/register" ? "tab is-active" : "tab"}
            to="/register"
          >
            Register
          </Link>
        </div>

        {feedback ? (
          <div className={`feedback ${feedback.type}`} role="status" aria-live="polite">
            {feedback.message}
          </div>
        ) : null}

        <section className="tab-panel">{children}</section>
      </section>
    </main>
  );
}
