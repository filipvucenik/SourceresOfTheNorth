import { useState, useEffect } from "react";
import "./report.css";
import { useParams } from "react-router-dom";

const server = "https://ostecenja-progi-fer.onrender.com";

const Report = ({ id }) => {
  console.log(id.id);

  const [value, setValue] = useState();
  const handleChange = (event) => {
    setValue(event.target.value);
  };

  const [data, setData] = useState({});

  const fetchData = async () => {
    try {
      const response = await fetch(`${server}reports/${id.id}`);
      const result = await response.json();
      setData(result);
      console.log(result);
      setValue(result.trStatus);
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
        <div>
          <img src="https://picsum.photos/400/300" />
        </div>
        <div>Naslov: {data.reportHeadline}</div>
        <div>
          <p>Opis: {data.description}</p>
        </div>
        <div>Datum: {data.reportTS}</div>
      </div>
      <div className="report-right">
        <div>
          <img src="https://picsum.photos/300/300" />
          {data.location}
        </div>
        <div>
          <select value={value} onChange={handleChange}>
            <option value="aktivno">Aktivno</option>
            <option value="neobrađeno">Neobrađeno</option>
            <option value="obrađeno">Odrađeno</option>
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

  const iddd = useParams();

  console.log(iddd);

  return (
    <div>
      <h1>REPORT</h1>
      <div className="page">
        <Report id={iddd}></Report>
      </div>
    </div>
  );
}

export default Reports;
