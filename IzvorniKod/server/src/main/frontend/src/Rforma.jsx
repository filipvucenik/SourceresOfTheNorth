import React, { useState, useEffect } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  useMapEvents,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "./Rforma.css";
import L from "leaflet";
import markerIcon from "./marker.svg";
import apiConfig from "./apiConfig";
import FooterComponent from "./FooterComponent";
import HeaderComponent from "./HeaderComponent";
import EXIF from "exif-js";
import { useNavigate } from "react-router-dom";

let categoryData = "";
try {
  let url= apiConfig.getCategory
  const fetchCategory = await fetch(
    url
  );
  const fetchData = await fetchCategory.json();
  categoryData = Object.fromEntries(
    fetchData.map((item) => [item.categoryID, item.categoryName])
  );
} catch (error) {
  console.error(error);
  alert("Greška prilikom dohvaćanja kategorija, molimo osvježite stranicu!");
}

let keyWordData = "";
try {
  let url=apiConfig.getKeyword
  const fecthKeyData = await fetch(
    url
  );
  const fetchData = await fecthKeyData.json();
  keyWordData = Object.fromEntries(
    fetchData.map((item) => [item.keyword.toLowerCase(), item.categoryID])
  );
} catch (error) {
  alert(
    "Greška prilikom učitavanja stranice.\n Automatski odabir kategorije neće raditi!"
  );
}
const pinpointIconUrl = markerIcon;
const customIcon = new L.Icon({
  iconUrl: pinpointIconUrl,
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32],
});

const ReportCard = () => {

  const customAlertReturn = (message, onOk) => {

    const overlay = document.createElement('div');
    overlay.style.cssText = `
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      z-index: 9998; 
    `;
    document.body.appendChild(overlay);
  

    const alertContainer = document.createElement('div');
    alertContainer.style.cssText = `
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      padding: 20px;
      background-color: white;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
      border-radius: 5px;
      text-align: center;
      z-index: 9999; 
    `;
  
    const alertText = document.createElement('p');
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;
  
    const closeButton = document.createElement('button');
    closeButton.textContent = 'OK';
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;
  
    closeButton.addEventListener('click', () => {
      document.body.removeChild(overlay);
      document.body.removeChild(alertContainer);
      navigate("/");
    });
  
    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);
    document.body.appendChild(alertContainer);
  };

  
  const customAlert = (message) => {
    const alertContainer = document.createElement('div');
    alertContainer.style.cssText = `
      position: fixed;
      top: 20px; /* Adjust the top distance as needed */
      left: 50%;
      transform: translateX(-50%);
      padding: 20px;
      background-color: white;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
      border-radius: 5px;
      text-align: center;
      z-index: 9999; /* Set a high z-index to ensure it's on top */
    `;
  
    const alertText = document.createElement('p');
    alertText.style.cssText = `
      font-weight: bold;
      font-size: 16px;
    `;
    alertText.textContent = message;
  
    const closeButton = document.createElement('button');
    closeButton.textContent = 'OK';
    closeButton.style.cssText = `
      margin-top: 10px;
      padding: 5px 10px;
      cursor: pointer;
      background-color: black;
      color: white;
      border: none;
      border-radius: 3px;
    `;
    closeButton.addEventListener('click', () => document.body.removeChild(alertContainer));
  
    alertContainer.appendChild(alertText);
    alertContainer.appendChild(closeButton);
  
    document.body.appendChild(alertContainer);
  };


  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [selectedReport, setSelectedReport] = useState(null);
  const [hoveredReport, setHoveredReport] = useState(null);
  const [displayTable,setDisplayTable] = useState(false);
  const [similarReport,setSimilarReport]=useState([]);
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [fetchedAdress,setFetchedAdress] = useState([]);
  const [location, setLocation] = useState({
    lat: 45.804270084085914,
    lng: 15.978798866271974,
  }); // Default na Zg
  const [picture, setPicture] = useState(null);
  const [previwPicture, setPreviewPicture] = useState(null); 
  const [manualAddress, setManualAddress] = useState("");
  const [originalReport,setOriginalReport]=useState();

