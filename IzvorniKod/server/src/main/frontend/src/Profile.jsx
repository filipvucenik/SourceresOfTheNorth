import React from 'react'
import HeaderComponent from './HeaderComponent'

const Profile = () => {
  return (
    <>
    <HeaderComponent/>
      <div className="report-card">
        <h1>Korisnicki podaci</h1>
        <label>
          Username
          <input type="text" name="username" placeholder='username'/>
        </label>
        <label>
          Password
          <input type="text" name="pass" />
        </label>
        <label>
          Email
          <input type="text" name="email" />
        </label>
      </div>
    </>
    
  )
}

export default Profile
