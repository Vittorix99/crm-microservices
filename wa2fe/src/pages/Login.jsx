import {useEffect} from "react";
import {fetchMe} from "../api/security.js";
import {useOutletContext} from "react-router";
import { Button, Modal, Form, Container, Col, Row, Navbar, Nav } from "react-bootstrap";
import { useState } from "react";
import "./styles/login.css"
import { useNavigate } from "react-router-dom";
import { HOME_PATH } from "./routes.js";
import { useAuth } from "../contexts/AuthProvider.jsx";
import '../pages/styles/home.css';



function Login() {
     // Usa il contesto per ottenere e impostare l'utente
     const navigate = useNavigate();
     const { me } = useAuth();



     useEffect(() => {
      console.log(me);
      document.body.classList.add("login-bk");
      document.body.classList.remove("home-bk");
    }, []);



  

const handleLoginButton = () => {
  console.log(me);

if(me && me.principal){
navigate(HOME_PATH)



}else{
  window.location.href = me?.loginUrl || '/ui'


}

}


  const handleLogout = async () => {
 window.location.href = me?.logoutUrl
  };


  
    return (
 
<Container
  fluid
  className="d-flex justify-content-center align-items-center login  bg-dark "
>
<Container className="login-box bg-white shadow-lg rounded mt-5 col-12 rounded rounded-3" style={{width:'120%', maxWidth:'1200px'}}>
  {/* Header con il nome del sito e la navbar */}
  <Row className="login-header">
    <Col>
    <Navbar bg="light" expand="lg" className="p-4 shadow-lg rounded">
        <Navbar.Brand href="#home">CRM WA2 WEBSITE</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#about">About</Nav.Link>
            <Nav.Link href="#contact">Contact</Nav.Link>
            {me && me.principal ? (
              <Button
                variant="danger"
                className="ms-3 rounded rounded-pill"
                onClick={() => handleLogout()}
              >
                Logout
              </Button>
            ) : null}
          </Nav>
        </Navbar.Collapse>
      </Navbar>
    </Col>
  </Row>

  {/* Contenuto centrato */}
  <Row className="login-content w-100 d-flex align-items-center justify-content-center" style={{ height: 'calc(100vh - 340px)' }}>
  {/* Colonna del form di login */}
  <Col sm={12} md={4} className="text-center d-flex flex-column justify-content-center align-items-center">
    {me && me.principal ? (
      <>
        <h1 className="text-center  fw-bold">Welcome back {me.name}!</h1>
        <Form>
          <Button variant="" type="button" onClick={handleLoginButton} className="login-btn btn-grad  rounded rounded-pill">
            Get Started
          </Button>
        </Form>
      </>
    ) : (
      <>
        <h1 className="text-center fw-bold">Login into your CRM</h1>
        <Form>
          <Button variant="" type="button" onClick={handleLoginButton} className="login-btn btn-grad rounded rounded-pill">
            Login
          </Button>
        </Form>
      </>
    )}
  </Col>

  {/* Colonna con l'immagine, visibile solo su schermi md e superiori */}
  <Col md={10} className="d-none d-md-flex  justify-content-center align-items-center w-50 h-50 rounded rounded-3">
    <img
      src="./ui/src/images/crm.jpeg"
      alt="Login Illustration"
      className="img-fluid crm-image "
    />
  </Col>
</Row> 

</Container>


</Container>



    );








      
    
    
  }
  
  export default Login;


  