import { BrowserRouter as Router, useNavigate, Link } from "react-router-dom";
import Cookies from "js-cookie";
import React, { useState, useEffect } from "react";
import apiConfig from "./apiConfig";

const server = apiConfig.getLogoutUrl;

const HeaderComponent = () => {
  const [postojiKolacic, postaviPostojiKolacic] = useState(false);
  const email = Cookies.get("name");
  const role = Cookies.get("role");
  const navigate = useNavigate();

  useEffect(() => {
    const kolacici = Cookies.get();

    if (Object.keys(kolacici).length > 0) {
      postaviPostojiKolacic(true);
    }
  }, []);

  const handleLogout = async () => {
    try {
      const response = await fetch(`${server}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        Cookies.remove("name");
        Cookies.remove("role");
        Cookies.remove("id");
        postaviPostojiKolacic(false);
        console.log("Korisnik odjavljen!");
        navigate("/");
      } else {
        console.error("Error logging out");
      }
    } catch (error) {
      console.error("Error logging out:", error);
    }

    // Clear cookies and navigate logic (your existing code)
  };

  return (
    <header className="p-3 text-bg-dark">
      <div className="container">
        <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
          <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
            <li>
              <Link to="/">
                <img
                  src="../conLogo.png"
                  alt="logo2"
                  className="mx-4 logo-image"
                />
              </Link>
            </li>
            {role !== "admin" && (
              <li>
                <Link to="/prijava" className="nav-link px-2 text-secondary">
                  <button type="button" className="btn btn-outline-light me-2">
                    Prijava štete
                  </button>
                </Link>
              </li>
            )}

            <li>
              <Link to="/statistika" className="nav-link px-2 text-white">
                <button type="button" className="btn btn-outline-light me-2">
                  Statistika
                </button>
              </Link>
            </li>
          </ul>
          <div className="text-end">
            {postojiKolacic ? (
              <>
                <div className="dropdown text-end">
                  <a
                    href="#"
                    className="d-block link-body-emphasis text-decoration-none dropdown-toggle text-light"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <button
                      type="button"
                      className="btn btn-outline-light me-2"
                    >
                      {email}
                    </button>
                  </a>
                  <ul className="dropdown-menu text-small">
                    <li>
                      {role === "admin" ? (
                        <Link
                          to="/admin"
                          className="btn btn-outline-dark dropdown-item"
                        >
                          Ured
                        </Link>
                      ) : (
                        <Link
                          to="/profile"
                          className="btn btn-outline-dark dropdown-item"
                        >
                          Profile
                        </Link>
                      )}
                    </li>
                    <li>
                      <hr className="dropdown-divider" />
                    </li>
                    <li>
                      <button
                        type="button"
                        onClick={handleLogout}
                        className="btn btn-outline-dark dropdown-item"
                      >
                        Odjava
                      </button>
                    </li>
                  </ul>
                </div>
              </>
            ) : (
              <>
                <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                  <div className="dropdown">
                    <button
                      className="btn btn-outline-light dropdown-toggle me-2"
                      type="button"
                      id="loginDropdown"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      Prijava
                    </button>
                    <ul
                      className="dropdown-menu"
                      aria-labelledby="loginDropdown"
                    >
                      <li>
                        <Link to="/user-login" className="dropdown-item">
                          Prijava korisnika
                        </Link>
                      </li>
                      <li>
                        <Link to="/office-login" className="dropdown-item">
                          Gradski ured prijava
                        </Link>
                      </li>
                    </ul>
                  </div>
                  <div className="dropdown">
                    <button
                      className="btn btn-warning dropdown-toggle"
                      type="button"
                      id="registerDropdown"
                      data-bs-toggle="dropdown"
                      aria-expanded="false"
                    >
                      Registracija
                    </button>
                    <ul
                      className="dropdown-menu"
                      aria-labelledby="registerDropdown"
                    >
                      <li>
                        <Link to="/user-register" className="dropdown-item">
                          Registracija korisnika
                        </Link>
                      </li>
                      <li>
                        <Link to="/office-register" className="dropdown-item">
                          Gradski ured registracija
                        </Link>
                      </li>
                    </ul>
                  </div>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default HeaderComponent;
