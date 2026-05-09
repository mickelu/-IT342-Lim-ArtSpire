import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children, getUser }) {
  const user = getUser();
  return user ? children : <Navigate to="/login" replace />;
}
