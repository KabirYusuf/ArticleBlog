import {http} from '@/utility/Http.js'

export const register = async (data)=>{
    return await http.post('/register', data);
}

