import React, { useState, useEffect, Link } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

const LoginComponent = () => {
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

    let url = apiConfig.getLoginUrl;
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
              <img src="logo2.png" alt="logo2" classNameName="mx-4" />
            </a>
            <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
              <li>
                <a href="#" className="nav-link px-2 text-secondary">
                  <a href="/prijava">
                    <button type="button" className="btn btn-outline-light me-2">
                      Prijava štete
                    </button>
                  </a>
                </a>
              </li>
              <li>
                <a href="#" className="nav-link px-2 text-white">
                  <a href="/statistika">
                    <button type="button" className="btn btn-outline-light me-2">
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

export default LoginComponent;
