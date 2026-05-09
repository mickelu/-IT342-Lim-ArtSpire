import { useLocation, useNavigate } from "react-router-dom";
import FeedbackMessage from "../../../shared/components/FeedbackMessage";
import { clearStoredUser, getStoredUser } from "../../../shared/lib/storage";

export default function Dashboard() {
  const navigate = useNavigate();
  const location = useLocation();
  const user = getStoredUser();

  function handleLogout() {
    clearStoredUser();
    navigate("/login", {
      replace: true,
      state: { message: "You have been logged out." }
    });
  }

  return (
    <main className="dashboard-shell">
      <section className="dashboard-panel">
        <div className="dashboard-header">
          <div>
            <p className="eyebrow">Authenticated Session</p>
            <h1 className="dashboard-title">Welcome, {user?.name || "User"}</h1>
            <p className="panel-copy">
              The React frontend is now connected to the backend authentication API and database.
            </p>
          </div>
          <button className="secondary-button" type="button" onClick={handleLogout}>
            Log Out
          </button>
        </div>

        <FeedbackMessage feedback={{ type: "success", message: location.state?.message }} />

        <div className="dashboard-grid">
          <article className="dashboard-card">
            <p className="card-label">Name</p>
            <h3>{user?.name || "-"}</h3>
          </article>
          <article className="dashboard-card">
            <p className="card-label">Email</p>
            <h3>{user?.email || "-"}</h3>
          </article>
          <article className="dashboard-card">
            <p className="card-label">Feature Status</p>
            <h3>Connected</h3>
            <p className="card-copy">
              Registration and login requests are saved and validated through the Spring Boot backend.
            </p>
          </article>
        </div>
      </section>
    </main>
  );
}
