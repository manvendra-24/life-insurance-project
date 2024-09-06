import axios from 'axios';

const API_BASE_URL = 'http://SecureLife.com';
const PasswordService = {
  requestOtp: async (usernameOrEmail) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/otp/send`, { usernameOrEmail });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Error sending OTP');
    }
  },

  verifyOtp: async (otp) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/otp/verify`, { otp });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Error verifying OTP');
    }
  },

  setNewPassword: async (otpForgetPasswordRequest) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/password/reset`, otpForgetPasswordRequest);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Error resetting password');
    }
  }
};

export default PasswordService;
