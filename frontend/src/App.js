import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminDashboard from './components/adminPages/AdminDashboard';
import CustomerDashboard from './components/customerPages/CustomerDashboard';
import AgentDashboard from './components/agentPages/AgentDashboard';
import EmployeeDashboard from './components/employeePages/EmployeeDashboard';
import RequestOtp from './components/RequestOtp';
import VerifyOtp from './components/VerifyOtp';
import ResetPassword from './components/ResetPassword';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/login' element={<Login />} />
        <Route path='/' element={<Login />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/customer-dashboard" element={<CustomerDashboard />}/>
        <Route path="/agent-dashboard" element={<AgentDashboard />}/>
        <Route path="/employee-dashboard" element={<EmployeeDashboard />}/>
        <Route path="/forget-password" element={<RequestOtp />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        <Route path="/reset-password" element={<ResetPassword />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
