import { useState, useEffect } from "react";
import "./report.css";
import { useParams } from "react-router-dom";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

const server = apiConfig.getUrl;

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
    <>
      <HeaderComponent />
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
      <FooterComponent />
    </>
  );
};

function Reports() {
  const iddd = useParams();

  console.log(iddd);

  return (
    <div>
      <div className="page">
        <Report id={iddd}></Report>
      </div>
    </div>
  );
}

export default Reports;
