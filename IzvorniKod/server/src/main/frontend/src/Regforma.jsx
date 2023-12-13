import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import "./Lforma.css";
import { useNavigate } from "react-router-dom";
import FooterComponent from "./FooterComponent";

const LoginComponent2 = () => {
    const navigate = useNavigate();

    useEffect(() => {
      const isLoggedIn = Cookies.get();
  
      if (Object.keys(isLoggedIn).length > 0) {
        navigate('/');
      }
    }, [navigate]);

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
        let errorMsg="";
        if(mail===''){
          errorMsg=errorMsg+"Mail ne može biti prazan!\n"
        }
        if(!mail.includes('.') || !mail.includes('@')){
          errorMsg=errorMsg+"Mail nije ispravan!\n";
        }

        if(name===''){
          errorMsg=errorMsg+"Ime ne može biti prazno!\n"
        }
        if(surr===''){
          errorMsg=errorMsg+"Prezime ne može biti prazno!\n"
        }
        if(pass.length<8 || pass.lenght>100){
          errorMsg=errorMsg+"Šifra je mora bit između 8 i 100 znakova"
          
        }
        if(errorMsg!=""){
          alert(errorMsg);
          return;
        }

        

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
        <>
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
        <FooterComponent/>
        </>
    );
}
 
export default LoginComponent2;