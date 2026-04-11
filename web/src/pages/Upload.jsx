import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Upload() {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState(null);
  const [loading, setLoading] = useState(false);

  function handleImageChange(e) {
    const file = e.target.files[0];
    if (file) {
      // Validate file type
      if (!file.type.startsWith('image/')) {
        alert("Please select an image file");
        return;
      }

      // Validate file size (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        alert("File size must be less than 5MB");
        return;
      }

      setImageFile(file);

      // Create preview URL
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result);
      };
      reader.readAsDataURL(file);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!title.trim()) {
      alert("Please enter a title");
      return;
    }

    if (!imageFile) {
      alert("Please select an image");
      return;
    }

    setLoading(true);

    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("description", description);
      formData.append("image", imageFile);

      const response = await fetch("http://localhost:8081/api/artworks", {
        method: "POST",
        headers: {
          "Authorization": "Basic " + btoa("admin:admin")
        },
        body: formData
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || "Upload failed");
      }

      alert("Artwork uploaded successfully to Supabase!");
      navigate("/gallery");
    } catch (err) {
      alert(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="upload-container">
      <div className="upload-form">
        <h2 className="upload-title">📤 Upload Artwork to Supabase</h2>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Title *</label>
            <input
              type="text"
              placeholder="Enter artwork title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              placeholder="Describe your artwork"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows="4"
            />
          </div>

          <div className="form-group">
            <label>Image File *</label>
            <input
              type="file"
              accept="image/jpeg,image/png,image/gif,image/jpg"
              onChange={handleImageChange}
              required
            />
            <small style={{ color: "#64574d", display: "block", marginTop: "5px" }}>
              Supported formats: JPG, PNG, GIF (Max 5MB)
            </small>
          </div>

          {preview && (
            <div className="image-preview">
              <img src={preview} alt="Preview" />
              <p className="preview-text">📷 {imageFile?.name} ({(imageFile?.size / 1024).toFixed(2)} KB)</p>
            </div>
          )}

          <button type="submit" className="primary-button" disabled={loading}>
            {loading ? "Uploading to Supabase..." : "🚀 Upload to Supabase"}
          </button>
        </form>
      </div>
    </div>
  );
}