import axios from 'axios';
import {http} from '../utility/Http.js'

export const getAllArticles = async ()=>{
    try {
        const response = await http.get('/articles')
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}