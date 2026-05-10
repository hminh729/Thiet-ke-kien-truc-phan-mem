import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Register() {
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();

    const handleRegister = (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        if (password !== confirmPassword) {
            setError("Mật khẩu xác nhận không khớp!");
            return;
        }

        fetch("/api/v1/users/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, username, password })
        })
        .then(async res => {
            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || "Đăng ký thất bại");
            }
            return res.json();
        })
        .then(() => {
            setSuccess("Đăng ký thành công! Đang chuyển hướng...");
            setTimeout(() => {
                navigate("/login");
            }, 2000);
        })
        .catch(err => {
            setError(err.message);
        });
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh'}}>
            <div className="glass-card" style={{width: '400px', padding: '2rem'}}>
                <h2 style={{textAlign: 'center', marginBottom: '2rem', color: 'var(--primary)'}}>💧 ĐĂNG KÝ</h2>
                
                {error && <div style={{color: '#f43f5e', background: 'rgba(244, 63, 94, 0.2)', padding: '0.5rem', borderRadius: '5px', marginBottom: '1rem', textAlign: 'center'}}>{error}</div>}
                {success && <div style={{color: '#10b981', background: 'rgba(16, 185, 129, 0.2)', padding: '0.5rem', borderRadius: '5px', marginBottom: '1rem', textAlign: 'center'}}>{success}</div>}
                
                <form onSubmit={handleRegister}>
                    <div className="form-group">
                        <label>Họ và tên</label>
                        <input className="glass-input" value={name} onChange={e => setName(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label>Tài khoản</label>
                        <input className="glass-input" value={username} onChange={e => setUsername(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label>Mật khẩu</label>
                        <input className="glass-input" type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                    </div>
                    <div className="form-group">
                        <label>Xác nhận mật khẩu</label>
                        <input className="glass-input" type="password" value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} required />
                    </div>
                    <button type="submit" className="btn" style={{width: '100%', marginTop: '1rem'}}>ĐĂNG KÝ</button>
                </form>

                <div style={{textAlign: 'center', marginTop: '1.5rem', fontSize: '0.9rem', color: '#94a3b8'}}>
                    Đã có tài khoản? <Link to="/login" style={{color: 'var(--primary)', textDecoration: 'none', fontWeight: 'bold'}}>Đăng nhập ngay</Link>
                </div>
            </div>
        </div>
    );
}
