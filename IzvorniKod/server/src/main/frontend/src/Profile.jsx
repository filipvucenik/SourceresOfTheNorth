import React from 'react'
import HeaderComponent from './HeaderComponent'

const Profile = () => {
  /*const fetchDataAndCreateMarkers = async () => {
        try {
          const response = await fetch(`${server}reports/unhandled`);
          const data = await response.json();
          console.log(data);
    
          for (const lokacija of data) {
            createMarker(lokacija.lat, lokacija.lng);
          }
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };*/

  return (
    <>
    <HeaderComponent/>
      <div className='najjaciDiv'>
        <form >
          <div class="row g-3">
            <div class="col-sm-6">
              <label for="firstName" class="form-label">Ime</label>
              <input type="text" class="form-control" id="firstName" placeholder="" value="" required=""/>
              <div class="invalid-feedback">
                Valid first name is required.
              </div>
            </div>

            <div class="col-sm-6">
              <label for="lastName" class="form-label">Prezime</label>
              <input type="text" class="form-control" id="lastName" placeholder="" value="" required=""/>
              <div class="invalid-feedback">
                Valid last name is required.
              </div>
            </div>

            <div class="col-12">
              <label for="username" class="form-label">Korisničko ime</label>
              <div class="input-group has-validation">
                <span class="input-group-text">@</span>
                <input type="text" class="form-control" id="username" placeholder="korisničko ime" required=""/>
              <div class="invalid-feedback">
                  Your username is required.
                </div>
              </div>
            </div>

            <div class="col-12">
              <label for="email" class="form-label">Email</label>
              <input type="email" class="form-control" id="email" placeholder="you@example.com"/>
              <div class="invalid-feedback">
                Please enter a valid email address for shipping updates.
              </div>
            </div>
          </div>
          <hr class="my-4"/>
          <button type="button" className="btn btn-outline-dark me-2">Spremi</button>
        </form>
      </div>
    </>
    
  )
}

export default Profile
