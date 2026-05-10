import { Link } from "react-router-dom";

export default function Dashboard() {
  return (
    <div>
      <h1 className="page-title">Hệ thống Dịch vụ Nước sạch</h1>
      
      <div className="dashboard-grid">
        <Link to="/customers" style={{textDecoration: 'none'}}>
            <div className="glass-card glass-card-hover" style={{textAlign: 'center'}}>
            <div style={{fontSize: '3rem', marginBottom: '1rem'}}>👥</div>
            <h2>Quản lý Khách hàng & Căn hộ</h2>
           
            <div className="btn" style={{marginTop: '1.5rem', width: '100%'}}>Truy cập</div>
            </div>
        </Link>
        
        <Link to="/bills" style={{textDecoration: 'none'}}>
            <div className="glass-card glass-card-hover" style={{textAlign: 'center'}}>
            <div style={{fontSize: '3rem', marginBottom: '1rem'}}>💸</div>
            <h2>Lên Hóa đơn (ERP)</h2>
           
            <div className="btn" style={{marginTop: '1.5rem', width: '100%'}}>Truy cập</div>
            </div>
        </Link>
        
        <Link to="/statistics" style={{textDecoration: 'none'}}>
            <div className="glass-card glass-card-hover" style={{textAlign: 'center'}}>
            <div style={{fontSize: '3rem', marginBottom: '1rem'}}>📈</div>
            <h2>Thống kê Doanh thu</h2>
            
            <div className="btn" style={{marginTop: '1.5rem', width: '100%'}}>Truy cập</div>
            </div>
        </Link>
      </div>
    </div>
  );
}
