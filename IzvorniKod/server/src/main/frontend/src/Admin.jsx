import { useState, useEffect } from "react";
import { BrowserRouter as Router, Link, useNavigate } from "react-router-dom";
import "./admin_view.css";
import HeaderComponent from "./HeaderComponent";
import apiConfig from "./apiConfig";

function Admin() {
  const server = apiConfig.getReportUrl;
  const [data, setData] = useState([]);
  const nav = useNavigate();
  const [displaied_data, set_display] = useState([]);
  const [transfer, set_transfer] = useState([]);
  const [update, set_update] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  const [newStatus, setNewStatus] = useState("");

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
        if (repo.report.status === status) {
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
    const reportToAdd = data.find((repo) => repo.report.reportID === id);
    if (reportToAdd) {
      set_transfer([...transfer, reportToAdd]);
      setData(data.filter((repo) => repo.report.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.report.reportID !== id));
    }
  };

  const confirmTransfer = () => {
    console.log(transfer);
    const dataForSend = {
      categoryID: selectedCategoryID,
      reports: transfer.map((repo) => repo.report.reportID),
    };
    console.log(dataForSend);
    console.log(JSON.stringify(dataForSend));
    set_transfer([]);
  };

  const remove = async (id) => {
    try {
      console.log("removing report" + id);
      console.log(`${server}/delete?repotId=${id}`);
      await fetch(`${server}/delete?repotId=${id}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });
      setData(data.filter((repo) => repo.report.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.report.reportID !== id));
    } catch (error) {
      console.error("Error removing report:", error);
    }
  };

  const addToUpdate = (id) => {
    const reportToAdd = data.find((repo) => repo.report.reportID === id);
    if (reportToAdd) {
      set_update([...update, reportToAdd]);
      setData(data.filter((repo) => repo.report.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.report.reportID !== id));
    }
  };

  const cancelUpdate = () => {
    setData([...data, ...update]);
    set_display([...displaied_data, ...update]);
    set_update([]);
  };

  const confirmUpdate = async () => {
    console.log(update);
    const dataForSend = {
      status: newStatus,
      reports: update.map((repo) => repo.report.reportID),
    };
    console.log(dataForSend);
    console.log(JSON.stringify(dataForSend));
    set_update([]);
  };

  const handleStatusChange = (event) => {
    const selectedStatus = event.target.value;
    setNewStatus(selectedStatus);
  };

  return (
    <>
      <HeaderComponent />

      {update.length > 0 && (
        <div className="container">
          <h1 className="text-center">PRIJAVE ZA IZMJENU STATUSA</h1>
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>

                <th>Kategorija</th>
              </tr>
            </thead>
            <tbody>
              {update.map((repo) => {
                return (
                  <tr>
                    <td>{repo.report.reportID}</td>
                    <td>{repo.report.reportHeadline}</td>

                    <td>{categoryData[repo.report.categoryID]}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <label>
            Novi status:
            <select name="status" onChange={handleStatusChange}>
              <option key="default" value="default">
                izaberite status
              </option>
              <option key="U Procesu" value="U Procesu">
                U Procesu
              </option>
              <option key="Neobrađeno" value="Neobrađeno">
                Neobrađeno
              </option>
              <option key="Obrađeno" value="Obrađeno">
                Obrađeno
              </option>
            </select>
          </label>

          <button className="btn btn-success" onClick={() => confirmUpdate()}>
            Izmjeni status
          </button>
          <button className="btn btn-primary" onClick={() => cancelUpdate()}>
            Odustani
          </button>
        </div>
      )}

      {transfer.length > 0 && (
        <div className="container">
          <h1 className="text-center">PRIJAVE ZA PROSLIJEDITI</h1>
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>

                <th>Kategorija</th>
              </tr>
            </thead>
            <tbody>
              {transfer.map((repo) => {
                return (
                  <tr>
                    <td>{repo.report.reportID}</td>
                    <td>{repo.report.reportHeadline}</td>

                    <td>{categoryData[repo.report.categoryID]}</td>
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
            U procesu
          </button>
          <button
            className="btn btn-lg btn-primary"
            onClick={() => updateDisplay("Neobrađeno")}
          >
            Neobrađene
          </button>
          <button
            className="btn btn-lg btn-primary"
            onClick={() => updateDisplay("Obrađeno")}
          >
            Obrađeno
          </button>
        </div>
        {displaied_data.length > 0 && (
          <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 row-cols-xl-4 g-4">
            {displaied_data.map((repo) => {
              return (
                <div className="card h-100 border border-2 rounded">
                  <div className="card-body">
                    <h5 className="card-title">{repo.report.reportHeadline}</h5>
                    <hr />
                    <p className="card-text">{repo.report.description}</p>
                    <p className="card-text">
                      {categoryData[repo.report.categoryID]}
                    </p>
                    <p className="card-text">{repo.report.status}</p>
                    <div className="btn-group">
                      <button
                        className="btn btn-primary btn-sm"
                        onClick={() => redirect(repo.report.reportID)}
                      >
                        Pregled
                      </button>
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => remove(repo.report.reportID)}
                      >
                        Obrisi
                      </button>
                      <button
                        className="btn btn-success btn-sm"
                        onClick={() => addToTransfer(repo.report.reportID)}
                      >
                        Proslijedi
                      </button>
                      <button
                        className="btn btn-warning btn-sm"
                        onClick={() => addToUpdate(repo.report.reportID)}
                      >
                        Update
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
