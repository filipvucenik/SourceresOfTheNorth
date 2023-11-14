import React from "react";
import MapComponent from "./MapComponent.jsx";
import { BrowserRouter, Route, Routes, Link } from "react-router-dom";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";

import Admin from "./Admin.jsx";
import Main from "./Main.jsx";
import Reports from "./Report.jsx";

const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/admin" element={<Admin />} />
        <Route path="/report" element={<Reports />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
