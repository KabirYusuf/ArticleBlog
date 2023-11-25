import {http} from '@/utility/Http.js'

export const getAllArticles = async ()=>{
    try {
        const response = await http.get('/articles')
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

export const getArticleById = async (articleId)=>{
    try {
        const response = await http.get(`/articles/${articleId}`)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}