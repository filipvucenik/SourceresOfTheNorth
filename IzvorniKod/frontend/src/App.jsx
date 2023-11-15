import React from 'react';
import MapComponent from './MapComponent.jsx';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import PrijavaSteteComponent from './Rforma.jsx';
import LoginComponent from './Lforma.jsx';
import StatisticComponent from './StatisticComponent.jsx';
import './App.css';


function App() {
  const signedIn = false;
  return (
    <Router>
    <div className="App">
      <div className='title'><h1> Mapa prijavljenih šteta</h1></div>
      <div className='header'>
        <div className='left'>
          <Link to="/prijava" className="profile-button"><button className='prijavaStete'>Prijava štete</button></Link>
          <Link to="/statistika" className="profile-button"><button className='prijavaStete'>Statistika</button></Link>
        </div>
          {signedIn ? (
            <Link to="/profile" className="profile-button"><button className='username'>Prijavljeni korisnik</button></Link>
          ) : (
            <>
            <div className='right'>
              <Link to="/login" className="profile-button"><button className='login'>Log in</button></Link>
              <Link to="/registracija" className="profile-button"><button className='login'>Register</button></Link>
            </div>
            </>
          )}
        </div>
      <div className='footer'></div>
      <Routes>
          <Route path="/" element={<MapComponent />} />
          <Route path="/prijava" element={<PrijavaSteteComponent />} />
          <Route path="/login" element={<LoginComponent />} />
          <Route path="/statistika" element={<StatisticComponent />} />
          {/*}<Route path="/registracija" element={<RegisterComponent />} />
          <Route path="/profile" element={<ProfilePage />} />{*/}
    </Routes>
    </div>
    </Router>
  );
}

export default App;

