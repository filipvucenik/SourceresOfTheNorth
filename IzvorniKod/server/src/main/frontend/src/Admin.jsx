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
  const [group, set_group] = useState([]);
  const [categoryData, setCategoryData] = useState({});
  const [selectedCategoryID, setSelectedCategoryID] = useState("");
  const [newStatus, setNewStatus] = useState("");
  const [initialData, setInitialData] = useState("");

  useEffect(() => {
    fetchReports();
  }, []);

  useEffect(() => {
    getCategory();
  }, []);

  useEffect(() => {
    if (initialData === "YES") {
      set_display(data);
    }
    console.log(data);
  }, [initialData]);

  const fetchReports = async () => {
    try {
      const response = await fetch(apiConfig.getReportUrl + "/unhandled");
      const reports = await response.json();
      console.log(reports);
      setData(reports);
    } catch (error) {
      console.error("Error fetching reports:", error);
    }

    try {
      const response = await fetch(apiConfig.getReportUrl + "/status/uProcesu");
      const reports = await response.json();
      if (reports.length > 0) {
        for (const r of reports) {
          r.status = "U Procesu";
        }
        setData([...data, ...reports]);
      }
      const response1 = await fetch(
        apiConfig.getReportUrl + "/status/neobrađen"
      );
      const reports1 = await response1.json();
      if (reports1.length > 0) {
        for (const r of reports1) {
          r.status = "Neobrađeno";
        }
        setData([...data, ...reports1]);
      }
      const response2 = await fetch(apiConfig.getReportUrl + "/status/obrađen");
      const reports2 = await response2.json();
      if (reports2.length > 0) {
        for (const r of reports2) {
          r.status = "Obrađeno";
        }
        setData([...data, ...reports2]);
      }
    } catch (error) {
      console.error("Error fetching reports:", error);
    } finally {
      setInitialData("YES");
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
    const reportToAdd = data.find((repo) => repo.report.reportID === id);
    if (reportToAdd) {
      set_transfer([...transfer, reportToAdd]);
      setData(data.filter((repo) => repo.report.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.report.reportID !== id));
    }
  };

  const confirmTransfer = () => {
    var formData = new FormData();
    formData.append("CatID", selectedCategoryID);
    formData.append(
      "reports",
      transfer.map((repo) => repo.report.reportID)
    );
    console.log(formData);
    try {
      const response = fetch(`${server}/changeOffice`, {
        method: "PUT",
        credentials: "include",
        body: formData,
      });
      set_transfer([]);
    } catch (error) {
      console.error("Error updating report:", error);
    }
  };

  const remove = async (id) => {
    try {
      console.log("removing report " + id);

      await fetch(`${server}/delete?repotId=${id}`, {
        method: "DELETE",
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
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
    try {
      for (const r of update) {
        var formData = new FormData();
        formData.append("status", newStatus);
        formData.append("reports", r.report.reportID);

        const response = await fetch(`${server}/updateStatus`, {
          method: "PUT",
          credentials: "include",

          body: formData,
        });
      }

      set_update([]);
    } catch (error) {
      console.error("Error updating report:", error);
    }
  };

  const handleStatusChange = (event) => {
    const selectedStatus = event.target.value;
    setNewStatus(selectedStatus);
  };

  const addToGroup = async (id) => {
    const reportToAdd = data.find((repo) => repo.report.reportID === id);
    if (reportToAdd) {
      set_group([...group, reportToAdd]);
      setData(data.filter((repo) => repo.report.reportID !== id));
      set_display(displaied_data.filter((repo) => repo.report.reportID !== id));
    }
  };

  const cancelGroup = () => {
    setData([...data, ...group]);
    set_display([...displaied_data, ...group]);
    set_group([]);
  };

  const confirmGroup = async () => {
    console.log(group);
    var formData = new FormData();
    formData.append("mainReportID", group[0].report.reportID);
    formData.append(
      "reports",
      group.slice(1).map((repo) => repo.report.reportID)
    );

    console.log(formData);

    try {
      const response = await fetch(`${server}/groupReports`, {
        method: "PUT",
        credentials: "include",
        body: formData,
      });
      set_group([]);
    } catch (error) {
      console.error("Error updating report:", error);
    }
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
                <th>Status</th>
                <th>Kategorija</th>
              </tr>
            </thead>
            <tbody>
              {update.map((repo) => {
                return (
                  <tr>
                    <td>{repo.report.reportID}</td>
                    <td>{repo.report.reportHeadline}</td>
                    <td>{repo.report.status}</td>
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
              <option key="U Procesu" value="uProcesu">
                U Procesu
              </option>
              <option key="Neobrađeno" value="neobrađen">
                Neobrađeno
              </option>
              <option key="Obrađeno" value="obrađen">
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

      {group.length > 0 && (
        <div className="container">
          <h1 className="text-center">PRIJAVE ZA GRUPIRATI</h1>
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Id</th>
                <th>Name</th>

                <th>Kategorija</th>
              </tr>
            </thead>
            <tbody>
              {group.map((repo) => {
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

          <button className="btn btn-success" onClick={() => confirmGroup()}>
            Grupiraj
          </button>
          <button className="btn btn-primary" onClick={() => cancelGroup()}>
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
            Obrađene
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
                    <ul className="list-group list-group-flush">
                      {data.map((miniR) => {
                        if (miniR.report.group === repo.report.reportID) {
                          return (
                            <li className="list-group-item">
                              <Link to={`/report/${miniR.report.reportID}`}>
                                {miniR.report.reportHeadline}
                              </Link>
                            </li>
                          );
                        }
                        return null;
                      })}
                    </ul>

                    <div className="btn-group">
                      <button
                        className="btn btn-primary"
                        onClick={() => redirect(repo.report.reportID)}
                      >
                        Pregled
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => remove(repo.report.reportID)}
                      >
                        Obrisi
                      </button>

                      <div className="dropdown">
                        <button
                          className="btn btn-warning dropdown-toggle"
                          type="button"
                          id="actionsDropdown"
                          data-bs-toggle="dropdown"
                          aria-expanded="false"
                        >
                          Actions
                        </button>
                        <ul
                          className="dropdown-menu"
                          aria-labelledby="actionsDropdown"
                        >
                          <li>
                            <button
                              className="dropdown-item"
                              onClick={() =>
                                addToTransfer(repo.report.reportID)
                              }
                            >
                              Proslijedi
                            </button>
                          </li>
                          <li>
                            <button
                              className="dropdown-item"
                              onClick={() => addToUpdate(repo.report.reportID)}
                            >
                              Ažuriraj
                            </button>
                          </li>
                          <li>
                            <button
                              className="dropdown-item"
                              onClick={() => addToGroup(repo.report.reportID)}
                            >
                              Grupiraj
                            </button>
                          </li>
                        </ul>
                      </div>
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
