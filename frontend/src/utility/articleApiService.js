import { http } from '@/utility/Auth'
import { http as http2}  from './Http';

export const getAllArticles = async () => {
    try {
        const response = await http.get('/articles')
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

export const getArticleById = async (articleId) => {
    try {
        const response = await http.get(`/articles/${articleId}`)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

export const createArticle = async (createArticleRequest) => {
    try {
        const response = await http2.post('/articles', createArticleRequest)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

export const updateArticle = async (updateArticleRequest, articleId) => {
    try {
        const response = await http2.put(`/articles/${articleId}`, updateArticleRequest)
        return response.data;
    } catch (error) {
        console.log(error);
    }
}

