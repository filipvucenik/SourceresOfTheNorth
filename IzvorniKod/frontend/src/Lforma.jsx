import React, { useState } from "react";
import "./Lforma.css";
import { Navigate } from "react-router-dom";

const Lforma = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");

  const handleButtonClick = () => {
    let jsonData = {
      username: username,
      password: password,
    };

    let url = "http://localhost:8080/auth/userLogin";

    var stuff = JSON.stringify(jsonData);

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: stuff,
    }).then((response) => {
      if (response.status === 200) {
        Navigate;
      }
    });
  };

  const handleLoginInputChange = (e) => {
    setUsername(e.target.value);
  };

  const handlePasswordInputChange = (e) => {
    setPassword(e.target.value);
  };

  const handleBuildRegJson = () => {
    let url = "http://localhost:8080/auth/userRegister";
    let username = document.getElementById("userReg").value;
    let pass = document.getElementById("passReg").value;
    let name = document.getElementById("imeReg").value;
    let surr = document.getElementById("prezReg").value;
    let mail = document.getElementById("mailReg").value;

    const jsonData = {
      email: mail,
      firstName: name,
      lastName: surr,
      password: pass,
      userName: username,
    };

    console.log(jsonData);

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    })
      .then((response) => console.log(response))
      .then((data) => console.log("Registration Success:", data))
      .catch((error) => console.error("Registration Error:", error));
  };

  return (
    <>
      <div className="welcome">
        <h1>Dobrodošli!</h1>
      </div>
      <div className="form-container">
        <form className="login-form">
          <h2>Prijava</h2>
          <input
            type="text"
            placeholder="Korisničko ime"
            id="UserLogin"
            required=""
            onChange={handleLoginInputChange}
          />
          <input
            type="password"
            placeholder="Šifra"
            id="passLogin"
            required=""
            onChange={handlePasswordInputChange}
          />
          <button type="button" id="loginButton" onClick={handleButtonClick}>
            Prijava
          </button>
        </form>
        <form className="register-form">
          <h2>Registracija</h2>
          <input
            type="text"
            placeholder="Korisničko ime"
            id="userReg"
            required=""
          />
          <input type="password" placeholder="Šifra" id="passReg" required="" />
          <input type="text" placeholder="Ime" id="imeReg" required="" />
          <input type="text" placeholder="Prezime" id="prezReg" required="" />
          <input type="email" placeholder="E-mail" id="mailReg" required="" />
          <button type="button" onClick={handleBuildRegJson}>
            Registracija
          </button>
        </form>
      </div>
    </>
  );
};

export default Lforma;
