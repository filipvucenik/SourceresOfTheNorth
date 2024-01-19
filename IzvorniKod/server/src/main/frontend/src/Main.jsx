import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import MapComponent from "./MapComponent.jsx";
import FooterComponent from "./FooterComponent.jsx";
import HeaderComponent from "./HeaderComponent.jsx";
import FilterComponent from "./Filter.jsx";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";
import "./Main.css";
import Cookies from "js-cookie";


function Main() {
  const [postojiKolacic, postaviPostojiKolacic] = useState(false);
  const email = Cookies.get("mail");

  useEffect(() => {
    const kolacici = Cookies.get(); // Dohvaćanje svih kolačića
    console.log(kolacici);

    if (Object.keys(kolacici).length > 0) {
      postaviPostojiKolacic(true);
    }
  }, []);

  //const signedIn = false;
  
  return (
    <div className="App">
      <HeaderComponent/>
      <div className="title">
        <h1> Mapa prijavljenih šteta</h1>
      </div>
      <MapComponent />
      
      <FooterComponent />
    </div>
  );
}

export default Main;
