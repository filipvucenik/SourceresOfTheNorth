import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import "../Lforma.css";
import { useNavigate, Link } from "react-router-dom";
import apiConfig from "../apiConfig";
import FooterComponent from "../FooterComponent";

const Office_LoginComponent = () => {
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    // Provjeri postoji li kolačić koji ukazuje na prijavu
    const isLoggedIn = Cookies.get();

    if (Object.keys(isLoggedIn).length > 0) {
      navigate("/");
    }
  }, [navigate]);

  const handleButtonClick = () => {
    let jsonData = {
      email: email,
      password: password,
    };

    let url = apiConfig.getOfficeLoginUrl;
    console.log(jsonData);

    fetch(url, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    }).then((response) => {
      console.log(response);

      if (response.status === 200) {
        Cookies.set("name", email);
        navigate("/");
      } else {
        alert("User does not exist!");
      }
    });
  };

  const handleLoginInputChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordInputChange = (e) => {
    setPassword(e.target.value);
  };

  return (
    <>
      <header className="p-3 text-bg-dark">
        <div className="container">
          <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
              <li>
                <Link to="/">
                  <img
                    src="./logo_road.webp"
                    alt="logo2"
                    className="mx-4 logo-image"
                  />
                </Link>
              </li>
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
        <form className="login-form">
          <h2>Prijava</h2>
          <input
            type="text"
            placeholder="Email"
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
      </div>
      <FooterComponent />
    </>
  );
};

export default Office_LoginComponent;
