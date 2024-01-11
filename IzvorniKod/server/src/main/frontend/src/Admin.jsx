import { useState, useEffect } from "react";
import { BrowserRouter as Router, Link, useNavigate } from "react-router-dom";
import "./admin_view.css";
import HeaderComponent from "./HeaderComponent";
import apiConfig from "./apiConfig";

function Admin() {
  const fake_data = [
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

  const [data, setData] = useState(fake_data);
  const nav = useNavigate();
  const [displaied_data, set_display] = useState([]);
  const [transfer, set_transfer] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");

  // useEffect(() => {
  //   fetchReports();
  // }, []);

  // const fetchReports = async () => {
  //   try {
  //     const response = await fetch("/api/reports");
  //     const reports = await response.json();
  //     setData(reports);
  //     set_display(reports);
  //   } catch (error) {
  //     console.error("Error fetching reports:", error);
  //   }
  // };

  const getCategory = async () => {
    let url = apiConfig.getCategory;
    const fetchCategory = await fetch(url);
    const fetchData = await fetchCategory.json();
    const transformedData = Object.fromEntries(
      fetchData.map((item) => [item.categoryID, item.categoryName])
    );
    setCategoryData(transformedData);
  };
  useEffect(() => {
    getCategory();
  }, []);

  const handleCategoryChange = (event) => {
    const selectedID = event.target.value;
    setSelectedCategoryID(selectedID);
  };

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

  const cancelTransfer = () => {
    set_transfer([]);
  };

  const addToTransfer = (id) => {
    const reportToAdd = data.find((repo) => repo.id === id);
    if (reportToAdd) {
      set_transfer([...transfer, reportToAdd]);
      setData(data.filter((repo) => repo.id !== id));
      set_display(displaied_data.filter((repo) => repo.id !== id));
    }
  };

  const confirmTransfer = () => {
    console.log(transfer);
    const dataForSend = {
      categoryID: selectedCategoryID,
      reports: transfer.map((repo) => repo.id),
    };
    console.log(dataForSend);
    console.log(JSON.stringify(dataForSend));
    set_transfer([]);
  };

  return (
    <>
      <HeaderComponent />
      {transfer.length > 0 && (
        <div className="container">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>id</th>
                <th>name</th>
                <th>status</th>
                <th>lokacija</th>
                <th>kategorija</th>
              </tr>
            </thead>
            <tbody>
              {transfer.map((repo) => {
                return (
                  <tr>
                    <td>{repo.id}</td>
                    <td>{repo.name}</td>
                    <td>{repo.status}</td>
                    <td>{repo.lokacija}</td>
                    <td>{repo.kategorija}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <label>
            ID Kategorije:
            <select name="categoryID" onChange={handleCategoryChange}>
              <option key="default" value="default">
                {" "}
                Izaberite kategoriju
              </option>
              {Object.keys(categoryData).map((key) => (
                <option key={key} value={key}>
                  {categoryData[key]}
                </option>
              ))}
            </select>
          </label>

          <button className="btn btn-success" onClick={() => confirmTransfer()}>
            Proslijedi
          </button>
          <button className="btn btn-primary" onClick={() => cancelTransfer()}>
            Odustani
          </button>
        </div>
      )}
      <hr />
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
                      <button
                        className="btn btn-success"
                        onClick={() => addToTransfer(repo.id)}
                      >
                        Proslijedi
                      </button>
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
