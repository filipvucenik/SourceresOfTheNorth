import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import MapComponent from "./MapComponent.jsx";
import FooterComponent from "./FooterComponent.jsx";
import HeaderComponent from "./HeaderComponent.jsx";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";
import "./Main.css";

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
      <div className="header">
        <div className="left">
          <Link to="/prijava" className="profile-button">
            <button className="prijavaStete">Prijava štete</button>
          </Link>
          <Link to="/statistika" className="profile-button">
            <button className="prijavaStete">Statistika</button>
          </Link>
        </div>
        {postojiKolacic ? (
          <Link to="/profile" className="profile-button">
            <button className="username">{email}</button>
          </Link>
        ) : (
          <>
            <div className="right">
              <Link to="/login" className="profile-button">
                <button className="login1">Log in</button>
              </Link>
              <Link to="/registracija" className="profile-button">
                <button className="login2">Register</button>
              </Link>
            </div>
          </>
        )}
        <Link to="/filter"><button className="btn btn-outline-dark m-2">Filter prijava</button></Link>
      </div>
      <MapComponent />
      <FooterComponent />
    </div>
  );
}

export default Main;
