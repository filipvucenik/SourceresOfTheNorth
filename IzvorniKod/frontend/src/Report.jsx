import { useState, useEffect } from "react";
import "./report.css";
import { useParams } from "react-router-dom";

const server = "http://localhost:8080/";

const Report = ({ report }) => {
  const s = report.status;
  const id = report.id;
  const { t_id } = useParams();

  const [value, setValue] = useState(s);
  const handleChange = (event) => {
    setValue(event.target.value);
  };

  const [data, setData] = useState({});

  const fetchData = async () => {
    try {
      const response = await fetch(`${server}reports/1`);
      const result = await response.json();
      setData(result);
      console.log(result);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="Report">
      <div className="report-left">
        <div>Image placeholder</div>
        <div>{data.reportHeadline}</div>
        <div>
          <p>{report.description}</p>
        </div>
        <div>{report.date}</div>
      </div>
      <div className="report-right">
        <div>{report.location}</div>
        <div>
          <select value={value} onChange={handleChange}>
            <option value="aktivna">Aktivna</option>
            <option value="wait">Na Čekanju</option>
            <option value="done">Rješeno</option>
          </select>
        </div>
        <button>Update status</button>
        <button>Delete report</button>
      </div>
    </div>
  );
};

function Reports() {
  const testerData = {
    naslov: "Problem s kanalizacijom",
    description: `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
   Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.`,
    date: "13.11.2023.",
    location: "ulica ta i ta ili kak bi vec bilo",
    status: "aktivna",
  };

  return (
    <div>
      <h1>REPORT</h1>
      <div>
        <Report report={testerData}></Report>
      </div>
    </div>
  );
}

export default Reports;
