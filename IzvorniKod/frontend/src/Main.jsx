import React from "react";
import MapComponent from "./MapComponent.jsx";
import { BrowserRouter as Router, Link } from "react-router-dom";
// import PrijavaSteteComponent from "./PrijavaSteteComponent.jsx";
// import LoginComponent from "./LoginComponent.jsx";
import "./Main.css";

function Main() {
  const signedIn = false;
  return (
    <div className="App">
      <div className="welcome">
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
        {signedIn ? (
          <Link to="/profile" className="profile-button">
            <button className="username">Prijavljeni korisnik</button>
          </Link>
        ) : (
          <>
            <div className="right">
              <Link to="/login" className="profile-button">
                <button className="login">Log in</button>
              </Link>
              <Link to="/login" className="profile-button">
                <button className="login">Register</button>
              </Link>
            </div>
          </>
        )}
      </div>
      <MapComponent />
      <div className="footer"></div>
      {/*<Link to="/prijava" className="profile-button">Prijava štete</Link>
      <Link to="/login" className="profile-button">Prijava</Link>*/}
    </div>
  );
}

export default Main;
