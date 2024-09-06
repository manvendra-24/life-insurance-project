import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { FaSignOutAlt } from 'react-icons/fa'; // For the logout icon

const Header = () => {
  return (
    <Navbar bg="light" expand="lg" className="shadow-sm">
      <Container>
        <Navbar.Brand href="#home" className="font-weight-bold text-primary">
          SecureLife
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
          <Nav className="ml-auto">
            <Nav.Link href="/logout" className="d-flex align-items-center text-dark">
              <FaSignOutAlt className="mr-2" />
              Logout
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
