import React from "react";
import MapComponent from "./MapComponent.jsx";
import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import ReportCard from "./Rforma.jsx";
import LoginComponent from "./Lforma.jsx";
import StatisticComponent from "./StatisticComponent.jsx";
import Main from "./Main.jsx";
import Reports from "./Report.jsx";
import Admin from "./Admin.jsx";
import LoginComponent2 from "./Regforma.jsx"
import "./App.css";
import Filter from "./Filter.jsx";
import Profile from "./Profile.jsx";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/prijava" element={<ReportCard />} />
        <Route path="/login" element={<LoginComponent />} />
        <Route path="/registracija" element={<LoginComponent2 />} />
        <Route path="/statistika" element={<StatisticComponent />} />
        <Route path="/report/:id" element={<Reports />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/filter" element={<Filter />} />
        <Route path="/profile" element={<Profile />} />

        {/*}<Route path="/registracija" element={<RegisterComponent />} />
          <Route path="/profile" element={<ProfilePage />} />{*/}
      </Routes>
    </Router>
  );
}

export default App;
