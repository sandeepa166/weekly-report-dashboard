import { useEffect, useState } from "react";
import api from "../services/api";

function CreateReport() {
  const user = JSON.parse(localStorage.getItem("user"));

  const [projects, setProjects] = useState([]);
  const [formData, setFormData] = useState({
    projectId: "",
    weekStart: "",
    weekEnd: "",
    tasksCompleted: "",
    tasksPlanned: "",
    blockers: "",
    hoursWorked: "",
    notes: "",
  });

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    if (!user) {
      setError("Please login first");
      return;
    }

    try {
      const requestData = {
        userId: user.userId,
        projectId: Number(formData.projectId),
        weekStart: formData.weekStart,
        weekEnd: formData.weekEnd,
        tasksCompleted: formData.tasksCompleted,
        tasksPlanned: formData.tasksPlanned,
        blockers: formData.blockers,
        hoursWorked: formData.hoursWorked ? Number(formData.hoursWorked) : null,
        notes: formData.notes,
      };

      await api.post("/reports", requestData);

      setMessage("Weekly report created successfully");

      setFormData({
        projectId: "",
        weekStart: "",
        weekEnd: "",
        tasksCompleted: "",
        tasksPlanned: "",
        blockers: "",
        hoursWorked: "",
        notes: "",
      });
    } catch (err) {
      setError(err.response?.data?.error || "Failed to create report");
    }
  };

  if (!user) {
    return (
      <div>
        <h1>Create Weekly Report</h1>
        <p className="error">Please login first.</p>
      </div>
    );
  }

  return (
    <div className="form-container large-form">
      <h1>Create Weekly Report</h1>

      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}

      <form onSubmit={handleSubmit}>
        <label>Project / Category</label>
        <select
          name="projectId"
          value={formData.projectId}
          onChange={handleChange}
          required
        >
          <option value="">Select project</option>
          {projects.map((project) => (
            <option key={project.id} value={project.id}>
              {project.name}
            </option>
          ))}
        </select>

        <label>Week Start Date</label>
        <input
          type="date"
          name="weekStart"
          value={formData.weekStart}
          onChange={handleChange}
          required
        />

        <label>Week End Date</label>
        <input
          type="date"
          name="weekEnd"
          value={formData.weekEnd}
          onChange={handleChange}
          required
        />

        <label>Tasks Completed</label>
        <textarea
          name="tasksCompleted"
          rows="4"
          placeholder="What did you complete this week?"
          value={formData.tasksCompleted}
          onChange={handleChange}
        />

        <label>Tasks Planned for Next Week</label>
        <textarea
          name="tasksPlanned"
          rows="4"
          placeholder="What will you do next week?"
          value={formData.tasksPlanned}
          onChange={handleChange}
        />

        <label>Blockers / Challenges</label>
        <textarea
          name="blockers"
          rows="3"
          placeholder="Any blockers or challenges?"
          value={formData.blockers}
          onChange={handleChange}
        />

        <label>Hours Worked</label>
        <input
          type="number"
          name="hoursWorked"
          placeholder="Example: 12"
          value={formData.hoursWorked}
          onChange={handleChange}
        />

        <label>Notes / Links</label>
        <textarea
          name="notes"
          rows="3"
          placeholder="Optional notes or links"
          value={formData.notes}
          onChange={handleChange}
        />

        <button type="submit">Create Report</button>
      </form>
    </div>
  );
}

export default CreateReport;