import React, { useState, useEffect } from "react";
import {useNavigate, Link} from "react-router-dom";
import HeaderComponent from "./HeaderComponent";
import Cookies from "js-cookie";
import apiConfig from "./apiConfig";
import "./profile.css";
import FooterComponent from "./FooterComponent";

const server = apiConfig.getUserInfoUrl;
const server2 = apiConfig.getReportUrl;
const server3 = apiConfig.getLogoutUrl;
var id;

const Profile = () => {
  const [postojiKolacic, postaviPostojiKolacic] = useState(false);
  const [filteredData, setFilteredData] = useState([]);
  const [filteredDataReports, setFilteredReportsData] = useState([]);
  const [manualAddress, setManualAddress] = useState("");
  const navigate = useNavigate();
  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    password: "",
    email: "",
  });
  const [showPasswordDiv, setShowPasswordDiv] = useState(false);

  const handleClick = () => {
    setShowPasswordDiv(!showPasswordDiv);
  };

  const handleLogout = async() => {
    try {
      const response = await fetch(`${server3}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        Cookies.remove("name");
    Cookies.remove("id");
    Cookies.remove("JSESSIONID");
    postaviPostojiKolacic(false);
    console.log("Korisnik odjavljen!");
    navigate("/");
      } else {
        console.error("Error logging out");
      }
    } catch (error) {
      console.error("Error logging out:", error);
    }
  };

  const customAlert = (message) => {
    const alertContainer = document.createElement("div");
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

    const alertText = document.createElement("p");
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;

    const closeButton = document.createElement("button");
    closeButton.textContent = "OK";
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;
    closeButton.addEventListener("click", () => {
      handleLogout();

      document.body.removeChild(alertContainer);
    });

    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);

    document.body.appendChild(alertContainer);
  };

  const customAlertUpdate = (message) => {
    const alertContainer = document.createElement("div");
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

    const alertText = document.createElement("p");
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;

    const closeButton = document.createElement("button");
    closeButton.textContent = "OK";
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;
    closeButton.addEventListener("click", () => {
      document.body.removeChild(alertContainer);
    });

    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);

    document.body.appendChild(alertContainer);
  };

  useEffect(() => {
    const kolacici = Cookies.get();
    console.log(kolacici);

    if (kolacici && kolacici.id) {
      id = kolacici.id;
      console.log(id);
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

  useEffect(() => {
    const fetchDataFromDatabase2 = async () => {
      try {
        const response = await fetch(`${server2}/user/${id}`);
        const data = await response.json();
        setFilteredData(data);
        setFilteredReportsData(data.reports);
        console.log(data);
        console.log(data.reports);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchDataFromDatabase2();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value,
    }));
  };

  const deleteProfile = async () => {
    try {
      const response = await fetch(`${server}/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        customAlert("Korisnički račun uspješno izbrisan!");
      } else {
        console.error("Error deleting profile");
      }
    } catch (error) {
      console.error("Error deleting profile:", error);
    }
  };
  

  const updateDataInDatabase = async (e) => {
    e.preventDefault();
    // Stvori novi objekt bez praznih stringova
    const dataForSend = Object.keys(userData).reduce((acc, key) => {
      if (key === "password" && userData[key] !== "") {
        acc[key] = userData[key];
      } else if (key !== "password" && userData[key] !== "") {
        acc[key] = userData[key];
      }
      return acc;
    }, {});
    console.log(dataForSend);
    try {
      const response = await fetch(`${server}/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(dataForSend),
      });

      if (response.ok) {
        setUserData(userData);
        customAlertUpdate("Podaci uspješno ažurirani!");
      } else {
        console.error("Greška prilikom ažuriranja podataka.");
      }
    } catch (error) {
      console.error("Greška pri ažuriranju podataka:", error);
    }
  };

  return (
    <>
      <HeaderComponent />
      <div className="najjaciDiv">
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

            <div>
              <button
                onClick={handleClick}
                type="button"
                className="btn btn-outline-dark me-2"
              >
                Promijeni lozinku
              </button>
              <br />
              <br />
              {showPasswordDiv && (
                <div className="col-12">
                  <label htmlFor="password" className="form-label">
                    Nova lozinka
                  </label>
                  <div className="input-group has-validation">
                    <input
                      type="password"
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
              )}
            </div>
          </div>
          <hr className="my-4" />
          <button
            type="button"
            className="btn btn-outline-dark me-2"
            onClick={updateDataInDatabase}
          >
            Spremi
          </button>
          <button type="button" className="btn btn-outline-dark me-2" onClick={deleteProfile}>
            Obriši račun
          </button>
        </form>
        <br />
        <h1>Moje prijave</h1>
        <ul className="profilStatistika">
          <li>Broj prijava na čekanju: {filteredData.waitingCount}</li>
          <li>
            Broj prijava u procesu rješavanja: {filteredData.inProgressCount}{" "}
          </li>
          <li>Broj riješenih prijava: {filteredData.solvedCount}</li>
        </ul>
      </div>
      <ul className="statistika">
        {filteredDataReports.length > 0 ? (
          filteredDataReports.map((item) => (
            <li key={item.report.reportID} className="statistika-child">
              <h2>
                <b>{item.report.reportHeadline}</b>
              </h2>
              <p><b>ID prijave:</b> {item.report.reportID}</p>
              <p><b>Kategorija:</b> {item.category.categoryName}</p>
              <p>
                <b>Vrijeme prijave:</b> <br />
                {item.report.reportTS.split("T")[0]}{" "}
                {item.report.reportTS.split("T")[1].split(".")[0]}
              </p>
              <p>
                <b>Opis prijave:</b> <br /> {item.report.description}
              </p>
              <p>
                <b>Status:</b> <nbsp></nbsp>
                {item.feedback.key.status === "uProcesu" ? (
                  <span>U procesu</span>
                ) : (
                  item.feedback.key.status === "neobraden" ? (<span>Neobrađen</span>) : (<span>Obrađen</span>)
                )}
              </p>

              {/* <p>Lokacija:</p> */}
              <Link to={`/report/${item.report.reportID}`}>
                stranica prijave
              </Link>
            </li>
          ))
        ) : (
          <p>Nema podataka</p>
        )}
      </ul>
      <FooterComponent />
    </>
  );
};

export default Profile;
