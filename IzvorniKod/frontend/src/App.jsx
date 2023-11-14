import { useState } from "react";
import "./admin_view.css";

const ReportListElement = ({ repo }) => {
  return (
    <div className="report">
      <div className="report-info">{repo.name}</div>
      <div className="report-info">{repo.kategorija}</div>
      <div className="report-info">{repo.lokacija}</div>
      <div className="report-info">{repo.status}</div>
    </div>
  );
};

function App() {
  const data = [
    {
      id: 1,
      name: "problem s vodom",
      status: "aktivna",
      lokacija: "vrbik",
      kategorija: "voda",
    },
    {
      id: 2,
      name: "problem s vodom",
      status: "aktivna",
      lokacija: "FER",
      kategorija: "voda",
    },
    {
      id: 3,
      name: "problem s vodom",
      status: "done",
      lokacija: "knezija",
      kategorija: "voda",
    },
  ];
  const [displaied_data, set_display] = useState([]);
  const updateDisplay = (status) => {
    set_display(
      data.filter((repo) => {
        if (repo.status === status) {
          return true;
        } else return false;
      })
    );
  };

  return (
    <div>
      <h1>Admin view all reports</h1>
      <div className="Sorting_buttons">
        <button onClick={() => updateDisplay("aktivna")}>Aktivne</button>
        <button onClick={() => updateDisplay("wait")}>Na čekanju</button>
        <button onClick={() => updateDisplay("done")}>Riješene</button>
      </div>
      {displaied_data.length > 0 && (
        <div className="report-columns">
          <div className="report-info-column">Naziv</div>
          <div className="report-info-column">Kategorija</div>
          <div className="report-info-column">Lokacija</div>
          <div className="report-info-column">Status</div>
        </div>
      )}
      <div className="report-list">
        <ul>
          {displaied_data.map((repo) => (
            <li key={repo.id}>
              <ReportListElement repo={repo}></ReportListElement>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default App;
