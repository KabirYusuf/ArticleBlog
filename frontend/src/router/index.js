import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import ViewAllArticles from '@/views/ViewAllArticles.vue';
import ArticleView from '@/views/ArticleView.vue';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
        },

        {
            path: '/view-all-articles',
            name: 'viewAllArticles', 
            component: ViewAllArticles,
        },
        {
            path: '/article/:id',
            name: 'article',
            component: ArticleView,
          },
    ],
});

export default router;
