import React from 'react';
import {Outlet} from "react-router-dom";
import '../styles/main.css'

const Layout = () => {
    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            margin: "0 auto",
            alignItems: 'center'
        }}>
    <Outlet/>
        </div>
    );
};

export default Layout;