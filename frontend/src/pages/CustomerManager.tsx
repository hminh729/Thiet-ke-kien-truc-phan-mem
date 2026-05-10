import { useState, useEffect } from "react";

interface Apartment {
    id: number;
    roomNumber: string;
    block: string;
    waterStatus: string;
}

interface ContractBrief {
    id: number;
    apartmentId: number;
    status: string;
}

interface Customer {
    id: number;
    fullName: string;
    phone: string;
    idCard: string;
    email: string;
}

interface CustomerInfo {
    customer: Customer;
    apartments: Apartment[];
    contracts: ContractBrief[];
}

export default function CustomerManager() {
    const [customers, setCustomers] = useState<CustomerInfo[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchPhone, setSearchPhone] = useState("");
    
    // Edit Modal State
    const [editingCustomer, setEditingCustomer] = useState<Customer | null>(null);
    const [editForm, setEditForm] = useState<Customer>({ id: 0, fullName: '', phone: '', idCard: '', email: '' });
    const [saving, setSaving] = useState(false);

    const loadData = () => {
        setLoading(true);
        fetch("/api/v1/customers")
            .then(r => r.json())
            .then(data => {
                setCustomers(data);
                setLoading(false);
            })
            .catch(e => {
                console.error(e);
                setLoading(false);
            });
    }

    useEffect(() => { loadData(); }, []);

    const handleEditClick = (cust: Customer) => {
        setEditingCustomer(cust);
        setEditForm({ ...cust });
    };

    const handleCloseModal = () => {
        setEditingCustomer(null);
    };

    const handleSave = (e: React.FormEvent) => {
        e.preventDefault();
        setSaving(true);
        fetch(`/api/v1/customers/${editForm.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(editForm)
        })
        .then(res => {
            if(!res.ok) throw new Error("Cập nhật thất bại");
            return res.json();
        })
        .then(() => {
            setSaving(false);
            setEditingCustomer(null);
            loadData(); // Reload table
        })
        .catch(err => {
            alert(err);
            setSaving(false);
        });
    }

    const displayedCustomers = customers.filter(c => 
        searchPhone === "" || c.customer.phone.includes(searchPhone)
    );

    return (
        <div>
            <h1 className="page-title">Quản Lý Khách Hàng & Căn Hộ</h1>
            
            <div className="glass-card" style={{marginBottom: '1rem', padding: '1rem 2rem'}}>
                <div style={{display: 'flex', alignItems: 'center', gap: '1rem', flexWrap: 'wrap'}}>
                    <h3 style={{margin: 0, whiteSpace: 'nowrap'}}>🔍 Tìm kiếm Khách Hàng:</h3>
                    <input 
                        className="glass-input" 
                        type="text" 
                        placeholder="Nhập số điện thoại..." 
                        value={searchPhone}
                        onChange={(e) => setSearchPhone(e.target.value)}
                        style={{maxWidth: '300px'}}
                    />
                </div>
            </div>

            <div className="glass-card">
                {loading ? <div className="loader"></div> : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Họ & Tên</th>
                                    <th>Số điện thoại</th>
                                    <th>Danh sách Căn Hộ Sở Quản</th>
                                    <th style={{textAlign: 'center'}}>Dịch Vụ Nước</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                {displayedCustomers.flatMap(c => {
                                    if (c.contracts.length === 0) {
                                        return [(
                                            <tr key={`${c.customer.id}-noapt`}>
                                                <td>#{c.customer.id}</td>
                                                <td style={{fontWeight: 'bold', color: 'var(--primary)'}}>{c.customer.fullName}</td>
                                                <td>📞 {c.customer.phone}</td>
                                                <td><span style={{color: 'var(--text-muted)'}}>Chưa ghi nhận căn hộ</span></td>
                                                <td style={{textAlign: 'center'}}>-</td>
                                                <td>
                                                    <button className="btn btn-secondary" style={{padding: '0.5rem 1rem', fontSize: '0.9rem'}} onClick={() => handleEditClick(c.customer)}>
                                                        ✏️ Chi tiết / Sửa
                                                    </button>
                                                </td>
                                            </tr>
                                        )];
                                    }
                                    return c.contracts.map(contract => {
                                        const apt = c.apartments.find(a => a.id === contract.apartmentId);
                                        return (
                                            <tr key={`${c.customer.id}-apt-${contract.id}`}>
                                                <td>#{c.customer.id}</td>
                                                <td style={{fontWeight: 'bold', color: 'var(--primary)'}}>{c.customer.fullName}</td>
                                                <td>📞 {c.customer.phone}</td>
                                                <td>
                                                    {apt ? (
                                                        <span style={{color: '#94a3b8'}}>Phòng: <strong style={{color: '#fff'}}>{apt.roomNumber}</strong>, Tòa: <strong style={{color: '#fff'}}>{apt.block}</strong></span>
                                                    ) : "Lỗi dữ liệu phòng"}
                                                </td>
                                                <td style={{textAlign: 'center'}}>
                                                    {apt?.waterStatus === 'ON' ? (
                                                        <span style={{background: 'rgba(52, 211, 153, 0.2)', color: '#34d399', padding: '0.3rem 0.8rem', borderRadius: '20px', fontWeight: 'bold', fontSize: '0.85rem'}}>
                                                            🟢 ĐANG CẤP NƯỚC
                                                        </span>
                                                    ) : apt?.waterStatus === 'OFF' ? (
                                                        <span style={{background: 'rgba(244, 63, 94, 0.2)', color: '#f43f5e', padding: '0.3rem 0.8rem', borderRadius: '20px', fontWeight: 'bold', fontSize: '0.85rem'}}>
                                                            🔴 ĐÃ KHÓA VAN
                                                        </span>
                                                    ) : (
                                                        <span style={{color: 'var(--text-muted)'}}>KHÔNG RÕ</span>
                                                    )}
                                                </td>
                                                <td>
                                                    <button className="btn btn-secondary" style={{padding: '0.5rem 1rem', fontSize: '0.9rem'}} onClick={() => handleEditClick(c.customer)}>
                                                        ✏️ Chi tiết / Sửa
                                                    </button>
                                                </td>
                                            </tr>
                                        );
                                    });
                                })}
                                {displayedCustomers.length === 0 && (
                                    <tr>
                                        <td colSpan={5} style={{textAlign:'center', color:'var(--text-muted)'}}>Không tìm thấy khách hàng nào khớp với số điện thoại này.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {/* Modal Edit Customer */}
            {editingCustomer && (
                <div style={{
                    position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', 
                    background: 'rgba(0,0,0,0.6)', backdropFilter: 'blur(4px)',
                    display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000
                }}>
                    <div className="glass-card" style={{width: '450px', position: 'relative', background: '#1e293b'}}>
                        <button onClick={handleCloseModal} style={{
                            position: 'absolute', top: '15px', right: '15px', background: 'none', border: 'none', 
                            color: '#cbd5e1', fontSize: '1.5rem', cursor: 'pointer'
                        }}>×</button>
                        <h2 style={{borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1rem', marginBottom: '1.5rem'}}>
                            Thông tin chi tiết
                        </h2>
                        
                        <form onSubmit={handleSave}>
                            <div className="form-group">
                                <label>Họ và Tên</label>
                                <input className="glass-input" value={editForm.fullName} onChange={e => setEditForm({...editForm, fullName: e.target.value})} required/>
                            </div>
                            <div className="form-group">
                                <label>Số điện thoại</label>
                                <input className="glass-input" value={editForm.phone} onChange={e => setEditForm({...editForm, phone: e.target.value})} required/>
                            </div>
                            <div className="form-group">
                                <label>Căn Cuớc CD</label>
                                <input className="glass-input" value={editForm.idCard} onChange={e => setEditForm({...editForm, idCard: e.target.value})} />
                            </div>
                            <div className="form-group">
                                <label>Email</label>
                                <input className="glass-input" type="email" value={editForm.email} onChange={e => setEditForm({...editForm, email: e.target.value})} />
                            </div>
                            
                            <div style={{display: 'flex', gap: '1rem', marginTop: '2rem'}}>
                                <button type="button" className="btn btn-secondary" style={{flex: 1}} onClick={handleCloseModal}>Đóng</button>
                                <button type="submit" className="btn" style={{flex: 1}} disabled={saving}>
                                    {saving ? "Đang lưu..." : "Lưu thay đổi"}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

        </div>
    );
}
