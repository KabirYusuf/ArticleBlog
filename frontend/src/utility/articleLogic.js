import { ref, computed } from 'vue';
import { getAllArticles } from '@/utility/articleApiService'; 

const rawCards = ref([]);

export const fetchData = async () => {
  try {
    const articles = await getAllArticles();
    rawCards.value = articles._embedded.items;
  } catch (error) {
    console.error('Error fetching articles:', error);
  }
};

export const formatDate = (dateString) => {
  const options = { year: '2-digit', month: '2-digit', day: '2-digit' };
  const formattedDate = new Intl.DateTimeFormat('en-GB', options).format(new Date(dateString));
  return formattedDate.replace(/\//g, '.');
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

export const lastCard = computed(() => {
  return cards.value.length > 0 ? cards.value[cards.value.length - 1] : null;
});


