import React from "react";
import MapComponent from "./MapComponent.jsx";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import PrijavaSteteComponent from "./Rforma.jsx";
import LoginComponent from "./Lforma.jsx";
import StatisticComponent from "./StatisticComponent.jsx";
import Main from "./Main.jsx";
import Reports from "./Report.jsx";
import Admin from "./Admin.jsx";
import "./App.css";

function App() {
  const signedIn = false;
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/prijava" element={<PrijavaSteteComponent />} />
        <Route path="/login" element={<LoginComponent />} />
        <Route path="/statistika" element={<StatisticComponent />} />
        <Route path="/report/:id" element={<Reports />} />
        <Route path="/admin" element={<Admin />} />

        {/*}<Route path="/registracija" element={<RegisterComponent />} />
          <Route path="/profile" element={<ProfilePage />} />{*/}
      </Routes>
    </Router>
  );
}

export default App;
