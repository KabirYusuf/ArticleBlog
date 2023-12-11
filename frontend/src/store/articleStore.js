import { defineStore } from 'pinia';
import { getArticleById } from '../utility/articleApiService';

export const useArticleStore = defineStore('article', {
    state: () => ({
        currentArticle: null,
    }),
    actions: {
        async fetchArticle(articleId) {
            try {
                const response = await getArticleById(articleId);
                this.currentArticle = response;
            } catch (error) {
                console.error('Error fetching article:', error);
            }
        },
    },
});