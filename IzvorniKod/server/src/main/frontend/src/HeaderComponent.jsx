import { BrowserRouter as Router, Link } from "react-router-dom";
import Cookies from "js-cookie";
import React, { useState, useEffect } from "react";

const HeaderComponent = () => {
  const [postojiKolacic, postaviPostojiKolacic] = useState(false);
  const email = Cookies.get("name");

  useEffect(() => {
    const kolacici = Cookies.get();
    console.log(kolacici);

    if (Object.keys(kolacici).length > 0) {
      postaviPostojiKolacic(true);
    }
  }, []);

  const handleLogout = () => {
    Cookies.remove("name");
    postaviPostojiKolacic(false);
    console.log("Korisnik odjavljen!");
  };

  return (
    <header className="p-3 text-bg-dark">
      <div className="container">
        <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        
          <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
            <li>
            <Link to="/">
                  <img src="./logo_road.webp" alt="logo2" className="mx-4 logo-image" />
              </Link>
            </li>
            <li>
                <Link to="/prijava" className="nav-link px-2 text-secondary">
                  <button type="button" className="btn btn-outline-light me-2">
                    Prijava štete
                  </button>
                </Link>
              
            </li>
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
                    <button type="button" className="btn btn-outline-light me-2">
                      {email}
                    </button>
                  </a>
                  <ul className="dropdown-menu text-small">
                    <li>
                      <Link
                        to="/profile"
                        className="btn btn-outline-dark dropdown-item"
                      >
                        Profile
                      </Link>
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
                <div className="right">
                  <Link to="/login">
                    <button type="button" className="btn btn-outline-light me-2">
                      Login
                    </button>
                  </Link>
                  <Link to="/registracija">
                    <button type="button" className="btn btn-warning">
                      Register
                    </button>
                  </Link>
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
