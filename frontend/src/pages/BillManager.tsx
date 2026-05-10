import { useState } from "react";

interface Customer {
    id: number;
    fullName: string;
    phone: string;
}

interface Apartment {
    id: number;
    roomNumber: string;
    block: string;
}

interface ContractBrief {
    id: number;
    apartmentId: number;
    status: string;
}

interface CustomerInfo {
    customer: Customer;
    apartments: Apartment[];
    contracts: ContractBrief[];
}

interface PriceTier {
    minVolume: number;
    maxVolume: number | null;
    price: number;
}

interface Bill {
    id: number;
    contractId: number;
    customerId: number;
    waterVolume: number;
    waterPrice: number;
    totalAmount: number;
    billingDate: string;
    priceTiers?: PriceTier[];
}

export default function BillManager() {
    const [searchPhone, setSearchPhone] = useState("");
    const [loading, setLoading] = useState(false);
    const [customerInfo, setCustomerInfo] = useState<CustomerInfo | null>(null);
    const [selectedContractId, setSelectedContractId] = useState<number | "">("");
    const [waterVolume, setWaterVolume] = useState("");
    const [generatedBill, setGeneratedBill] = useState<Bill | null>(null);
    const [error, setError] = useState("");

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setCustomerInfo(null);
        setSelectedContractId("");
        setGeneratedBill(null);

        fetch("/api/v1/customers")
            .then(r => r.json())
            .then((data: CustomerInfo[]) => {
                const found = data.find(c => c.customer.phone === searchPhone);
                if (found) {
                    setCustomerInfo(found);
                } else {
                    setError("Không tìm thấy khách hàng với số điện thoại này. Vui lòng kiểm tra lại bên Quản lý Khách hàng.");
                }
                setLoading(false);
            })
            .catch(err => {
                setError("Lỗi kết nối tới Server: " + err.message);
                setLoading(false);
            });
    };

    const handleGenerate = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError("");
        setGeneratedBill(null);
        const userStr = localStorage.getItem("user");
        const user = userStr ? JSON.parse(userStr) : null;
        const userId = user ? user.id : 1;
        
        fetch(`/api/v1/bills/generate?contractId=${selectedContractId}&waterVolume=${waterVolume}&userId=${userId}`, {
            method: "POST"
        })
        .then(res => {
            if(!res.ok) throw new Error("Chưa có hợp đồng nào khả dụng ở căn này hoặc Server xử lý lỗi.");
            return res.json();
        })
        .then(data => {
            setGeneratedBill(data);
            setLoading(false);
            setWaterVolume("");
        })
        .catch(e => {
            setError(e.message);
            setLoading(false);
        });
    };

    const getAptText = (contractId: number) => {
        if(!customerInfo) return "";
        const con = customerInfo.contracts.find(c => c.id === contractId);
        if(!con) return "";
        const apt = customerInfo.apartments.find(a => a.id === con.apartmentId);
        if(!apt) return "";
        return `Phòng ${apt.roomNumber} - Tòa ${apt.block}`;
    }

    return (
        <div>
            <h1 className="page-title">Xuất Phiếu Hóa Đơn Trực Tuyến</h1>
            
            <div style={{display: 'flex', gap: '2rem', flexWrap: 'wrap'}}>
                <div className="glass-card" style={{flex: '1 1 300px', alignSelf: 'flex-start'}}>
                    <h2>Quy trình Tạo Hóa Đơn</h2>
                    {error && <div className="alert" style={{color: '#f87171', background: 'rgba(248, 113, 113, 0.1)', borderColor: 'rgba(248, 113, 113, 0.5)'}}>{error}</div>}
                    
                    {/* BƯỚC 1: TÌM KHÁCH HÀNG */}
                    <form onSubmit={handleSearch} style={{marginBottom: '1.5rem', borderBottom: '1px solid rgba(255,255,255,0.1)', paddingBottom: '1.5rem'}}>
                        <div className="form-group">
                            <label>1. Tra cứu số điện thoại Khách Hàng</label>
                            <div style={{display: 'flex', gap: '0.5rem'}}>
                                <input 
                                    className="glass-input" 
                                    type="text" 
                                    required 
                                    value={searchPhone} 
                                    onChange={e => setSearchPhone(e.target.value)}
                                    placeholder="Ví dụ: 0912345678"
                                />
                                <button type="submit" className="btn btn-secondary" disabled={loading} style={{whiteSpace: 'nowrap'}}>
                                    🔍 Tìm kiếm
                                </button>
                            </div>
                        </div>
                    </form>

                    {/* BƯỚC 2 & 3: CHỌN CĂN HỘ VÀ NHẬP SỐ */}
                    {customerInfo && (
                        <form onSubmit={handleGenerate}>
                            <div style={{marginBottom: '1rem'}}>
                                <div style={{color: '#34d399', fontWeight: 'bold', marginBottom: '0.5rem', fontSize: '1.1rem'}}>
                                    👤 Khách hàng: {customerInfo.customer.fullName}
                                </div>
                                
                                <div className="form-group">
                                    <label>2. Chọn Căn hộ muốn thu tiền</label>
                                    <select 
                                        className="glass-input" 
                                        required 
                                        value={selectedContractId} 
                                        onChange={e => setSelectedContractId(Number(e.target.value))}
                                    >
                                        <option value="">-- Chọn Căn Hộ --</option>
                                        {customerInfo.contracts.map(contract => {
                                            const apt = customerInfo.apartments.find(a => a.id === contract.apartmentId);
                                            return (
                                                <option key={contract.id} value={contract.id}>
                                                    {apt ? `Phòng ${apt.roomNumber} - Tòa ${apt.block}` : `Hợp đồng #${contract.id}`} 
                                                    {contract.status !== 'ACTIVE' ? ' (Đã ngừng HĐ)' : ''}
                                                </option>
                                            );
                                        })}
                                    </select>
                                </div>
                            </div>
                            
                            {selectedContractId !== "" && (
                                <div className="form-group">
                                    <label>3. Nhập số khối nước tiêu thụ (m³)</label>
                                    <input 
                                        className="glass-input" 
                                        type="number" 
                                        step="0.1"
                                        required 
                                        value={waterVolume} 
                                        onChange={e => setWaterVolume(e.target.value)}
                                        placeholder="Ví dụ: 15.5 m3"
                                    />
                                </div>
                            )}

                            <button className="btn" type="submit" disabled={loading || selectedContractId === ""} style={{width: '100%', marginTop: '1rem'}}>
                                {loading ? "Đang xử lý đa luồng..." : "Phát hành Hóa Đơn Nước"}
                            </button>
                        </form>
                    )}
                </div>
                
                {generatedBill && (
                    <div className="glass-card" style={{flex: '1 1 300px', border: '1px solid var(--primary)', background: 'linear-gradient(180deg, rgba(30, 41, 59, 0.8) 0%, rgba(59, 130, 246, 0.1) 100%)'}}>
                        <h2 style={{color: '#60a5fa', textAlign: 'center', marginBottom: '0'}}>💦 Hóa Đơn Mới Lập</h2>
                        <div style={{textAlign:'center', color: 'var(--text-muted)', marginBottom: '2rem', fontSize: '0.9rem'}}>{new Date(generatedBill.billingDate).toLocaleDateString()}</div>
                        
                        <div style={{display: 'flex', flexDirection: 'column', gap: '1rem'}}>
                            <div style={{display: 'flex', justifyContent: 'space-between', borderBottom: '1px dashed rgba(255,255,255,0.2)', paddingBottom: '0.5rem'}}>
                                <span style={{color: 'var(--text-muted)'}}>Mã Hóa đơn:</span>
                                <strong>#HD-{generatedBill.id}</strong>
                            </div>
                            <div style={{display: 'flex', justifyContent: 'space-between', borderBottom: '1px dashed rgba(255,255,255,0.2)', paddingBottom: '0.5rem'}}>
                                <span style={{color: 'var(--text-muted)'}}>Tên khách hàng:</span>
                                <strong>{customerInfo?.customer.fullName}</strong>
                            </div>
                            <div style={{display: 'flex', justifyContent: 'space-between', borderBottom: '1px dashed rgba(255,255,255,0.2)', paddingBottom: '0.5rem'}}>
                                <span style={{color: 'var(--text-muted)'}}>Căn hộ:</span>
                                <strong>{getAptText(generatedBill.contractId)}</strong>
                            </div>
                            <div style={{marginTop: '1rem', marginBottom: '1rem'}}>
                                <div style={{color: '#60a5fa', marginBottom: '0.8rem', fontSize: '1rem', fontWeight: 'bold', borderBottom: '1px solid rgba(96, 165, 250, 0.3)', paddingBottom: '0.3rem'}}>
                                    📊 Chi tiết tính toán bậc thang
                                </div>
                                <table style={{width: '100%', fontSize: '0.85rem', borderCollapse: 'collapse', background: 'rgba(0,0,0,0.3)', borderRadius: '8px', overflow: 'hidden'}}>
                                    <thead>
                                        <tr style={{background: 'rgba(255,255,255,0.1)', color: '#93c5fd'}}>
                                            <th style={{padding: '8px 5px', textAlign: 'left'}}>Bậc giá (m³)</th>
                                            <th style={{padding: '8px 5px', textAlign: 'right'}}>Đơn giá</th>
                                            <th style={{padding: '8px 5px', textAlign: 'right'}}>Sử dụng</th>
                                            <th style={{padding: '8px 5px', textAlign: 'right'}}>Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {(() => {
                                            let remaining = generatedBill.waterVolume;
                                            return generatedBill.priceTiers?.map((tier, idx) => {
                                                if (remaining <= 0) return null;
                                                
                                                const range = tier.maxVolume ? (tier.maxVolume - tier.minVolume) : remaining;
                                                const usageInTier = Math.min(remaining, range);
                                                const subtotal = usageInTier * tier.price;
                                                remaining -= usageInTier;

                                                return (
                                                    <tr key={idx} style={{borderBottom: '1px solid rgba(255,255,255,0.05)'}}>
                                                        <td style={{padding: '8px 5px', color: 'var(--text-muted)'}}>
                                                            {tier.minVolume} - {tier.maxVolume ?? '∞'}
                                                        </td>
                                                        <td style={{padding: '8px 5px', textAlign: 'right'}}>
                                                            {tier.price.toLocaleString('vi-VN')}
                                                        </td>
                                                        <td style={{padding: '8px 5px', textAlign: 'right', fontWeight: 'bold', color: '#f43f5e'}}>
                                                            {usageInTier.toFixed(1)}
                                                        </td>
                                                        <td style={{padding: '8px 5px', textAlign: 'right', fontWeight: 'bold'}}>
                                                            {subtotal.toLocaleString('vi-VN')}
                                                        </td>
                                                    </tr>
                                                );
                                            });
                                        })()}
                                    </tbody>
                                </table>
                            </div>
                            <div style={{display: 'flex', justifyContent: 'space-between', borderBottom: '1px dashed rgba(255,255,255,0.2)', paddingBottom: '0.5rem'}}>
                                <span style={{color: 'var(--text-muted)'}}>Khối lượng SD:</span>
                                <strong style={{color: '#f43f5e', fontSize: '1.2rem'}}>{generatedBill.waterVolume} m³</strong>
                            </div>
                            
                            <div style={{display: 'flex', justifyContent: 'space-between', marginTop: '1rem', fontSize: '1.5rem'}}>
                                <span style={{color: '#93c5fd', fontWeight: 'bold'}}>TỔNG TIỀN:</span>
                                <strong style={{color: '#d8b4fe'}}>{generatedBill.totalAmount.toLocaleString('vi-VN')} đ</strong>
                            </div>
                            
                            <div style={{color: '#34d399', fontSize: '0.8rem', textAlign: 'center', marginTop: '0.5rem'}}>
                                ✔ Đã đồng bộ Lịch sử Nước sang Meter-Service
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
