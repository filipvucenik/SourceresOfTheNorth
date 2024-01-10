import React, { useState, useEffect } from "react";
import HeaderComponent from './HeaderComponent';
import Cookies from "js-cookie";
import apiConfig from "./apiConfig";
import './profile.css';
import FooterComponent from "./FooterComponent";

const server = apiConfig.getUserInfoUrl;

const Profile = () => {
  const [postojiKolacic, postaviPostojiKolacic] = useState(false);
  

  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    password: "",
    email: "",
  });
  var id;

  useEffect(() => {
    const kolacici = Cookies.get();
    console.log(kolacici);
    id=kolacici.id;
    if (Object.keys(kolacici).length > 0) {
      postaviPostojiKolacic(true);
    }
  }, []);

  useEffect(() => {
    const fetchDataFromDatabase = async () => {
      try {
        const response = await fetch(`${server}/${id}`);
        const data = await response.json();
        if (data) {
          setUserData(data);
        }
      } catch (error) {
        console.error("Greška pri dohvaćanju podataka:", error);
      }
    };
    fetchDataFromDatabase();
  }, []);
  
  const updateDataInDatabase = async (e) => {
    e.preventDefault();
  
    const firstName = e.target.elements.firstName.value;
    const lastName = e.target.elements.lastName.value;
    const password = e.target.elements.password.value;
    const email = e.target.elements.email.value;
  
    const updatedData = {
      firstName,
      lastName,
      email,
    };
  
    if (password.trim() !== "") {
      updatedData.password = password;
    }
  
    try {
      const response = await fetch(`${server}/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedData),
      });
  
      if (response.ok) {
        console.log("Podaci uspješno ažurirani.");
        setUserData(updatedData);
      } else {
        console.error("Greška prilikom ažuriranja podataka.");
      }
    } catch (error) {
      console.error("Greška pri ažuriranju podataka:", error);
    }
  };  

  return (
    <>
    <HeaderComponent/>
      <div className='najjaciDiv'>
        <form>
          <div className="row g-3">
            <div className="col-sm-6">
              <label for="firstName" className="form-label">Ime</label>
              <input type="text" className="form-control" name="firstName" defaultValue={userData.firstName || ""}/>
              <div className="invalid-feedback">
                Valid first name is required.
              </div>
            </div>

            <div className="col-sm-6">
              <label for="lastName" className="form-label">Prezime</label>
              <input type="text" className="form-control" name="lastName" defaultValue={userData.lastName || ""}/>
              <div className="invalid-feedback">
                Valid last name is required.
              </div>
            </div>

            <div className="col-12">
              <label for="email" className="form-label">Email</label>
              <input type="email" className="form-control" name="email" defaultValue={userData.email || ""}/>
              <div className="invalid-feedback">
                Please enter a valid email address for shipping updates.
              </div>
            </div>

            <div className="col-12">
              <label for="password" className="form-label">Lozinka</label>
              <div className="input-group has-validation">
                <input type="text" className="form-control" id="password"/>
              <div className="invalid-feedback">
                  Your password is required.
                </div>
              </div>
            </div>
            
          </div>
          <hr className="my-4"/>
          <button type="button" className="btn btn-outline-dark me-2" onClick={updateDataInDatabase} >Spremi</button>
          <button type="button" className="btn btn-outline-dark me-2">Obriši račun</button>
        </form>
      </div>
    <FooterComponent/>
    </>
    
  )
}

export default Profile
