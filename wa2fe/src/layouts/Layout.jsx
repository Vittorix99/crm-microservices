import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Outlet} from "react-router";
import {useEffect, useState} from "react";
import '../pages/styles/login.css';


function BaseLayout() {
    const [me, setMe] = useState(null)
useEffect(() => {
    document.body.classList.add("login-bk"); // Applica classe home
    document.body.classList.remove("home-bk"); // Rimuovi classe login (se presente)
}
, []);



    return (
        <div className="" >
       
               
                    <Outlet context={[me, setMe]}/>
               
        </div>
    );
}

export default BaseLayout;