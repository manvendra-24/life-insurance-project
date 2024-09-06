import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Container, Row, Col, Card } from 'react-bootstrap';
import PasswordService from '../services/PasswordService';
import { successToast, errorToast } from '../sharedComponents/MyToast';
import { ToastContainer } from 'react-toastify';

const RequestOtp = () => {
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const handleRequestOtp = async (event) => {
    event.preventDefault();
    try {
      await PasswordService.requestOtp(email);
      successToast('OTP sent successfully!');
      navigate('/verify-otp', { state: { email } });  // Pass email to the next step
    } catch (error) {
      errorToast(error.message);
    }
  };

  // Inline style for centering and setting width
  const formStyle = {
    width: '100%',
    maxWidth: '400px',
    margin: '0 auto', // Center horizontally
    padding: '20px', // Padding inside form
  };

  return (
    <Container fluid className="d-flex justify-content-center align-items-center min-vh-100 bg-light">
      <Row className="w-100 justify-content-center">
        <Col>
          <Card className="shadow-lg" style={{ ...formStyle }}>
            <Card.Body>
              <h3 className="text-center mb-4 text-primary">Request OTP</h3>
              <Form onSubmit={handleRequestOtp}>
                <Form.Group className="mb-3">
                  <Form.Label>Enter your registered email</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    style={{ height: '40px', fontSize: '16px' }} // Styling the input field
                  />
                </Form.Group>
                <Button
                  type="submit"
                  variant="primary"
                  style={{
                    width: '100%',
                    backgroundColor: '#007bff',
                    border: 'none',
                    padding: '10px 0',
                    fontSize: '18px',
                  }}
                >
                  Send OTP
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

export default RequestOtp;
