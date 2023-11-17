import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";
import apiConfig from "./apiConfig";

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
      <div className="welcome">
        <h1>Dobrodošli!</h1>
      </div>
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
    </>
  );
};

export default LoginComponent;