//rucna promjena adrese
  const manualAddressChange = async (e) => {
    const address = e.target.value;
    setManualAddress(address)
    const apiKey = "7fbe9533c0c9424aa41c500419e5ef83";
    const url = `https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(address)}&key=${apiKey}`;
    try{
      const response= await fetch(url)
      const data = await response.json();
      if(data.results.length > 0){
        const {lat , lng} = data.results[0].geometry;
        setLocation({
          lat : lat,
          lng : lng
        })

      }
    } catch(error){
      customAlert("Greška prilikom dohvaćanja API-ja za adrese!")
    }

  };

  const handleRowClick = (index) => {
    setSelectedReport(index);
    
  };
  useEffect(() => {
  }, [selectedReport]);
  const handleCategoyChange = (e) =>{
    setCategory(e.target.value)
    
  }
  const handleMapClick = async (e) => {
    const clickedLatLng = e.latlng;
    setLocation({
      lat: clickedLatLng.lat,
      lng: clickedLatLng.lng,
    });

    // GEOCODING API
    const apiKey = "7fbe9533c0c9424aa41c500419e5ef83";
    const url = `https://api.opencagedata.com/geocode/v1/json?q=${clickedLatLng.lat}+${clickedLatLng.lng}&key=${apiKey}`;

    try {
      const response = await fetch(url);
      const data = await response.json();

      if (data.results.length > 0) {
        const formattedAddress = data.results[0].formatted;
        setManualAddress(formattedAddress);

      }
    } catch (error) {
      console.error("Error fetching address:", error);
       
    }
  };

  const checkForKeyword = (text) => {
    const words = text.trim().toLowerCase().split(/\s+/);

    const categoryCounts = {};

    words.forEach((word) => {
      if (keyWordData.hasOwnProperty(word)) {
        const categoryId = keyWordData[word];
        categoryCounts[categoryId] = (categoryCounts[categoryId] || 0) + 1;
      }
    });

    let maxCategory = null;
    let maxCount = 0;

    for (const categoryId in categoryCounts) {
      if (categoryCounts[categoryId] > maxCount) {
        maxCategory = categoryId;
        maxCount = categoryCounts[categoryId];
      }
    }

    if (maxCategory !== null) {
      setCategory(maxCategory.toString());
    }
  };

  const handlePictureChange = (file) => {
    console.log("Picture changed file:", file);
    console.log("Picture changed file.target.files:", file.target.files[0]);
    setPicture(file);

    const img = file.target.files[0];
    
    if (img) {
      setPreviewPicture(URL.createObjectURL(img));
      EXIF.getData(img, async function () {
        const lat = EXIF.getTag(this, "GPSLatitude");
        const lng = EXIF.getTag(this, "GPSLongitude");

        if (lat && lng) {
          const latRef = EXIF.getTag(this, "GPSLatitudeRef");
          const lngRef = EXIF.getTag(this, "GPSLongitudeRef");

          const latDec = lat[0] + lat[1] / 60 + lat[2] / 3600;
          const lngDec = lng[0] + lng[1] / 60 + lng[2] / 3600;

          const latFinal = latRef === "N" ? latDec : -latDec;
          const lngFinal = lngRef === "E" ? lngDec : -lngDec;

          setLocation({
            lat: latFinal,
            lng: lngFinal,
          });

          // GEOCODING API
          const apiKey = "7fbe9533c0c9424aa41c500419e5ef83";
          const url = `https://api.opencagedata.com/geocode/v1/json?q=${latFinal}+${lngFinal}&key=${apiKey}`;

          try {
            const response = await fetch(url);
            const data = await response.json();

            if (data.results.length > 0) {
              const formattedAddress = data.results[0].formatted;
              setManualAddress(formattedAddress);
            }
          } catch (error) {
            console.error("Error fetching address:", error);
          }

          console.log("EXIF location:", latFinal, lngFinal);
        }
      });
    }
  };

  const sendReport = async (isLink) =>{
    const jsonServerSendData=new FormData();


    jsonServerSendData.append("reportHeadline",title);
    jsonServerSendData.append("lat",location.lat);
    jsonServerSendData.append("lng",location.lng);
    jsonServerSendData.append("description",description);
    jsonServerSendData.append("categoryID",category);
    jsonServerSendData.append("adress",manualAddress);
    if(picture){
    jsonServerSendData.append("images",picture.target.files[0])
    }
    if(isLink){
      jsonServerSendData.append("groupID",originalReport[selectedReport].report.groupID) ;
    }

    let url = apiConfig.getReportUrl;
    const submitResponse = await fetch(url, {
      method: "POST",
      body: jsonServerSendData,
    });
    const returnReport = await submitResponse.json()
    if (submitResponse.status === 200) {
      
      isLink ? customAlertReturn("Vaša prijava je nadovezana!\nKod vaše prijave je: "+returnReport.reportID) : customAlertReturn("Vaša prijava je podnešena!\nKod vaše prijave je: "+returnReport.reportID); 
    } else {
      customAlert("Server trenutno nije dostupan, molimo pričekajte ili pokušajte ponovo!");
    }
  }



  const handleSubmit = async () => {



    const jsonServerSendData=new FormData();
    
    jsonServerSendData.append("reportHeadline",title);
    jsonServerSendData.append("lat",location.lat);
    jsonServerSendData.append("lng",location.lng);
    jsonServerSendData.append("description",description);
    jsonServerSendData.append("categoryID",category);
    jsonServerSendData.append("adress",manualAddress);
    if(picture){
    jsonServerSendData.append("images",picture.target.files[0])
    }

    if (
      jsonServerSendData.reportHeadline === "" ||
      jsonServerSendData.description === "" ||
      jsonServerSendData.categoryID === ""
    ) {
      customAlert("Molimo popunite SVA polja!");
    }
    
    let testUrl = apiConfig.getTestReport;
    let simReportJson = "";

    for (const [key, value] of jsonServerSendData.entries()) {
      console.log(key, value);
    }
    
    try {
      const response = await fetch(testUrl, {
        method: "POST",
        body: jsonServerSendData,
      });
      
      simReportJson = await response.json();
      console.log(simReportJson);
      let ModifiedResponse= simReportJson;
      if (simReportJson.length > 0) {
        for(let i=0;i<simReportJson.length;i++){
          const apiKey = "7fbe9533c0c9424aa41c500419e5ef83";
          const apiUrl = `https://api.opencagedata.com/geocode/v1/json?q=${simReportJson[i].report.lat}+${simReportJson[i].report.lng}&key=${apiKey}`;
          const apiResponse = await fetch(apiUrl);
          const apiData = await apiResponse.json();
          if (apiData.results.length > 0) {

            const formattedAddress = apiData.results[0].formatted;
            ModifiedResponse[i].report={address:formattedAddress,...ModifiedResponse[i].report};
          }
        }
        setSimilarReport(simReportJson)
        setOriginalReport(simReportJson)
        customAlert("U blizini vaše lokacije detekriano je nekoliko sličnih prijava, molimo pogledajte odnosili se koja na istu stvar, ako se odnosi pritisnite na tu prijavu te nadoveži, ako ne pritisnite predaj novu.");
        setDisplayTable(true);
        return;
      }
      
      
      let url = apiConfig.getReportUrl;
      const submitResponse = await fetch(url, {
        method: "POST",
        body: jsonServerSendData,
      });
      const returnReport = await submitResponse.json()

      if (submitResponse.status === 200) {
        customAlertReturn("Vaša prijava je podnešena!\n Kod vaše prijave: "+returnReport.reportID);
      } else {
        customAlert("Server trenutno nije dostupan, molimo pričekajte ili pokušajte ponovo!");
      }
    } catch (error) {
      customAlert("Server trenutno nije dostupan, molimo pričekajte ili pokušajte ponovo!");
    }
  };

  function MapClickHandler() {
    useMapEvents({
      click: handleMapClick,
    });

    return null;
  }

  useEffect(() => {
    setManualAddress("");
  }, []);

  return (
    <>
      <HeaderComponent />
      <div className="col-lg-6 col-md-10 col-sm-12 report-card">
        <h2>Prijava oštečenja</h2>

        <label htmlFor="title">Naslov:</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e) => {
            setTitle(e.target.value);
            checkForKeyword(e.target.value);
          }}
          required
        />

        <label htmlFor="description">Opis:</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => {
            setDescription(e.target.value);
            checkForKeyword(e.target.value);
          }}
          required
        />

        <label htmlFor="category">Kategorija:</label>
        <select
          id="category"
          value={category}
          onChange={handleCategoyChange}
          required
        >
          <option key="default" value="default"> Izaberite kategoriju</option>
          {Object.entries(categoryData).map(([categoryId, categoryName]) => (
            <option key={categoryId} value={categoryId}>
              {categoryName}
            </option>
          ))}
        </select>
        <label htmlFor="picture">Prikvači sliku:</label>
        <input
          type="file"
          id="picture"
          accept="image/*"
          onChange={handlePictureChange}
        />
        {previwPicture && (
          <img src={previwPicture} alt="preview" style={{ width: "100%" }} />
        )}
        <MapContainer
          center={[location.lat, location.lng]}
          zoom={13}
          style={{ height: "300px", width: "100%", margin: "10px 0" }}
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          <MapClickHandler />
          <Marker position={location} icon={customIcon}>
            <Popup>Odaberi lokaciju</Popup>
          </Marker>
        </MapContainer>

        <div>
          <label htmlFor="address">Upiši adresu:</label>
          <input
            type="text"
            id="address"
            value={manualAddress}
            onChange={manualAddressChange}
            required
          />
        </div>

        <button onClick={handleSubmit}>Predaj prijavu</button>
      </div>
      <div>
      {displayTable && (
        <div>
          <h3 style={{ textAlign: 'center', margin: '20px 0' }}>Slične prijave:</h3>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'separate', borderSpacing: '0 5px' }}>
              <thead style={{ backgroundColor: '#f2f2f2' }}>
                <tr>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left', borderRadius: '10px' }}>Index</th>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left' }}>ID kategorije</th>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left' }}>Naslov</th>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left' }}>Opis</th>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left' }}>Adresa</th>
                  <th style={{ padding: '10px', border: '1px solid #dddddd', textAlign: 'left' }}>Slika</th>
                </tr>
              </thead>
              <tbody>
                {similarReport.map((report, index) => (
                  <React.Fragment key={index}>
                    <tr
                      style={{
                        cursor: 'pointer',
                        background: selectedReport === index ? '#4CAF50' : (hoveredReport === index ? '#DFF2BF' : 'white'),
                        borderRadius: '8px',
                      }}
                      onClick={() => handleRowClick(index)}
                      onMouseEnter={() => setHoveredReport(index)}
                      onMouseLeave={() => setHoveredReport(null)}
                    >
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{index + 1}</td>
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{report.category.categoryName}</td>
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{report.report.reportHeadline}</td>
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{report.report.description}</td>
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{report.report.address}</td>
                      <td style={{ padding: '10px', border: '1px solid #dddddd' }}>{report.image && <img src={report.image.url} alt="Report Image" style={{ width: "50%", height: "50%" }}/>}</td>
                    </tr>
                  </React.Fragment>
                ))}
              </tbody>
            </table>
          </div>
          
            <div style={{ textAlign: 'center' }}>
            {selectedReport !== null && (
              <button onClick={() => sendReport(true)} style={{ marginRight: '10px' }}>
                Nadoveži
              </button>
              )}
              <button onClick={() => sendReport(false)}>Predaj novu</button>
              
            </div>
          
          
        </div>
      )}
    </div>

      <FooterComponent />
    </>
  );
};

export default ReportCard;