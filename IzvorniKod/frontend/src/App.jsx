import React from "react";
import MapComponent from "./MapComponent.jsx";
import { BrowserRouter, Route, Routes, Link } from "react-router-dom";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";

import Admin from "./Admin.jsx";
import Main from "./Main.jsx";
import Reports from "./Report.jsx";
import Lforma from "./Lforma.jsx";
import Rforma from "./Rforma.jsx";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/report" element={<Reports />} />
        <Route path="/login" element={<Lforma />} />
        <Route path="/prijava" element={<Rforma></Rforma>} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
