import React, { useState, useEffect } from "react";
import HeaderComponent from './HeaderComponent'
import './profile.css'
import FooterComponent from "./FooterComponent";

const server = "http://localhost:8080/users/";

const Profile = () => {
  /*const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    password: "",
    email: "",
  });
  const id = kolacici.id;

  useEffect(() => {
    const kolacici = Cookies.get();
    console.log(kolacici);

    if (Object.keys(kolacici).length > 0) {
      postaviPostojiKolacic(true);
    }
  }, []);

  useEffect(() => {
    const fetchDataFromDatabase = async () => {
      try {
        const response = await fetch(`${server}${id})`);
        const data = await response.json();
        if (data) {
          setUserData(data[0]);
        }
      } catch (error) {
        console.error("Greška pri dohvaćanju podataka:", error);
      }
    };
    fetchDataFromDatabase();
  }, []);
  
  const updateDataInDatabase = async () => {
    try {
      const updatedData = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value
      };
  
      // Poziv API-ja za ažuriranje podataka u bazi
      const response = await fetch(`${server}${id}`), {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedData),
      });
  
      if (response.ok) {
        console.log("Podaci uspješno ažurirani.");
        // Ažuriraj lokalno stanje s novim podacima
        setUserData(updatedData);
      } else {
        console.error("Greška prilikom ažuriranja podataka.");
      }
    } catch (error) {
      console.error("Greška pri ažuriranju podataka:", error);
    }
  };*/
  return (
    <>
    <HeaderComponent/>
      <div className='najjaciDiv'>
        <form>
          <div className="row g-3">
            <div className="col-sm-6">
              <label for="firstName" className="form-label">Ime</label>
              <input type="text" className="form-control" id="firstName" placeholder="" value="" required=""/>
              {/*}{userData.ime || ""}{ pod value */}
              <div className="invalid-feedback">
                Valid first name is required.
              </div>
            </div>

            <div className="col-sm-6">
              <label for="lastName" className="form-label">Prezime</label>
              <input type="text" className="form-control" id="lastName" placeholder="" value="" required=""/>
              {/*}{userData.prezime || ""}{ pod value */}
              <div className="invalid-feedback">
                Valid last name is required.
              </div>
            </div>

            <div className="col-12">
              <label for="password" className="form-label">Lozinka</label>
              <div className="input-group has-validation">
                <input type="text" className="form-control" id="password" placeholder="password" required=""/>
                {/*}{userData.password || ""}{ pod value */}
              <div className="invalid-feedback">
                  Your password is required.
                </div>
              </div>
            </div>

            <div className="col-12">
              <label for="email" className="form-label">Email</label>
              <input type="email" className="form-control" id="email" placeholder="you@example.com" value=""/>
              {/*}{userData.email || ""}{ pod value */}
              <div className="invalid-feedback">
                Please enter a valid email address for shipping updates.
              </div>
            </div>
          </div>
          <hr className="my-4"/>
          <button type="button" className="btn btn-outline-dark me-2" >Spremi</button>
          {/*onClick={updateDataInDatabase} deti gore kod Spremi buttona*/}
          <button type="button" className="btn btn-outline-dark me-2">Obriši račun</button>
        </form>
      </div>
    <FooterComponent/>
    </>
    
  )
}

export default Profile
