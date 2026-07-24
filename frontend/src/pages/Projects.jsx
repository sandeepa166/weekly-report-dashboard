import { useEffect, useState } from "react";
import api from "../services/api";

function Projects() {
  const user = JSON.parse(localStorage.getItem("user"));

  const [projects, setProjects] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });

  const [editingProjectId, setEditingProjectId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const response = await api.get("/projects");
      setProjects(response.data);
    } catch (err) {
      setError("Failed to load projects");
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const resetForm = () => {
    setFormData({
      name: "",
      description: "",
    });
    setEditingProjectId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      if (editingProjectId) {
        await api.put(`/projects/${editingProjectId}`, formData);
        setMessage("Project updated successfully");
      } else {
        await api.post("/projects", formData);
        setMessage("Project added successfully");
      }

      resetForm();
      fetchProjects();
    } catch (err) {
      setError(err.response?.data?.error || "Failed to save project");
    }
  };

  const handleEdit = (project) => {
    setEditingProjectId(project.id);
    setFormData({
      name: project.name,
      description: project.description || "",
    });
  };

  const handleDelete = async (projectId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this project?"
    );

    if (!confirmDelete) {
      return;
    }

    setMessage("");
    setError("");

    try {
      await api.delete(`/projects/${projectId}`);
      setMessage("Project deleted successfully");
      fetchProjects();
    } catch (err) {
      setError(err.response?.data?.error || "Failed to delete project");
    }
  };

  if (!user) {
    return (
      <div>
        <h1>Projects / Categories</h1>
        <p className="error">Please login first.</p>
      </div>
    );
  }

  if (user.role !== "MANAGER") {
    return (
      <div>
        <h1>Projects / Categories</h1>
        <p className="error">Only managers can manage projects.</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Projects / Categories</h1>

      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}

      <div className="form-container">
        <h2>{editingProjectId ? "Edit Project" : "Add New Project"}</h2>

        <form onSubmit={handleSubmit}>
          <label>Project Name</label>
          <input
            type="text"
            name="name"
            placeholder="Example: Internal Tooling"
            value={formData.name}
            onChange={handleChange}
            required
          />

          <label>Description</label>
          <textarea
            name="description"
            rows="3"
            placeholder="Project or category description"
            value={formData.description}
            onChange={handleChange}
          />

          <button type="submit">
            {editingProjectId ? "Update Project" : "Add Project"}
          </button>

          {editingProjectId && (
            <button
              type="button"
              className="cancel-btn"
              onClick={resetForm}
            >
              Cancel Edit
            </button>
          )}
        </form>
      </div>

      <div className="dashboard-section">
        <h2>Project List</h2>

        {projects.length === 0 ? (
          <p>No projects found.</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Project Name</th>
                <th>Description</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {projects.map((project) => (
                <tr key={project.id}>
                  <td>{project.id}</td>
                  <td>{project.name}</td>
                  <td>{project.description || "No description"}</td>
                  <td>
                    <button
                      className="edit-btn"
                      onClick={() => handleEdit(project)}
                    >
                      Edit
                    </button>

                    <button
                      className="delete-btn"
                      onClick={() => handleDelete(project.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default Projects;