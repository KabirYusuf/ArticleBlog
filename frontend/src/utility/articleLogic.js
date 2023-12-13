import { ref, computed } from 'vue';
import { getAllArticles } from '@/utility/articleApiService';
import { formatDate } from './dateAndTimeLogic';

const rawCards = ref([]);

export const fetchData = async () => {
    try {
        const articles = await getAllArticles();
        rawCards.value = articles._embedded.items;
    } catch (error) {
        console.error('Error fetching articles:', error);
    }
};

export const cards = computed(() => {
    return rawCards.value.map(card => {
        const formattedTime = formatDate(card.createdAt);
        let formattedContent = card.content;
        if (card.content.length > 5) {
            formattedContent = card.content.substring(0, 5) + '...';
        }

        return {
            ...card,
            createdAt: formattedTime,
            content: formattedContent
        };
    });
});


export const handleArticleClick = (articleId, router) => {
    router.push({ name: 'article', params: { id: articleId } });
};
