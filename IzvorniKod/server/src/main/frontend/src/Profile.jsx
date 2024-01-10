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
  const [id, setId] = useState("");

  useEffect(() => {
    const kolacici = Cookies.get();
    console.log(kolacici);

    if (kolacici && kolacici.id) {
      setId(kolacici.id);
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
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value,
    }));
  };

  const updateDataInDatabase = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(`${server}/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log("Podaci uspješno ažurirani.");
        setUserData(userData);
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
              <label htmlFor="firstName" className="form-label">
                Ime
              </label>
              <input
                type="text"
                className="form-control"
                name="firstName"
                value={userData.firstName}
                onChange={handleChange}
              />
              <div className="invalid-feedback">
                Valid first name is required.
              </div>
            </div>

            <div className="col-sm-6">
              <label htmlFor="lastName" className="form-label">
                Prezime
              </label>
              <input
                type="text"
                className="form-control"
                name="lastName"
                value={userData.lastName}
                onChange={handleChange}
              />
              <div className="invalid-feedback">
                Valid last name is required.
              </div>
            </div>

            <div className="col-12">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                type="email"
                className="form-control"
                name="email"
                value={userData.email}
                onChange={handleChange}
              />
              <div className="invalid-feedback">
                Please enter a valid email address for shipping updates.
              </div>
            </div>

            <div className="col-12">
              <label htmlFor="password" className="form-label">
                Lozinka
              </label>
              <div className="input-group has-validation">
                <input
                  type="text"
                  className="form-control"
                  id="password"
                  name="password"
                  onChange={handleChange}
                />
                <div className="invalid-feedback">
                  Your password is required.
                </div>
              </div>
            </div>
          </div>
          <hr className="my-4"/>
          <button
            type="button"
            className="btn btn-outline-dark me-2"
            onClick={updateDataInDatabase}
          >
            Spremi
          </button>
          <button type="button" className="btn btn-outline-dark me-2">
            Obriši račun
          </button>
        </form>
      </div>
    <FooterComponent/>
    </>
    
  )
}

export default Profile
