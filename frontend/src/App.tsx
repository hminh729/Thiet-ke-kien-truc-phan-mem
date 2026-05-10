import Dashboard from "./pages/Dashboard";
import CustomerManager from "./pages/CustomerManager";
import BillManager from "./pages/BillManager";
import Statistics from "./pages/Statistics";
import Login from "./pages/Login";
import Register from "./pages/Register";
import "./index.css";
import { NavLink, Routes, Route } from 'react-router-dom';
import React from 'react';

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const user = localStorage.getItem("user");
  return user ? <>{children}</> : <Login />;
}

function App() {
  const handleLogout = () => {
      localStorage.removeItem("user");
      window.location.href = "/login";
  };

  const userStr = localStorage.getItem("user");
  const loggedInUser = userStr ? JSON.parse(userStr) : null;

  return (
    <div className="app-root">
      <nav className="navbar">
        <NavLink to="/" className="nav-brand">AQUA💧FLOW</NavLink>
        <div className="nav-links">
          <NavLink to="/" className={({isActive}) => isActive ? "active" : ""}>Trang chủ</NavLink>
          <NavLink to="/customers" className={({isActive}) => isActive ? "active" : ""}>Khách hàng</NavLink>
          <NavLink to="/bills" className={({isActive}) => isActive ? "active" : ""}>Hóa đơn (ERP)</NavLink>
          <NavLink to="/statistics" className={({isActive}) => isActive ? "active" : ""}>Thống kê</NavLink>
          {localStorage.getItem("user") && (
            <button className="btn btn-secondary" onClick={handleLogout} style={{marginLeft: '20px', padding: '0.4rem 1rem'}}>Đăng xuất</button>
          )}
        </div>
      </nav>
      <main className="app-content">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
          <Route path="/customers" element={<PrivateRoute><CustomerManager /></PrivateRoute>} />
          <Route path="/bills" element={<PrivateRoute><BillManager /></PrivateRoute>} />
          <Route path="/statistics" element={<PrivateRoute><Statistics /></PrivateRoute>} />
          <Route path="*" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
        </Routes>
      </main>
      
      {loggedInUser && (
        <div style={{
            position: 'fixed', 
            bottom: '20px', 
            right: '20px', 
            background: 'rgba(30, 41, 59, 0.9)', 
            backdropFilter: 'blur(8px)',
            border: '1px solid var(--primary)', 
            padding: '10px 20px', 
            borderRadius: '30px', 
            color: '#e2e8f0', 
            boxShadow: '0 4px 15px rgba(0,0,0,0.3)',
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            zIndex: 1000
        }}>
            <div style={{
                width: '10px', height: '10px', borderRadius: '50%', background: '#34d399', 
                boxShadow: '0 0 8px #34d399'
            }}></div>
            <span style={{fontSize: '0.9rem'}}>Xin chào, <strong style={{color: '#60a5fa'}}>{loggedInUser.name}</strong> ({loggedInUser.role})</span>
        </div>
      )}
    </div>
  );
}

export default App;
