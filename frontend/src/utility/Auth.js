
import axios from 'axios';

export const http = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}:${import.meta.env.VITE_BACKEND_PORT}`
});

export const register = async (data)=>{
    return await http.post('/auth/register', data);
}
export const login = async (data) => {
    return await http.post('/auth/login', data)
}

