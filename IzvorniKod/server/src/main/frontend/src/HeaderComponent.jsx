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
    <header class="p-3 text-bg-dark">
      <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <a href="/">
          <img src="./logo2.png" className="mx-4" alt="Logo" />
        </a>
          <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
            <li>
              <a href="#" class="nav-link px-2 text-secondary">
                <Link to="/prijava">
                  <button type="button" class="btn btn-outline-light me-2">
                    Prijava štete
                  </button>
                </Link>
              </a>
            </li>
            <li>
              <a href="#" class="nav-link px-2 text-white">
                <Link to="/statistika">
                  <button type="button" class="btn btn-outline-light me-2">
                    Statistika
                  </button>
                </Link>
              </a>
            </li>
          </ul>
          <div class="text-end">
            {postojiKolacic ? (
              <>
                <div class="dropdown text-end">
                  <a
                    href="#"
                    class="d-block link-body-emphasis text-decoration-none dropdown-toggle text-light"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <button type="button" class="btn btn-outline-light me-2">
                      {email}
                    </button>
                  </a>
                  <ul class="dropdown-menu text-small">
                    <li>
                      <Link
                        to="/profile"
                        class="btn btn-outline-dark dropdown-item"
                      >
                        Profile
                      </Link>
                    </li>
                    <li>
                      <hr class="dropdown-divider" />
                    </li>
                    <li>
                      <button
                        type="button"
                        onClick={handleLogout}
                        class="btn btn-outline-dark dropdown-item"
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
                    <button type="button" class="btn btn-outline-light me-2">
                      Login
                    </button>
                  </Link>
                  <Link to="/registracija">
                    <button type="button" class="btn btn-warning">
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
