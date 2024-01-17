import { useState, useEffect } from "react";
import "./report.css";
import { useParams } from "react-router-dom";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";

const server = apiConfig.getUrl;

function Reports() {
  const iddd = useParams();

  const [data, setData] = useState({});
  const [categoryData, setCategoryData] = useState({});

  useEffect(() => {
    getCategory();
  }, []);

  const getCategory = async () => {
    let url = apiConfig.getCategory;
    const fetchCategory = await fetch(url);
    const fetchData = await fetchCategory.json();
    const transformedData = Object.fromEntries(
      fetchData.map((item) => [item.categoryID, item.categoryName])
    );
    setCategoryData(transformedData);
  };

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
      <HeaderComponent />

      {data.report && (
        <>
          <div className="container">
            <div className="card h-100 border border-2 rounded">
              if (data.images[0]){" "}
              {
                <img
                  src={data.images[0].url}
                  class="card-img-top"
                  alt="glavna slika"
                ></img>
              }
              <div className="card-body">
                <h5 className="card-title">
                  <b>{data.report.reportHeadline}</b>
                </h5>
                <hr />
                <p className="card-text">
                  <b>Opis:</b> {data.report.description}
                </p>
                <p className="card-text">
                  <b>Datum:</b>{" "}
                  {new Date(data.report.reportTS).toLocaleString("de-DE", {
                    timeZone: "UTC",
                  })}
                </p>
                <p className="card-text">
                  <b>Kategorija:</b> {categoryData[data.report.categoryID]}
                </p>
              </div>
            </div>
          </div>
        </>
      )}

      <FooterComponent />
    </div>
  );
}

export default Reports;
