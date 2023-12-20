import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";

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
      navigate('/');
    }
  }, [navigate]);

  const handleButtonClick = () => {
    let jsonData = {
      email: email,
      password: password,
    };

    let url = "http://localhost:8080/auth/userLogin";

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
        Cookies.set("name", "maillllllllic yeah");
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

  const handleBuildRegJson = () => {
    let url = "http://localhost:8080/auth/userRegister";
    let pass = document.getElementById("passReg").value;
    let name = document.getElementById("imeReg").value;
    let surr = document.getElementById("prezReg").value;
    let mail = document.getElementById("mailReg").value;

    const jsonData = {
      email: mail,
      firstName: name,
      lastName: surr,
      password: pass
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