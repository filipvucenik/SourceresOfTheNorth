import { useState, useEffect } from "react";
import "./report.css";
import { useParams } from "react-router-dom";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

const server = apiConfig.getUrl;

function Reports() {
  const iddd = useParams();

  console.log(iddd);

  const [data, setData] = useState({});

  const fetchData = async () => {
    try {
      const response = await fetch(`${server}/reports/${iddd.id}`);
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
    <div>
      <div className="page">
        {data.report && (
          <>
            <HeaderComponent />
            <div className="Report">
              <div className="report-left">
                <div>
                  <img src="https://picsum.photos/400/300" />
                </div>
                <div>Naslov: {data.report.reportHeadline}</div>
                <div>
                  <p>Opis: {data.report.description}</p>
                </div>
                <div>
                  Datum:{" "}
                  {new Date(data.report.reportTS).toLocaleString("de-DE", {
                    timeZone: "UTC",
                  })}
                </div>
              </div>
              <div className="report-right">
                <div>
                  <img src="https://picsum.photos/300/300" />
                  {data.location}
                </div>
              </div>
            </div>
            <FooterComponent />
          </>
        )}
      </div>
    </div>
  );
}

export default Reports;
