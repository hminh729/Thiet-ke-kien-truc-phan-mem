import { useState, useEffect } from "react";

interface Customer {
    id: number;
    fullName: string;
    phone: string;
}

interface RevDTO {
    customer: Customer;
    totalAmount: number;
    paidAmount: number;
    unpaidAmount: number;
}

export default function Statistics() {
    const [stats, setStats] = useState<RevDTO[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch("/api/v1/customers/statistics/revenue")
            .then(r => r.json())
            .then(data => {
                // Sort by totalAmount descending
                const sorted = data.sort((a: RevDTO, b: RevDTO) => b.totalAmount - a.totalAmount);
                setStats(sorted);
                setLoading(false);
            })
            .catch(e => {
                console.error(e);
                setLoading(false);
            });
    }, []);

    const totalGlobal = stats.reduce((acc, curr) => acc + curr.totalAmount, 0);
    const totalPaidGlobal = stats.reduce((acc, curr) => acc + curr.paidAmount, 0);
    const totalUnpaidGlobal = stats.reduce((acc, curr) => acc + curr.unpaidAmount, 0);

    return (
        <div>
            <h1 className="page-title">Báo Cáo Tài Chính & Công Nợ</h1>
            
            <div style={{display: 'flex', gap: '1.5rem', marginBottom: '2rem', flexWrap: 'wrap'}}>
                {/* TỔNG ĐÃ THU */}
                <div className="glass-card" style={{flex: 1, minWidth: '250px', borderLeft: '4px solid #34d399'}}>
                    <h2 style={{color: 'var(--text-muted)', fontSize: '1rem', textTransform: 'uppercase'}}>💰 Tổng Tiền Đã Thu</h2>
                    <div style={{fontSize: '2.5rem', fontWeight: 'bold', color: '#34d399'}}>
                        {loading ? "..." : `${totalPaidGlobal.toLocaleString('vi-VN')} đ`}
                    </div>
                </div>

                {/* TỔNG DƯ NỢ */}
                <div className="glass-card" style={{flex: 1, minWidth: '250px', borderLeft: '4px solid #f43f5e'}}>
                    <h2 style={{color: 'var(--text-muted)', fontSize: '1rem', textTransform: 'uppercase'}}>⚠️ Tổng Tiền Dư Nợ (Chưa Thu)</h2>
                    <div style={{fontSize: '2.5rem', fontWeight: 'bold', color: '#f43f5e'}}>
                        {loading ? "..." : `${totalUnpaidGlobal.toLocaleString('vi-VN')} đ`}
                    </div>
                </div>

                {/* TỔNG QUAN */}
                <div className="glass-card" style={{flex: 1, minWidth: '250px', borderLeft: '4px solid #3b82f6'}}>
                    <h2 style={{color: 'var(--text-muted)', fontSize: '1rem', textTransform: 'uppercase'}}>📉 Phân Bổ Tổng Doanh Thu</h2>
                    <div style={{fontSize: '2.5rem', fontWeight: 'bold', color: '#60a5fa'}}>
                        {loading ? "..." : `${totalGlobal.toLocaleString('vi-VN')} đ`}
                    </div>
                </div>
            </div>

            <div className="glass-card">
                <h3>Chi Tiết Công Nợ Từng Khách Hàng</h3>
                {loading ? <div className="loader"></div> : (
                    <div className="table-container" style={{marginTop: '1.5rem'}}>
                        <table>
                            <thead>
                                <tr>
                                    <th>Top</th>
                                    <th>Tên Khách Hàng</th>
                                    <th>SDT Liên Hệ</th>
                                    <th style={{textAlign: 'right', color: '#34d399'}}>Đã Thu (VND)</th>
                                    <th style={{textAlign: 'right', color: '#f43f5e'}}>Chưa Thu (VND)</th>
                                    <th style={{textAlign: 'right'}}>Tổng Cộng</th>
                                </tr>
                            </thead>
                            <tbody>
                                {stats.map((s, idx) => (
                                    <tr key={s.customer.id}>
                                        <td>
                                            {idx === 0 && <span style={{fontSize: '1.5rem'}}>🥇</span>}
                                            {idx === 1 && <span style={{fontSize: '1.5rem'}}>🥈</span>}
                                            {idx === 2 && <span style={{fontSize: '1.5rem'}}>🥉</span>}
                                            {idx > 2 && <span style={{color: 'var(--text-muted)', marginLeft: '10px'}}>#{idx+1}</span>}
                                        </td>
                                        <td style={{fontWeight: 'bold', color: 'var(--primary)'}}>{s.customer.fullName}</td>
                                        <td>{s.customer.phone}</td>
                                        
                                        <td style={{textAlign: 'right', fontWeight: 'bold', color: '#34d399'}}>
                                            {s.paidAmount > 0 ? `${s.paidAmount.toLocaleString('vi-VN')} đ` : '-'}
                                        </td>
                                        
                                        <td style={{textAlign: 'right', fontWeight: 'bold', color: s.unpaidAmount > 0 ? '#f43f5e' : 'var(--text-muted)'}}>
                                            {s.unpaidAmount > 0 ? `${s.unpaidAmount.toLocaleString('vi-VN')} đ` : '-'}
                                        </td>

                                        <td style={{textAlign: 'right', fontWeight: 'bold', color: '#93c5fd', fontSize: '1.1rem'}}>
                                            {s.totalAmount.toLocaleString('vi-VN')} đ
                                        </td>
                                    </tr>
                                ))}
                                {stats.length === 0 && (
                                    <tr>
                                        <td colSpan={6} style={{textAlign:'center', color:'var(--text-muted)'}}>Chưa phát sinh doanh thu từ hóa đơn.</td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}
