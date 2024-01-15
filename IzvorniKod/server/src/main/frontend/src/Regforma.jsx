import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";
import FooterComponent from "./FooterComponent";
import apiConfig from "./apiConfig";

const LoginComponent2 = () => {
  const customAlert = (message) => {
    const alertContainer = document.createElement('div');
    alertContainer.style.cssText = `
      position: fixed;
      top: 20px; /* Adjust the top distance as needed */
      left: 50%;
      transform: translateX(-50%);
      padding: 20px;
      background-color: white;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
      border-radius: 5px;
      text-align: center;
      z-index: 9999; /* Set a high z-index to ensure it's on top */
    `;
  
    const alertText = document.createElement('p');
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;
  
    const closeButton = document.createElement('button');
    closeButton.textContent = 'OK';
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;
    closeButton.addEventListener('click', () => document.body.removeChild(alertContainer));
  
    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);
  
    document.body.appendChild(alertContainer);
  };
  const navigate = useNavigate();

  useEffect(() => {
    const isLoggedIn = Cookies.get();

    if (Object.keys(isLoggedIn).length > 0) {
      navigate("/");
    }
  }, [navigate]);

  const handleBuildRegJson = async() => {
    let url = apiConfig.getRegisterUrl;
    let pass = document.getElementById("passReg").value;
    let name = document.getElementById("imeReg").value;
    let surr = document.getElementById("prezReg").value;
    let mail = document.getElementById("mailReg").value;

    const jsonData = {
      email: mail,
      firstName: name,
      lastName: surr,
      password: pass,
    };
    let errorMsg = "";
    if (mail === "") {
      errorMsg = errorMsg + "Mail ne može biti prazan!\n";
    }
    if (!mail.includes(".") || !mail.includes("@")) {
      errorMsg = errorMsg + "Mail nije ispravan!\n";
    }

    if (name === "") {
      errorMsg = errorMsg + "Ime ne može biti prazno!\n";
    }
    if (surr === "") {
      errorMsg = errorMsg + "Prezime ne može biti prazno!\n";
    }
    if (pass.length < 8 || pass.lenght > 100) {
      errorMsg = errorMsg + "Šifra je mora bit između 8 i 100 znakova!";
    }
    if (errorMsg != "") {
      customAlert(errorMsg);
      return;
    }

   const  backFetch= await fetch(url, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    })
    const dataFetched= await backFetch.json()
    console.log(dataFetched);
    if(backFetch.status==200){
      Cookies.set("name",mail);
      Cookies.set("id",dataFetched.userId)
      navigate("/");
    }else{
      customAlert("Registracija nije uspjela!");
    }

  };

  return (
    <>
      <header className="p-3 text-bg-dark">
        <div className="container">
          <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a
              href="/"
              className="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none"
            >
              <svg
                className="bi me-2"
                width="40"
                height="32"
                role="img"
                aria-label="Bootstrap"
              >
                <use href="#bootstrap"></use>
              </svg>
            </a>
            <a href="/">
              <img
                src="./conLogo.png"
                alt="logo2"
                className="mx-4 logo-image"
              />
            </a>
            <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
              <li>
                <a href="#" className="nav-link px-2 text-secondary">
                  <a href="/prijava">
                    <button
                      type="button"
                      className="btn btn-outline-light me-2"
                    >
                      Prijava štete
                    </button>
                  </a>
                </a>
              </li>
              <li>
                <a href="#" className="nav-link px-2 text-white">
                  <a href="/statistika">
                    <button
                      type="button"
                      className="btn btn-outline-light me-2"
                    >
                      Statistika
                    </button>
                  </a>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </header>
      <div className="form-container">
        <form className="register-form">
          <h2>Registracija</h2>
          <input type="text" placeholder="Ime" id="imeReg" required />
          <input type="text" placeholder="Prezime" id="prezReg" required />
          <input type="email" placeholder="E-mail" id="mailReg" required />
          <input type="password" placeholder="Šifra" id="passReg" required />
          <button type="button" onClick={handleBuildRegJson}>
            Registracija
          </button>
        </form>
      </div>
      <FooterComponent />
    </>
  );
};

export default LoginComponent2;
