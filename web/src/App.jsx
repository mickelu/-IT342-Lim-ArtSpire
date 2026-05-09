import { Navigate, Route, Routes } from "react-router-dom";
import ProtectedRoute from "./features/auth/components/ProtectedRoute";
import Login from "./features/auth/pages/Login";
import Register from "./features/auth/pages/Register";
import Dashboard from "./features/dashboard/pages/Dashboard";
import { getStoredUser } from "./shared/lib/storage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute getUser={getStoredUser}>
            <Dashboard />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
