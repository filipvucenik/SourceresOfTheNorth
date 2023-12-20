import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import MapComponent from "./MapComponent.jsx";
import FooterComponent from "./FooterComponent.jsx";
import HeaderComponent from "./HeaderComponent.jsx";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";
import "./Main.css";

function Main() {
  //const signedIn = false;

  return (
    <div className="App">
      <HeaderComponent/>
      <div className="title">
        <h1> Mapa prijavljenih šteta</h1>
        <Link to="/filter"><button className="btn btn-outline-dark m-2">Filter prijava</button></Link>
      </div>
      <MapComponent />
      <FooterComponent />
    </div>
  );
}

export default Main;