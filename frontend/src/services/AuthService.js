import axios from 'axios';
import {UnAuthorized, InvalidCredentialError, InternalServerError} from '../utils/errors/Error';

export const verifyAdmin = async (headers = {}) => {
    if(!localStorage.getItem('token')){
        throw new UnAuthorized("User is not logged in");
    }
    const token = localStorage.getItem('token');
    try {
        const response = await axios.get(`http://localhost:8081/api/auth/verifyAdmin`, {
            headers: {
                Authorization: `Bearer ${token}`,
                ...headers
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const verifyCustomer = async (headers = {}) => {
    if(!localStorage.getItem('token')){
        throw new UnAuthorized("User is not logged in");
    }
    const token = localStorage.getItem('token');
    try {
        const response = await axios.get(`http://localhost:8081/api/auth/verifyCustomer`, {
            headers: {
                Authorization: `Bearer ${token}`,
                ...headers
            }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};


export const loginService = async (usernameOrEmail, password) => {
    try {
      const response = await axios.post(`http://localhost:8081/SecureLife.com/login`, { usernameOrEmail, password }, {
        headers: { 'Content-Type': 'application/json' }
<<<<<<< HEAD

      });


    const token = response.headers['authorization'];
=======
      });
      const token = response.headers['authorization'];
>>>>>>> 8fa54374722a0ecd854084a1ec2dd44cf32d35a3

    if (token) {
      localStorage.setItem('token', token);
    }
<<<<<<< HEAD
    console.log(response.data);
=======
>>>>>>> 8fa54374722a0ecd854084a1ec2dd44cf32d35a3
      return response.data;
    } catch (error) {
        if(error.response && error.response.status === 400){
            throw new InvalidCredentialError("Invalid Credentials");
<<<<<<< HEAD
        }else{
=======
        }else if(error.response && error.response.status === 401){
>>>>>>> 8fa54374722a0ecd854084a1ec2dd44cf32d35a3
            throw new InternalServerError("Internal Server Error");
        }
    }
  };
  

  export const getUser = async () => {
    if(!localStorage.getItem('token')){
        throw new UnAuthorized("User is not logged in");
    }
    const token = localStorage.getItem('token');
    try {
        const response = await axios.get(`http://localhost:8081/api/auth/profile`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
<<<<<<< HEAD
        
        return response.data;
    } catch (error) {
        
=======
        return response.data;
    } catch (error) {
>>>>>>> 8fa54374722a0ecd854084a1ec2dd44cf32d35a3
        throw error;
    }
  };


  export const setUser = async (userData) => {
    if(!localStorage.getItem('token')){
        throw new UnAuthorized("User is not logged in");
    }
    const token = localStorage.getItem('token');
    try {
        const response = await axios.put(`http://localhost:8081/api/auth/profile`, 
            userData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
<<<<<<< HEAD
        console.log(response.data);
        return response.data;
    } catch (error) {
      
=======
        return response.data;
    } catch (error) {
        console.error('Error updating user profile:', error);
>>>>>>> 8fa54374722a0ecd854084a1ec2dd44cf32d35a3
        throw error;
    }
};