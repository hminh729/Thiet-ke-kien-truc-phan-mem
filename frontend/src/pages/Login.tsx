import { useState } from "react";
import { Link } from "react-router-dom";


export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleLogin = (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        fetch("/api/v1/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        })
        .then(async res => {
            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || "Đăng nhập thất bại");
            }
            return res.json();
        })
        .then(user => {
            localStorage.setItem("user", JSON.stringify(user));
            window.location.href = "/";
        })
        .catch(err => {
            setError(err.message);
        });
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh'}}>
            <div className="glass-card" style={{width: '400px', padding: '2rem'}}>
                <h2 style={{textAlign: 'center', marginBottom: '2rem', color: 'var(--primary)'}}>💧 ĐĂNG NHẬP</h2>
                {error && <div style={{color: '#f43f5e', background: 'rgba(244, 63, 94, 0.2)', padding: '0.5rem', borderRadius: '5px', marginBottom: '1rem', textAlign: 'center'}}>{error}</div>}
                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Tài khoản</label>
                        <input className="glass-input" value={username} onChange={e => setUsername(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label>Mật khẩu</label>
                        <input className="glass-input" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className="btn" style={{width: '100%', marginTop: '1rem'}}>VÀO HỆ THỐNG</button>
                </form>

                <div style={{textAlign: 'center', marginTop: '1.5rem', fontSize: '0.9rem', color: '#94a3b8'}}>
                    Chưa có tài khoản? <Link to="/register" style={{color: 'var(--primary)', textDecoration: 'none', fontWeight: 'bold'}}>Đăng ký ngay</Link>
                </div>
            </div>
        </div>
    );
}
