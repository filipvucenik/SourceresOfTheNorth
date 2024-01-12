import { useState, useEffect } from "react";
import { BrowserRouter as Router, Link, useNavigate } from "react-router-dom";
import "./admin_view.css";
import HeaderComponent from "./HeaderComponent";
import apiConfig from "./apiConfig";

function Admin() {
  const [data, setData] = useState([]);
  const nav = useNavigate();
  const [displaied_data, set_display] = useState([]);
  const [transfer, set_transfer] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    try {
      const response = await fetch(apiConfig.getReportUrl + "/unhandled");
      const reports = await response.json();
      console.log(reports);
      setData(reports);
      set_display(reports);
    } catch (error) {
      console.error("Error fetching reports:", error);
    }
  };

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
    setData([...data, ...transfer]);
    set_display([...displaied_data, ...transfer]);
    set_transfer([]);
  };

  const addToTransfer = (id) => {
    const reportToAdd = data.find((repo) => repo.reportID === id);
    if (reportToAdd) {
      set_transfer([...transfer, reportToAdd]);
      setData(data.filter((repo) => repo.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.reportID !== id));
    }
  };

  const confirmTransfer = () => {
    console.log(transfer);
    const dataForSend = {
      categoryID: selectedCategoryID,
      reports: transfer.map((repo) => repo.reportID),
    };
    console.log(dataForSend);
    console.log(JSON.stringify(dataForSend));
    set_transfer([]);
  };

  const remove = async (id) => {
    try {
      await fetch(`/delete`, {
        method: "DELETE",
        body: JSON.stringify({ id: id }),
      });
      setData(data.filter((repo) => repo.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.reportID !== id));
    } catch (error) {
      console.error("Error removing report:", error);
    }
  };

  return (
    <>
      <HeaderComponent />
      {transfer.length > 0 && (
        <div className="container">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Status</th>

                <th>Kategorija</th>
              </tr>
            </thead>
            <tbody>
              {transfer.map((repo) => {
                return (
                  <tr>
                    <td>{repo.reportID}</td>
                    <td>{repo.reportHeadline}</td>
                    <td>{repo.status}</td>
                    <td>{categoryData[repo.categoryID]}</td>
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

        <div className="btn-group btn-group-toggle m-1 p-1">
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
          <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 row-cols-xl-4 g-4">
            {displaied_data.map((repo) => {
              return (
                <div className="card h-100 border border-2 rounded">
                  <div className="card-body">
                    <h5 className="card-title">{repo.reportHeadline}</h5>
                    <hr />
                    <p className="card-text">{repo.description}</p>
                    <p className="card-text">{categoryData[repo.categoryID]}</p>
                    <p className="card-text">{repo.status}</p>
                    <div className="btn-group">
                      <button
                        className="btn btn-primary"
                        onClick={() => redirect(repo.reportID)}
                      >
                        Uredi
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => remove(repo.reportID)}
                      >
                        Obrisi
                      </button>
                      <button
                        className="btn btn-success"
                        onClick={() => addToTransfer(repo.reportID)}
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
