import './App.css'
import Login from "./auth/login/Login.jsx";
import AuthCallback from "./auth/AuthCallback.jsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";

function App() {

    function Dashboard() {
        return <h1>Dashboard</h1>;
    }

  return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/auth/callback" element={<AuthCallback />} />
                <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </BrowserRouter>
  );
}

export default App
