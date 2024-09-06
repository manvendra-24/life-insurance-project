import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Form, Button, Container, Row, Col, Card } from 'react-bootstrap';
import PasswordService from '../services/PasswordService';
import { successToast, errorToast } from '../sharedComponents/MyToast';
import { ToastContainer } from 'react-toastify';

const ResetPassword = () => {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const { email } = location.state;  // Extract email passed from the OTP verification page

  const handleResetPassword = async (event) => {
    event.preventDefault();
    if (newPassword !== confirmPassword) {
      errorToast('Passwords do not match');
      return;
    }
    try {

      await PasswordService.setNewPassword(email,newPassword,confirmPassword);    
      successToast('Password reset successfully!');
      setTimeout(()=>{navigate('/login'); },3000)
    } catch (error) {
      errorToast(error.message);
    }
  };

  
  const formStyle = {
    width: '100%',
    maxWidth: '400px',
    margin: '0 auto',  // Center form horizontally
    padding: '20px',   // Padding inside form
  };

  const buttonStyle = {
    width: '100%',
    backgroundColor: '#007bff',
    border: 'none',
    padding: '10px 0',
    fontSize: '18px',
    color: '#fff',
  };

  return (
    <Container fluid className="d-flex justify-content-center align-items-center min-vh-100 bg-light">
      <Row className="w-100 justify-content-center">
        <Col>
          <Card className="shadow-lg" style={{ ...formStyle }}>
            <Card.Body>
              <h3 className="text-center mb-4 text-primary">Reset Password</h3>
              <Form onSubmit={handleResetPassword}>
                <Form.Group className="mb-3">
                  <Form.Label>New Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Enter new password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    required
                    style={{ height: '40px', fontSize: '16px' }}  // Styling input field
                  />
                </Form.Group>
                <Form.Group className="mb-3">
                  <Form.Label>Confirm Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Confirm new password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    style={{ height: '40px', fontSize: '16px' }}  // Styling input field
                  />
                </Form.Group>
                <Button type="submit" variant="primary" style={{ ...buttonStyle }}>
                  Reset Password
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <ToastContainer />
    </Container>
  );
};

export default ResetPassword;
