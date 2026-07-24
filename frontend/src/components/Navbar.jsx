import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav className="navbar">
      <h2>Weekly Report Dashboard</h2>

      <div className="nav-links">
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
        <Link to="/my-reports">My Reports</Link>
        <Link to="/create-report">Create Report</Link>
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/projects">Projects</Link>
      </div>
    </nav>
  );
}

export default Navbar;