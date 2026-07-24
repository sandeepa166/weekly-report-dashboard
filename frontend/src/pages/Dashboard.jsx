import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import api from "../services/api";

function Dashboard() {
  const user = JSON.parse(localStorage.getItem("user"));

  const [weekStart, setWeekStart] = useState("");
  const [weekEnd, setWeekEnd] = useState("");

  const [summary, setSummary] = useState(null);
  const [reports, setReports] = useState([]);
  const [submissionStatus, setSubmissionStatus] = useState([]);
  const [workload, setWorkload] = useState([]);
  const [recentReports, setRecentReports] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const buildQuery = () => {
    const params = new URLSearchParams();

    if (weekStart) {
      params.append("weekStart", weekStart);
    }

    if (weekEnd) {
      params.append("weekEnd", weekEnd);
    }

    const query = params.toString();
    return query ? `?${query}` : "";
  };

  const fetchDashboardData = async () => {
    setError("");

    try {
      const query = buildQuery();

      const summaryResponse = await api.get(`/dashboard/summary${query}`);
      const reportsResponse = await api.get(`/dashboard/reports${query}`);
      const statusResponse = await api.get(
        `/dashboard/submission-status${query}`
      );
      const workloadResponse = await api.get(
        `/dashboard/workload-by-project${query}`
      );
      const recentResponse = await api.get("/dashboard/recent-reports");

      setSummary(summaryResponse.data);
      setReports(reportsResponse.data);
      setSubmissionStatus(statusResponse.data);
      setWorkload(workloadResponse.data);
      setRecentReports(recentResponse.data);
    } catch (err) {
      setError("Failed to load dashboard data");
    }
  };

  if (!user) {
    return (
      <div>
        <h1>Manager Dashboard</h1>
        <p className="error">Please login first.</p>
      </div>
    );
  }

  if (user.role !== "MANAGER") {
    return (
      <div>
        <h1>Manager Dashboard</h1>
        <p className="error">Only managers can view this dashboard.</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Manager Dashboard</h1>

      {error && <p className="error">{error}</p>}

      <div className="filter-box">
        <div>
          <label>Week Start</label>
          <input
            type="date"
            value={weekStart}
            onChange={(e) => setWeekStart(e.target.value)}
          />
        </div>

        <div>
          <label>Week End</label>
          <input
            type="date"
            value={weekEnd}
            onChange={(e) => setWeekEnd(e.target.value)}
          />
        </div>

        <button onClick={fetchDashboardData}>Apply Filter</button>
      </div>

      {summary && (
        <div className="dashboard-grid">
          <div className="summary-card">
            <h3>Total Team Members</h3>
            <p>{summary.totalTeamMembers}</p>
          </div>

          <div className="summary-card">
            <h3>Submitted Reports</h3>
            <p>{summary.submittedReports}</p>
          </div>

          <div className="summary-card">
            <h3>Pending Reports</h3>
            <p>{summary.pendingReports}</p>
          </div>

          <div className="summary-card">
            <h3>Open Blockers</h3>
            <p>{summary.openBlockers}</p>
          </div>

          <div className="summary-card">
            <h3>Compliance Rate</h3>
            <p>{summary.complianceRate}%</p>
          </div>
        </div>
      )}

      <div className="dashboard-section">
        <h2>Workload by Project</h2>

        {workload.length === 0 ? (
          <p>No workload data found.</p>
        ) : (
          <div className="chart-box">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={workload}>
                <XAxis dataKey="projectName" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="totalHours" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
      </div>

      <div className="dashboard-section">
        <h2>Submission Status</h2>

        {submissionStatus.length === 0 ? (
          <p>No submission status data found.</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Team Member</th>
                <th>Email</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {submissionStatus.map((item) => (
                <tr key={item.userId}>
                  <td>{item.userName}</td>
                  <td>{item.email}</td>
                  <td>
                    <span className={`status ${item.status.toLowerCase()}`}>
                      {item.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="dashboard-section">
        <h2>All Reports</h2>

        {reports.length === 0 ? (
          <p>No reports found.</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Member</th>
                <th>Project</th>
                <th>Week</th>
                <th>Status</th>
                <th>Hours</th>
                <th>Blockers</th>
              </tr>
            </thead>

            <tbody>
              {reports.map((report) => (
                <tr key={report.id}>
                  <td>{report.userName}</td>
                  <td>{report.projectName}</td>
                  <td>
                    {report.weekStart} to {report.weekEnd}
                  </td>
                  <td>
                    <span className={`status ${report.status.toLowerCase()}`}>
                      {report.status}
                    </span>
                  </td>
                  <td>{report.hoursWorked || 0}</td>
                  <td>{report.blockers || "No blockers"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="dashboard-section">
        <h2>Recent Reports</h2>

        {recentReports.length === 0 ? (
          <p>No recent reports found.</p>
        ) : (
          <div className="report-list">
            {recentReports.map((report) => (
              <div className="report-card" key={report.id}>
                <h3>
                  {report.userName} — {report.projectName}
                </h3>

                <p>
                  <strong>Week:</strong> {report.weekStart} to {report.weekEnd}
                </p>

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
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;