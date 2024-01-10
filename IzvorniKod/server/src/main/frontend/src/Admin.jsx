import { useState } from "react";
import { BrowserRouter as Router, Link, useNavigate } from "react-router-dom";
import "./admin_view.css";
import HeaderComponent from "./HeaderComponent";

function Admin() {
  const data = [
    {
      id: 1,
      name: "problem s vodom",
      status: "Neobrađeno",
      lokacija: "vrbik",
      kategorija: "voda",
    },
    {
      id: 2,
      name: "problem s vodom",
      status: "Neobrađeno",
      lokacija: "FER",
      kategorija: "voda",
    },
    {
      id: 3,
      name: "problem s vodom",
      status: "Neobrađeno",
      lokacija: "knezija",
      kategorija: "voda",
    },
  ];
  const nav = useNavigate();
  const [displaied_data, set_display] = useState([]);
  const [transfer, set_transfer] = useState([]);
  const updateDisplay = (status) => {
    set_display(
      data.filter((repo) => {
        if (repo.status === status) {
          return true;
        } else return false;
      })
    );
  };

  const redirect = (id) => {
    nav("/report/" + id);
  };

  return (
    <>
      <HeaderComponent />
      <div className="container">
        <h1>POPIS SVIH PRIJAVA</h1>

        <hr />

        <div className="btn-group btn-group-toggle">
          <button
            className="btn btn-lg btn-primary"
            onClick={() => updateDisplay("U Procesu")}
          >
            Aktivne
          </button>
          <button
            className="btn btn-lg btn-primary"
            onClick={() => updateDisplay("Neobrađeno")}
          >
            Na čekanju
          </button>
          <button
            className="btn btn-lg btn-primary"
            onClick={() => updateDisplay("Obrađeno")}
          >
            Riješene
          </button>
        </div>
        {displaied_data.length > 0 && (
          <div className="card-group p-4 flex-column flex-lg-row">
            {displaied_data.map((repo) => {
              return (
                <div className="card card-sm-12 m-1 border border-2 rounded">
                  <div className="card-body">
                    <h5 className="card-title">{repo.name}</h5>
                    <hr />
                    <p className="card-text">{repo.lokacija}</p>
                    <p className="card-text">{repo.kategorija}</p>
                    <p className="card-text">{repo.status}</p>
                    <div className="btn-group">
                      <button
                        className="btn btn-primary"
                        onClick={() => redirect(repo.id)}
                      >
                        Uredi
                      </button>
                      <button className="btn btn-danger">Obrisi</button>
                      <button className="btn btn-success">Proslijedi</button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </>
  );
}

export default Admin;
