import { useState } from "react";

export default function MeterManager() {
    const [apartmentId, setApartmentId] = useState("");
    const [readingValue, setReadingValue] = useState("");
    const [status, setStatus] = useState("ON");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const checkMeterStatus = (id: string) => {
        fetch(`/api/v1/meters/${id}/status`)
            .then(r => r.json())
            .then(data => setStatus(data.status || 'ON'))
            .catch(() => setStatus('ON'));
    }

    const onApartmentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const id = e.target.value;
        setApartmentId(id);
        if (id) {
            checkMeterStatus(id);
        }
    }

    const handleRead = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setMessage("");
        setError("");

        fetch(`/api/v1/meters/readings`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ apartmentId: Number(apartmentId), readingValue: Number(readingValue) })
        })
        .then(res => {
            if(!res.ok) throw new Error("Lỗi truy xuất hoặc lưu số đọc vào cơ sở dữ liệu ảo IoT.");
            return res.json();
        })
        .then(() => {
            setMessage("Đã lưu chỉ số nước điện tử qua hệ thống!");
            setLoading(false);
            setReadingValue("");
        })
        .catch(e => {
            setError(e.message);
            setLoading(false);
        });
    };

    const handleToggleStatus = () => {
        setLoading(true);
        const nextStatus = status === "ON" ? "OFF" : "ON";
        fetch(`/api/v1/meters/${apartmentId}/status?status=${nextStatus}`, {
            method: "PUT"
        })
        .then(res => {
            if(!res.ok) throw new Error("Lỗi kết nối bộ điều khiển.");
            return res.json();
        })
        .then(data => {
            setStatus(data.status);
            setMessage("Đã điều khiển Van nước điện tử thành công: " + data.status);
            setLoading(false);
        })
        .catch(e => {
            setError(e.message);
            setLoading(false);
        });
    }

    return (
        <div>
            <h1 className="page-title">Quản Lý Đồng Hồ Nước (IoT)</h1>
            
            <div className="glass-card" style={{maxWidth: '600px', margin: '0 auto'}}>
                <h2 style={{borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem', marginBottom: '1rem'}}>Đọc/Ghi Chỉ Số - Điều Khiển Van</h2>
                {error && <div className="alert" style={{color: '#f87171', background: 'rgba(248, 113, 113, 0.1)', borderColor: 'rgba(248, 113, 113, 0.5)'}}>{error}</div>}
                {message && <div className="alert" style={{color: '#34d399', background: 'rgba(52, 211, 153, 0.1)', borderColor: 'rgba(52, 211, 153, 0.5)'}}>{message}</div>}
                
                <form onSubmit={handleRead}>
                    <div className="form-group">
                        <label>Mã Căn Hộ (Apartment ID) đo được</label>
                        <input className="glass-input" type="number" required value={apartmentId} onChange={onApartmentChange} placeholder="Nối kết mã căn hộ..." />
                    </div>
                    {apartmentId && (
                         <div style={{display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '1rem', background: 'rgba(255,255,255,0.05)', borderRadius: '8px', marginBottom: '1.5rem'}}>
                            <div>
                                <span style={{color: 'var(--text-muted)'}}><span style={{fontSize:'1.2rem', verticalAlign:'middle'}}>🚰</span> Trạng thái Van Cấp Nước: </span>
                                <strong style={{color: status === 'ON' ? '#34d399' : '#f87171'}}>{status}</strong>
                            </div>
                            <button type="button" className="btn btn-secondary" onClick={handleToggleStatus}>
                                {status === 'ON' ? 'Khoá Nước' : 'Mở Nước'}
                            </button>
                         </div>
                    )}
                    <div className="form-group">
                        <label>Chỉ Số Đồng Hồ Hiện Đang Lưu (m3)</label>
                        <input className="glass-input" type="number" step="0.1" required value={readingValue} onChange={e => setReadingValue(e.target.value)} placeholder="0.0" />
                    </div>
                    <button className="btn" type="submit" disabled={loading} style={{width: '100%', marginTop: '0.5rem', background: 'linear-gradient(135deg, #10b981, #059669)'}}>
                        {loading ? "Đang xử lý truyền tin..." : "Lưu dữ liệu đo vào Hệ Thống Đám Mây"}
                    </button>
                </form>
            </div>
        </div>
    );
}
