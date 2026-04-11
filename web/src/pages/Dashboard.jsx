import { useLocation, useNavigate } from "react-router-dom";
import { clearStoredUser, getStoredUser } from "../lib/storage";

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

  function handleGoToGallery() {
    navigate("/gallery");
  }

  function handleGoToUpload() {
    navigate("/upload");
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

        {location.state?.message ? (
          <div className="feedback success" role="status">
            {location.state.message}
          </div>
        ) : null}

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

        {/* New Artwork Actions Section */}
        <div className="artwork-actions-section">
          <h2 className="section-title">Artwork Gallery</h2>
          <div className="action-buttons">
            <button
              className="primary-button"
              type="button"
              onClick={handleGoToUpload}
            >
              📤 Upload Artwork
            </button>
            <button
              className="secondary-button"
              type="button"
              onClick={handleGoToGallery}
            >
              🖼️ View Gallery
            </button>
          </div>
          <p className="section-copy">
            Share your artistic creations or explore the artwork collection.
          </p>
        </div>
      </section>
    </main>
  );
}