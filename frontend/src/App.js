import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import AdminDashboard from './components/adminPages/AdminDashboard';


function App() {
  return (

    <BrowserRouter>
    <Routes>
            <Route exact path='/login' element={<Login/>}></Route>
            <Route exact path='' element={<Login/>}></Route>
            <Route exact path='/admin-dashboard' element={<AdminDashboard/>}/>
        </Routes>
      </BrowserRouter>
   
  );
}

export default App;
