import { useEffect, useState } from "react";
import api from "../services/api";

function MyReports() {
  const user = JSON.parse(localStorage.getItem("user"));

  const [reports, setReports] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    if (user) {
      fetchMyReports();
    }
  }, []);

  const fetchMyReports = async () => {
    try {
      const response = await api.get(`/reports/my/${user.userId}`);
      setReports(response.data);
    } catch (err) {
      setError("Failed to load reports");
    }
  };

  const handleSubmitReport = async (reportId) => {
    try {
      await api.put(`/reports/${reportId}/submit`);
      fetchMyReports();
    } catch (err) {
      setError("Failed to submit report");
    }
  };

  if (!user) {
    return (
      <div>
        <h1>My Weekly Reports</h1>
        <p className="error">Please login first.</p>
      </div>
    );
  }

  return (
    <div>
      <h1>My Weekly Reports</h1>

      {error && <p className="error">{error}</p>}

      {reports.length === 0 ? (
        <p>No reports found.</p>
      ) : (
        <div className="report-list">
          {reports.map((report) => (
            <div className="report-card" key={report.id}>
              <h3>
                {report.projectName} — {report.weekStart} to {report.weekEnd}
              </h3>

              <p>
                <strong>Status:</strong>{" "}
                <span className={`status ${report.status.toLowerCase()}`}>
                  {report.status}
                </span>
              </p>

              <p>
                <strong>Tasks Completed:</strong>
                <br />
                {report.tasksCompleted || "No details"}
              </p>

              <p>
                <strong>Next Week Plan:</strong>
                <br />
                {report.tasksPlanned || "No details"}
              </p>

              <p>
                <strong>Blockers:</strong>
                <br />
                {report.blockers || "No blockers"}
              </p>

              <p>
                <strong>Hours Worked:</strong> {report.hoursWorked || 0}
              </p>

              <p>
                <strong>Notes:</strong>
                <br />
                {report.notes || "No notes"}
              </p>

              {report.status === "DRAFT" && (
                <button onClick={() => handleSubmitReport(report.id)}>
                  Submit Report
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyReports;