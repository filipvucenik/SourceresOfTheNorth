import React, { useState } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";

const LoginComponent2 = () => {
    const navigate = useNavigate();
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
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(jsonData),
        })
          .then((response) => {
            if(response.status==200){
                Cookies.set("mail", mail);
                navigate("/");
            }else{
                alert("Registracije nije uspijela");
            }
          });
      };

    return (  
        <div className="form-container">

        <form className="register-form">
          <h2>Registracija</h2>
          <input type="text" placeholder="Ime" id="imeReg" required="" />
          <input type="text" placeholder="Prezime" id="prezReg" required="" />
          <input type="email" placeholder="E-mail" id="mailReg" required="" />
          <input type="password" placeholder="Šifra" id="passReg" required="" />
          <button type="button" onClick={handleBuildRegJson}>
            Registracija
          </button>
        </form>
        </div>
    );
}
 
export default LoginComponent2;