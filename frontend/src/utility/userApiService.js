import { http } from '@/utility/Http'

export const getUser = async () => {
    try {
        const response = await http.get('/users/me')
        return response.data;
    } catch (error) {
        console.log(error.message);
    }
}

export const updateUser = async (updateUserRequest, userId) => {
    try {
        const response = await http.put(`/users/${userId}`, updateUserRequest)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

