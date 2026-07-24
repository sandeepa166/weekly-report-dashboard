import { Link, useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <h2>Weekly Report Dashboard</h2>

      <div className="nav-links">
        {!user && (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}

        {user && user.role === "TEAM_MEMBER" && (
          <>
            <Link to="/my-reports">My Reports</Link>
            <Link to="/create-report">Create Report</Link>
          </>
        )}

        {user && user.role === "MANAGER" && (
          <>
            <Link to="/dashboard">Dashboard</Link>
            <Link to="/projects">Projects</Link>
          </>
        )}

        {user && (
          <>
            <span className="user-info">
              {user.name} ({user.role})
            </span>
            <button className="logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;