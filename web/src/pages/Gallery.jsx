import { useEffect, useState } from "react";

export default function Gallery() {
  const [artworks, setArtworks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchArtworks();
  }, []);

  async function fetchArtworks() {
    try {
      const response = await fetch("http://localhost:8081/api/artworks", {
        headers: {
          "Authorization": "Basic " + btoa("admin:admin")
        }
      });
      const data = await response.json();
      setArtworks(data);
    } catch (err) {
      alert("Failed to load artworks");
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id, imageUrl) {
    if (window.confirm("Are you sure you want to delete this artwork from Supabase?")) {
      try {
        const response = await fetch(`http://localhost:8081/api/artworks/${id}`, {
          method: "DELETE",
          headers: {
            "Authorization": "Basic " + btoa("admin:admin")
          }
        });

        if (response.ok) {
          setArtworks(artworks.filter(art => art.id !== id));
          alert("Artwork deleted from Supabase successfully");
        } else {
          const error = await response.text();
          alert("Failed to delete artwork: " + error);
        }
      } catch (err) {
        alert("Error deleting artwork");
      }
    }
  }

  function formatDate(dateString) {
    if (!dateString) return "Unknown date";
    return new Date(dateString).toLocaleDateString();
  }

  if (loading) {
    return (
      <div className="gallery-container">
        <div className="loading">
          <div className="loading-spinner"></div>
          <p>Loading artworks from Supabase...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="gallery-container">
      <div className="dashboard-panel" style={{ padding: "32px" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px" }}>
          <h2>🎨 Artwork Gallery (Supabase Storage)</h2>
          <button
            className="primary-button"
            onClick={() => window.location.href = "/upload"}
            style={{ marginTop: 0 }}
          >
            + Upload New
          </button>
        </div>

        {artworks.length === 0 ? (
          <div className="empty-gallery">
            <p>No artworks yet. Upload your first artwork to Supabase!</p>
            <button className="primary-button" onClick={() => window.location.href = "/upload"}>
              Upload Artwork
            </button>
          </div>
        ) : (
          <div className="gallery-grid">
            {artworks.map((art) => (
              <div key={art.id} className="gallery-card">
                <img
                  src={art.imageUrl}
                  alt={art.title}
                  className="gallery-image"
                  onError={(e) => {
                    e.target.src = "https://via.placeholder.com/400x300?text=Image+Loading+Failed";
                  }}
                />
                <div className="gallery-content">
                  <h3 className="gallery-title">{art.title}</h3>
                  <p className="gallery-description">
                    {art.description || "No description provided"}
                  </p>
                  <div className="gallery-meta">
                    <span className="gallery-date">
                      📅 {formatDate(art.createdAt)}
                    </span>
                    <button
                      className="delete-button"
                      onClick={() => handleDelete(art.id, art.imageUrl)}
                    >
                      🗑️ Delete from Supabase
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}