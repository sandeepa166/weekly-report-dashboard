import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import MyReports from "./pages/MyReports";
import CreateReport from "./pages/CreateReport";
import Projects from "./pages/Projects";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <main className="container">
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />

          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route
            path="/my-reports"
            element={
              <ProtectedRoute allowedRoles={["TEAM_MEMBER"]}>
                <MyReports />
              </ProtectedRoute>
            }
          />

          <Route
            path="/create-report"
            element={
              <ProtectedRoute allowedRoles={["TEAM_MEMBER"]}>
                <CreateReport />
              </ProtectedRoute>
            }
          />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute allowedRoles={["MANAGER"]}>
                <Dashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/projects"
            element={
              <ProtectedRoute allowedRoles={["MANAGER"]}>
                <Projects />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;