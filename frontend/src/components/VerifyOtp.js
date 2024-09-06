import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Form, Button, Container, Row, Col, Card } from 'react-bootstrap';
import PasswordService from '../services/PasswordService';
import { successToast, errorToast } from '../sharedComponents/MyToast';
import { ToastContainer } from 'react-toastify';

const VerifyOtp = () => {
  const [otp, setOtp] = useState('');
  const [timeLeft, setTimeLeft] = useState(600); // Set initial time (600 seconds = 10 minutes)
  const navigate = useNavigate();
  const location = useLocation();
  const { email } = location.state;  // Extract email passed from the previous page

  // Start the countdown timer when the component mounts
  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft((prevTime) => (prevTime > 0 ? prevTime - 1 : 0));
    }, 1000);

    // Cleanup the timer when the component unmounts
    return () => clearInterval(timer);
  }, []);

  // Convert seconds into minutes:seconds format
  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds < 10 ? `0${remainingSeconds}` : remainingSeconds}`;
  };

  const handleVerifyOtp = async (event) => {
    event.preventDefault();
    if (timeLeft === 0) {
      errorToast('OTP has expired, please request a new one.');
      return;
    }
    try {
      await PasswordService.verifyOtp(otp);
      successToast('OTP verified successfully!');
      setTimeout(()=>{ navigate('/reset-password', { state: { email } })},3000);
     
    } catch (error) {
      errorToast(error.message);
    }
  };

  return (
    <Container fluid className="d-flex justify-content-center align-items-center min-vh-100 bg-light">
      <Row className="w-100 justify-content-center">
        <Col>
          <Card className="shadow-lg" style={{ maxWidth: '400px', margin: '0 auto', padding: '20px' }}>
            <Card.Body>
              <h3 className="text-center mb-4 text-primary">Verify OTP</h3>
              <p className="text-center" style={{"color":"red"}}>Time left: {formatTime(timeLeft)}</p>
              <Form onSubmit={handleVerifyOtp}>
                <Form.Group className="mb-3">
                  <Form.Label>Enter OTP</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter the OTP"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    required
                    style={{ height: '40px', fontSize: '16px' }}  
                  />
                </Form.Group>
                <Button type="submit" variant="primary" style={{ width: '100%', padding: '10px 0', fontSize: '18px' }}>
                  Verify OTP
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

export default VerifyOtp;
