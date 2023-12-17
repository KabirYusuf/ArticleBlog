import axios from 'axios';

export const http = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}:${import.meta.env.VITE_BACKEND_PORT}`
});

http.interceptors.request.use((config) => {
    const rawToken = localStorage.getItem('token');
    if (rawToken) {
        config.headers['Authorization'] = 'Bearer ' + rawToken;
    }
    return config;
});

export const postComment = async (addCommentRequest) => {
    try {
        const response = await http.post('/comments', addCommentRequest)
        return response.data;
    } catch (error) {
        return { data: null, error: error.response?.data?.message || error.message };
    }
}
